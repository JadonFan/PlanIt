package ui;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;

/**
 *  This class "pairs" a text field with a name or a label. It may be useful when mapping to similar text fields
 *  or to the labels associated with different text fields. 
 *  @author Jadon
 */
public class AttachedTextField extends TextField {
	private String fieldName;
	private Label associatedLabel;
	
	
	public AttachedTextField() {
		super();
	}
	
	public AttachedTextField(String text) {
		super(text);
	}
	
	public AttachedTextField(String text, String fieldName) {
		super(text);
		this.fieldName = fieldName;
	}
	
	public AttachedTextField(String text, Label associatedLabel) {
		this(text, associatedLabel.getText());
		this.associatedLabel = associatedLabel;
	}

	
	public String getFieldName() {
		return this.fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public Label getAssociatedLabel() {
		return this.associatedLabel;
	}

	public void setAssociatedLabel(Label associatedLabel) {
		this.associatedLabel = associatedLabel;
	}
}
