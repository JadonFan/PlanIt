package ui;

import javafx.scene.control.Slider;

public class StandardSlider extends Slider {
	public StandardSlider() {
		super();
		super.setMin(0);
		super.setMax(100);
		super.setShowTickLabels(false);
		super.setShowTickLabels(false);
		super.setBlockIncrement(10);
	}
}
