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
	
	/**
	 * Modifies the name of the course, which is the concatenation of the subject and course code, in the database
	 * @param con the JDBC connection to the database
	 * @param courseToUpdate the course object whose name is to be updated
	 * @param updatedCourseIdentifier the updated name of the course
	 * @throws SQLException the standard SQL exception 
	 */
	void editCourseName(Connection con, Course courseToUpdate, String updatedCourseName) throws SQLException;
	
	/**
	 * Modifies the course priority in the database, with a higher value equating to higher priority 
	 * <p>
	 * <u>Note</u>: The priority is set to -1 by default
	 * @param con the JDBC connection to the database
	 * @param courseToUpdate the course object whose priority is to be updated
	 * @param updatedPriority the updated value for the priority of the course
	 * @throws SQLException the standard SQL exception 
	 */
	void editCoursePriority(Connection con, Course courseToUpdate, float updatedPriority) throws SQLException;
	
	boolean deleteCourse(Connection con, Course course) throws SQLException;
}
