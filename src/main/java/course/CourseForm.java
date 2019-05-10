package course;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import alerts.ErrorBox;
import ui.PopupForm;
import application.PlannerDb;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import utility.CommonUtils;

public class CourseForm {
	private Stage window = new Stage();

	
	public Stage getWindow() {
		return this.window;
	}
	
	
	private boolean hasErroneousFields(TextField[] textFields) {
		Set<TextField> erroneousFields = new HashSet<>();

		// TODO in a separate method, change the border of a erroneous text field to red.
		TextField crsCodeField = textFields[2];
		if (!Pattern.matches("\\d{3}", crsCodeField.getText())) {
			new ErrorBox("Invalid Input: The course code must be 3 digits long").display(); 
			erroneousFields.add(crsCodeField);
		} 
		
		TextField crsNoField = textFields[0];
		if (CommonUtils.isEmptyOrNull(crsNoField.getText())) {
			new ErrorBox("Invalid Input: The course number cannot be empty");
			erroneousFields.add(crsNoField);
		}
		
		TextField crsSubjectField = textFields[1];
		if (CommonUtils.isEmptyOrNull(crsSubjectField.getText())) {
			new ErrorBox("Invalid Input: The course subject cannot be empty");
			erroneousFields.add(crsSubjectField);
		}
		
		return erroneousFields.size() != 0;
	}
	
	
	public void display(MyCourses myCourses) {
		List<Label> labels = new ArrayList<>(List.of(new Label("Course Number:"), new Label("Subject:"), new Label("Course Code:"),
				new Label("Course Title:")));
		Button doneButton = new Button("Done");
		PopupForm popupForm = new PopupForm(labels, doneButton);
		
		popupForm.mapLayout(this.window);
		
		// TODO have the button do nothing on action if the value in any of the text fields violate a SQL constraint
		doneButton.setOnAction(event -> {  
			TextField[] textFields = popupForm.getTextFields();
			
			if (this.hasErroneousFields(textFields)) {
				return;
			}
			
			int crsNo = Integer.parseInt(textFields[0].getText());
			String crsSubject = textFields[1].getText();
			short crsCode = Short.parseShort(textFields[2].getText()); 
			String crsTitle = textFields[3].getText();
			
			try {
				new CourseDaoImpl(myCourses.getCurrentCourses()).addCourse(PlannerDb.getConnection(), 
						new Course(crsNo, crsSubject, crsCode, crsTitle));
				new CourseDaoImpl(myCourses.getCurrentCourses()).loadCourses(PlannerDb.getConnection());
			} catch (SQLIntegrityConstraintViolationException e) { // XXX better to use IntegrityConstraintViolationException in Spring?
				new ErrorBox("The course was already added!").display();
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			this.window.close();
			myCourses.display();
			event.consume();
		});
		
		
		
		Scene scene = new Scene(popupForm.getFormBox());
		this.window.setScene(scene);
		this.window.show();
	}
}
