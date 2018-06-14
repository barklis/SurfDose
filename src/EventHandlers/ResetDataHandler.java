package EventHandlers;

import DataModule.DcmData;
import GUI.GUI;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

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
