package ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;

public final class CommonToggleGroupFactory {
	public static Pair<GridPane, RadioButton[]> buildStdRadioGrp(ToggleGroup group, String[] radioOptionTexts) {
		GridPane gridPane = new GridPane();
		gridPane.setVgap(10);
		gridPane.setPadding(new Insets(15));
		gridPane.setAlignment(Pos.CENTER);
		
		RadioButton[] radioButtons = new RadioButton[radioOptionTexts.length];
		int index = 0;
		
		for (; index < radioOptionTexts.length; index++) {
			RadioButton rb = new RadioButton(radioOptionTexts[index]);
			rb.setToggleGroup(group);
			radioButtons[index] = rb;
			gridPane.add(rb, 0, index);
		}
		
		return new Pair<>(gridPane, radioButtons);
	}
}
