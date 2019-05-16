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
import javafx.geometry.Pos;
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
import ui.CommonUi;
import ui.PopupForm;

@SuppressWarnings("synthetic-access")
public class AssessmentForm {
	private Stage window = new Stage();
	private static final List<Label> labels = new ArrayList<>();
	private static final Map<AssessmentType, List<Label>> labelMap = new HashMap<>();	
	private static final String[] LABEL_TEXTS = new String[] {"Name", "Weight", "Component", "Submission Method", "Location"}; 

	private TextField[] textFields = new TextField[LABEL_TEXTS.length];
	private JFXDatePicker datePicker;
	private JFXTimePicker timePicker;
	private ToggleGroup astmtTypeToggleGrp;
	
	
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
	
	
	public GridPane buildAstmtTypeTogglePane() {		
		this.astmtTypeToggleGrp = new ToggleGroup();
		this.astmtTypeToggleGrp.selectedToggleProperty().addListener((change, oldToggle, currToggle) -> {
			AssessmentType astmtType = AssessmentType.valueOf(((RadioButton) currToggle).getText()); 
			CommonUi.disableUnusedFields(AssessmentForm.labelMap.getOrDefault(astmtType, null), AssessmentForm.labels, this.textFields);
		});
		
		Pair<GridPane, RadioButton[]> astmtTypeBtns = CommonUi.buildStdRadioGrp(this.astmtTypeToggleGrp, AssessmentType.reprAllAstmtTypes());
		PopupForm.skinGridPane(astmtTypeBtns.getKey());
		astmtTypeBtns.getKey().setAlignment(Pos.CENTER_LEFT);
		
		return astmtTypeBtns.getKey();
	}
	
	
	public GridPane buildAstmtDetailPane() {
		GridPane astmtDetailPane = new GridPane();
		PopupForm.skinGridPane(astmtDetailPane);
		
		for (int i = 0; i < LABEL_TEXTS.length; i++) {
			Label label = new Label(LABEL_TEXTS[i]);
			astmtDetailPane.add(label, 0, i);
			TextField textField = new TextField();
			astmtDetailPane.add(textField, 1, i);
			this.textFields[i] = textField;
		}
		
		Label dateLabel = new Label("Date");
		this.datePicker = new JFXDatePicker();
		astmtDetailPane.add(dateLabel, 0, LABEL_TEXTS.length);
		astmtDetailPane.add(this.datePicker, 1, LABEL_TEXTS.length);
		
		Label timeLabel = new Label("Time");
		this.timePicker = new JFXTimePicker();
		this.timePicker.set24HourView(true);
		astmtDetailPane.add(timeLabel, 0, LABEL_TEXTS.length + 1);
		astmtDetailPane.add(this.timePicker, 1, LABEL_TEXTS.length + 1);
		
		return astmtDetailPane;
	}
	
	
	public void display(MyCourses myCourses, Course course) {	
		this.window.setWidth(400);
				
		VBox formLayoutBox = new VBox();
		PopupForm.skinFormBox(formLayoutBox, this.window);
		
		Text selectToggleText = new Text("1. Select the type of assessment");
		formLayoutBox.getChildren().add(selectToggleText);
		
		GridPane astmtTypeTogglePane = this.buildAstmtTypeTogglePane();
		formLayoutBox.getChildren().add(astmtTypeTogglePane);
		
		Separator separator = new Separator();
		Text enterValueText = new Text("2. Enter the details for this assessment");
		formLayoutBox.getChildren().addAll(separator, enterValueText);
		
		GridPane astmtDetailPane = this.buildAstmtDetailPane();
		formLayoutBox.getChildren().add(astmtDetailPane);
		
		Button doneButton = new Button("Done");
		doneButton.setOnAction(event -> {  			
			if (this.hasErroneousFields()) {
				return;
			}
			
			@SuppressWarnings("unused")
			AssessmentType astmtType = AssessmentType.valueOf(((RadioButton) this.astmtTypeToggleGrp.getSelectedToggle()).getText());
			
			String astmtName = this.textFields[0].getText();
			float astmtWeight = Float.parseFloat(this.textFields[1].getText());
			
			String astmtDate = this.datePicker.getEditor().getText();
			String astmtTime = this.timePicker.getEditor().getText();
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
		formLayoutBox.getChildren().add(doneButton);
		
		Scene scene = new Scene(formLayoutBox);
		this.window.setScene(scene);
		this.window.show();
	}
}
