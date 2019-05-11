package application;

import alerts.ConfirmBox;
import javafx.stage.Stage;

public class CloseProgram {
	public static void closeProgram(Stage window) {
		if (new ConfirmBox("Quit Confirmation", "Are you sure you wish to close the application?").display()) {
			System.out.println(">> The application has been closed.");
			window.close();
			// PlannerDb.getEntityManagerFactory().close();
			System.exit(0);
		}
	}
}
