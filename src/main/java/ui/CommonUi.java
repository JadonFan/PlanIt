package ui;

import java.util.List;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class CommonUi {
	public static void disableUnusedFields(List<Label> relevantLabels, List<Label> allLabels, TextField[] allTextFields) {
		if (relevantLabels == null) {
			return;
		}
		
		for (int i = 0; i < allLabels.size(); i++) {
			// TODO use label name to check, rather than the default contains method
			if (relevantLabels.contains(allLabels.get(i))) {
				allTextFields[i].setDisable(false);
			} else {
				allTextFields[i].setDisable(true);
				allTextFields[i].setStyle(""); // TODO get darker shade of grey to make it more clear that the field is disabled
			}
		}
	}
	
}
