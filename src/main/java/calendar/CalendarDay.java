package calendar;

import java.util.Calendar;

import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class CalendarDay implements DayPaneI {
	private final Calendar jCal; // the Calendar class in the java.util package
	private Color boxColor;
	private boolean isImportantDay;
	private boolean isSelected;
	
	
	public CalendarDay() {
		this.jCal = Calendar.getInstance();
		this.boxColor = Color.WHITE;
	}
	
	public CalendarDay(Calendar jCal) {
		this.jCal = jCal;
		this.boxColor = Color.WHITE;
	}
	
	public CalendarDay(Calendar jCal, Color boxColor) {
		this.jCal = jCal;
		this.boxColor = boxColor;
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
	
	
	@Override
	public StackPane buildCalendarDayPane() {
		StackPane dayPane = new StackPane();
		
		Label dateLbl = new Label(Integer.toString(this.jCal.get(Calendar.DAY_OF_MONTH)));

		Rectangle dayRect = new Rectangle(100, 75);
		dayRect.setFill(this.boxColor);
		
		dayPane.getChildren().addAll(dayRect, dateLbl);
		
		// TODO get borders to the day panes
		// dayPane.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
		
		return dayPane;
	}
}
