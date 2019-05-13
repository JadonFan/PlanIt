package user;

public class AppSession {
	private static int studentId;
	
	public static int getStudentId() {
		return studentId;
	}

	public static void setStudentId(int studentId) {
		AppSession.studentId = studentId;
	}
}
