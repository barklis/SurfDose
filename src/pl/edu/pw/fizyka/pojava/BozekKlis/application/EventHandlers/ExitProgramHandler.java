package pl.edu.pw.fizyka.pojava.BozekKlis.application.EventHandlers;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class ExitProgramHandler implements EventHandler<ActionEvent> {

	@Override
	public void handle(ActionEvent arg0) {
		if(ContourLineWidthHandler.optionsWindow != null)
			ContourLineWidthHandler.optionsWindow.close();
		System.exit(0);
	}

}
