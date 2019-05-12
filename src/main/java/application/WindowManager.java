package application;

import javafx.stage.Stage;

// NOTE: This class will be expanded to include other windows as needed...
public class WindowManager {
	private static int primaryWidth; 
	private static int primaryHeight;
	private static Stage primaryWindow;

	
	static {
		WindowManager.primaryWidth = 500;
		WindowManager.primaryHeight = 500;
	}
	
	
	public static Stage getPrimaryWindow() {
		return WindowManager.primaryWindow;
	}
	
	public static void setPrimaryWindow(Stage window) {
		WindowManager.primaryWindow = window;
		window.setOnCloseRequest(event -> {
			event.consume();
			CloseProgram.closeProgram(window);
		});
	}
	
	public static int getPrimaryWidth() {
		return WindowManager.primaryWidth;
	}

	public static void setPrimaryWidth(int width) {
		WindowManager.primaryWidth = width;
	}
	
	public static int getPrimaryHeight() {
		return primaryHeight;
	}

	public static void setPrimaryHeight(int height) {
		WindowManager.primaryHeight = height;
	}
}
