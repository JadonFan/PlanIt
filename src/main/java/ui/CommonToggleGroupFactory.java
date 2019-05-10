package ui;

import javafx.geometry.Insets;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Separator;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;
import javafx.util.Pair;

public final class CommonToggleGroupFactory {
	public static Pair<VBox, RadioButton[]> buildStdRadioGrp(ToggleGroup group, String[] radioOptionTexts) {
		VBox vb = new VBox();
		vb.setSpacing(10);
		vb.setPadding(new Insets(15, 15, 0, 15));
		
		RadioButton[] radioButtons = new RadioButton[radioOptionTexts.length];
		
		for (int index = 0; index < radioOptionTexts.length; index++) {
			RadioButton rb = new RadioButton(radioOptionTexts[index]);
			rb.setToggleGroup(group);
			radioButtons[index] = rb;
			vb.getChildren().add(rb);
		}
		
		Separator separator = new Separator();
		vb.getChildren().add(separator);
		
		return new Pair<>(vb, radioButtons);
	}
}
