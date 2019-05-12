package course;

import javafx.scene.paint.Color;

public class Examination extends Assessment {
	private int examType;
	
	public static final int MIDTERM1 = 0;
	public static final int MIDTERM2 = 1;
	public static final int MIDTERM3 = 2;
	public static final int FINAL = 3;
	public static final int MAKEUPEXAM = 4;
	public static final int SPECIALEXAM = 5;
	
	
	public Examination(String name, float weighting, int examType) {
		super(name, weighting);
		this.examType = examType;
	}

	
	public int getExamType() {
		return this.examType;
	}

	public void setExamType(int examType) {
		this.examType = examType;
	} 
	
	
	@Override
	public Color getDateWarningColor() {
		return null;
	}

	@Override
	public Rating comparedToCourseAvg(int avgGrade) {
		return null;
	}
}
