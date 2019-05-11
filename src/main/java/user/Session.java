package user;

public class Session {
	private static int studentId;
	
	public static int getStudentId() {
		return studentId;
	}

	public static void setStudentId(int studentId) {
		Session.studentId = studentId;
	}
}
