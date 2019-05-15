package home;

import java.io.IOException;
import java.io.InputStream;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import alerts.ErrorBox;
import application.PlannerDb;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class LogIn extends Application {
	public void display(Stage window) {
		window.setWidth(500);
		window.setHeight(500);
		window.setTitle("Planner by Jadon");
		
		GridPane signInPane = new GridPane();
		signInPane.setPadding(new Insets(10));
		signInPane.setVgap(10);
		signInPane.setHgap(10);
		signInPane.setAlignment(Pos.CENTER);
		
		// Username Input
		Label usernameLabel = new Label("Username:");
		GridPane.setConstraints(usernameLabel, 0, 0);
		TextField usernameInput = new TextField();
		usernameInput.setPromptText("username");
		GridPane.setConstraints(usernameInput, 1, 0);
		
		// Password Input
		Label passwordLabel = new Label("Password:");
		GridPane.setConstraints(passwordLabel, 0, 1);
		PasswordField passwordInput = new PasswordField();
		passwordInput.setPromptText("password");
		GridPane.setConstraints(passwordInput, 1, 1);
		
		// Buttons
		HBox logInBar = new HBox();
		logInBar.setSpacing(15);
		GridPane.setConstraints(logInBar, 1, 2);
		
		Button logInButton = new Button("Log In");
		logInButton.setOnAction(event -> {
			String username = usernameInput.getText().trim();
			String password = passwordInput.getText().trim();
			try {
				if (new LogInDaoImpl().isCorrectCredentials(PlannerDb.getConnection(), username, password)) {
					System.out.println(">> An user has logged in");
					Home.getInstance(new Stage()).display();
					window.close();
				} else {
					System.out.println(">> An user attempted to log in, but entered incorrect credentials");
					ErrorBox errorBox = new ErrorBox("Wrong username or password! Please try again.");
					errorBox.display();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		});
		logInBar.getChildren().add(logInButton);
		
		Button registerButton = new Button("Register");
		registerButton.setOnAction(event -> {
			new AccountRegistrationForm().display();
			System.out.println(">> An user has registered an account");
		});
		logInBar.getChildren().add(registerButton);
		
		signInPane.getChildren().addAll(usernameLabel, usernameInput, passwordLabel, passwordInput, logInBar);
		
		Scene logInScene = new Scene(signInPane, 1000, 500);
		window.setScene(logInScene);
		window.show();
	}
	
	
	@Override
	public void start(Stage primaryWindow) throws Exception {
		this.display(primaryWindow);
	}
	

	public static void main(String[] args) {
		System.out.println(">> The application has been launched");
		
		try (InputStream inStream = LogIn.class.getResourceAsStream("/login.properties")) { //$NON-NLS-1$
			Properties logInProperties = new Properties();
			logInProperties.load(inStream);

			String dbUser = logInProperties.getProperty("username"); //$NON-NLS-1$
			String dbPass = logInProperties.getProperty("password"); //$NON-NLS-1$
			
			// PlannerDb.setEntityManagerFactory(Persistence.createEntityManagerFactory("Planner"));
	        Class.forName("com.mysql.cj.jdbc.Driver");
	       
	       
			PlannerDb.setConnection(DriverManager.getConnection("jdbc:mysql://localhost:3306/planner?useUnicode=" + "true&use"
					+ "JDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC", dbUser, dbPass)); //$NON-NLS-1$
	        
			/*
			PlannerDb.setConnection(DriverManager.getConnection("jdbc:mysql://planner.cljnz1x2e9qg.us-east-2.rds.amazonaws.com:3306/planner?useUnicode="
					+ "true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC", dbUser, dbPass)); //$NON-NLS-1$
			*/
		} catch (IOException | SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		} 
		
		launch(args);
	}

}
