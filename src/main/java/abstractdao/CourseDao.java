package abstractdao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import course.Course;

@DaoInterface
public interface CourseDao {
	List<Course> getCourses();
	
	void loadCourses(Connection con) throws SQLException;
	
	void addCourse(Connection con, Course crsDet) throws SQLException;
	
	void editCourseIdentifier(Connection con, Course courseToUpdate, String updatedCourseIdentifier) throws SQLException;
	
	boolean deleteCourse(Connection con, Course course) throws SQLException;
}
