package home;

import java.sql.SQLException;

import alerts.ConfirmBox;
import application.EmailManager2;
import application.PlannerDb;
import calendar.PlannerCalendar;
import course.MyCourses;
import gallery.ImageGallery;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import properties.Help;
import properties.MainSettings;
import user.AppSession;
import utility.CommonUtils;

public class MenuController {
	@FXML
	private VBox menuLayoutBox;
	
	@FXML
	protected void initialize() {
		this.menuLayoutBox.prefWidthProperty().bind(Home.getInstance().getWindow().widthProperty());
	}
	
	@FXML
	public void loadHome() {
		Home.getInstance().display();
	}
	
	@FXML
	public void loadEmailForm() {
		try {
			EmailManager2 emailManager2 = new EmailManager2();
			LogInDaoImpl lidi = new LogInDaoImpl();
			lidi.loadUserEmail(PlannerDb.getConnection());
			
			boolean hasEmailRegistered = CommonUtils.isEmptyOrNull(AppSession.getEmailAddress());
			boolean hasAccessCode = CommonUtils.isEmptyOrNull(lidi.loadGoogleAccessCode(PlannerDb.getConnection()));
			
			// It's important that the Boolean expression is ordered this way to exploit short circuit evaluation
			if (!hasEmailRegistered && (AppSession.getIsTempAccessCode() || !hasAccessCode)) {
				new EmailForm(emailManager2, new Stage()).displayEmailReqForm(new StringBuilder());
			} else {
				new EmailRegistrationForm().displayEmailSetUpProcess(emailManager2);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@FXML
	public void loadCalendar() {
		new PlannerCalendar().display(Home.getInstance().getWindow());
	}
	
	@FXML
	public void loadCourses() {
		MyCourses.getInstance().display(true);
	}
	
	@FXML
	public void loadSettings() {
		MainSettings.display(Home.getInstance().getWindow());
	}
	
	@FXML
	public void loadHelp() {
		Help.getInstance().display(Home.getInstance().getWindow());
	}
	
	@FXML
	public void loadImageGallery() {
		new ImageGallery(Home.getInstance().getWindow()).display();
	}
	
	@FXML
	public void logOut() {
		new LogIn().display(Home.getInstance().getWindow());
	}
	
	@FXML 
	public void close() {
		boolean isToClose = new ConfirmBox("Quit Program?", "Are you sure you wish to close the application?").display();
		if (isToClose) Home.getInstance().getWindow().close();
	}
}
