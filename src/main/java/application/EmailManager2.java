package application;

import java.awt.Desktop;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import javafx.util.Pair;
import user.AppSession;
import utility.CommonUtils;


public final class EmailManager2 {
	private static String authToken = null;
	
	
	public boolean isAuthTokenSet() {
		return EmailManager2.authToken != null;
	}
	
	
	public static String getQuery(List<Pair<String, String>> params) throws UnsupportedEncodingException {
    	StringBuilder sb = new StringBuilder();
    	boolean first = true;
    	
    	for (Pair<String, String> param : params) {
    		if (first) {
    			first = false;
    			sb.append("?");
    		} else {
    			sb.append("&");
    		}
    		
    		sb.append(URLEncoder.encode(param.getKey(), "UTF-8")); 
    		sb.append("=");
    		sb.append(URLEncoder.encode(param.getValue(), "UTF-8"));
    	}
    	
    	return sb.toString();
    }
	   
	   
    /**
     * Create a MimeMessage using the parameters provided.
     *
     * @param to email address of the receiver
     * @param from email address of the sender, the mailbox account
     * @param subject subject of the email
     * @param bodyText body text of the email
     * @return the MimeMessage to be used to send email
     * @throws MessagingException
     */
    MimeMessage createEmail(String to, String from, String subject, String bodyText) throws MessagingException {
        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);

        MimeMessage email = new MimeMessage(session);

        email.setFrom(new InternetAddress(from));
        email.addRecipient(javax.mail.Message.RecipientType.TO, new InternetAddress(to));
        email.setSubject(subject);
        email.setText(bodyText);
        return email;
    }
    
    
    /**
     * Create a message from an email.
     * @param emailContent Email to be set to raw of message
     * @return a message containing a base64url encoded email
     * @throws IOException
     * @throws MessagingException
     */
    String createMessageWithEmail(MimeMessage emailContent) throws MessagingException, IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        emailContent.writeTo(buffer);
        byte[] bytes = buffer.toByteArray();
        return new String(bytes);
    }
     
    
    public void reqAccessCode() throws UnsupportedEncodingException, IOException, URISyntaxException {
    	List<Pair<String, String>> params = new ArrayList<Pair<String, String>>() {
			private static final long serialVersionUID = 1L;
			{
				add(new Pair<>("client_id", "1036300734576-3llrcq340gtqre8fg083pchqh8lk1qnn."
						+ "apps.googleusercontent.com")); //NON-NLS-1$ //NON-NLS-2$
				add(new Pair<>("redirect_uri", "urn:ietf:wg:oauth:2.0:oob")); //NON-NLS-1$ //NON-NLS-2$
				add(new Pair<>("response_type", "code")); //NON-NLS-1$ //NON-NLS-2$
				add(new Pair<>("scope", "https://mail.google.com/")); //NON-NLS-1$ //NON-NLS-2$
				add(new Pair<>("code_challenge_method", "plain")); //NON-NLS-1$ //NON-NLS-2$
    		}
    	};
    	
    	Desktop.getDesktop().browse(new URI("https://accounts.google.com/o/oauth2/v2/auth" + EmailManager2.getQuery(params))); //NON-NLS-1$
    }
    
    
    public void reqAuthToken(final String accessCode) throws ClientProtocolException, IOException {
    	List<NameValuePair> params = new ArrayList<NameValuePair>() {
			private static final long serialVersionUID = 1L;
			{
				add(new BasicNameValuePair("code", accessCode));
				add(new BasicNameValuePair("client_id", "1036300734576-3llrcq340gtqre8fg083pchqh8lk1qnn.apps."
						+ "googleusercontent.com")); //NON-NLS-1$ //NON-NLS-2$
				add(new BasicNameValuePair("client_secret", "IXbEmcor9uQi1-mn9oMNtk7L")); //NON-NLS-1$ //NON-NLS-2$
				add(new BasicNameValuePair("redirect_uri", "urn:ietf:wg:oauth:2.0:oob")); //NON-NLS-1$ //NON-NLS-2$
				// add(new BasicNameValuePair("scope", ""));
				add(new BasicNameValuePair("grant_type", "authorization_code")); //NON-NLS-1$ //NON-NLS-2$
    		}
    	};
    	
       	CloseableHttpClient client = HttpClients.createDefault();
    	HttpPost tokenReq = new HttpPost("https://www.googleapis.com/oauth2/v4/token"); //NON-NLS-1$
    	tokenReq.setEntity(new UrlEncodedFormEntity(params));
    	
    	CloseableHttpResponse response = client.execute(tokenReq);
    	JsonParser parser = new JsonParser();
		JsonElement jse = parser.parse(new String(EntityUtils.toString(response.getEntity(), "UTF-8"))); //NON-NLS-1$
		if (jse.isJsonObject()) {
			EmailManager2.authToken = jse.getAsJsonObject().get("access_token").getAsString(); //NON-NLS-1$
		}
		
    	response.close();
    	client.close();
    }
        
    
    public void sendEmail(final String title, final String body) throws IllegalStateException {
    	this.sendEmail(AppSession.getEmailAddress(), title, body);
    }
    
    
    public void sendEmail(final String to, final String title, final String body) throws IllegalStateException {
    	if (EmailManager2.authToken == null) {
    		throw new IllegalStateException("The authorization token has yet been set up");
    	}  
    	
    	if (CommonUtils.isEmptyOrNull(AppSession.getEmailAddress())) {
    		throw new IllegalStateException("Missing user's email address");
    	}
    	
    	try {
			String msg = this.createMessageWithEmail(this.createEmail(to, AppSession.getEmailAddress(), title, body));
			String json = String.format("{\"raw\":\"%s", msg); //NON-NLS-1$
		
			HttpPost tokenReq = new HttpPost("https://www.googleapis.com/upload/gmail/v1/users/me/messages/send?key=AIzaSy"
					+ "B2_a0z5Lm0vFTp7HoCdyym7dc4fDo7Yq4"); //NON-NLS-1$
			tokenReq.setEntity(new StringEntity(json));
			tokenReq.setHeader("Authorization", "Bearer " + EmailManager2.authToken); //NON-NLS-1$
			tokenReq.setHeader("Accept", "application/json"); //NON-NLS-1$
			tokenReq.setHeader("Content-Type", "message/rfc822"); //NON-NLS-1$
		  	
	       	CloseableHttpClient client = HttpClients.createDefault();
			client.execute(tokenReq);		
	    	
	    	client.close();
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }
}
