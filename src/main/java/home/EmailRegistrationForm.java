package home;

import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import alerts.ErrorBox;
import application.EmailManager2;
import application.PlannerDb;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import ui.PopupForm;
import user.AppSession;

// TODO if the access code is no longer valid, then the user should be re-directed to this form
// TODO encrypt the access code before storing it in the database (likewise, decrypt it when loading the 
// access code from the database)
public class EmailRegistrationForm {
	private Stage window;
	private static final int FORM_WIDTH = 350;
	private static final int FORM_HEIGHT = 250;

	
	public void displayEmailSetUpProcess(EmailManager2 emailManager2) {
		this.window = new Stage();
		this.window.initModality(Modality.APPLICATION_MODAL);
		this.window.setWidth(EmailRegistrationForm.FORM_WIDTH);
		this.window.setHeight(EmailRegistrationForm.FORM_HEIGHT);
		this.window.setResizable(false);
		
		List<Label> labels = new ArrayList<>();
		labels.add(new Label("Email Address:"));
		Button nextStepBtn = new Button("Next");
		PopupForm popupForm = new PopupForm(labels, nextStepBtn);
		
		popupForm.mapLayout(this.window);
		
		nextStepBtn.setOnAction(event -> {  
			String emAd = popupForm.getTextFields()[0].getText().trim();
			if (emAd.isEmpty()) {
				new ErrorBox("You must enter an email address").display();
			} else {
				try {
					new LogInDaoImpl().addUserEmail(PlannerDb.getConnection(), emAd);
					this.displayAuthRedirectInfo(emailManager2);
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		});
		
		Scene scene = new Scene(popupForm.getFormBox());
		this.window.setScene(scene);
		this.window.show();
	}
	
	
	private void displayAuthRedirectInfo(EmailManager2 emailManager2) {	
		HBox hb = new HBox();
		hb.setPadding(new Insets(5));
		hb.setSpacing(5);
		hb.setAlignment(Pos.CENTER);
		// hb.prefWidthProperty().bind(this.window.widthProperty());
		
		Circle circle = new Circle(15);
		Stop[] stops = {new Stop(0.5, Color.YELLOW), new Stop(0.5, Color.AQUAMARINE)}; 
		LinearGradient linearGradient = new LinearGradient(0, 0, 1, 0, true, CycleMethod.NO_CYCLE, stops);
		circle.setFill(linearGradient);
		AnimationTimer rotateCircleAnimation = new AnimationTimer() {
			@Override
			public void handle(long now) {
				circle.setRotate(circle.getRotate() + 5);
			}
		};
		rotateCircleAnimation.start();
		hb.getChildren().add(circle);
		
		Label redirectInfoLabel = new Label("You will be re-directed to the Google sign in page shortly on your default browser." + 
				" Please follow the instructions on that page.");
		redirectInfoLabel.setWrapText(true);
		hb.getChildren().add(redirectInfoLabel);
		
		Timeline goToNextPart = new Timeline(new KeyFrame(Duration.seconds(5)));
		goToNextPart.setAutoReverse(false);
		goToNextPart.setCycleCount(1);
		goToNextPart.setOnFinished(event -> {
			goToNextPart.stop();
			rotateCircleAnimation.stop();
			this.displayAccessCodeField(emailManager2);
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
	
	
	private void displayAccessCodeField(EmailManager2 emailManager2) {		
		VBox formLayoutBox = new VBox();
		formLayoutBox.setSpacing(10);
		formLayoutBox.setPadding(new Insets(15));
		formLayoutBox.setAlignment(Pos.CENTER);
		
		Label instrLabel = new Label("Once you have accepted the permissions, you should see a long string of symbols. Please"
				+ " copy and paste that code into the text field below. THE ACCESS CODE WILL BE STORED IN THE DB NAKED."
				+ " THE ACCESS CODES, BOTH EXISTING AND NEW, WILL BE ENCRYPTED LATER.");
		instrLabel.setWrapText(true);
		instrLabel.setPadding(new Insets(0, 0, 10, 0));
		formLayoutBox.getChildren().add(instrLabel);
		
		GridPane gridPane = new GridPane();
		gridPane.setHgap(10);
		gridPane.setVgap(10);
		formLayoutBox.getChildren().add(gridPane);
		
		Label acLabel = new Label("Access Code:");
		gridPane.add(acLabel, 0, 0);
		TextField acField = new TextField();
		gridPane.add(acField, 1, 0);

		HBox codeActionBarBox = new HBox();
		codeActionBarBox.setAlignment(Pos.CENTER);
		codeActionBarBox.setSpacing(10);
		formLayoutBox.getChildren().add(codeActionBarBox);
		
		Button registerSaveBtn = new Button("Register and Save");
		registerSaveBtn.setOnAction(event -> {  
			String accessCode = acField.getText().trim();
			if (accessCode.isEmpty()) {
				new ErrorBox("You have not entered an access code").display();
			} else {
				try {
					AppSession.setIsTempAccessCode(false);
					new LogInDaoImpl().addGoogleAccessCode(PlannerDb.getConnection(), accessCode);
				} catch (SQLException e) {
					e.printStackTrace();
				} finally {
					Home.getInstance().display();
					this.window.close();
				}
			}
		});
		codeActionBarBox.getChildren().add(registerSaveBtn);
		
		Button registerTempBtn = new Button("Register Temporarily");
		registerTempBtn.setOnAction(event -> {  
			String accessCode = acField.getText().trim();
			if (accessCode.isEmpty()) {
				new ErrorBox("You have not entered an access code").display();
			} else {
				try {
					AppSession.setIsTempAccessCode(true);
					emailManager2.reqAuthToken(accessCode);
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					Home.getInstance().display();
					this.window.close();
				}
			}
		});
		codeActionBarBox.getChildren().add(registerTempBtn);
		
		Scene scene = new Scene(formLayoutBox);
		this.window.setScene(scene);
		this.window.show();
	}
}
