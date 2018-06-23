package EventHandlers;

import DataModule.DcmData;
import GUI.GUI;
import application.Preferences;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ButtonType;

public class ResetDataHandler implements EventHandler<ActionEvent> {
	
	GUI gui;
	
	public ResetDataHandler(GUI gui) {
		this.gui = gui;
	}

	@Override
	public void handle(ActionEvent event) {
		if(gui.showConfirmationDialog(Preferences.getLabel("resetDataTitle"), Preferences.getLabel("resetDataContent")) == ButtonType.OK) {
			DcmData.resetData();
			gui.getCenterPanel().getDrawingPanel().unEmbed();
			gui.getBottomPanel().resetData();
			gui.getMenuBarClass().reset();
		}
	}

}
