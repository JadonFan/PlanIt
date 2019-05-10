package user;

public enum UserStatus {
	LOGGEDIN(0),
	MANUALLOGGEDOUT(1),
	AUTOLOGGEDOUT(2),
	SIGNUP(3),
	FORGETPASSWORD(4)
	;
	
	
	private int statusId;
	
	private UserStatus(int statusId) {
		this.statusId = statusId;
	}
	
	
	public static UserStatus getUserStatusById(int statusId) {
		for (UserStatus status : UserStatus.values()) {
			if (status.statusId == statusId) {
				return status;
			}
		}
		
		return null;
	}
}
