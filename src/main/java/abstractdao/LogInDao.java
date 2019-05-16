package abstractdao;

import java.sql.Connection;
import java.sql.SQLException;

import org.springframework.stereotype.Repository;

@Repository
public interface LogInDao {
	boolean registerUser(Connection con, final int studentId, final String username, final String unhashedPassword) throws SQLException;
	
	boolean isCorrectCredentials(Connection con, final String username, final String unhashedPassword) throws SQLException;
	
	void addUserEmail(Connection con, final String emailAddress) throws SQLException;
	
	void loadUserEmail(Connection con) throws SQLException;

	void addGoogleAccessCode(Connection con, final String accessToken) throws SQLException;
	
	String loadGoogleAccessCode(Connection con) throws SQLException;

	void deleteGoogleAccessCode(Connection con) throws SQLException;
}
