package user;

public class User {
	private static int studentId = 332;
	

	public static int getStudentId() {
		return studentId;
	}

	public static void setStudentId(int studentId) {
		User.studentId = studentId;
	}
}
