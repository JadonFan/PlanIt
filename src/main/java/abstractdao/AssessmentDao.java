package abstractdao;

import java.sql.Connection;
import java.sql.SQLException;

import course.Assessment;

@DaoInterface
public interface AssessmentDao {
	void addAssessment(Connection con, java.sql.Timestamp sqlDueTimestamp, Assessment astmt) throws SQLException;
	
	void loadAssessments(Connection con) throws SQLException;
	
	void removeAssessment(Connection con, Assessment astmt) throws SQLException;
}
