package home;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

import alerts.ErrorBox;
import ui.PopupForm;
import application.PlannerDb;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

class RegistrationForm {
	private Stage window;
	
	
	public void display() {
		this.window = new Stage();
		
		List<Label> labels = new ArrayList<>(List.of(new Label("Student ID:"), new Label("Username:"), new Label("Password:")));
		Button registerBtn = new Button("Register");
		PopupForm popupForm = new PopupForm(labels, registerBtn);
		
		popupForm.mapLayout(this.window);
		
		// TODO have the button do nothing on action if the value in any of the text fields violate a SQL constraint
		registerBtn.setOnAction(event -> {  
			TextField[] textFields = popupForm.getTextFields();
			int studentId = Integer.parseInt(textFields[0].getText());
			String username = textFields[1].getText();
			String unhashedPassword = textFields[2].getText();
			
			try {
				new LogInDaoImpl().registerUser(PlannerDb.getConnection(), studentId, username, unhashedPassword);
			} catch (SQLIntegrityConstraintViolationException e) { // TODO implement and use an inspection method to check 
																   // uniqueness rather than depending on this exception
				new ErrorBox("The username is taken. Please choose a new one.").display();
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			this.window.close();
			event.consume();
		});
		
		
		Scene scene = new Scene(popupForm.getFormBox());
		this.window.setScene(scene);
		this.window.show();
	}
}
