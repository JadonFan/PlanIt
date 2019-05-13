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
import user.Session;
import user.User;

/*package-private*/ class LogInDaoImpl implements LogInDao {
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
			Session.setStudentId(resultSet.getInt(1));
			String hashedPassword = resultSet.getNString(2);
			isCorrectCredential = BCrypt.checkpw(unhashedPassword, hashedPassword);
		}
		
		resultSet.close();
		pstmt.close();
		
		return isCorrectCredential;
	}
}
