package ui;

import javafx.scene.control.Tooltip;
import javafx.stage.PopupWindow.AnchorLocation;
import javafx.util.Duration;

public final class CommonTooltipFactory {
	public static Tooltip buildRectToolTip(final String tipText) {
		Tooltip toolTip = new Tooltip(tipText);
		toolTip.setShowDelay(Duration.millis(500));
		toolTip.setWrapText(true);
		toolTip.setAnchorLocation(AnchorLocation.CONTENT_BOTTOM_LEFT);
		// TODO set styling
		
		return toolTip;
	}
}
