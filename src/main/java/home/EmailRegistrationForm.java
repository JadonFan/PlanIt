package home;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import alerts.ErrorBox;
import application.EmailManager2;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import ui.PopupForm;
import user.AppSession;

// TODO store the user's email address and, if permitted, the access token in the database
// TODO if the access token is no longer valid, then the user should be re-directed to this form
public class EmailRegistrationForm {
	private Stage window;
	
	
	public void displayEmailSetUpProcess(EmailManager2 emailManager2) {
		this.window = new Stage();
		this.window.initModality(Modality.APPLICATION_MODAL);
		
		List<Label> labels = new ArrayList<>();
		labels.add(new Label("Email Address:"));
		Button nextStepBtn = new Button("Next");
		PopupForm popupForm = new PopupForm(labels, nextStepBtn);
		
		popupForm.mapLayout(this.window);
		
		nextStepBtn.setOnAction(event -> {  
			String emAd = popupForm.getTextFields()[0].getText();
			if (emAd.isEmpty()) {
				new ErrorBox("You must enter an email address").display();
			} else {
				AppSession.setEmailAddress(emAd);
				this.displayAuthRedirectInfo(emailManager2);
			}
		});
		
		Scene scene = new Scene(popupForm.getFormBox());
		this.window.setScene(scene);
		this.window.show();
	}
	
	
	private void displayAuthRedirectInfo(EmailManager2 emailManager2) {	
		VBox hb = new VBox();
		hb.setPadding(new Insets(5));
		hb.setSpacing(5);
		hb.setAlignment(Pos.CENTER);
		hb.prefWidthProperty().bind(this.window.widthProperty());
		
		Circle circle = new Circle(15);
		Stop[] stops = {new Stop(0.5, Color.YELLOW), new Stop(0.5, Color.BLUE)}; 
		LinearGradient linearGradient = new LinearGradient(0, 0, 1, 0, true, CycleMethod.NO_CYCLE, stops);
		circle.setFill(linearGradient);
		hb.getChildren().add(circle);
		AnimationTimer rotateCircleAnimation = new AnimationTimer() {
			@Override
			public void handle(long now) {
				circle.setRotate(circle.getRotate() + 5);
			}
		};
		rotateCircleAnimation.start();
		
		Text redirectInfoText = new Text("You'll be re-directed to the Google sign in page shortly.");
		hb.getChildren().add(redirectInfoText);
		
		Timeline goToNextPart = new Timeline(new KeyFrame(Duration.seconds(5)));
		goToNextPart.setAutoReverse(false);
		goToNextPart.setCycleCount(1);
		goToNextPart.setOnFinished(event -> {
			goToNextPart.stop();
			rotateCircleAnimation.stop();
			this.displayAccessTokenField(emailManager2);
			event.consume();
			try {
				emailManager2.reqAccessCode();
			} catch (IOException | URISyntaxException e) {
				e.printStackTrace();
			}
		});
		goToNextPart.playFromStart();
		
		Scene scene = new Scene(hb);
		this.window.setScene(scene);
		this.window.show();
	}
	
	
	private void displayAccessTokenField(EmailManager2 emailManager2) {		
		List<Label> labels = new ArrayList<>();
		labels.add(new Label("Access Code:"));
		Button registerBtn = new Button("Register");
		PopupForm popupForm = new PopupForm(labels, registerBtn);
		
		Text instrText = new Text("Once you have accepted the permissions, you should see a code with a long string of symbols. Please"
				+ " copy and paste that code into the text field below. Click the help button for more information.");
		instrText.wrappingWidthProperty().bind(popupForm.getFormBox().widthProperty());
		popupForm.getFormBox().getChildren().add(instrText);
		popupForm.mapLayout(this.window);
		
		registerBtn.setOnAction(event -> {  
			String accessCode = popupForm.getTextFields()[0].getText();
			if (accessCode.isEmpty()) {
				new ErrorBox("You must enter an email address").display();
			} else {
				try {
					emailManager2.reqAuthToken(accessCode);
					Home.getInstance().display();
					this.window.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		
		Scene scene = new Scene(popupForm.getFormBox());
		this.window.setScene(scene);
		this.window.show();
	}
}
