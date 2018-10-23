package EventHandlers;

import DataModule.DcmData;
import DataModule.DcmManager;
import GUI.GUI;
import application.Preferences;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class ExportSurfaceDataHandler implements EventHandler<ActionEvent> {

	@Override
	public void handle(ActionEvent event) {
		double[][] matrix = GUI.instance().getMapPanel().getMatrix();
		
		if(matrix != null && DcmData.isPlanLoaded()) {
			DcmManager.saveDataToFile(GUI.instance().getMainWindow(), matrix);
		}
		else if(!DcmData.isPlanLoaded()){
			String message = Preferences.getLabel("missingFiles") + ":\n\nRTPLAN";	
			GUI.instance().showInformationDialog(Preferences.getLabel("cannotExportData"), message);
		}
		else {
			GUI.instance().showInformationDialog(Preferences.getLabel("cannotExportData"), Preferences.getLabel("matrixNotGeneratedContent"));
		}
	}

}
