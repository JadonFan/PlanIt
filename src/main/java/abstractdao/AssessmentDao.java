package abstractdao;

import java.sql.Connection;
import java.sql.SQLException;

import org.springframework.stereotype.Repository;

import course.Assessment;

@Repository
public interface AssessmentDao {
	void addAssessment(Connection con, java.sql.Timestamp sqlDueTimestamp, Assessment astmt) throws SQLException;
	
	void loadAssessments(Connection con) throws SQLException;
	
	void deleteAssessment(Connection con, Assessment astmt) throws SQLException;
}
