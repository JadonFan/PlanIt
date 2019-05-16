package user;

public class Email {
	private String[] recipients;
	private String emailTitle;
	private String bodyText;
	
	
	public Email() { }
	
	public Email(String[] recipients, String emailTitle, String bodyText) { 
		this.recipients = recipients;
		this.emailTitle = emailTitle;
		this.bodyText = bodyText;
	}
	
	
	public String[] getRecipients() {
		return this.recipients;
	}
	
	public void setRecipients(String[] recipients) {
		this.recipients = recipients;
	}
	
	public String getEmailTitle() {
		return this.emailTitle;
	}
	
	public void setEmailTitle(String emailTitle) {
		this.emailTitle = emailTitle;
	}

	public String getBodyText() {
		return this.bodyText;
	}

	public void setBodyText(String bodyText) {
		this.bodyText = bodyText;
	}
}
