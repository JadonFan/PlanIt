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
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Pair;
import ui.CommonToggleGroupFactory;
import ui.CommonUi;
import ui.PopupForm;

@SuppressWarnings("synthetic-access")
public class AssessmentForm {
	private Stage window = new Stage();
	private static final List<Label> labels = new ArrayList<>();
	private static final Map<AssessmentType, List<Label>> labelMap = new HashMap<>();	
	private static final String[] LABEL_TEXTS = new String[] {"Name", "Weight", "Component", "Submission Method", "Location"}; 

	
	static {
		for (String labelText : LABEL_TEXTS) {
			labels.add(new Label(labelText));
		}
		
		labelMap.put(AssessmentType.ASSIGNMENT, new ArrayList<>() {
			private static final long serialVersionUID = 1L;
			{
				add(AssessmentForm.labels.get(0));
				add(AssessmentForm.labels.get(1));
				add(AssessmentForm.labels.get(3));
			}
		});
		
		labelMap.put(AssessmentType.EXAMINATION, new ArrayList<>() {
			private static final long serialVersionUID = 1L;
			{
				add(AssessmentForm.labels.get(0));
				add(AssessmentForm.labels.get(1));
				add(AssessmentForm.labels.get(2));
				add(AssessmentForm.labels.get(4));
			}
		});		
	}
	
	
	public Stage getWindow() {
		return this.window;
	}
	
	
	private boolean hasErroneousFields() {
		Set<TextField> erroneousFields = new HashSet<>();
		
		return erroneousFields.size() != 0;
	}
	
	
	public void display(MyCourses myCourses, Course course) {	
		this.window.setWidth(400);
		TextField[] textFields = new TextField[LABEL_TEXTS.length];
		
		VBox formBoxLayout = new VBox();
		GridPane formPane = new GridPane();
		PopupForm.skinFormLayout(formBoxLayout, formPane, this.window);
				
		Text selectToggleText = new Text("1. Select the type of assessment");
		formBoxLayout.getChildren().add(selectToggleText);
		
		ToggleGroup group = new ToggleGroup();
		group.selectedToggleProperty().addListener((change, oldToggle, currToggle) -> {
			AssessmentType astmtType = AssessmentType.valueOf(((RadioButton) currToggle).getText()); 
			CommonUi.disableUnusedFields(AssessmentForm.labelMap.getOrDefault(astmtType, null), AssessmentForm.labels, textFields);
		});
		Pair<GridPane, RadioButton[]> astmtTypeBtns = CommonToggleGroupFactory.buildStdRadioGrp(group, AssessmentType.reprAllAstmtTypes());
		formBoxLayout.getChildren().add(astmtTypeBtns.getKey());
		
		Separator separator = new Separator();
		Text enterValueText = new Text("2. Enter the details for this assessment");
		formBoxLayout.getChildren().addAll(separator, enterValueText);
		
		for (int i = 0; i < LABEL_TEXTS.length; i++) {
			Label label = new Label(LABEL_TEXTS[i]);
			formPane.add(label, 0, i);
			TextField textField = new TextField();
			formPane.add(textField, 1, i);
			textFields[i] = textField;
		}
		
		Label dateLbl = new Label("Date");
		formPane.add(dateLbl, 0, LABEL_TEXTS.length);
		JFXDatePicker datePicker = new JFXDatePicker();
		formPane.add(datePicker, 1, LABEL_TEXTS.length);
		
		Label timeLbl = new Label("Time");
		formPane.add(timeLbl, 0, LABEL_TEXTS.length + 1);
		JFXTimePicker timePicker = new JFXTimePicker();
		timePicker.set24HourView(true);
		formPane.add(timePicker, 1, LABEL_TEXTS.length + 1);
		
		formBoxLayout.getChildren().add(formPane);
		
		Button doneButton = new Button("Done");
		formBoxLayout.getChildren().add(doneButton);
		doneButton.setOnAction(event -> {  			
			if (this.hasErroneousFields()) {
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
			} catch (SQLIntegrityConstraintViolationException e) { 
				new ErrorBox("The course was already added!").display();
				e.printStackTrace();
			} catch (SQLException | ParseException e) {
				e.printStackTrace();
			}
			
			this.window.close();
			myCourses.display(true);
			event.consume();
		});		
		
		Scene scene = new Scene(formBoxLayout);
		this.window.setScene(scene);
		this.window.show();
	}
}
