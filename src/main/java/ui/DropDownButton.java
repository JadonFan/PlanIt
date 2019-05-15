package ui;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;

public class DropDownButton extends Button {
	private boolean isShowingDropDown = false;
	
	public DropDownButton(Pane dropDownPane, Node beforeNode, Pane parentPane) {
		super();
		this.createDropDownEffect(dropDownPane, beforeNode, parentPane);
	}
	
	public DropDownButton(String text, Pane dropDownPane, Node beforeNode, Pane parentPane) {
		super(text);
		this.createDropDownEffect(dropDownPane, beforeNode, parentPane);
	}

	
	public void createDropDownEffect(Pane dropDownPane, Node beforeNode, Pane parentPane) {
		super.setOnAction(event -> {
			this.isShowingDropDown = !this.isShowingDropDown;
			if (this.isShowingDropDown) {
				parentPane.getChildren().add(parentPane.getChildren().indexOf(beforeNode) + 1, dropDownPane);
			} else {
				parentPane.getChildren().remove(dropDownPane);
			}
		});
	}
}
