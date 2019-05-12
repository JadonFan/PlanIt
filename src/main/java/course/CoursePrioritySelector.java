package course;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import ui.StandardSlider;

public class CoursePrioritySelector {
	private Stage window;
	
	
	public CoursePrioritySelector() {
		this.window = new Stage();
	}
	
	
	public Stage getWindow() {
		return this.window;
	}
	
	
	public void assignCrsPriority(Course course, double astmtDuePriority, double astmtCountPriority) {
		if (astmtDuePriority > 1)	 astmtDuePriority /= 100;
		if (astmtCountPriority > 1)  astmtCountPriority /= 100;
		
		double astmtDueBalance = 0;
		Assessment earliestAstmt = course.findEarliestAssessment();
		if (earliestAstmt != null) {
			astmtDueBalance = astmtDuePriority  / course.findEarliestAssessment().findRemainingDays();
		}
		
		double astmtCountBalance = astmtCountPriority * course.getAssessments().stream().filter(x -> x.getWeighting() > 0).count();
		
		// TODO use big decimal???
		course.setPriority((float) (astmtDueBalance + astmtCountBalance));
	}
	
	
	public void displaySelectorScreen(MyCourses myCourses) {
		this.window.setWidth(500);
		
		GridPane selectorPane = new GridPane();
		selectorPane.setHgap(50);
		selectorPane.setVgap(20);
		selectorPane.setPadding(new Insets(25));
		selectorPane.setAlignment(Pos.CENTER);
		
		Label astmtDuePriorityLabel = new Label("Earliest Due Date");
		Label astmtCountPriorityLabel = new Label("Assessment Count");
		GridPane.setConstraints(astmtDuePriorityLabel, 0, 0);
		GridPane.setConstraints(astmtCountPriorityLabel, 0, 1);
		selectorPane.getChildren().addAll(astmtDuePriorityLabel, astmtCountPriorityLabel);

		StandardSlider astmtDuePrioritySlider = new StandardSlider();
		StandardSlider astmtCountPrioritySlider = new StandardSlider();
		GridPane.setConstraints(astmtDuePrioritySlider, 1, 0);
		GridPane.setConstraints(astmtCountPrioritySlider, 1, 1);
		selectorPane.getChildren().addAll(astmtDuePrioritySlider, astmtCountPrioritySlider);

		Text astmtDuePriorityText = new Text("0%");
		Text astmtCountPriorityText = new Text("0%");
		GridPane.setConstraints(astmtDuePriorityText, 2, 0);
		GridPane.setConstraints(astmtCountPriorityText, 2, 1);
		selectorPane.getChildren().addAll(astmtDuePriorityText, astmtCountPriorityText);
		
		Button doneBtn = new Button("Sort");
		doneBtn.setOnAction(event -> {
			myCourses.getCurrentCourses().forEach(x -> this.assignCrsPriority(x, astmtDuePrioritySlider.getValue(), 
					astmtCountPrioritySlider.getValue()));
			myCourses.sortCoursesByDescPriority();
			myCourses.display(false);
		});
		selectorPane.add(doneBtn, 1, 2);
		
		// TODO ensure that the slider values add up to 100%
		astmtDuePrioritySlider.valueProperty().addListener((ov, oldValue, newValue) -> {
			astmtDuePriorityText.setText(Integer.toString(newValue.intValue()).concat("%"));
		});
		astmtCountPrioritySlider.valueProperty().addListener((ov, oldValue, newValue) -> {
			astmtCountPriorityText.setText(Integer.toString(newValue.intValue()).concat("%"));
		});
				
		Scene scene = new Scene(selectorPane);
		this.window.setScene(scene);
		this.window.show();
	}
}
