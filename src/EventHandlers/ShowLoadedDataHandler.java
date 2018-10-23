package EventHandlers;

import DataModule.DcmData;
import GUI.GUI;
import application.Preferences;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

//shows data stored in loaded DICOM files
public class ShowLoadedDataHandler implements EventHandler<ActionEvent> {

	@Override
	public void handle(ActionEvent event) {
		if(DcmData.isDoseLoaded() && DcmData.isContourLoaded()) {
			GUI.instance().getDrawingPanel().placeCanvas();
			GUI.instance().getCanvasPanel().setContainerSize();
			GUI.instance().getCanvasPanel().drawFrame();
			
			GUI.instance().getBottomPanel().showFrameCountMode();
			GUI.instance().getScalePanel().setMaxDoseAndReload(DcmData.getMaxSumDoseValue());
			
			GUI.instance().getMenuBarClass().getShowHistogramItem().setSelected(false);
			GUI.instance().getMenuBarClass().getShowMapItem().setSelected(false);
			GUI.instance().getMenuBarClass().getShowFilesContentItem().setSelected(true);
		}
		else {
			GUI.instance().getMenuBarClass().getShowFilesContentItem().setSelected(false);
			
			String message = Preferences.getLabel("missingFiles") + ":\n";	
			if(!DcmData.isDoseLoaded() && !DcmData.isDosePreLoaded())
				message += "\nRTDOSE";
			if(!DcmData.isContourLoaded())
				message += "\nRTSTRUCUR";
			
			GUI.instance().showInformationDialog(Preferences.getLabel("cannotDisplayLoadedDataTitle"), message);
			
		}
	}

}
