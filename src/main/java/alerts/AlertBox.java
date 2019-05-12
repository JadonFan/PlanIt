package alerts;

import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.Node;

public abstract class AlertBox {
	public abstract boolean display();
	
	public void setBtnEnterHandler(Node root) {
		   root.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
		        if (event.getCode() == KeyCode.ENTER) {
		        	((Button) root).fire();
		        	event.consume(); 
		        }
		    });
	}
}
