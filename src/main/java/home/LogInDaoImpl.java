package home;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;

import org.hibernate.HibernateException;
import org.mindrot.jbcrypt.BCrypt;

import abstractdao.LogInDao;
import user.AppSession;
import user.User;
import utility.CommonUtils;

public class LogInDaoImpl implements LogInDao {
	public LogInDaoImpl() { }
	
	
	private final String hashPassword(final String unhashedPassword) {
		return BCrypt.hashpw(unhashedPassword, BCrypt.gensalt());
	}

	
	public void registerUser(EntityManagerFactory emf, final int studentId, final String username, final String unhashedPassword) 
			throws SQLException {
		EntityManager entityManager = emf.createEntityManager();
		EntityTransaction entityTransaction = null;
		try {
			entityTransaction = entityManager.getTransaction();
			entityTransaction.begin();
			User user = new User(studentId, username, this.hashPassword(unhashedPassword));
			entityManager.persist(user);
			entityTransaction.commit();
		} catch (HibernateException e) {
			if (entityTransaction != null) {
				entityTransaction.rollback();
			}
			e.printStackTrace();
		} finally {
			entityManager.close();
		}
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
		PreparedStatement pstmt = con.prepareStatement("SELECT student_id, hashed_password FROM users WHERE username = ?"
				+ " LIMIT 1"); //$NON-NLS-1$
		pstmt.setNString(1, username);
		
		ResultSet resultSet = pstmt.executeQuery();
		boolean isCorrectCredential = false;
		if (resultSet.next()) {
			String hashedPassword = resultSet.getNString(2);
			isCorrectCredential = BCrypt.checkpw(unhashedPassword, hashedPassword);
		}
		
		if (isCorrectCredential) {
			AppSession.setStudentId(resultSet.getInt(1));
		}
		
		resultSet.close();
		pstmt.close();
		
		return isCorrectCredential;
	}
	
	
	@Override
	public void addUserEmail(Connection con, final String emailAddress) throws SQLException {
		PreparedStatement pstmt = con.prepareStatement("UPDATE users SET email_address = ? WHERE student_id = ?"); //$NON-NLS-1$
		pstmt.setNString(1, emailAddress);
		pstmt.setInt(2, AppSession.getStudentId());
		
		pstmt.execute();
		AppSession.setEmailAddress(emailAddress); // set the email address even if the execution of the query is unsuccessful
		
		pstmt.close();
	}
	
	
	@Override
	public void loadUserEmail(Connection con) throws SQLException {
		PreparedStatement pstmt = con.prepareStatement("SELECT email_address FROM users WHERE student_id = ? LIMIT 1"); //$NON-NLS-1$
		pstmt.setInt(1, AppSession.getStudentId());
		
		ResultSet resultSet = pstmt.executeQuery();
		if (resultSet.next()) {
			AppSession.setEmailAddress(resultSet.getNString(1)); 
		}
		
		resultSet.close();
		pstmt.close();
	}
	
	
	@Override
	// TODO encrypt the access code with a key from the Java security API, and tie that access code to an external server
	// (yeah, there are more secure methods to store the key, but I don't have the resources for that...)
	public void addGoogleAccessCode(Connection con, String accessToken) throws SQLException {
		if (AppSession.getIsTempAccessCode()) {
			return;
		}
		
		PreparedStatement pstmt = con.prepareStatement("UPDATE users SET google_access_code = ? WHERE student_id = ?"
				+ " AND email_address = ?"); //$NON-NLS-1$
		pstmt.setNString(1, accessToken);
		pstmt.setInt(2, AppSession.getStudentId());
		pstmt.setNString(3, AppSession.getEmailAddress());
		
		pstmt.execute();
		
		pstmt.close();
	}
	
	
	@Override
	// TODO decrypt the access code once it has been encrypted through a key in the addGoogleAccessCode(Connection, String) method
	public String loadGoogleAccessCode(Connection con) throws SQLException {
		PreparedStatement pstmt = con.prepareStatement("SELECT google_access_code FROM users WHERE student_id = ?"
				+ " AND email_address = ? LIMIT 1"); //$NON-NLS-1$
		pstmt.setInt(1, AppSession.getStudentId());
		if (CommonUtils.isEmptyOrNull(AppSession.getEmailAddress())) {
			this.loadUserEmail(con);
		} 
		pstmt.setNString(2, AppSession.getEmailAddress());
		
		String accessCode = null;
		ResultSet resultSet = pstmt.executeQuery();
		if (resultSet.next()) {
			accessCode = resultSet.getNString(1);
			if (accessCode != null) {
				accessCode = accessCode.trim();
			}
		} 
		
		resultSet.close();
		pstmt.close();
		
		return accessCode;
	}
	
	
	@Override
	public void deleteGoogleAccessCode(Connection con) throws SQLException {
		PreparedStatement pstmt = con.prepareStatement("UPDATE users SET google_access_code = NULL WHERE student_id = ?"
				+ " AND email_address = ? LIMIT 1"); //$NON-NLS-1$
		pstmt.setInt(1, AppSession.getStudentId());
		pstmt.setNString(2, AppSession.getEmailAddress());
		pstmt.execute();
		
		pstmt.close();		
	}
}
