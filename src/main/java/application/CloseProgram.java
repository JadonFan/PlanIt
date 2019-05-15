package application;

import alerts.ConfirmBox;

public class CloseProgram {
	public static void closeProgram() {
		if (new ConfirmBox("Quit Program?", "Are you sure you wish to quit the program?").display()) {
			System.out.println(">> The application has been closed");
			// PlannerDb.getEntityManagerFactory().close();
			System.exit(0);
		}
	}
}
