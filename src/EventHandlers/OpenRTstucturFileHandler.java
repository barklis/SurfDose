package EventHandlers;

import java.io.File;

import DataModule.DcmData;
import DataModule.DcmManager;
import GUI.GUI;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

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
