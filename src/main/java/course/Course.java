package course;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class Course {
	private int crsNo; // primary key
	private String subject;
	private short code;
	private String name;
	private List<Assessment> assessments = Collections.synchronizedList(new ArrayList<>());
	public static final String COURSE_IDENTIFIER_REGEX = "^([a-zA-Z]{2,})(\\d{3})$"; //$NON-NLS-1$
	
	
	public Course() { }
	
	public Course(int crsNo, String subject, short code, String name) {
		this.crsNo = crsNo;
		this.subject = subject;
		this.code = code;  // Note: subject + code = course identifier 
		this.name = name;
	}
	
	
	public int getCrsNo() {
		return this.crsNo;
	}

	public void setCrsNo(int crsNo) {
		this.crsNo = crsNo;
	}
	
	public String getSubject() {
		return this.subject;
	}
	
	public void setSubject(String subject) {
		this.subject = subject;
	}
	
	public short getCode() {
		return this.code;
	}
	
	public void setCode(short code) {
		this.code = code;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public List<Assessment> getAssessments() {
		return this.assessments;
	}
	
	
	public void skinAssessmentPane(VBox vb) {
		vb.setPadding(new Insets(15));
		vb.setAlignment(Pos.CENTER_LEFT);
		
		for (Assessment astmt : this.assessments) {
			Text astmtNameText = new Text(astmt.getName());
			astmtNameText.setFont(Font.font("Verdana", FontWeight.BOLD, 13));
			vb.getChildren().add(astmtNameText); // Set assessment name;
			
			HBox hb = new HBox();
			hb.setSpacing(10);
					
			Text separatorText = new Text("|");			
			Text dueText = new Text(astmt.reprDue());
			Text weightingText = new Text(astmt.reprWeighting());
			// Text gradeText = new Text(Float.toString(astmt.getGrade()));			
			hb.getChildren().addAll(dueText, separatorText, weightingText);
			
			vb.getChildren().add(hb);
		}
	}
	
	
	/**
	 * Prepares the display string for all the assessments of a particular type for the specified course.
	 * <p>
	 * <u>Note</u>: There may be cases where the same assessment is stored multiple times for some reason, 
	 * but we only want to display that assessment once. Hence, this method only applies to each of the first
	 * distinct assessment in the list.
	 * @param clazz : the type of assessment 
	 * @param displayFunc : the function to determine what is to be displayed for each assessment
	 * @return An array of strings to be displayed as assessment details
	 */
	public String[] prepareSpecificAstmtsForDisplay(Class<? extends Assessment> clazz, Function<Assessment, String> displayFunc) {
		return this.assessments.stream().filter(clazz::isInstance).map(displayFunc.andThen(x -> x.trim()))
				.distinct().toArray(String[]::new);
	}
	
	
	/**
	 * Overrides the toString() method in the Object class, and returns the course identifier (subject + code)
	 * @return The primary string to display for the course
	 */
	@Override
	public String toString() {
		return this.subject + Short.toString(this.code);
	}
}
