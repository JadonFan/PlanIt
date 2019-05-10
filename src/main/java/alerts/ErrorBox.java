package alerts;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ErrorBox extends AlertBox {
	private String message;
	
	
	public ErrorBox(String message) {
		this.message = message;
	}
	
	
	public boolean display() {		
		Stage window = new Stage();
		window.initModality(Modality.APPLICATION_MODAL);
		window.setTitle("ERROR!");
		window.setMinWidth(250);
		
		Button closeBtn = new Button("Close");
		super.setBtnEnterHandler(closeBtn);
		closeBtn.setOnAction(event -> window.close());
		
		Label label = new Label(this.message);
		
		VBox layout = new VBox(10);
		layout.getChildren().addAll(label, closeBtn);
		layout.setAlignment(Pos.CENTER);
		layout.setPadding(new Insets(25));
		
		Scene scene = new Scene(layout);
		window.setScene(scene);
		window.showAndWait();
		
		return true;
	}
}
