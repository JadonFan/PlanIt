package calendar;

import java.io.IOException;
import java.util.Calendar;
import java.util.Locale;

import home.Home;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class PlannerCalendar {	
	public String reprCalTitle(CalendarMonth calendarMonth) {
		Locale locale = Locale.getDefault();
		return calendarMonth.getJCal().getDisplayName(Calendar.MONTH, Calendar.LONG, locale) + " " + 
				calendarMonth.getJCal().get(Calendar.YEAR);
	}
	
	
	public void display(Stage window) {
		this.display(window, new CalendarMonth());
	}
	
	
	public void display(Stage window, CalendarMonth calendarMonth) {
		window.setTitle("Calendar");
		
		VBox pageLayoutBox = new VBox(10);		
		try {
			Parent menuBar = FXMLLoader.load(Home.class.getResource("/menubar.fxml")); //$NON-NLS-1$
			pageLayoutBox.getChildren().add(menuBar);
		} catch (IOException e) {
			e.printStackTrace();
		} 		
		
		VBox calContainerBox = new VBox();
		calContainerBox.setPadding(new Insets(25));

		ScrollPane calPane = new ScrollPane(calendarMonth.buildCalendarMonthPane(window));
		calPane.setFitToWidth(true);
		calPane.setFitToHeight(true);
		
		HBox calTitleBox = this.buildCalTitleBox(calendarMonth, calPane, window);
		calContainerBox.getChildren().addAll(calTitleBox, calPane);
		pageLayoutBox.getChildren().add(calContainerBox);
		
		Scene scene = new Scene(pageLayoutBox);
		window.setScene(scene);
		window.show();
	}
	
	
	private HBox buildCalTitleBox(CalendarMonth calendarMonth, ScrollPane sp, Stage window) {
		Text monthTitle = new Text(this.reprCalTitle(calendarMonth));
		monthTitle.setFont(Font.font("Veranda", FontWeight.BOLD, 25));
		
		Button prevMonthBtn = new Button("<"); // TODO replace with image
		prevMonthBtn.setOnAction(event -> {
			calendarMonth.moveToNeighbourCal(-1);
			sp.setContent(calendarMonth.buildCalendarMonthPane(window));
			monthTitle.setText(this.reprCalTitle(calendarMonth));
		});
		Button nextMonthBtn = new Button(">"); // TODO replace with image
		nextMonthBtn.setOnAction(event -> {
			calendarMonth.moveToNeighbourCal(1);
			sp.setContent(calendarMonth.buildCalendarMonthPane(window));
			monthTitle.setText(this.reprCalTitle(calendarMonth));
		});
		
		Pane spacer = new Pane();
		
		HBox calTitleBox = new HBox();
		HBox.setHgrow(spacer, Priority.ALWAYS);
		calTitleBox.setPadding(new Insets(20, 0, 0, 0));
		calTitleBox.setSpacing(5);
		calTitleBox.getChildren().addAll(monthTitle, spacer, prevMonthBtn, nextMonthBtn);
		
		return calTitleBox;
	}
}
