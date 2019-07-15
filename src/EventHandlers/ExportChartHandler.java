package EventHandlers;

import DataModule.DcmData;
import DataModule.DcmManager;
import  GUI.GUI;
import  GUI.DrawingPanel;
import application.Preferences;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class ExportChartHandler implements EventHandler<ActionEvent> {

	@Override
	public void handle(ActionEvent event) {
		boolean correctPanelLoaded =
				GUI.instance().getDrawingPanel().isCanvasEmbeded() ||
				GUI.instance().getDrawingPanel().isChartEmbeded();

		if(DcmData.isDoseCalculated() && correctPanelLoaded) {
			int currentFrameNumber = GUI.instance().getDrawingPanel().getCurrentFrame();
			DcmManager.exportChart(currentFrameNumber);
		}
		else if(correctPanelLoaded) {
			GUI.instance().showInformationDialog(Preferences.getLabel("cannotExportChartTitle"), Preferences.getLabel("notCalculatedInformationContent"));
		}
		else {
			GUI.instance().showInformationDialog(Preferences.getLabel("cannotExportChartTitle"), Preferences.getLabel("cannotExportChartContent-wrongPanel"));
		}
	}

}