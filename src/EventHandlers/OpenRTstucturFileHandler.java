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
	
	@Override
	public void handle(ActionEvent event) {
		File contourFile = DcmManager.getDcmFile(GUI.instance().getMainWindow(), "RTSTRUCTUR", "dcm");
		if(contourFile != null) {
			GUI.instance().getBottomPanel().setStructurFileName("Processing...");
			Thread loadData = new Thread(new Runnable() {	
				@Override
				public void run() {
					DcmData.setContourData(contourFile);
					Platform.runLater(()->{
						GUI.instance().getBottomPanel().setStructurFileName(contourFile.getName());
						GUI.instance().getBottomPanel().setMaxFrameNumberLabel(DcmData.getNumberOfFrames());
					});
				}
			});
			loadData.start();
		}
	}
}
