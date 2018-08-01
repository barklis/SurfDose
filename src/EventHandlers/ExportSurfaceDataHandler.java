package EventHandlers;

import DataModule.DcmManager;
import GUI.GUI;
import application.Preferences;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class ExportSurfaceDataHandler implements EventHandler<ActionEvent> {

	GUI gui;
	
	public ExportSurfaceDataHandler(GUI gui) {
		this.gui = gui;
	}

	@Override
	public void handle(ActionEvent event) {
		double[][] matrix = gui.getCenterPanel().getDrawingPanel().getMapPanel().getMatrix();
		
		if(matrix != null) {
			DcmManager.saveDataToFile(gui.getMainWindow(), matrix);
		}
		else {
			gui.showInformationDialog(Preferences.getLabel("matrixNotGeneratedTitle"), Preferences.getLabel("matrixNotGeneratedContent"));
		}
	}

}
