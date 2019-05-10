package home;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.mindrot.jbcrypt.*;

import abstractdao.LogInDao;
import user.User;

/*package-private*/ class LogInDaoImpl implements LogInDao {
	public LogInDaoImpl() { }
	
	
	private final String hashPassword(final String unhashedPassword) {
		return BCrypt.hashpw(unhashedPassword, BCrypt.gensalt());
	}

	
	@Override
	public boolean registerUser(Connection con, final int studentId, final String username, final String unhashedPassword) 
			throws SQLException {
		PreparedStatement pstmt = con.prepareStatement("INSERT INTO users VALUES(? , ? , ?)"); //$NON-NLS-1$
		pstmt.setInt(1, studentId);
		pstmt.setNString(2, username);
		pstmt.setNString(3, this.hashPassword(unhashedPassword));
		boolean isExecuted = pstmt.execute();
		pstmt.close();
		
		return isExecuted;
	}
	
	
	@Override
	public boolean isCorrectCredentials(Connection con, final String username, final String unhashedPassword) throws SQLException {
		PreparedStatement pstmt = con.prepareStatement("SELECT hashed_password, student_id FROM users WHERE username = ?"); //$NON-NLS-1$
		pstmt.setNString(1, username);
		
		ResultSet resultSet = pstmt.executeQuery();
		boolean isCorrectCredential = false;
		if (resultSet.next()) {
			String hashedPassword = resultSet.getNString(1);
			User.setStudentId(resultSet.getInt(2));
			isCorrectCredential = BCrypt.checkpw(unhashedPassword, hashedPassword);
		}
		
		resultSet.close();
		pstmt.close();
		
		return isCorrectCredential;
	}
}
