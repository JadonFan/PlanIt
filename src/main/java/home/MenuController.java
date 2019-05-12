package home;

import alerts.ConfirmBox;
import calendar.PlannerCalendar;
import application.WindowManager;
import course.MyCourses;
import gallery.ImageGallery;
import javafx.fxml.FXML;
import properties.Help;
import properties.MainSettings;

public class MenuController {
	@FXML
	public void loadCalendar() {
		new PlannerCalendar().display(WindowManager.getPrimaryWindow());
	}
	
	@FXML
	public void loadCourses() {
		MyCourses.getInstance().display(true);
	}
	
	@FXML
	public void loadSettings() {
		MainSettings.display(WindowManager.getPrimaryWindow());
	}
	
	@FXML
	public void loadHelp() {
		Help.getInstance().display(WindowManager.getPrimaryWindow());
	}
	
	@FXML
	public void loadImageGallery() {
		new ImageGallery().display();
	}
	
	@FXML
	public void logOut() {
		new LogIn().display(WindowManager.getPrimaryWindow());
	}
	
	@FXML 
	public void close() {
		boolean isToClose = new ConfirmBox("Quit Confirmation", "Are you sure you wish to close the application?").display();
		if (isToClose) WindowManager.getPrimaryWindow().close();
	}
}
