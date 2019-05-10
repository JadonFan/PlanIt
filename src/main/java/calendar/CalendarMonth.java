package calendar;

import java.util.Calendar;
import java.util.GregorianCalendar;

import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;

public class CalendarMonth implements MonthPaneI {
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
	 * <i> Using the Calendar's roll method(int, int) directly on the {@code currentCal} parameter might cause some unexpected
	 * changes to {@code currentCal} elsewhere in the codebase </i>
	 * @param currentCal : calendar object with the current date
	 * @param up : positive to roll up the month of {@code currentCal} by 1, 0 to not roll, negative to roll down the month by 1
	 * @return a clone of the currentCal after the roll is completed
	 */
	public static Calendar findNeighbourCal(Calendar currentCal, int up) {
		Calendar neighbourCal = (Calendar) currentCal.clone();  // A potentially expensive operation, but works for all types of 
															    // calendars that extend Java's Calendar abstract class
		up = Integer.signum(up); // To help avoid errors, we'll only roll the calendar up or down by one "unit" (month/year)
		
		if (neighbourCal.get(Calendar.MONTH) == 0 && up == -1) {
			neighbourCal.roll(Calendar.YEAR, -1);
			neighbourCal.set(Calendar.MONTH, 11);
		} else if (neighbourCal.get(Calendar.MONTH) == 11 && up == 1) {
			neighbourCal.roll(Calendar.YEAR, 1);
			neighbourCal.set(Calendar.MONTH, 0);
		} else {
			neighbourCal.roll(Calendar.MONTH, up);
		}
		
		return neighbourCal;
	}
	
	
	// See https://docs.oracle.com/javase/7/docs/api/constant-values.html
	@Override
	public GridPane buildCalendarMonthPane() {
		final int daysInMonth = this.jCal.getActualMaximum(Calendar.DAY_OF_MONTH);
		// tracks the date in the calendar currently being examined, starting from the first date of the specified month 
		final Calendar trackedDate = new GregorianCalendar(this.jCal.get(Calendar.YEAR), this.jCal.get(Calendar.MONTH), 1);

		GridPane monthPane = new GridPane(); // A grid pane is preferred over tile pane since a tile pane automatically adjusts  
									         // with the the size of the parent pane (in this case, the scroll pane)
		monthPane.setGridLinesVisible(true);
		
		int weekInMonthCount = 1;
		int dayInWeekCount = trackedDate.get(Calendar.DAY_OF_WEEK);

		for (int i = 0; i < daysInMonth; i++) {	 
			StackPane dayPane = new CalendarDay(trackedDate).buildCalendarDayPane();
			trackedDate.roll(Calendar.DAY_OF_MONTH, 1);
			
			GridPane.setConstraints(dayPane, dayInWeekCount - 1, weekInMonthCount - 1);
			monthPane.getChildren().add(dayPane);
						
			dayInWeekCount += 1;
			if (dayInWeekCount > 7) {				
				weekInMonthCount += 1;
				dayInWeekCount = 1;
			}
		}
				
		return monthPane;
	}
}
