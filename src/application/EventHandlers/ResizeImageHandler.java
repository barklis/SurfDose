package application.EventHandlers;

import application.GUI.GUI;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class ResizeImageHandler implements EventHandler<ActionEvent> {
	GUI root;
	
	public ResizeImageHandler(GUI root) {
		this.root = root;
	}

	@Override
	public void handle(ActionEvent arg0) {
		root.getCenterPanel().getDrawingPanel().resize();
	}
}
