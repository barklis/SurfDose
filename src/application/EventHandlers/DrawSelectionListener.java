package application.EventHandlers;

import application.GUI.Selection;
import application.GUI.SelectionPanel;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

public class DrawSelectionListener implements EventHandler<MouseEvent> {

	static SelectionPanel gc;
	
	public DrawSelectionListener(SelectionPanel gc) {
		DrawSelectionListener.gc = gc;
	}

	@Override
	public void handle(MouseEvent e) {
		if(e.getEventType() == MouseEvent.MOUSE_PRESSED) {
			Selection.initSelection(e.getX(), e.getY());
		}
		else if(e.getEventType() == MouseEvent.MOUSE_DRAGGED) {
			Selection.getCurrentSelection().setSecondCorner(e.getX(), e.getY());
			gc.draw();
			gc.drawCurrentSelection();
		}
		else if(e.getEventType() == MouseEvent.MOUSE_RELEASED) {
			Selection.getCurrentSelection().finalizeSelection();
			gc.draw();
		}
		
	}

}
