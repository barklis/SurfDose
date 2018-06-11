package pl.edu.pw.fizyka.pojava.BozekKlis.application.EventHandlers;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import pl.edu.pw.fizyka.pojava.BozekKlis.application.DicomDataModule.DcmData;
import pl.edu.pw.fizyka.pojava.BozekKlis.application.GUI.GUI;

public class ResetDataHandler implements EventHandler<ActionEvent> {
	
	GUI gui;
	
	public ResetDataHandler(GUI gui) {
		this.gui = gui;
	}

	@Override
	public void handle(ActionEvent event) {
		DcmData.resetData();
		gui.getCenterPanel().getDrawingPanel().unEmbed();
		gui.getBottomPanel().resetData();
		gui.getMenuBarClass().reset();
	}

}
