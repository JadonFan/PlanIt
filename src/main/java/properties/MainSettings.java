package properties;

import java.util.Arrays;
import java.util.TimeZone;

import home.Home;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainSettings {
	public static void display(Stage window) {
		window.setTitle("Settings");
		
		Label langLabel = new Label("Language:");
		ChoiceBox<String> langChoice = new ChoiceBox<>();
		langChoice.getItems().addAll("English", "French", "Traditional Chinese");
		langChoice.setValue("English");
		HBox langBox = new HBox(10);
		langBox.getChildren().addAll(langLabel, langChoice);
		
		Label timezoneLabel = new Label("Time Zone:");
		ChoiceBox<String> timezoneChoice = new ChoiceBox<>();
		timezoneChoice.getItems().addAll(Arrays.asList(TimeZone.getAvailableIDs()));
		timezoneChoice.setValue(TimeZone.getDefault().getDisplayName());
		HBox timezoneBox = new HBox(10);
		timezoneBox.getChildren().addAll(timezoneLabel, timezoneChoice);
		
		Button backBtn = new Button("Back to Home");
		backBtn.setOnAction(event -> Home.getInstance().display());
		
		VBox layout = new VBox(10);
		layout.setPadding(new Insets(25));
		layout.getChildren().addAll(langBox, timezoneBox, backBtn);
		Scene scene = new Scene(layout);
		window.setScene(scene);
		window.show();	
	}
}
