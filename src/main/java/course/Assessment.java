package course;

import java.util.Calendar;
import java.util.Locale;

import javafx.scene.paint.Color;
import utility.CommonUtils;
import utility.DateUtils;

public abstract class Assessment {
	private int id;
	private String name;
	private int loggedStudyMinutes;
	private Calendar dueDate;
	private float weighting;
	private float grade;
	private boolean isBonus = false;
	

	public Assessment(String name, float weighting) {
		this.name = name;
		this.loggedStudyMinutes = 0;
		this.setWeighting(weighting);
	}
	
	public Assessment(String name, Calendar dueDate, float weighting) {
		this(name, weighting);
		this.dueDate = dueDate;
	}
	
	
	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getLoggedStudyMinutes() {
		return this.loggedStudyMinutes;
	}

	public void setLoggedStudyMinutes(int loggedStudyMinutes) {
		this.loggedStudyMinutes = loggedStudyMinutes;
	}
	
	public Calendar getDueDate() {
		return this.dueDate;
	}

	public void setDueDate(Calendar dueDate) {
		this.dueDate = dueDate;
	}
	
	public float getWeighting() {
		return this.weighting;
	}

	public void setWeighting(float weighting) {
		this.weighting = weighting;
	}
	
	public float getGrade() {
		return this.grade;
	}

	public void setGrade(float grade) {
		if (grade < 0 || grade > 100) {
			throw new IllegalArgumentException("The grade must be between 0 and 100");
		}
		
		this.grade = grade;
	}
	
	public boolean getIsBonus() {
		return this.isBonus;
	}

	public void setIsBonus(boolean isBonus) {
		this.isBonus = isBonus;
	}	
	
	
	/**
	 * Gets the text colour of the date display for an assessment to remind the user how much
	 * closer the deadline for that assessment is approaching
	 * @return A JavaFX colour based on the days remaining
	 */
	public abstract Color getDateWarningColor();
	
	
	public abstract Rating comparedToCourseAvg(final int avgGrade);
		
	
	public int findAstmtTypeId() {
		int typeId = -1;
		
		if (this.getClass() == Assignment.class) {
			typeId = AssessmentType.ASSIGNMENT.getAstmtTypeId();
		} else if (this.getClass() == Examination.class) {
			typeId = AssessmentType.EXAMINATION.getAstmtTypeId();
		}
			
		return typeId;
	}
	
	
	public int findRemainingDays() {
		return (int) ((this.dueDate.getTimeInMillis() - Calendar.getInstance().getTimeInMillis()) / (1000 * 60 * 60 * 24));
	}
	
	
	public boolean isPredictedFinishable() {
		return this.loggedStudyMinutes > this.findRemainingDays();
	}
	
	
	public boolean isDueMidnightish() {
		return this.dueDate.get(Calendar.HOUR_OF_DAY) >= 23 && this.dueDate.get(Calendar.MINUTE) >= 45;
	}
	
	
	public String reprDue() {
		Locale locale = Locale.getDefault();
		
		String dueMonth = this.dueDate.getDisplayName(Calendar.MONTH, Calendar.LONG, locale);
		String dueDate = CommonUtils.getOrderWithSuffix(this.dueDate.get(Calendar.DAY_OF_MONTH));
		String dueTime = DateUtils.reprHourMinutes(this.dueDate);
		int remainingDays = this.findRemainingDays();
		String dayUnit = Math.abs(remainingDays) > 1 ? " days" : " day";
		
		return String.format("Due: %s %s (%s) at %s", dueMonth, dueDate, Integer.toString(remainingDays).concat(dayUnit), dueTime);
	}
	
	
	public String reprWeighting() {
		String weight;
		if (this.weighting == (int) this.weighting) {
			weight = Integer.toString((int) this.weighting);
		} else {
			weight = Float.toString(this.weighting);
			if (weight.split("\\.")[1].length() == 1) {
				weight += "0";
			}
		}
		
		return "Weight: " + weight.concat("%");
	}
}
