package gallery;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import application.PlannerDb;
import home.Home;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class ImageGallery {
	private Stage window;
	private List<ImageView> imageViews;
	
	
	public ImageGallery() {
		this.window = new Stage();
		this.imageViews = new ArrayList<>();
	}
	
	public ImageGallery(Stage window) {
		this.window = window;
		this.imageViews = new ArrayList<>();
	}
	
	
	public Stage getWindow() {
		return this.window;
	}
	
	public List<ImageView> getImageViews() {
		return this.imageViews;
	}

	public void setImageViews(List<ImageView> imageViews) {
		this.imageViews = imageViews;
	}
	

	public void setExpandOnClick(ImageView iv) {
		iv.setOnMouseClicked(event -> {
			iv.setFitHeight(700);
			iv.setFitWidth(400);
			iv.setSmooth(true);
			iv.setPreserveRatio(true);
			
			VBox vb = new VBox();
			vb.setSpacing(10);
			vb.setPadding(new Insets(10));
			vb.setAlignment(Pos.CENTER);
			vb.getChildren().add(iv); 
			
			TextArea caption = new TextArea();
			caption.setPromptText("Write your caption here");
			caption.setPrefWidth(iv.getFitWidth());
			caption.setPrefRowCount(2);
			vb.getChildren().add(caption);
			
			Button backToGalleryBtn = new Button("Gallery");
			backToGalleryBtn.setOnAction(event2 -> this.display());
			vb.getChildren().add(backToGalleryBtn);
			
			this.window.setScene(new Scene(vb));
			this.window.show();
		});
	}
	
	
	public void display() {
		this.window.setTitle("Image Gallery");
		
		try {
			new ImageDaoImpl(this.imageViews).loadImages(PlannerDb.getConnection());
			for (ImageView iv : this.imageViews) {
				this.setExpandOnClick(iv);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
        
		VBox galleryLayoutBox = new VBox();
		try {
			Parent menuBar = FXMLLoader.load(Home.class.getResource("/menubar.fxml")); //$NON-NLS-1$
			galleryLayoutBox.getChildren().add(menuBar);
		} catch (IOException e) {
			e.printStackTrace();
		} 
		
		VBox imageContainerBox = new VBox();
		imageContainerBox.setPadding(new Insets(0, 15, 15, 15));
        Button openButton = new Button("Select Image");
        openButton.setOnAction(event -> {
            File file = new FileChooser().showOpenDialog(this.window);
            if (file != null && file.exists()) {
            	try {
            		ImageView iv = new ImageDaoImpl().addImage(PlannerDb.getConnection(), file);
            		this.setExpandOnClick(iv);
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
        
        imageContainerBox.getChildren().addAll(btnBox, imagePane);
        galleryLayoutBox.getChildren().add(imageContainerBox);
        
	    Scene scene = new Scene(galleryLayoutBox, 500, 500);
	    this.window.setScene(scene);
	    this.window.show();	
	}
}
