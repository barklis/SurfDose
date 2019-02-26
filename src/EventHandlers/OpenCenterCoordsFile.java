package EventHandlers;

import java.io.File;
import java.io.IOException;

import DataModule.DcmData;
import DataModule.DcmManager;
import Exceptions.ErrorHandler;
import Exceptions.InvalidCenterCoordsFile;
import GUI.GUI;
import application.ContourCenterContainer;
import application.Preferences;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class OpenCenterCoordsFile implements EventHandler<ActionEvent> {

	@Override
	public void handle(ActionEvent event) {
		if(!DcmData.isContourLoaded() && DcmData.isPlanLoaded()) {
			String message = Preferences.getLabel("missingFiles") + ":\n";
			if(!DcmData.isContourLoaded())
				message += "\nRTSTRUCTUR";
			if(!DcmData.isPlanLoaded())
				message += "\nRTPLAN";
			GUI.instance().showInformationDialog(Preferences.getLabel("cannotImportData"), message);
			return;
		}
		
		File centerFile = DcmManager.getDcmFile(GUI.instance().getMainWindow(), "TXT", "txt");
		if(centerFile != null) {
			ContourCenterContainer dataContainer  = new ContourCenterContainer();
			try {
				dataContainer.readFile(centerFile);
				dataContainer.decode(DcmData.getIsocenterPosition());
				DcmData.setContourCenterData(dataContainer);
			} catch (IOException e) {
				new ErrorHandler(Preferences.getLabel("openFileError")).showDialog();
				e.printStackTrace();
			} catch (InvalidCenterCoordsFile e) {
				new ErrorHandler(e.getMessage()).showDialog();
			}
		}
	}

}
