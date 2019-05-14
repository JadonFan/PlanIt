package application;

import java.awt.Desktop;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javafx.util.Pair;


public final class EmailManager2 {
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
    	System.out.println(sb.toString());
    	return sb.toString();
    }
    
        
    
    public static void main(String[] args) throws MalformedURLException, IOException, URISyntaxException {
    	List<Pair<String, String>> params = new ArrayList<Pair<String, String>>() {
			private static final long serialVersionUID = 1L;
			{
				add(new Pair<>("client_id", "1036300734576-c7u6dbvh06s3p7lv6tucpknfq5nln6g3.apps.googleusercontent.com"));
				add(new Pair<>("redirect_uri", "urn:ietf:wg:oauth:2.0:oob"));
				add(new Pair<>("response_type", "code"));
				add(new Pair<>("scope", "https://mail.google.com/"));
				add(new Pair<>("code_challenge_method", "plain"));
    		}
    	};
    	String query = getQuery(params);
    	Desktop.getDesktop().browse(new URI("https://accounts.google.com/o/oauth2/v2/auth" + query));
    	Scanner kb = new Scanner(System.in);
    	System.out.println("TOKEN: ");
    	String tok = kb.next().trim();
    	kb.close();
    	
       	List<Pair<String, String>> params2 = new ArrayList<Pair<String, String>>() {
    			private static final long serialVersionUID = 1L;
    			{
    				add(new Pair<>("code", tok));
    				add(new Pair<>("client_id", "1036300734576-c7u6dbvh06s3p7lv6tucpknfq5nln6g3.apps.googleusercontent.com"));
    				add(new Pair<>("client_secret", "lmZhxqKSbP3hc7M4HUr3BuVU"));
    				add(new Pair<>("redirect_uri", "urn:ietf:wg:oauth:2.0:oob"));
    				add(new Pair<>("grant_type", "authorization_code"));
        		}
        	};
    	HttpURLConnection connection = (HttpURLConnection) new URL("https://www.googleapis.com/oauth2/v4/token" + getQuery(params2))
    			.openConnection();
    	// HttpsURLConnection.setFollowRedirects(true);
        // connection.setInstanceFollowRedirects(true);
        connection.setRequestMethod("POST");
        connection.setDoInput(true);
        connection.setDoOutput(true);
        // connection.setRequestProperty("Accept", "application/json");
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        // connection.setRequestProperty("Content-Length", "0");
 
        
        InputStream response = connection.getInputStream();
        System.out.println(new String(response.readAllBytes()));
        response.close();
    	
    	/*
    	if (200 <= connection.getResponseCode() && connection.getResponseCode() <= 299) {
	    	try (InputStream response = connection.getInputStream()) {
	    		JsonParser parser = new JsonParser();
	    		JsonElement jse = parser.parse(new String(response.readAllBytes()));
	    		if (jse.isJsonObject()) {
	    			JsonObject jso = jse.getAsJsonObject();
	    		}
	    	}
    	} else {
        	try (InputStream response = connection.getErrorStream()) {
	    		System.out.println("HTTP response code not in 200s \n" + new String(response.readAllBytes()));
	    	}
    	}
    	*/
    }
}
