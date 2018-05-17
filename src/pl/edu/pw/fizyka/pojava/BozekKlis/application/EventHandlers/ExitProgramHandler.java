package pl.edu.pw.fizyka.pojava.BozekKlis.application.EventHandlers;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;

// run when program is closing
public class ExitProgramHandler implements EventHandler<ActionEvent> {

	@Override
	public void handle(ActionEvent arg0) {
		System.exit(0);
	}

}
