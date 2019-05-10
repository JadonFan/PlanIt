package gallery;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import application.PlannerDb;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class ImageGallery {
	private List<ImageView> imageViews;
	private Stage window = new Stage();
	
	
	public ImageGallery() {
		this.imageViews = new ArrayList<>();
	}
	
	
	public List<ImageView> getImageViews() {
		return this.imageViews;
	}

	public void setImageViews(List<ImageView> imageViews) {
		this.imageViews = imageViews;
	}
	
	public Stage getWindow() {
		return this.window;
	}

	
	public void display() {
		this.window.setTitle("Image Gallery");
		
		try {
			new ImageDaoImpl(this.imageViews).loadImages(PlannerDb.getConnection());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
        VBox layout = new VBox();
        layout.setPadding(new Insets(0, 15, 15, 15));
		
        Button openButton = new Button("Select Image");
        openButton.setOnAction(event -> {
            File file = new FileChooser().showOpenDialog(this.window);
            if (file != null && file.exists()) {
            	try {
            		new ImageDaoImpl().addImage(PlannerDb.getConnection(), file);
				} catch (IOException | SQLException e) {
					e.printStackTrace();
				} finally {
	            	this.display();
				}
            }
        }); 
        
        HBox btnBox = new HBox();   
        btnBox.setPadding(new Insets(10));
        btnBox.setSpacing(15);
        btnBox.getChildren().addAll(openButton);
        
        FlowPane imagePane = new FlowPane(10, 10);
        imagePane.getChildren().addAll(this.imageViews);
        
        layout.getChildren().addAll(btnBox, imagePane);
        
        // TODO code refactoring : move scene to the top of the method (same for all the classes)
	    Scene scene = new Scene(layout, 500, 500);
	    this.window.setScene(scene);
	    this.window.show();	
	}
}
