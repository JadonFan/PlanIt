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
	

	public GridPane buildFormPane(List<Pair<Label, Control>> extraControls) {
		GridPane formPane = new GridPane();
		formPane.setVgap(10);
		formPane.setHgap(10);
		formPane.setAlignment(Pos.CENTER);
		formPane.setPadding(new Insets(25));
		int i = 0, j = 0;
		
		for (; i < this.labels.size(); i++) {
			this.textFields[i] = new TextField();
			formPane.add(this.labels.get(i), 0, i);
			formPane.add(this.textFields[i], 1, i);
			this.textFields[i].setCache(false);
		}
		
		if (extraControls != null) {
			for (; j < extraControls.size(); i++, j++) {
				formPane.add(extraControls.get(j).getKey(), 0, i);
				formPane.add(extraControls.get(j).getValue(), 1, i);
			}
		}
		
		return formPane;
	}
	
	
	// TODO change to set the scene width instead
	public void mapLayout(Stage window) {
		window.setWidth(350);

		this.formBox.setPadding(new Insets(5));
		this.formBox.setSpacing(5);
		this.formBox.setAlignment(Pos.CENTER);
		this.formBox.prefWidthProperty().bind(window.widthProperty());
		
		this.formBox.getChildren().add(this.buildFormPane(null));
		
		this.formBox.getChildren().addAll(this.buttons);
	}
	
	
	// TODO change to set the scene width instead
	public void mapLayout(Stage window, List<Pair<Label, Control>> extraControls) {
		window.setWidth(350);

		this.formBox.setPadding(new Insets(5));
		this.formBox.setSpacing(5);
		this.formBox.setAlignment(Pos.CENTER);
		this.formBox.prefWidthProperty().bind(window.widthProperty());
		
		this.formBox.getChildren().add(this.buildFormPane(extraControls));
		
		this.formBox.getChildren().addAll(this.buttons);
	}
}
