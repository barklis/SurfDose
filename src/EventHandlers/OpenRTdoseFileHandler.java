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

	GUI gui;
	
	public OpenRTdoseFileHandler(GUI gui) {
		this.gui = gui;
	}
	
	@Override
	public void handle(ActionEvent event) {
		List<File> doseFiles = DcmManager.getDcmFiles(gui.getMainWindow(), "RTDOSE");
		if(doseFiles != null) {
			gui.getBottomPanel().setDoseFilesLoadedLabel(DcmData.getDoseFilesLoaded()+" + ...");
			Thread loadData = new Thread(new Runnable() {
				@Override
				public void run() {
					for(File file : doseFiles)
						DcmData.setDoseData(file);
					Platform.runLater(() -> {
						gui.getBottomPanel().setDoseFilesLoadedLabel(String.valueOf(DcmData.getDoseFilesLoaded()));
						gui.getBottomPanel().setMaxFrameNumberLabel(DcmData.getNumberOfFrames());
						gui.getCenterPanel().getScalePanel().setMaxDoseAndReload(DcmData.getMaxSumDoseValue());
						gui.getCenterPanel().getDrawingPanel().redraw();
					});
				}
			});
			loadData.start();
		}
	}

}
