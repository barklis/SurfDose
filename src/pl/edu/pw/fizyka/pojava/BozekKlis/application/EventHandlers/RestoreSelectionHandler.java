package pl.edu.pw.fizyka.pojava.BozekKlis.application.EventHandlers;

import pl.edu.pw.fizyka.pojava.BozekKlis.application.GUI.GUI;
import pl.edu.pw.fizyka.pojava.BozekKlis.application.GUI.Selection;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class RestoreSelectionHandler implements EventHandler<ActionEvent> {
	
	GUI gui;
	
	public RestoreSelectionHandler(GUI gui) {
		this.gui = gui;
	}
	
	@Override
	public void handle(ActionEvent arg0) {
		Selection.restoreSelection();
		gui.getCenterPanel().getDrawingPanel().getSelectionPanel().draw();
	}
}
