package pl.edu.pw.fizyka.pojava.BozekKlis.application.EventHandlers;

import java.io.File;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.FileChooser;
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
		FileChooser chooser = new FileChooser();
		chooser.setInitialDirectory(new File(System.getProperty("user.dir")));
		File doseFile = DcmManager.getDcmFile(null, "RTDOSE");
		if(doseFile != null) {
			gui.getBottomPanel().setDoseFileName("Processing...");
			Thread loadData = new Thread(new Runnable() {
				@Override
				public void run() {	
					DcmData.setDoseData(doseFile);
					Platform.runLater(() -> {
						gui.getBottomPanel().setDoseFileName(doseFile.getName());
						gui.getBottomPanel().setMaxFrameNumberLabel(DcmData.getNumberOfFrames());
					});
				}
			});
			loadData.start();
		}
	}

}
