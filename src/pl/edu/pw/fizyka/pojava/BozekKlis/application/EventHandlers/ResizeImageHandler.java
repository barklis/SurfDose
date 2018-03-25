package pl.edu.pw.fizyka.pojava.BozekKlis.application.EventHandlers;

import pl.edu.pw.fizyka.pojava.BozekKlis.application.GUI.GUI;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class ResizeImageHandler implements EventHandler<ActionEvent> {
	GUI gui;
	
	public ResizeImageHandler(GUI gui) {
		this.gui = gui;
	}

	@Override
	public void handle(ActionEvent arg0) {
		gui.getCenterPanel().getDrawingPanel().resizeImage();
	}
}
