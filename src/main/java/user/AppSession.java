package user;

public class AppSession {
	private static int studentId;
	private static String emailAddress = null;
	private static boolean isTempAccessCode = false;
	
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

	public static boolean getIsTempAccessCode() {
		return isTempAccessCode;
	}

	public static void setIsTempAccessCode(boolean isTempAccessCode) {
		AppSession.isTempAccessCode = isTempAccessCode;
	}
}
