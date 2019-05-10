package alerts;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

// TODO combine with the closeConfirmationBox class
public class ConfirmBox extends AlertBox {
	private String boxTitle;
	private String boxText;
	private boolean isConfirmed;

	
	public ConfirmBox(String boxText) {
		this.boxTitle = "Confirm Changes";
		this.boxText = boxText;
	}
	
	public ConfirmBox(String boxTitle, String boxText) {
		this.boxTitle = boxTitle;
		this.boxText = boxText;
	}
	
	
	public String getBoxText() {
		return this.boxText;
	}

	public void setBoxText(String boxText) {
		this.boxText = boxText;
	}

	public String getBoxTitle() {
		return this.boxTitle;
	}

	public void setBoxTitle(String boxTitle) {
		this.boxTitle = boxTitle;
	}
	
	public boolean getIsConfirmed() {
		return this.isConfirmed;
	}

	
	public boolean display() {		
		Stage window = new Stage();
		window.initModality(Modality.APPLICATION_MODAL);
		window.setTitle("Confirm Changes");
		window.setMinWidth(250);
		
		Label label = new Label(this.boxText);
		
		Button yesBtn = new Button("Yes");
		super.setBtnEnterHandler(yesBtn);
		yesBtn.setOnAction(event -> {
			this.isConfirmed = true;
			window.close();
		});
		Button noBtn = new Button("No");
		super.setBtnEnterHandler(noBtn);
		noBtn.setOnAction(event -> {
			this.isConfirmed = false;
			window.close();
		});
		
		VBox layout1 = new VBox(10);
		layout1.getChildren().add(label);
		layout1.setAlignment(Pos.CENTER);
		
		HBox layout2 = new HBox(10);
		layout2.getChildren().addAll(yesBtn, noBtn);
		layout2.setPadding(new Insets(10));
		layout2.setAlignment(Pos.CENTER);
		
		BorderPane borderPane = new BorderPane();
		borderPane.setPadding(new Insets(50));
		borderPane.setTop(layout1);
		borderPane.setBottom(layout2);
		
		Scene scene = new Scene(borderPane);
		window.setScene(scene);
		window.showAndWait();
		
		return this.isConfirmed;
	}
}
