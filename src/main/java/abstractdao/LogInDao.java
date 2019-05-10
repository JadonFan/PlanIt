package abstractdao;

import java.sql.Connection;
import java.sql.SQLException;

@DaoInterface
public interface LogInDao {
	boolean registerUser(Connection con, final int studentId, final String username, final String unhashedPassword) throws SQLException;
	
	boolean isCorrectCredentials(Connection con, final String username, final String unhashedPassword) throws SQLException;
}
