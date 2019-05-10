package ui;

import javafx.scene.control.TreeItem;

public final class TreeComponentsFactory<T> {
	public TreeItem<T> buildBranch(final T title, TreeItem<T> parent) {
		TreeItem<T> item = new TreeItem<>(title);
		item.setExpanded(true);
		parent.getChildren().add(item);
		
		return item;
	}
}
