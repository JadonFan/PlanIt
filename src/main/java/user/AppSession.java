package user;

public class AppSession {
	private static int studentId;
	private static String emailAddress = null;
	
	public static int getStudentId() {
		return studentId;
	}

	public static void setStudentId(int studentId) {
		AppSession.studentId = studentId;
	}

	public static String getEmailAddress() {
		return emailAddress;
	}

	public static void setEmailAddress(String emailAddress) {
		AppSession.emailAddress = emailAddress;
	}
}
