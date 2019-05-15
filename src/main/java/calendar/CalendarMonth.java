package calendar;

import java.util.Calendar;
import java.util.GregorianCalendar;

import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class CalendarMonth {
	private Calendar jCal;
	
	
	public CalendarMonth() {
		this.jCal = Calendar.getInstance();
	}
	
	public CalendarMonth(Calendar jCal) {
		this.jCal = jCal;
	}
	
	
	public Calendar getJCal() {
		return this.jCal;
	}
	
	public void setJCal(Calendar jCal) {
		this.jCal = jCal;
	}
	
	
	/**
	 * Finds the calendar object for the previous or next month, with the year adjusted as necessary
	 * <p>
	 * Using the Calendar's {@code roll(int, int)} method directly on the {@code currentCal} parameter might 
	 * cause some unexpected changes to {@code currentCal} elsewhere in the codebase
	 * @param up positive to roll up the month of {@code currentCal} by 1, 0 to not roll, negative to roll down the month by 1
	 */
	public void moveToNeighbourCal(int up) {
		up = Integer.signum(up); // To help avoid errors, we'll only roll the calendar up or down by one "unit" (month/year)
		
		if (this.jCal.get(Calendar.MONTH) == 0 && up == -1) {
			this.jCal.roll(Calendar.YEAR, -1);
			this.jCal.set(Calendar.MONTH, 11);
		} else if (this.jCal.get(Calendar.MONTH) == 11 && up == 1) {
			this.jCal.roll(Calendar.YEAR, 1);
			this.jCal.set(Calendar.MONTH, 0);
		} else {
			this.jCal.roll(Calendar.MONTH, up);
		}		
	}
	
	
	// See https://docs.oracle.com/javase/7/docs/api/constant-values.html
	public GridPane buildCalendarMonthPane(Stage window) {
		final int daysInMonth = this.jCal.getActualMaximum(Calendar.DAY_OF_MONTH);
		// tracks the date in the calendar currently being examined, starting from the first date of the specified month 
		final Calendar trackedDate = new GregorianCalendar(this.jCal.get(Calendar.YEAR), this.jCal.get(Calendar.MONTH), 1);

		GridPane monthPane = new GridPane(); // A grid pane is preferred over tile pane since a tile pane automatically adjusts  
									         // with the the size of the parent pane (in this case, the scroll pane)
		monthPane.setGridLinesVisible(true);
		
		int weekInMonthCount = 1; 
		int dayInWeekCount = trackedDate.get(Calendar.DAY_OF_WEEK); // get the day of the week that the first date of the month lies on

		for (int i = 0; i < daysInMonth; i++) {	 
			// Bad things can happen if you don't make a shallow copy of trackedDate
			StackPane dayPane = new CalendarDay((Calendar) trackedDate.clone()).buildCalendarDayPane(window, this);
			trackedDate.roll(Calendar.DAY_OF_MONTH, 1);
			
			monthPane.add(dayPane, dayInWeekCount - 1, weekInMonthCount - 1);
						
			dayInWeekCount += 1;
			if (dayInWeekCount > 7) {				
				weekInMonthCount += 1;
				dayInWeekCount = 1;
			}
		}
				
		return monthPane;
	}
}
