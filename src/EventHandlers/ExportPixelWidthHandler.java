package EventHandlers;

import DataModule.DcmData;
import DataModule.DcmManager;

import GUI.GUI;
import application.Preferences;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class ExportPixelWidthHandler implements EventHandler<ActionEvent> {

	@Override
	public void handle(ActionEvent event) {
		if(DcmData.isContourLoaded())
			DcmManager.exportPixelWidth();
		else {
			String message = Preferences.getLabel("missingFiles") + ":\n";
			if(!DcmData.isContourLoaded())
				message += "\nRTSTRUCTUR";
			GUI.instance().showInformationDialog(Preferences.getLabel("cannotExportData"), message);
		}

	}

}