package pl.edu.pw.fizyka.pojava.BozekKlis.application.EventHandlers;

import pl.edu.pw.fizyka.pojava.BozekKlis.application.GUI.GUI;
import pl.edu.pw.fizyka.pojava.BozekKlis.application.GUI.Selection;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class DeleteSelectionHandler implements EventHandler<ActionEvent> {
	
	GUI gui;
	
	public DeleteSelectionHandler(GUI gui) {
		this.gui = gui;
	}
	
	@Override
	public void handle(ActionEvent arg0) {
		Selection.removeLast();
		gui.getCenterPanel().getDrawingPanel().getSelectionPanel().draw();
	}
}
