package api;

import java.io.IOException;
import java.io.InputStream;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import home.LogIn;

/*
 NOTE: The APIs will be part of a separate Gradle project in the future and link 
 this project as a dependency 
 */
@SpringBootApplication
@ComponentScan ({"course", "api"})
class ApiStarter {
	public static void main(String[] args) {
		try (InputStream inStream = LogIn.class.getResourceAsStream("/login.properties")) { //$NON-NLS-1$
			Properties logInProperties = new Properties();
			logInProperties.load(inStream);

			String dbUser = logInProperties.getProperty("username"); //$NON-NLS-1$
			String dbPass = logInProperties.getProperty("password"); //$NON-NLS-1$
			
			// PlannerDb.setEntityManagerFactory(Persistence.createEntityManagerFactory("Planner"));
	        Class.forName("com.mysql.cj.jdbc.Driver");
	       
			ApiDatabase.setConnection(DriverManager.getConnection("jdbc:mysql://localhost:3306/planner?useUnicode=" + "true&use"
					+ "JDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC", dbUser, dbPass)); //$NON-NLS-1$
	        
			/*
			PlannerDb.setConnection(DriverManager.getConnection("jdbc:mysql://planner.cljnz1x2e9qg.us-east-2.rds.amazonaws.com:3306/planner?useUnicode="
					+ "true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC", dbUser, dbPass)); //$NON-NLS-1$
			*/
		} catch (IOException | SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		} 
		
		System.out.println(">> Launching Tomcat server");
		SpringApplication.run(ApiStarter.class, args);
	}
}
