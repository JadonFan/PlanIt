package home;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import alerts.ConfirmBox;
import application.CloseProgram;
import application.EmailManager2;
import application.PlannerDb;
import calendar.Event;
import course.Course;
import course.CourseDaoImpl;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import user.AppSession;
import utility.CommonUtils;

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
	public String formatReminderEmailBody(StringBuilder emailBodyBuilder) {
		return emailBodyBuilder.toString();
	}
	
	
	public void display() {		
		try {
			this.window.setTitle("Home Page -- Planner by Jadon");
			this.window.setMinWidth(250);
			this.window.setWidth(800);
			this.window.setHeight(700);
			// FIXME set this on every new stage so that, even if the user chooses "no" when asked to confirm that the
			// user wants to quit the program, the other stages are not closed. 
			this.window.setOnCloseRequest(event -> {
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
			
			EmailManager2 emailManager2 = new EmailManager2();
			Button emailMeBtn = new Button("Email Me");
			homeLayoutBox.getChildren().add(emailMeBtn);
			
			List<Course> courses = new ArrayList<>();
			new CourseDaoImpl(courses).loadCourses(PlannerDb.getConnection());
			List<Event> urgentEvents = courses.stream().map(x -> x.findEarliestAssessment()).collect(Collectors.toList());
			StringBuilder emailBodyBuilder = new StringBuilder();
			
			for (Event urgentEvent : urgentEvents) {
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
					
					// U+2022 is the Unicode encoding for the bullet point 
					emailBodyBuilder.append(String.format("\\u2022 %s due in %d \n", eventNameText.getText(), 
							urgentEvent.findRemainingDays()));
					
					homeLayoutBox.getChildren().add(urgentEventPane);
				}
			}
			
			emailMeBtn.setOnAction(event -> {
				if (!CommonUtils.isEmptyOrNull(AppSession.getEmailAddress()) && emailManager2.isAuthTokenSet()) {
					boolean isConfirmed = new ConfirmBox("Send Email?", 
							"Are you sure you want to send a reminder email to yourself?").display();
					if (isConfirmed) {
						String reminderEmailBody = this.formatReminderEmailBody(emailBodyBuilder);
						emailManager2.sendEmail("**URGENT** Reminder from Planner App", reminderEmailBody);
					}
				} else {
					new EmailRegistrationForm().displayEmailSetUpProcess(emailManager2);
				}
			});

			homeLayoutBoxWrapper.getChildren().add(homeLayoutBox);
			Scene scene = new Scene(homeLayoutBoxWrapper);
			this.window.setScene(scene);
			this.window.show();
		} catch (IOException | SQLException e) {
			e.printStackTrace();
			this.window.close();
		} 
	}
}
