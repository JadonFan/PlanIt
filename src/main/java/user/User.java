/*
package user;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "users")
public class User implements Serializable {	
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "student_id", nullable = false)
	private int studentId;
	@Column(name = "username", nullable = false, unique = true)
	private String username;
	@Column(name = "hashed_password", nullable = false)
	private transient String password;
	
	
	public User(int studentId, String username, String hashedPassword) {
		this.studentId = studentId;
		this.username = username;
		this.password = hashedPassword;
	}
	

	public int getStudentId() {
		return this.studentId;
	}

	public void setStudentId(int studentId) {
		this.studentId = studentId;
	}
}
*/
