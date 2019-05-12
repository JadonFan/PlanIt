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


/**
 * @deprecated Use the {@link ConfirmBox} class instead
 * @author jadon
 */
@Deprecated
public class CloseConfirmationBox extends AlertBox {	
	private static boolean confirmClose;

	public boolean display() {		
		Stage window = new Stage();
		window.initModality(Modality.APPLICATION_MODAL);
		window.setTitle("Quit Confirmation");
		window.setMinWidth(250);
		
		Label label = new Label("Are you sure you wish to close the application?");
		
		Button yesBtn = new Button("Yes");
		super.setBtnEnterHandler(yesBtn);
		yesBtn.setOnAction(event -> {
			confirmClose = true;
			window.close();
		});
		Button noBtn = new Button("No");
		super.setBtnEnterHandler(noBtn);
		noBtn.setOnAction(event -> {
			confirmClose = false;
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
		
		return confirmClose;
	}
}
