package ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.RowConstraints;
import javafx.util.Pair;

public class CoordinatesManager {
	private Pane pane;
	private List<Pair<Number,Number>> takenCoords;

	
	public CoordinatesManager(Pane pane) {
		this.pane = pane;
		this.takenCoords = new ArrayList<>();
	}
	
	public CoordinatesManager(Pane pane, List<Pair<Number,Number>> takenCoords) {
		this.pane = pane;
		this.takenCoords = takenCoords;
	}
	
	
	public List<Pair<Number,Number>> getTakenCoords() {
		return this.takenCoords;
	}
	
	public void setTakenCoords(List<Pair<Number,Number>> takenCoords) {
		this.takenCoords = takenCoords;
	}
	
	
	
	public static void forceColsAndRows(final GridPane gridPane, final int numCols, final int numRows) {
        for (int i = 0; i < numCols; i++) {
            ColumnConstraints colConst = new ColumnConstraints();
            colConst.setPercentWidth(100.0 / numCols);
            gridPane.getColumnConstraints().add(colConst);
        }
        
        for (int i = 0; i < numRows; i++) {
            RowConstraints rowConst = new RowConstraints();
            rowConst.setPercentHeight(100.0 / numRows);
            gridPane.getRowConstraints().add(rowConst);         
        }
	}
	
	
	public boolean isOccupiedCoords(final Number columnIndex, final Number rowIndex) {
		if (columnIndex == null || rowIndex == null) {
			return true;
		}
		
		boolean isOccupiedCoords = false;
		
		for (Pair<Number,Number> currentCoords : this.takenCoords) {
			if (currentCoords.getKey().equals(columnIndex) && currentCoords.getValue().equals(rowIndex)) {
				isOccupiedCoords = true;
				break;
			}
		}
		
		return isOccupiedCoords;
	}
	
	
	public Pair<Number, Number> getRandomUniqueCoordinates() {
		Number columnIndex = null;
		Number rowIndex = null;

		do {
			if (this.pane instanceof GridPane) {
				columnIndex = new Random().nextInt(((GridPane) this.pane).getColumnCount() + 1);
				rowIndex = new Random().nextInt(((GridPane) this.pane).getRowCount() + 1);
			} else {
				columnIndex = new Random().nextDouble() * this.pane.getHeight();
				rowIndex = new Random().nextDouble() * this.pane.getWidth();
			}
		} while (this.isOccupiedCoords(columnIndex, rowIndex));
		
		Pair<Number, Number> rCoords = new Pair<Number, Number>(columnIndex, rowIndex);
		this.takenCoords.add(rCoords);
		
		return rCoords;
	}
}
