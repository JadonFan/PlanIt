package calendar;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class CalendarDay implements DayPaneI {
	private final Calendar jCal; // the Calendar class in the java.util package
	private Color boxColor;
	private List<CalendarEvent> events;
	private boolean isImportantDay;
	private boolean isSelected;
	
	
	public CalendarDay(Calendar jCal) {
		this.jCal = jCal;
		this.boxColor = Color.WHITE;
		this.events = new ArrayList<>();

	}
	
	public CalendarDay(Calendar jCal, Color boxColor) {
		this.jCal = jCal;
		this.boxColor = boxColor;
		this.events = new ArrayList<>();
	}
	
	
	public Calendar getJCal() {
		return this.jCal;
	}
	
	public boolean getIsImportantDay() {
		return this.isImportantDay;
	}
	
	public void setImportantDay(boolean isImportantDay) {
		this.isImportantDay = isImportantDay;
	}

	public boolean getIsSelected() {
		return this.isSelected;
	}

	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}
	
	public List<CalendarEvent> getEvents() {
		return this.events;
	}

	
	public String reprDay() {
		Locale locale = Locale.getDefault();
		return String.format("%s %s %s, %s", this.jCal.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, locale), 
				this.jCal.getDisplayName(Calendar.MONTH, Calendar.LONG, locale), this.jCal.get(Calendar.DAY_OF_MONTH),
				this.jCal.get(Calendar.YEAR));
	}


	public void showDayInfo(Stage window) {				
		VBox dayInfoLayoutBox = new VBox();
		dayInfoLayoutBox.setPadding(new Insets(15));
		dayInfoLayoutBox.setSpacing(15);
		
		HBox btnTopBarBox = new HBox();
		btnTopBarBox.setSpacing(10);
		dayInfoLayoutBox.getChildren().add(btnTopBarBox);

		Button addEventBtn = new Button("Add Event");
		btnTopBarBox.getChildren().add(addEventBtn);
		
		Button removeEventBtn = new Button("Remove Event");
		btnTopBarBox.getChildren().add(removeEventBtn);
		
		Button goBackToCalBtn = new Button("Back to Calendar");
		goBackToCalBtn.setOnAction(event -> new PlannerCalendar().display(window));
		btnTopBarBox.getChildren().add(goBackToCalBtn);
		
		Text dayTitleText = new Text(this.reprDay());
		dayTitleText.setFont(Font.font(35));
		dayInfoLayoutBox.getChildren().add(dayTitleText);
		
		for (CalendarEvent event : this.events) {
			VBox vb = new VBox();
			Text text = new Text(event.toString());
			vb.getChildren().add(text);
			
			StackPane coursePane = new StackPane();
			Rectangle rect = new Rectangle();
			rect.setWidth(window.getWidth() - 200);
			coursePane.getChildren().addAll(rect, vb);
			// TODO change to be more colourful
			coursePane.setStyle("-fx-border-color: black; \n" +
								"-fx-border-insets: 5; \n" +
								"-fx-border-width: 2; \n"); //$NON-NLS-1$	
			dayInfoLayoutBox.getChildren().add(coursePane);
		}
		
		Scene scene = new Scene(dayInfoLayoutBox);
		window.setScene(scene);
		window.show();
	}
	
	
	@Override
	public StackPane buildCalendarDayPane(Stage window) {
		StackPane dayPane = new StackPane();
		
		Label dateLabel = new Label(Integer.toString(this.jCal.get(Calendar.DAY_OF_MONTH)));

		Rectangle dayRect = new Rectangle(100, 75);
		dayRect.setFill(this.boxColor);
		dayPane.getChildren().addAll(dayRect, dateLabel);
		dayPane.setOnMouseClicked(event -> this.showDayInfo(window));
		// TODO get borders to the day panes
		// dayPane.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
		
		return dayPane;
	}
}
