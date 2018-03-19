package pl.edu.pw.fizyka.pojava.BozekKlis.application.EventHandlers;

import pl.edu.pw.fizyka.pojava.BozekKlis.application.GUI.Selection;
import pl.edu.pw.fizyka.pojava.BozekKlis.application.GUI.SelectionPanel;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

public class DrawSelectionListener implements EventHandler<MouseEvent> {

	static SelectionPanel gContext;
	
	public DrawSelectionListener(SelectionPanel gContext) {
		DrawSelectionListener.gContext = gContext;
	}

	@Override
	public void handle(MouseEvent e) {
		if(e.getEventType() == MouseEvent.MOUSE_PRESSED) {
			Selection.initSelection(e.getX(), e.getY(), gContext.getWidth(), gContext.getHeight());
		}
		else if(e.getEventType() == MouseEvent.MOUSE_DRAGGED) {
			Selection.getCurrentSelection().setSecondCorner(e.getX(), e.getY());
			gContext.draw();
			gContext.drawCurrentSelection();
		}
		else if(e.getEventType() == MouseEvent.MOUSE_RELEASED) {
			Selection.getCurrentSelection().finalizeSelection();
			gContext.draw();
		}
		
	}

}
