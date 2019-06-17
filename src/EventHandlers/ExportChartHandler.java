package EventHandlers;

import DataModule.DcmData;
import DataModule.DcmManager;
import  GUI.GUI;
import application.Preferences;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class ExportChartHandler implements EventHandler<ActionEvent> {

	@Override
	public void handle(ActionEvent event) {
		if(DcmData.isDoseCalculated()) {
			int currentFrameNumber = GUI.instance().getDrawingPanel().getCurrentFrame();
			DcmManager.exportChart(currentFrameNumber);
		}
		else {
			GUI.instance().showInformationDialog(Preferences.getLabel("notCalculatedInformationTitle"), Preferences.getLabel("notCalculatedInformationContent"));
		}
	}

}