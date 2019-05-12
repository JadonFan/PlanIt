package course;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

import application.PlannerDb;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class Course implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private int crsNo; // primary key
	private String subject;
	private short code;
	private String title;
	private float priority = -1;
	
	private List<Assessment> assessments = Collections.synchronizedList(new ArrayList<>());
	public static final String COURSE_IDENTIFIER_REGEX = "^([a-zA-Z]{2,})(\\d{3})$"; //$NON-NLS-1$
	
	
	public Course() { }
	
	public Course(int crsNo, String subject, short code, String title) {
		this.crsNo = crsNo;
		this.subject = subject;
		this.code = code;  // Note: subject + code = course identifier 
		this.title = title;
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

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	public float getPriority() {
		return this.priority;
	}

	public void setPriority(float priority) {
		this.priority = priority;
	}

	public List<Assessment> getAssessments() {
		return this.assessments;
	}
	
	
	public void sortAstmtsByDate() {
		// We'll refrain from using the sort(List<T>) method in Java's Collection framework since that will mutate the object 
		// itself. We could clone the assessment list, but that is an extra operation of at least O(n*m) time complexity where 
		// n is the size of the assessment list, and m is the number of fields in an Assessment subclass (recall that Assessment
		// class itself cannot be initialized directly since it's an abstract class)! Therefore, we'll use bubblesort instead.		
	}
	
	
	/**
	 * Finds the assessment with the earliest due date amongst all the "open" assessments for this course <br>
	 * <p>
	 * <u>Note 1</u>: This method is not necessary if the list of assessments is already sorted, such as by calling the 
	 * {@link sortAstmtByDate()} method <br>
	 * <u>Note 2</u>: If the course doesn't have any assessment, then this method does nothing
	 * @return The assessment object with the closest due date from today
	 */
	public Assessment findEarliestAssessment() {
		if (this.assessments.size() == 0) {
			return null;
		}
		
		Assessment earliestAstmt = this.assessments.get(0);
		for (Assessment currentAstmt : this.assessments) {
			if (currentAstmt.getDueDate().before(earliestAstmt.getDueDate())) {
				earliestAstmt = currentAstmt;
			}
		}
		
		return earliestAstmt;
	}
	
	
	public void skinAssessmentPane(VBox vb) {
		vb.setPadding(new Insets(15));
		vb.setAlignment(Pos.CENTER_LEFT);
		
		for (Assessment astmt : this.assessments) {
			GridPane astmtPane = new GridPane();
			astmtPane.setHgap(30);
			
			Text astmtNameText = new Text(astmt.getName());
			astmtNameText.setFont(Font.font("Verdana", FontWeight.BOLD, 13));
			astmtPane.add(astmtNameText, 0, 0); // Set assessment name
			
			HBox astmtDetailBox = new HBox();
			astmtDetailBox.setSpacing(10);
			Text separatorText = new Text("|");			
			Text dueText = new Text(astmt.reprDue());
			Text weightingText = new Text(astmt.reprWeighting());
			// Text gradeText = new Text(Float.toString(astmt.getGrade()));	
			astmtDetailBox.getChildren().addAll(dueText, separatorText, weightingText);
			astmtPane.add(astmtDetailBox, 0, 1);

			Button removeAstmtBtn = new Button("REMOVE");
			removeAstmtBtn.setVisible(false);
			removeAstmtBtn.setOnAction(event -> {
				try {
					new AssessmentDaoImpl(this).deleteAssessment(PlannerDb.getConnection(), astmt);
					// astmtDetailBox.getChildren().removeAll(dueText, separatorText, weightingText);
					vb.getChildren().removeAll(astmtPane);
				} catch (SQLException e) {
					e.printStackTrace();
				}
			});
			astmtPane.setOnMouseEntered(event -> removeAstmtBtn.setVisible(true));
			astmtPane.setOnMouseExited(event -> removeAstmtBtn.setVisible(false));
			astmtPane.add(removeAstmtBtn, 1, 0);

			vb.getChildren().addAll(astmtPane);
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
