package EventHandlers;

import DataModule.DcmData;
import GUI.GUI;
import application.Preferences;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

//shows data stored in loaded DICOM files
public class ShowLoadedDataHandler implements EventHandler<ActionEvent> {

	GUI gui;
	
	public ShowLoadedDataHandler(GUI gui) {
		this.gui = gui;
	}

	@Override
	public void handle(ActionEvent event) {
		if(DcmData.isDoseLoaded() && DcmData.isContourLoaded()) {
			gui.getCenterPanel().getDrawingPanel().placeCanvas();
			gui.getCenterPanel().getDrawingPanel().getCanvasPanel().setContainerSize();
			gui.getCenterPanel().getDrawingPanel().getCanvasPanel().drawFrame();
			
			gui.getBottomPanel().showFrameCountMode();
			gui.getCenterPanel().getScalePanel().setMaxDoseAndReload(DcmData.getMaxSumDoseValue());
			
			gui.getMenuBarClass().getShowHistogramItem().setSelected(false);
			gui.getMenuBarClass().getShowMapItem().setSelected(false);
			gui.getMenuBarClass().getShowFilesContentItem().setSelected(true);
		}
		else {
			gui.getMenuBarClass().getShowFilesContentItem().setSelected(false);
			
			String message = Preferences.getLabel("missingFiles") + ":\n";	
			if(!DcmData.isDoseLoaded())
				message += "\nRTDOSE";
			if(!DcmData.isContourLoaded())
				message += "\nRTSTRUCUR";
			
			gui.showInformationDialog(Preferences.getLabel("cannotDisplayLoadedDataTitle"), message);
			
		}
	}

}
