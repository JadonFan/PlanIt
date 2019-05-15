package ui;

import java.util.function.Function;

import home.Home;
import javafx.scene.control.Button;

public final class CommonButtonFactory {
	public static final Button buildBackToHomeButton() {
		Button backBtn = new Button("Back To Home");		
		backBtn.setOnAction(event -> Home.getInstance().display());
		
		return backBtn;
	}
	
	
	public static final Button buildBackToHomeButton(Function<?,?> closeAction) {
		Button backBtn = new Button("Back To Home");		
		backBtn.setOnAction(event -> {
			Home.getInstance().display();
			closeAction.apply(null);
		});
		
		return backBtn;
	}
}
