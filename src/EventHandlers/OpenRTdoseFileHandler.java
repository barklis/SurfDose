package EventHandlers;

import java.io.File;
import java.util.List;

import DataModule.DcmData;
import DataModule.DcmManager;
import GUI.GUI;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

// Load RTDOSE file
public class OpenRTdoseFileHandler implements EventHandler<ActionEvent> {
	
	@Override
	public void handle(ActionEvent event) {
		List<File> doseFiles = DcmManager.getDcmFiles(GUI.instance().getMainWindow(), "RTDOSE", "dcm");
		if(doseFiles != null) {
			GUI.instance().getBottomPanel().setDoseFilesLoadedLabel(DcmData.getDoseFilesLoaded()+" + ...");
			Thread loadData = new Thread(new Runnable() {
				@Override
				public void run() {
					for(File file : doseFiles)
						DcmData.setDoseData(file);
					Platform.runLater(() -> {
						GUI.instance().getBottomPanel().setDoseFilesLoadedLabel(String.valueOf(DcmData.getDoseFilesLoaded()));
						GUI.instance().getBottomPanel().setMaxFrameNumberLabel(DcmData.getNumberOfFrames());
						GUI.instance().getScalePanel().setMaxDoseAndReload(DcmData.getMaxSumDoseValue());
						GUI.instance().getDrawingPanel().redraw();
					});
				}
			});
			loadData.start();
		}
	}

}
