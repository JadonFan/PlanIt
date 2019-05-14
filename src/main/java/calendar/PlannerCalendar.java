package calendar;

import java.util.Calendar;
import java.util.Locale;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import ui.CommonButtonFactory;

public class PlannerCalendar {	
	public void display(Stage window) {
		this.display(window, new CalendarMonth());
	}
	
	
	public void display(Stage window, CalendarMonth calendarMonth) {
		window.setTitle("Calendar");
		
		// calendar filters
		CheckBox vacationFilter = new CheckBox("Vacation");
		CheckBox assignmentFilter = new CheckBox("Assignment");
		CheckBox labFilter = new CheckBox("Lab");
		CheckBox examFilter = new CheckBox("Exam");

		// calendar visual
		GridPane calPane = calendarMonth.buildCalendarMonthPane(window);
		ScrollPane calLayout = new ScrollPane(calPane);
		calLayout.setFitToWidth(true);
		calLayout.setFitToHeight(true);
		
		// page layout
		VBox pageLayout = new VBox(10);
		pageLayout.setPadding(new Insets(25));
		pageLayout.getChildren().addAll(CommonButtonFactory.buildBackToHomeButton(window), vacationFilter, assignmentFilter,  
				labFilter, examFilter, this.buildCalTitleBox(window, calendarMonth), calLayout);
		
		Scene scene = new Scene(pageLayout);
		window.setScene(scene);
		window.show();
	}
	
	
	private HBox buildCalTitleBox(Stage window, CalendarMonth calendarMonth) {
		Button prevMonthBtn = new Button("<"); // TODO replace with image
		prevMonthBtn.setOnAction(event -> {
			this.display(window, new CalendarMonth(CalendarMonth.findNeighbourCal(calendarMonth.getJCal(), -1)));
		});
		Button nextMonthBtn = new Button(">"); // TODO replace with image
		nextMonthBtn.setOnAction(event -> {
			this.display(window, new CalendarMonth(CalendarMonth.findNeighbourCal(calendarMonth.getJCal(), 1)));
		});
		
		Locale locale = Locale.getDefault();
		Text monthTitle = new Text(calendarMonth.getJCal().getDisplayName(Calendar.MONTH, Calendar.LONG, locale) + " " + 
				calendarMonth.getJCal().get(Calendar.YEAR));
		monthTitle.setFont(Font.font("Veranda", FontWeight.BOLD, 25));
		
		Pane spacer = new Pane();
		
		HBox calTitleBox = new HBox();
		HBox.setHgrow(spacer, Priority.ALWAYS);
		calTitleBox.setPadding(new Insets(20, 0, 0, 0));
		calTitleBox.setSpacing(5);
		calTitleBox.getChildren().addAll(monthTitle, spacer, prevMonthBtn, nextMonthBtn);
		
		return calTitleBox;
	}
}
