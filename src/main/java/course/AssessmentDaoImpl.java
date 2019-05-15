package course;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.GregorianCalendar;

import abstractdao.AssessmentDao;
import user.AppSession;

public class AssessmentDaoImpl implements AssessmentDao {
	private Course course; // XXX should this be a list of assessments instead???
	
	
	public AssessmentDaoImpl(Course course) {
		this.course = course;
	}

	
	public Course getCourse() {
		return this.course;
	}

	
	@Override
	public void addAssessment(Connection con, java.sql.Timestamp sqlDueTimestamp, Assessment astmt) throws SQLException {
		PreparedStatement pstmt = con.prepareStatement("INSERT INTO assessments(astmt_name, due_date, weighting, astmt_type, "
				+ "course_number, student_id) VALUE(? , ? , ? , ? , ? , ?)"); //$NON-NLS-1$
		pstmt.setNString(1, astmt.getName());
		pstmt.setTimestamp(2, sqlDueTimestamp, astmt.getDueDate());
		pstmt.setFloat(3, astmt.getWeighting());
		pstmt.setInt(4, astmt.findAstmtTypeId()); // TODO get astmt type id in a separate method in the Assessment class
		pstmt.setInt(5, this.course.getCrsNo());
		pstmt.setInt(6, AppSession.getStudentId());
		
		pstmt.execute();
		this.course.getAssessments().add(astmt);
		
		pstmt.close();
	}
	

	@Override
	public void loadAssessments(Connection con) throws SQLException {
		PreparedStatement pstmt = con.prepareStatement("SELECT astmt_id, astmt_name, due_date, weighting, astmt_type "
				+ "FROM assessments WHERE course_number = ? AND student_id = ?"); //$NON-NLS-1$
		pstmt.setInt(1, this.course.getCrsNo());
		pstmt.setInt(2, AppSession.getStudentId());
		
		this.course.getAssessments().clear();
		ResultSet resultSet = pstmt.executeQuery();
		while (resultSet.next()) {
			Calendar dueDate = new GregorianCalendar();
			
			int astmtId = resultSet.getInt(1);
			String astmtName = resultSet.getNString(2);
			dueDate.setTime(resultSet.getDate(3));
			float weighting = resultSet.getFloat(4);
			int astmt_type = resultSet.getInt(5); 
			
			// TODO move as a method in the Assessment class
			Assessment astmt = null;
			switch (AssessmentType.getAstmtTypeById(astmt_type)) {
				case ASSIGNMENT:
					astmt = new Assignment(astmtName, dueDate, weighting);
					break;
				case EXAMINATION:
					astmt = new Examination(astmtName, weighting, Examination.MIDTERM1);
					break;
				default:
					// does nothing
			}
			
			if (astmt != null) {
				astmt.setId(astmtId);
				this.course.getAssessments().add(astmt);
			}
		}
		
		resultSet.close();
		pstmt.close();		
	}
	
	
	@Override
	public void deleteAssessment(Connection con, Assessment astmt) throws SQLException {
		PreparedStatement pstmt = con.prepareStatement("DELETE FROM assessments WHERE astmt_id = ? AND astmt_name = ?"); //$NON-NLS-1$
		pstmt.setInt(1, astmt.getId());
		pstmt.setNString(2, astmt.getName());
		
		pstmt.execute();
		this.course.getAssessments().remove(astmt);
		
		pstmt.close();
	}
}
