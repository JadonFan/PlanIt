package home;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import application.CloseProgram;
import application.PlannerDb;
import calendar.Event;
import course.AssessmentDaoImpl;
import course.Course;
import course.CourseDaoImpl;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Home {
	private static volatile Home Home = null;
	private Stage window;
	
	
	private Home() { 
		this.window = new Stage();
	}
	
	public static Home getInstance() {
		if (Home == null) {
			Home = new Home();
		}
		
		return Home;
	}
	
	/*package-private*/ static Home getInstance(Stage window) {
		getInstance().window = window;
		
		return Home;
	}
	
	
	public Stage getWindow() {
		return this.window;
	}
	
	
	// TODO expand upon this method...
	public static String formatReminderEmailBody(StringBuilder emailBodyBuilder) {
		return emailBodyBuilder.toString();
	}
	
	
	public static List<Event> findUrgentEvents() {
		List<Event> urgentEvents = null;
		try {
			List<Course> courses = new ArrayList<>();
			new CourseDaoImpl(courses).loadCourses(PlannerDb.getConnection());	
			for (Course course : courses) {
				if (course.getAssessments().isEmpty()) {
					new AssessmentDaoImpl(course).loadAssessments(PlannerDb.getConnection());
				}
			}
			
			urgentEvents = courses.stream().map(x -> x.findEarliestAssessment()).collect(Collectors.toList());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return urgentEvents;
	}
	
	
	public void display() {		
		try {
			// TODO move to the constructors
			Home.window.setTitle("Home Page -- Planner by Jadon");
			Home.window.setMinWidth(250);
			Home.window.setWidth(800);
			Home.window.setHeight(700);
			// FIXME set this on every new stage so that, even if the user chooses "no" when asked to confirm that the
			// user wants to quit the program, the other stages are not closed. 
			Home.window.setOnCloseRequest(event -> {
				event.consume();
				CloseProgram.closeProgram();
			});
			
			VBox homeLayoutBoxWrapper = new VBox();
			homeLayoutBoxWrapper.setSpacing(10);
			
			Parent menuBar = FXMLLoader.load(Home.class.getResource("/menubar.fxml")); //$NON-NLS-1$
			homeLayoutBoxWrapper.getChildren().add(menuBar);
			
			VBox homeLayoutBox = new VBox();
			homeLayoutBox.setSpacing(10);
			homeLayoutBox.setPadding(new Insets(10));
						
			for (Event urgentEvent : findUrgentEvents()) {
				if (urgentEvent != null) {
					VBox vb = new VBox();
					vb.setPadding(new Insets(10));
					vb.setSpacing(10);
					
					Text eventNameText = new Text(urgentEvent.getName());
					vb.getChildren().add(eventNameText);
					
					StackPane urgentEventPane = new StackPane();
					Rectangle rect = new Rectangle();
					rect.setWidth(this.window.getWidth() - 200);
					urgentEventPane.getChildren().addAll(rect, vb);
					urgentEventPane.setStyle("-fx-border-color: black; \n" +
										"-fx-border-insets: 5; \n" +
										"-fx-border-width: 2;"); //$NON-NLS-1$
					
					homeLayoutBox.getChildren().add(urgentEventPane);
				}
			}

			homeLayoutBoxWrapper.getChildren().add(homeLayoutBox);
			Scene scene = new Scene(homeLayoutBoxWrapper);
			this.window.setScene(scene);
			this.window.show();
		} catch (IOException e) {
			e.printStackTrace();
			this.window.close();
		} 
	}
}
