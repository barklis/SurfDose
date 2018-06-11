package pl.edu.pw.fizyka.pojava.BozekKlis.application.EventHandlers;

import java.io.File;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import pl.edu.pw.fizyka.pojava.BozekKlis.application.GUI.GUI;
import pl.edu.pw.fizyka.pojava.BozekKlis.application.DicomDataModule.DcmData;
import pl.edu.pw.fizyka.pojava.BozekKlis.application.DicomDataModule.DcmManager;

// Load RTSTRUCUR file
public class OpenRTstucturFileHandler implements EventHandler<ActionEvent> {

	GUI gui;
	
	public OpenRTstucturFileHandler(GUI gui) {
		this.gui = gui;
	}
	
	@Override
	public void handle(ActionEvent event) {
		File contourFile = DcmManager.getDcmFile(gui.getMainWindow(), "RTSTRUCTUR");
		if(contourFile != null) {
			gui.getBottomPanel().setStructurFileName("Processing...");
			Thread loadData = new Thread(new Runnable() {	
				@Override
				public void run() {
					DcmData.setContourData(contourFile);
					Platform.runLater(()->{
						gui.getBottomPanel().setStructurFileName(contourFile.getName());
						gui.getBottomPanel().setMaxFrameNumberLabel(DcmData.getNumberOfFrames());
					});
				}
			});
			loadData.start();
		}
	}
}
