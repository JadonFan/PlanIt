package home;

import java.io.IOException;

import application.WindowManager;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Home {
	public static void display(Stage window) {		
		try {
			Parent root = FXMLLoader.load(Home.class.getResource("/menubar.fxml")); //$NON-NLS-1$
			WindowManager.setPrimaryWidth(800);
			WindowManager.setPrimaryHeight(700);
			
			window.setTitle("Home Page -- Planner by Jadon");
			window.setMinWidth(250);
			window.setWidth(WindowManager.getPrimaryWidth());
			window.setHeight(WindowManager.getPrimaryHeight());

			Scene scene = new Scene(root);
			window.setScene(scene);
			window.show();
		} catch (IOException e) {
			e.printStackTrace();
			window.close();
		} 
	}
}
