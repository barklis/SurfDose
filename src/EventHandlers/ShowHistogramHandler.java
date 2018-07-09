package EventHandlers;

import DataModule.DcmData;
import GUI.GUI;
import application.Preferences;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

//Show chart of calculated dose
public class ShowHistogramHandler implements EventHandler<ActionEvent> {

	GUI gui;
	
	public ShowHistogramHandler(GUI gui) {
		this.gui = gui;
	}

	@Override
	public void handle(ActionEvent event) {
		if(DcmData.isDoseCalculated()) {
			if(!gui.getCenterPanel().getDrawingPanel().getChartPanel().isListInited())
				gui.getCenterPanel().getDrawingPanel().getChartPanel().initChartList();
			
			gui.getBottomPanel().showFrameCountMode();
			gui.getCenterPanel().getDrawingPanel().getChartPanel().initChartList();
			gui.getCenterPanel().getDrawingPanel().placeChart();
			gui.getBottomPanel().setzCoordLabel(DcmData.getDcmFrames().get(gui.getCenterPanel().getDrawingPanel().getCurrentFrame()).getZ());
			
			gui.getMenuBarClass().getShowMapItem().setSelected(false);
			gui.getMenuBarClass().getShowFilesContentItem().setSelected(false);
			gui.getMenuBarClass().getShowHistogramItem().setSelected(true);
		}
		else {
			gui.getMenuBarClass().getShowHistogramItem().setSelected(false);
			gui.showInformationDialog(Preferences.getLabel("notCalculatedInformationTitle"), Preferences.getLabel("notCalculatedInformationContent"));
		}
	}

}
