package EventHandlers;

import DataModule.DcmData;
import GUI.GUI;
import application.Preferences;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

//Show chart of calculated dose
public class ShowChartHandler implements EventHandler<ActionEvent> {

	@Override
	public void handle(ActionEvent event) {
		if(DcmData.isDoseCalculated()) {
			if(!GUI.instance().getChartPanel().isListInited())
				GUI.instance().getChartPanel().initChartList();
			
			GUI.instance().getBottomPanel().showFrameCountMode();
			GUI.instance().getChartPanel().initChartList();
			GUI.instance().getDrawingPanel().placeChart();
			GUI.instance().getBottomPanel().setzCoordLabel(DcmData.getDcmFrames().get(GUI.instance().getDrawingPanel().getCurrentFrame()).getZ());
			
			GUI.instance().getMenuBarClass().getShowMapItem().setSelected(false);
			GUI.instance().getMenuBarClass().getShowFilesContentItem().setSelected(false);
			GUI.instance().getMenuBarClass().getShowHistogramItem().setSelected(true);
		}
		else {
			GUI.instance().getMenuBarClass().getShowHistogramItem().setSelected(false);
			GUI.instance().showInformationDialog(Preferences.getLabel("notCalculatedInformationTitle"), Preferences.getLabel("notCalculatedInformationContent"));
		}
	}

}
