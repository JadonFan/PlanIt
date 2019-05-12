package ui;

import java.util.function.Function;

import home.Home;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public final class CommonButtonFactory {
	public static final Button buildBackToHomeButton(Stage window) {
		Button backBtn = new Button("Back To Home");		
		backBtn.setOnAction(event -> Home.display(window));
		
		return backBtn;
	}
	
	
	public static final Button buildBackToHomeButton(Stage window, Function<?,?> closeAction) {
		Button backBtn = new Button("Back To Home");		
		backBtn.setOnAction(event -> {
			Home.display(window);
			closeAction.apply(null);
		});
		
		return backBtn;
	}
}
