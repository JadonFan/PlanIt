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
// TODO split this class into two: one for DAO, one for UI
public class MyCourses {
	private static volatile MyCourses myCourses = null;
	private List<Course> currentCourses;
	
	
	public MyCourses() {
		this.currentCourses = new ArrayList<>();
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
	
	
	public void display() {
		try {
			display(new Stage());
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	public void display(Stage window) throws SQLException {		
		GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		final double goodWidthToDisplay = gd.getDisplayMode().getWidth() / 1.5;
		window.setWidth(goodWidthToDisplay);
		window.setMinWidth(goodWidthToDisplay); 
		window.setMaxWidth(goodWidthToDisplay); 
		window.setHeight(700);
		window.setTitle("My Courses");
		
		CourseForm courseForm = new CourseForm();
		
		VBox courseBox = new VBox();
		courseBox.setSpacing(10);
		
		// *** Top button bar ***
		courseBox.getChildren().add(this.buildTopBtnBox(courseForm, window));
		
		new CourseDaoImpl(this.currentCourses).loadCourses(PlannerDb.getConnection());
		for (Course crsDet : this.getCurrentCourses()) { 
			courseBox.getChildren().add(this.buildCourseInfoPane(crsDet, window));
		}

		Scene scene = new Scene(courseBox);
		window.setScene(scene);
		window.setOnCloseRequest(event -> {
			if (courseForm.getWindow().isShowing()) {
				courseForm.getWindow().close();
			}
		});
		window.show();
	}
	
	
	private HBox buildTopBtnBox(CourseForm courseForm, Stage window) {
		HBox btnTopBarBox = new HBox();
		btnTopBarBox.setSpacing(10);
		btnTopBarBox.setPadding(new Insets(10));
		btnTopBarBox.setMaxWidth(window.getWidth());
		btnTopBarBox.setStyle("-fx-background-color:#ADD8E6;");
		
		// "Add course" button
		// TODO direct to res directory
		// TODO map to a specific resources directory that is packaged with the JAR file for this project, and allow the user
		// to re-route the path of that directory
		// final String plusImgPath = Paths.get(System.getProperty("user.dir")).toString().concat("src/main/java/resources/add.png");
		ImageView plusImgView = new ImageView(new Image(getClass().getResourceAsStream("add.png"))); //$NON-NLS-1$
		plusImgView.setFitHeight(20);
		plusImgView.setFitWidth(20);
		
		Button addCourseBtn = new Button("Add"); 
		addCourseBtn.setGraphic(plusImgView);
		addCourseBtn.setMinWidth(100);
		addCourseBtn.setMaxWidth(100);
		addCourseBtn.setTooltip(CommonTooltipFactory.buildRectToolTip("Add a course"));
		addCourseBtn.setOnAction(event -> courseForm.display(this));
		btnTopBarBox.getChildren().add(addCourseBtn);
		
		// "Show assessment workflow" button
		// TODO map to a specific resources directory that is packaged with the JAR file for this project, and allow the user
		// to re-route the path of that directory
		// final String flowImgPath = Paths.get(System.getProperty("user.dir")).toString().concat("src/main/java/resources/workflow.png");
		ImageView workflowImgView = new ImageView(new Image(getClass().getResourceAsStream("workflow.png"))); //$NON-NLS-1$
		workflowImgView.setFitHeight(20);
		workflowImgView.setFitWidth(20);
		
		Button workflowBtn = new Button("Workflow"); 
		workflowBtn.setGraphic(workflowImgView);
		workflowBtn.setMinWidth(100);
		workflowBtn.setMaxWidth(100);
		workflowBtn.setTooltip(CommonTooltipFactory.buildRectToolTip("Show the workflow for the assessments"));
		workflowBtn.setOnAction(event -> new AssessmentWorkflow(this).display(window));		
		btnTopBarBox.getChildren().add(workflowBtn);
		
		return btnTopBarBox;
	}
	
	
	private StackPane buildCourseInfoPane(Course crsDet, Stage window) throws SQLException {
		new AssessmentDaoImpl(crsDet).loadAssessments(PlannerDb.getConnection());

		MaskedTextField crsText = new MaskedTextField(crsDet.toString(), Font.font("Times New Roman", FontWeight.BOLD, 32));
		Consumer<String> onEnterFunc = str -> {
			try {
				new CourseDaoImpl(this.currentCourses).editCourseIdentifier(PlannerDb.getConnection(), crsDet, str);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		};
		Predicate<String> isProperFormat = str -> Pattern.matches(Course.COURSE_IDENTIFIER_REGEX, str);
		crsText.configEditActions(isProperFormat, onEnterFunc);
		Rectangle rect = new Rectangle();
		rect.setWidth(window.getWidth() - 200);
		
		Button addAstmtBtn = new Button("Add Astmt");
		addAstmtBtn.setOnAction(event -> {
			try {
				new AssessmentForm().display(this, crsDet);
				new AssessmentDaoImpl(crsDet).loadAssessments(PlannerDb.getConnection());
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			event.consume();
		});
		
		Button deleteCourseBtn = new Button("Remove Course");
		deleteCourseBtn.setOnAction(event -> {
			try {
				new CourseDaoImpl(this.currentCourses).deleteCourse(PlannerDb.getConnection(), crsDet);
				new CourseDaoImpl(this.currentCourses).loadCourses(PlannerDb.getConnection());
				this.display(window);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			event.consume();
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
		coursePane.getChildren().addAll(rect, vb);
		// Move to a css file
		coursePane.setStyle("-fx-border-color: black; \n" +
							"-fx-border-insets: 5; \n" +
							"-fx-border-width: 2; \n"); //$NON-NLS-1$
		
		return coursePane;	
	}
}
