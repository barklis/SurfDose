package pl.edu.pw.fizyka.pojava.BozekKlis.application.EventHandlers;

import java.io.File;
import java.util.List;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import pl.edu.pw.fizyka.pojava.BozekKlis.application.DicomDataModule.DcmData;
import pl.edu.pw.fizyka.pojava.BozekKlis.application.DicomDataModule.DcmManager;
import pl.edu.pw.fizyka.pojava.BozekKlis.application.GUI.GUI;

// Load RTDOSE file
public class OpenRTdoseFileHandler implements EventHandler<ActionEvent> {

	GUI gui;
	
	public OpenRTdoseFileHandler(GUI gui) {
		this.gui = gui;
	}
	
	@Override
	public void handle(ActionEvent event) {
		List<File> doseFiles = DcmManager.getDcmFiles(null, "RTDOSE");
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
					});
				}
			});
			loadData.start();
		}
	}

}
