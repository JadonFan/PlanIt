package ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Pair;

public class PopupForm {
	private List<Label> labels;  
	private TextField[] textFields;
	private List<Button> buttons; 
	private VBox formBox;
	
	
	public PopupForm(List<Label> labels, Button button) {
		this.labels = Collections.unmodifiableList(labels);
		this.textFields = new TextField[labels.size()];
		this.buttons = new ArrayList<>();
		this.formBox = new VBox();
		
		this.buttons.add(button);
	}
	
	public PopupForm(List<Label> labels, TextField[] textFields, List<Button> buttons, VBox formBox) {
		this.labels = Collections.unmodifiableList(labels);
		this.textFields = (TextField[]) textFields;
		this.buttons = buttons;
		this.formBox = formBox;
	}
	
	public PopupForm(List<Label> labels, List<Button> buttons) {
		this.labels = Collections.unmodifiableList(labels);
		this.textFields = new TextField[labels.size()];
		this.buttons = buttons;
		this.formBox = new VBox();
	}
	
	
	public List<Label> getLabels() {
		return this.labels;
	}

	public void setLabels(List<Label> labels) {
		this.labels = labels;
	}

	public TextField[] getTextFields() {
		return this.textFields;
	}

	public void setTextFields(TextField[] textFields) {
		this.textFields = textFields;
	}

	public void setButtons(List<Button> buttons) {
		this.buttons = buttons;
	}

	public VBox getFormBox() {
		return this.formBox;
	}

	public void setFormBox(VBox formBox) {
		this.formBox = formBox;
	}
	
	
	public static void skinFormLayout(VBox vb, GridPane gp, Stage window) {
		window.setWidth(350);

		vb.setPadding(new Insets(5));
		vb.setSpacing(5);
		vb.setAlignment(Pos.CENTER);
		vb.prefWidthProperty().bind(window.widthProperty());
		
		gp.setVgap(10);
		gp.setHgap(10);
		gp.setAlignment(Pos.CENTER);
		gp.setPadding(new Insets(25));
	}
	
	
	public void mapPaneToPanels(GridPane gp, List<Pair<Label, Control>> extraControls) {
		int i = 0, j = 0;
		
		for (; i < this.labels.size(); i++) {
			this.textFields[i] = new TextField();
			gp.add(this.labels.get(i), 0, i);
			gp.add(this.textFields[i], 1, i);
			this.textFields[i].setCache(false);
		}
		
		if (extraControls != null) {
			for (; j < extraControls.size(); i++, j++) {
				gp.add(extraControls.get(j).getKey(), 0, i);
				gp.add(extraControls.get(j).getValue(), 1, i);
			}
		}		
	}
	
	
	// TODO change to set the scene width instead
	public void mapLayout(Stage window) {
		this.mapLayout(window, null);
	}
	
	
	// TODO change to set the scene width instead
	public void mapLayout(Stage window, List<Pair<Label, Control>> extraControls) {
		GridPane formPane = new GridPane();
		PopupForm.skinFormLayout(this.formBox, formPane, window);
		this.mapPaneToPanels(formPane, extraControls);
		
		this.formBox.getChildren().add(formPane);
		this.formBox.getChildren().addAll(this.buttons);
	}
}
