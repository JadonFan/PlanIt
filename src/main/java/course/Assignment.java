package course;

import java.util.Calendar;

import javafx.scene.paint.Color;

public class Assignment extends Assessment {
	private boolean isGroupProject = false;
	private AssignmentRequirements req;
	
	
	public Assignment(String name, float weighting) {
		super(name, weighting);
	}
	
	public Assignment(String name, Calendar dueDate, float weighting) {
		super(name, dueDate, weighting);
	}

	public Assignment(String name, Calendar dueDate, float weighting, AssignmentRequirements req) {
		super(name, dueDate, weighting);
		this.req = req;
	}
	
	
	public boolean getIsGroupProject() {
		return this.isGroupProject;
	}

	public void setIsGroupProject(boolean isGroupProject) {
		this.isGroupProject = isGroupProject;
	}
	
	public AssignmentRequirements getReq() {
		return this.req;
	}

	public void setReq(AssignmentRequirements req) {
		this.req = req;
	}
	

	
	@Override
	public Rating comparedToCourseAvg(int avgGrade) {
		return null;
	}
	
	
	@Override
	public Color getDateWarningColor() {
		Color color = null;
		final int dayDiff = super.findRemainingDays();
		
		if (dayDiff < 0) {
			color = Color.DARKRED;
		} else {
			/* With Java 12...
			color = switch (dayDiff) {
				case 0, 1 -> Color.DARKRED;
				case 2 -> Color.YELLOW;
				case 3, 4 -> Color.GREENYELLOW;
				default -> Color.GREEN;
			};
			*/
			switch (dayDiff) {
				case 0:
				case 1:
					color = Color.DARKMAGENTA;
					break;
				case 2:
					color = Color.YELLOW;
					break;
				case 3: 
				case 4:
					color = Color.GREENYELLOW;
					break;
				default:
					color = Color.GREEN;
					break;
			};
		}
		
		return color;
	}
}
