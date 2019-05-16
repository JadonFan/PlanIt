package ui;

import java.util.List;
import java.util.function.Function;

import home.Home;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.GridPane;
import javafx.stage.PopupWindow.AnchorLocation;
import javafx.util.Duration;
import javafx.util.Pair;

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
	
	public static final Button buildBackToHomeButton() {
		Button backBtn = new Button("Back To Home");		
		backBtn.setOnAction(event -> Home.getInstance().display());
		
		return backBtn;
	}
	
	
	public static final Button buildBackToHomeButton(Function<?, ?> closeAction) {
		Button backBtn = new Button("Back To Home");		
		backBtn.setOnAction(event -> {
			Home.getInstance().display();
			closeAction.apply(null);
		});
		
		return backBtn;
	}
	
	
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
	
	
	public static Tooltip buildRectToolTip(final String tipText) {
		Tooltip toolTip = new Tooltip(tipText);
		toolTip.setShowDelay(Duration.millis(500));
		toolTip.setWrapText(true);
		toolTip.setAnchorLocation(AnchorLocation.CONTENT_BOTTOM_LEFT);
		// TODO set styling
		
		return toolTip;
	}
}
