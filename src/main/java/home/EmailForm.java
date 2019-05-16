package home;

import alerts.ConfirmBox;
import application.EmailManager2;
import calendar.Event;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import user.Email;

public class EmailForm {
	public class SemiRectLabel extends Label {
		public SemiRectLabel() {
			super();
			this.skinLabel();
		}
		
		public SemiRectLabel(String text) {
			super(text);
			this.skinLabel();
		}
		
		public void skinLabel() {
			super.setBorder(new Border(new BorderStroke(Color.GREY, BorderStrokeStyle.SOLID, 
					new CornerRadii(15), BorderWidths.DEFAULT)));
			super.setPadding(new Insets(5));
			super.setAlignment(Pos.CENTER);
			super.setPrefHeight(30);
			super.setPrefWidth(120);
		}
	}
	
	
	private TextField recipientsField;
	private TextField emailTitleField;
	private TextArea bodyTextArea;
	private Button sendEmailBtn;

	private EmailManager2 emailManager2;
	private Stage window;
	
	
	public EmailForm(EmailManager2 emailManager2) {
		this.emailManager2 = emailManager2;
		this.window = new Stage();
	}
	
	public EmailForm(EmailManager2 emailManager2, Stage window) {
		this.emailManager2 = emailManager2;
		this.window = window;
	}
	
	
	public EmailManager2 getEmailManager2() {
		return this.emailManager2;
	}
	
	public Stage getWindow() {
		return this.window;
	}
	
	
	public GridPane buildEmailHeaderPart() {
		GridPane gridPane = new GridPane();
		gridPane.setHgap(10);
		gridPane.setVgap(10);
		
		ColumnConstraints leftCol = new ColumnConstraints();
		leftCol.setHgrow(Priority.SOMETIMES);
		ColumnConstraints rightCol = new ColumnConstraints();
		rightCol.setHgrow(Priority.ALWAYS);
		gridPane.getColumnConstraints().addAll(leftCol, rightCol);
		
		Label askRecipientsLabel = new Label("Recipients:");
		gridPane.add(askRecipientsLabel, 0, 0);
		
		this.recipientsField = new TextField();
		gridPane.add(this.recipientsField, 1, 0);
		
		Label askTitleLabel = new Label("Title:");
		gridPane.add(askTitleLabel, 0, 1);
		
		this.emailTitleField = new TextField();		
		gridPane.add(this.emailTitleField, 1, 1);
		
		return gridPane;
	}
	
	
	public VBox buildEmailBodyPart(StringBuilder sb) {
		VBox emailBodyPartBox = new VBox();
		
		this.bodyTextArea = new TextArea(sb.toString());
		emailBodyPartBox.getChildren().add(this.bodyTextArea);
		
		return emailBodyPartBox;
	}
	
	
	public HBox buildSendEmailBox(StringBuilder sb) {
		HBox buttonWrapper = new HBox();
		buttonWrapper.setAlignment(Pos.CENTER);		
		this.sendEmailBtn = new Button("Send"); // TODO move the button set up stuff to a separate method
		this.sendEmailBtn.setAlignment(Pos.CENTER);
		this.sendEmailBtn.setOnAction(event -> {
			ConfirmBox confirmSend = new ConfirmBox("Send Email?", "Are you sure you want to send this email?");
			if (confirmSend.display()) {				
				Email email = new Email(this.recipientsField.getText().split(","), this.emailTitleField.getText(), 
					sb.toString());
				this.emailManager2.sendEmail(email);				
			}
			this.window.close();
		});
		buttonWrapper.getChildren().add(this.sendEmailBtn);
		
		return buttonWrapper;
	}
	
	
	public HBox buildSuggestionBox() {
		HBox suggestionBox = new HBox();
		suggestionBox.setSpacing(10);
		suggestionBox.setPadding(new Insets(0, 0, 10, 0));
		
		SemiRectLabel urgentEventsLabel = new SemiRectLabel("Urgent Events");
		urgentEventsLabel.setOnMouseClicked(event -> {
			StringBuilder urgentEventsStrBuilder = new StringBuilder();
			for (Event urgentEvent : Home.findUrgentEvents()) {
				if (urgentEvent != null) {
					// 0x2022 is the Unicode encoding for the bullet point 
					urgentEventsStrBuilder.append(String.format("%c %s due in %d days \n", (char) 0x2022, 
							urgentEvent.getName(), urgentEvent.findRemainingDays()));
				}
			}
			this.bodyTextArea.setText(this.bodyTextArea.getText().concat(urgentEventsStrBuilder.toString()));
		});
		suggestionBox.getChildren().add(urgentEventsLabel);
		
		SemiRectLabel chandlerLabel = new SemiRectLabel("Chandler Lei");
		chandlerLabel.setOnMouseClicked(event -> {
			String chandlerName = String.format("Chandler Lei %c%c \n", (char) 0x96F7, (char) 0x8FC1);
			this.bodyTextArea.setText(this.bodyTextArea.getText().concat(chandlerName));
		});
		suggestionBox.getChildren().add(chandlerLabel);
		
		return suggestionBox;
	}
	
	public void displayEmailReqForm(StringBuilder sb) {
		if (sb == null) {
			sb = new StringBuilder();
		}
		
		VBox emailFormLayoutBox = new VBox();
		emailFormLayoutBox.setPadding(new Insets(10));
		emailFormLayoutBox.setSpacing(10);
		
		emailFormLayoutBox.getChildren().addAll(this.buildEmailHeaderPart(), this.buildEmailBodyPart(sb), 
				this.buildSuggestionBox(), this.buildSendEmailBox(sb));
		
		Scene scene = new Scene(emailFormLayoutBox);
		this.window.setScene(scene);
		this.window.show();
	}
 }
