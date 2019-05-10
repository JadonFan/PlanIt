package ui;

import java.util.function.Consumer;
import java.util.function.Predicate;

import alerts.ConfirmBox;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.input.KeyCode;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import utility.CommonUtils;

/**
 * Essentially skins the text field to look like (that is, avoid the special default styles of a JavaFX text field)
 * and act like nothing more than editable text that is restricted to just a single line 
 * @author Jadon
 */
public class MaskedTextField extends TextField {
	private String defaultText;
	
	 
	public MaskedTextField() {
		super();
		this.defaultText = "";
		this.maskEditableTextField();
	}
	
	public MaskedTextField(String defaultText) {
		super(defaultText);
		this.defaultText = CommonUtils.isEmptyOrNull(defaultText) ? "" : defaultText;;
		this.maskEditableTextField();
	}
	
	public MaskedTextField(String defaultText, Font font) {
		super(defaultText);
		super.setFont(font);
		this.defaultText = CommonUtils.isEmptyOrNull(defaultText) ? "" : defaultText;;
		this.maskEditableTextField();
	}
	
	
	public String getDefaultText() {
		return this.defaultText;
	}

	public void setDefaultText(String defaultText) {
		this.defaultText = defaultText;
	}

	
	protected void maskEditableTextField() {
		super.getStylesheets().add(getClass().getResource("editabletext.css").toExternalForm());
	}
	
	
	/**
	 * Adjusts the width and height of the text field to (almost) perfectly match the width and height of the text
	 * inside that text field
	 */
	public void wrapAroundText() {
		Text text = new Text(super.getText());
		text.setFont(super.getFont());
		super.setPrefWidth(text.getBoundsInLocal().getWidth() + 40);
		super.setPrefHeight(text.getBoundsInLocal().getHeight());
	}
	
	
	/**
	 * @param textMatch a predicate that determines when the edited text by the user should be accepted
	 * @param onEnterFunc a consumer to describe the behaviour of the app once the user presses enter on the text field
	 */
	public void configEditActions(Predicate<String> textMatch, Consumer<String> onEnterFunc) {
		TextFormatter<String> avoidEnterFormatter = new TextFormatter<>(change -> {
			if (change.getText().contains("\n") || change.getText().contains("\n\r") || change.getText().contains("\r")) {
				return null;
			}
			
			return change;
		});	
		
		this.configEditActions(textMatch, onEnterFunc, avoidEnterFormatter);
	}
	
	
	/**
	 * Configures the actions 
	 * @param textMatch a predicate that determines when the edited text by the user should be accepted
	 * @param onEnterFunc a consumer to describe the behaviour of the app once the user presses enter on the text field
	 * @param textFormatter a text formatter object to inspect and filter out unwanted changes by the user on the text field
	 */
	public void configEditActions(Predicate<String> textMatch, Consumer<String> onEnterFunc, TextFormatter<String> textFormatter) {
		super.setEditable(false);
		super.setTextFormatter(textFormatter);
		
		this.wrapAroundText();
		
		super.setOnMouseClicked(event -> {
			if (event.getClickCount() == 2) {
				super.setEditable(true);
			}
			event.consume();
		});
		
		super.setOnMouseExited(event -> {
			super.setText(this.defaultText);
			super.setEditable(false);		
		});
		
		super.setOnKeyPressed(event -> {
			if (event.getCode().equals(KeyCode.ENTER)) {
				final String confirmReqText = "Are you sure you wish to make this edit?";
				if (textMatch != null && textMatch.test(super.getText()) && new ConfirmBox(confirmReqText).display()) {
					// Recall that the user cannot switch out of the confirmation box window until that user has selected "yes" or "no" 
					super.setOnMouseExited(null); 
					super.setText(super.getText()); 
					super.setEditable(false);
								
					this.wrapAroundText();
					
					// Anything else that needs to be done after the edit has been confirmed to be valid 
					// and been confirmed by the user is done here
					onEnterFunc.accept(super.getText()); 
				} else {
					super.setText(this.defaultText);
					super.setEditable(false);
				}
			} else if (event.getCode().equals(KeyCode.ESCAPE)) {
				super.setText(this.defaultText);
				super.setEditable(false);
			}
		});
	}
 }
