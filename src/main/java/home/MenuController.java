package home;

import alerts.ConfirmBox;
import calendar.PlannerCalendar;
import course.MyCourses;
import gallery.ImageGallery;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import properties.Help;
import properties.MainSettings;

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
		new ImageGallery().display();
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
