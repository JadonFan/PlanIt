package course;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import abstractdao.CourseDao;
import user.Session;

//Some dependency injection in this class would probably be nice...
public class CourseDaoImpl implements CourseDao {	
	private List<Course> courses;
	
	
	public CourseDaoImpl() {
		this.courses = new ArrayList<>();
	}
	
	public CourseDaoImpl(List<Course> courses) {
		this.courses = courses;
	}
	
	
	@Override
	public List<Course> getCourses() {
		return this.courses;
	}
	
	
	@Override
	public void loadCourses(Connection con) throws SQLException {
		PreparedStatement pstmt = con.prepareStatement("SELECT * FROM my_courses WHERE student_id = ?"); //$NON-NLS-1$
		pstmt.setInt(1, Session.getStudentId());
		
		this.courses.clear();
		ResultSet resultSet = pstmt.executeQuery();
		while (resultSet.next()) {
			int crsNo = resultSet.getInt(1);
			String subject = resultSet.getNString(2);
			short crsCode = resultSet.getShort(3);
			String crsName = resultSet.getNString(4);
			
			this.courses.add(new Course(crsNo, subject, crsCode, crsName)); 
		}
		
		pstmt.close();
		resultSet.close();
	}
	
	
	@Override
	public void addCourse(Connection con, Course crsDet) throws SQLException {				
		PreparedStatement pstmt = con.prepareStatement("INSERT INTO my_courses VALUES(? , ? , ? , ?, ?)"); //$NON-NLS-1$
		pstmt.setInt(1, crsDet.getCrsNo());
		pstmt.setNString(2, crsDet.getSubject());
		pstmt.setShort(3, crsDet.getCode());
		pstmt.setNString(4, crsDet.getTitle());
		pstmt.setInt(5, Session.getStudentId());
				
		this.courses.add(crsDet);
		pstmt.execute();
		pstmt.close();
	}
	
	
	@Override
	public void editCourseIdentifier(Connection con, Course courseToUpdate, String updatedCourseIdentifier) throws SQLException {
		Pattern p = Pattern.compile(Course.COURSE_IDENTIFIER_REGEX);
		Matcher matcher = p.matcher(updatedCourseIdentifier);
		if (!matcher.matches()) { 
			throw new IllegalArgumentException("The course identifier is not correctly formatted");
		}
				
		PreparedStatement pstmt = con.prepareStatement("UPDATE my_courses SET subject = ?, code = ? WHERE course_number = ?"); //$NON-NLS-1$
		pstmt.setNString(1, matcher.group(1));
		pstmt.setInt(2, Integer.parseInt(matcher.group(2)));
		pstmt.setInt(3, courseToUpdate.getCrsNo());
		
		pstmt.execute();
		pstmt.close();
	}
	
	
	@Override
	public boolean deleteCourse(Connection con, Course course) throws SQLException {
		// TODO add a deletion guard 
		// TODO call the deleteAssessment(Connection, Assessment) method in the Course class once that method
		// has been implemented
		PreparedStatement pstmt = con.prepareStatement("DELETE FROM assessments WHERE course_number = ?"); //$NON-NLS-1$
		pstmt.setInt(1, course.getCrsNo());
		pstmt.execute();
		pstmt.close();
		
		// TODO add a deletion guard 
		pstmt = con.prepareStatement("DELETE FROM my_courses WHERE course_number = ?"); //$NON-NLS-1$
		pstmt.setInt(1, course.getCrsNo());
		boolean isExecuted = pstmt.execute();
		pstmt.close();
		
		return isExecuted;
	}
}
