package application;

import java.sql.SQLException;

import alerts.ConfirmBox;
import home.LogInDaoImpl;
import user.AppSession;

public class CloseProgram {
	public static void closeProgram() {
		if (new ConfirmBox("Quit Program?", "Are you sure you wish to quit the program?").display()) {
			System.out.println(">> The application has been closed");
			
			try {
				if (AppSession.getIsTempAccessCode() && new LogInDaoImpl().loadGoogleAccessCode(PlannerDb.getConnection()) != null) {
					new LogInDaoImpl().deleteGoogleAccessCode(PlannerDb.getConnection());
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			// Do NOT put this code fragment in a "finally" block
			// PlannerDb.getEntityManagerFactory().close();
			System.exit(0);
		}
	}
}
