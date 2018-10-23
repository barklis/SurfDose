package EventHandlers;

import DataModule.DcmData;
import GUI.GUI;
import application.Preferences;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ButtonType;

public class ResetDataHandler implements EventHandler<ActionEvent> {

	@Override
	public void handle(ActionEvent event) {
		if(GUI.instance().showConfirmationDialog(Preferences.getLabel("resetDataTitle"), Preferences.getLabel("resetDataContent"))
		== ButtonType.OK) {
			
			DcmData.resetData();
			GUI.instance().getDrawingPanel().unEmbedToReset();
			GUI.instance().getBottomPanel().resetData();
			GUI.instance().getMenuBarClass().reset();
			GUI.instance().getMapPanel().resetMatrix();
		}
	}

}
