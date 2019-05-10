package course;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTimePicker;

import alerts.ErrorBox;
import application.PlannerDb;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Pair;
import ui.CommonToggleGroupFactory;
import ui.PopupForm;

public class AssessmentForm {
	private Stage window = new Stage();
	private List<Label> labels;
	private Map<AssessmentType, List<Label>> labelMap;	
	
	@SuppressWarnings("synthetic-access")
	public AssessmentForm() {
		this.labels = new ArrayList<>();
		this.labelMap = new HashMap<>();
				
		String[] LabelTexts = new String[] {"Name", "Weight", "Component", "Submission Method", "Location"}; 
		
		for (String labelText : LabelTexts) {
			this.labels.add(new Label(labelText));
		}
		
		this.labelMap.put(AssessmentType.ASSIGNMENT, new ArrayList<>() {
			private static final long serialVersionUID = 1L;
			{
				add(AssessmentForm.this.labels.get(0));
				add(AssessmentForm.this.labels.get(1));
				add(AssessmentForm.this.labels.get(3));
			}
		});
		
		this.labelMap.put(AssessmentType.EXAMINATION, new ArrayList<>() {
			private static final long serialVersionUID = 1L;
			{
				add(AssessmentForm.this.labels.get(0));
				add(AssessmentForm.this.labels.get(1));
				add(AssessmentForm.this.labels.get(2));
				add(AssessmentForm.this.labels.get(4));
			}
		});		
	}
	
	
	public Stage getWindow() {
		return this.window;
	}
	
	
	// TODO move to the ui package in a suitable class
	public static void disableUnusedFields(List<Label> relevantLabels, List<Label> allLabels, TextField[] allTextFields) {
		if (relevantLabels == null) {
			return;
		}
		
		for (int i = 0; i < allLabels.size(); i++) {
			// TODO use label name to check, rather than the default contains method
			if (relevantLabels.contains(allLabels.get(i))) {
				allTextFields[i].setDisable(false);
			} else {
				allTextFields[i].setDisable(true);
				allTextFields[i].setStyle(""); // TODO get darker shade of grey to make it more clear that the field is disabled
			}
		}
	}
	
	
	@SuppressWarnings("unused")
	private boolean hasErroneousFields(TextField[] textFields) {
		Set<TextField> erroneousFields = new HashSet<>();
		
		return erroneousFields.size() != 0;
	}
	
	
	public void display(MyCourses myCourses, Course course) {	
		Button doneButton = new Button("Done");
		PopupForm popupForm = new PopupForm(this.labels, doneButton);
		
		ToggleGroup group = new ToggleGroup();
		group.selectedToggleProperty().addListener((change, oldToggle, currToggle) -> {
			AssessmentType astmtType = AssessmentType.valueOf(((RadioButton) currToggle).getText()); 
			AssessmentForm.disableUnusedFields(this.labelMap.getOrDefault(astmtType, null), this.labels, popupForm.getTextFields());
		});
		Pair<VBox, RadioButton[]> astmtTypeBtns = CommonToggleGroupFactory.buildStdRadioGrp(group, AssessmentType.reprAllAstmtTypes());
		
		
		// *** TODO use a separate method and not the popup form class ***
		Label dateLbl = new Label("Date");
		JFXDatePicker datePicker = new JFXDatePicker();
		Label timeLbl = new Label("Time");
		JFXTimePicker timePicker = new JFXTimePicker();
		timePicker.set24HourView(true);
		List<Pair<Label, Control>> extraControls = new ArrayList<>();
		extraControls.add(new Pair<>(dateLbl, datePicker));
		extraControls.add(new Pair<>(timeLbl, timePicker));
		
		popupForm.getFormBox().getChildren().addAll(astmtTypeBtns.getKey());
		popupForm.mapLayout(this.window, extraControls);
		this.window.setWidth(400);
		// ****************************************************************
		
		doneButton.setOnAction(event -> {  
			TextField[] textFields = popupForm.getTextFields();
			
			if (this.hasErroneousFields(textFields)) {
				return;
			}
			
			@SuppressWarnings("unused")
			AssessmentType astmtType = null;
			if (astmtTypeBtns.getValue().length > 0) {
				astmtType = AssessmentType.valueOf(((RadioButton) astmtTypeBtns.getValue()[0].getToggleGroup().getSelectedToggle()).getText());
			}
			
			String astmtName = textFields[0].getText();
			float astmtWeight = Float.parseFloat(textFields[1].getText());
			
			String astmtDate = datePicker.getEditor().getText();
			String astmtTime = timePicker.getEditor().getText();
			Calendar jCalDueDate = new GregorianCalendar();
			
			try {
				Date dueDate = new SimpleDateFormat("yyyy-MM-dd K:mm a").parse(astmtDate + " " + astmtTime);
				jCalDueDate.setTime(dueDate);
				new AssessmentDaoImpl(course).addAssessment(PlannerDb.getConnection(), new java.sql.Timestamp(dueDate.getTime()), 
						new Assignment(astmtName, jCalDueDate, astmtWeight));
				new AssessmentDaoImpl(course).loadAssessments(PlannerDb.getConnection());
			} catch (SQLIntegrityConstraintViolationException e) { // XXX better to use IntegrityConstraintViolationException in Spring?
				new ErrorBox("The course was already added!").display();
				e.printStackTrace();
			} catch (SQLException | ParseException e) {
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
