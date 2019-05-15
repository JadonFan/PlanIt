package course;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.regex.Pattern;

import application.PlannerDb;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import ui.CommonTooltipFactory;
import ui.MaskedTextField;

// TODO As part of code re-factoring, provide more consistency in how windows are transferred between classes
public class MyCourses {
	private static volatile MyCourses myCourses = null;
	private Stage window;
	private List<Course> currentCourses;
	
	
	private MyCourses() {
		this.currentCourses = new ArrayList<>();
		this.window = new Stage();
	}
	
	public static MyCourses getInstance() {
		if (MyCourses.myCourses == null) {
			MyCourses.myCourses = new MyCourses();
		}
		
		return MyCourses.myCourses;
	}
	
	
	public List<Course> getCurrentCourses() {
		return this.currentCourses;
	}
	
	
	public Course getCourseById(int crsNo) throws SQLException {
		Course targetCourse = null;
		
		for (Course currentCourse : this.getCurrentCourses()) {
			if (currentCourse.getCrsNo() == crsNo) {
				targetCourse = currentCourse;
				break;
			}
		}
		
		return targetCourse;
	}
	
	
	// Using the last element of the "non-sorted" partition of the list as the pivot...
	private int partitionForQuickSort(int low, int high) {
		float pivotValue = this.currentCourses.get(high).getPriority();
		int i = low - 1;
		for (int j = low; j < high; j++) {
			if (this.currentCourses.get(j).getPriority() > pivotValue) {	
				i++;

				Course temp = this.currentCourses.get(i);
				this.currentCourses.set(i, this.currentCourses.get(j));
				this.currentCourses.set(j, temp);
			}
		}
		
		Course temp = this.currentCourses.get(i + 1);
		this.currentCourses.set(i + 1, this.currentCourses.get(high));
		this.currentCourses.set(high, temp);
		
		return i + 1;
	}
	
	
	/**sortCoursesByAscPriority
	 * Sort all the courses in this MyCourses object by <strong>DESCENDING</strong> priority 
	 */
	public void sortCoursesByDescPriority() {
		this.sortCoursesByDescPriority(0, this.currentCourses.size() - 1);
	}
	
	
	/**
	 * Sort some courses in this MyCourses object by <strong>DESCENDING</strong> priority 
	 * @param low the lower bounded index in the list of courses to be stored
	 * @param high the upper bounded index in the list of courses to be stored
	 */
	public void sortCoursesByDescPriority(int low, int high) {
		if (low < high) {
			int partitionIndex = this.partitionForQuickSort(low, high);
			this.sortCoursesByDescPriority(low, partitionIndex - 1);
			this.sortCoursesByDescPriority(partitionIndex + 1, high);
		}
	}
	
	
	public void display(boolean reload) {		
		GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		final double goodWidthToDisplay = gd.getDisplayMode().getWidth() / 1.5;
		// this.window.setWidth(goodWidthToDisplay);
		this.window.setMinWidth(goodWidthToDisplay); 
		this.window.setMaxWidth(goodWidthToDisplay); 
		this.window.setHeight(700);
		this.window.setTitle("My Courses");
		
		CourseForm courseForm = new CourseForm();
		CoursePrioritySelector coursePrioritySelector = new CoursePrioritySelector();
		
		VBox courseBox = new VBox();
		courseBox.setSpacing(10);
		ScrollPane coursePane = new ScrollPane(courseBox);
		coursePane.setFitToWidth(true);
		coursePane.setVbarPolicy(ScrollBarPolicy.AS_NEEDED);
		coursePane.setHbarPolicy(ScrollBarPolicy.NEVER);
		coursePane.setStyle("-fx-background-color:transparent;");

		// *** Top button bar ***
		courseBox.getChildren().add(this.buildTopBtnBox(courseForm, coursePrioritySelector));
		
		try {
			if (reload) {
				new CourseDaoImpl(this.currentCourses).loadCourses(PlannerDb.getConnection());
			}
			for (Course crsDet : this.currentCourses) { 
				courseBox.getChildren().add(this.buildCourseInfoPane(crsDet));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		Scene scene = new Scene(coursePane);
		this.window.setScene(scene);
		this.window.setOnCloseRequest(event -> {
			if (courseForm.getWindow().isShowing()) {
				courseForm.getWindow().close();
			}
			if (coursePrioritySelector.getWindow().isShowing()) {
				coursePrioritySelector.getWindow().close();
			}
		});
		this.window.show();
	}
	
	
	private HBox buildTopBtnBox(CourseForm courseForm, CoursePrioritySelector coursePrioritySelector) {
		HBox btnTopBarBox = new HBox();
		btnTopBarBox.setSpacing(10);
		btnTopBarBox.setPadding(new Insets(10));
		btnTopBarBox.setMinWidth(this.window.getWidth());
		// btnTopBarBox.setMaxWidth(this.window.getWidth());
		btnTopBarBox.setStyle("-fx-background-color:#ADD8E6;");
		
		// "Add course" button
		ImageView plusImgView = new ImageView(new Image(getClass().getResourceAsStream("/add.png"))); //$NON-NLS-1$
		plusImgView.setFitHeight(20);
		plusImgView.setFitWidth(20);
		
		Button addCourseBtn = new Button("Add"); 
		addCourseBtn.setGraphic(plusImgView);
		addCourseBtn.setMinWidth(100);
		addCourseBtn.setMaxWidth(100);
		addCourseBtn.setTooltip(CommonTooltipFactory.buildRectToolTip("Add a course"));
		addCourseBtn.setOnAction(event -> courseForm.display(this));
		btnTopBarBox.getChildren().add(addCourseBtn);
		
		// "Sort courses" button
		ImageView sortImgView = new ImageView(new Image(getClass().getResourceAsStream("/sort.png"))); //$NON-NLS-1$
		sortImgView.setFitHeight(20);
		sortImgView.setFitWidth(20);
		
		Button sortBtn = new Button("Sort"); 
		sortBtn.setGraphic(sortImgView);
		sortBtn.setMinWidth(100);
		sortBtn.setMaxWidth(100);
		sortBtn.setTooltip(CommonTooltipFactory.buildRectToolTip("Sort my courses by priority"));
		sortBtn.setOnAction(event -> coursePrioritySelector.displaySelectorScreen(this));		
		btnTopBarBox.getChildren().add(sortBtn);
		
		Pane spacer = new Pane();
		spacer.setPrefSize(50, 1);
		btnTopBarBox.getChildren().add(spacer);
		
		// "Show assessment workflow" button
		ImageView workflowImgView = new ImageView(new Image(getClass().getResourceAsStream("/workflow.png"))); //$NON-NLS-1$
		workflowImgView.setFitHeight(20);
		workflowImgView.setFitWidth(20);
		
		Button workflowBtn = new Button("Workflow"); 
		workflowBtn.setGraphic(workflowImgView);
		workflowBtn.setMinWidth(100);
		workflowBtn.setMaxWidth(100);
		workflowBtn.setTooltip(CommonTooltipFactory.buildRectToolTip("Show the workflow for my ongoing assessments"));
		workflowBtn.setOnAction(event -> new AssessmentWorkflow(this).display(this.window));		
		btnTopBarBox.getChildren().add(workflowBtn);
		
		return btnTopBarBox;
	}
	
	
	private StackPane buildCourseInfoPane(Course crsDet) throws SQLException {
		new AssessmentDaoImpl(crsDet).loadAssessments(PlannerDb.getConnection());

		MaskedTextField crsText = new MaskedTextField(crsDet.toString(), Font.font("Times New Roman", FontWeight.BOLD, 32));
		Consumer<String> onEnterFunc = str -> {
			try {
				new CourseDaoImpl(this.currentCourses).editCourseName(PlannerDb.getConnection(), crsDet, str);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		};
		Predicate<String> isProperFormat = str -> Pattern.matches(Course.COURSE_IDENTIFIER_REGEX, str);
		crsText.configEditActions(isProperFormat, onEnterFunc);

		Button addAstmtBtn = new Button("Add Astmt");
		addAstmtBtn.setOnAction(event -> {
			try {
				new AssessmentForm().display(this, crsDet);
				new AssessmentDaoImpl(crsDet).loadAssessments(PlannerDb.getConnection());
			} catch (SQLException e) {
				e.printStackTrace();
			}			
		});
		
		Button deleteCourseBtn = new Button("Remove Course");
		deleteCourseBtn.setOnAction(event -> {
			try {
				new CourseDaoImpl(this.currentCourses).deleteCourse(PlannerDb.getConnection(), crsDet);
				this.display(true);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		});
		
		Pane spacer = new Pane();
		spacer.setMinSize(50, 1);
		
		HBox hb = new HBox();	
		HBox.setHgrow(spacer, Priority.ALWAYS);
		hb.setSpacing(25);
		hb.getChildren().addAll(crsText, spacer, addAstmtBtn, deleteCourseBtn);
		
		VBox vb = new VBox();
		vb.setPadding(new Insets(10));
		vb.setSpacing(10);
		vb.getChildren().add(hb);
		crsDet.skinAssessmentPane(vb);
		
		StackPane coursePane = new StackPane();
		Rectangle rect = new Rectangle();
		rect.setWidth(this.window.getWidth() - 200);
		coursePane.getChildren().addAll(rect, vb);
		// Move to a css file
		coursePane.setStyle("-fx-border-color: black; \n" +
							"-fx-border-insets: 5; \n" +
							"-fx-border-width: 2;"); //$NON-NLS-1$
		
		return coursePane;	
	}
}
