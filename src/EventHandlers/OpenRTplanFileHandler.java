package EventHandlers;

import java.io.File;

import DataModule.DcmData;
import DataModule.DcmManager;
import GUI.GUI;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class OpenRTplanFileHandler implements EventHandler<ActionEvent> {
	
	@Override
	public void handle(ActionEvent event) {
		File planFile = DcmManager.getDcmFile(GUI.instance().getMainWindow(), "RTPLAN");
		if(planFile != null) {
			GUI.instance().getBottomPanel().setPlanFileName("Processing...");
			Thread loadData = new Thread(new Runnable() {	
				@Override
				public void run() {
					DcmData.setPlanData(planFile);
					Platform.runLater(()->{
						GUI.instance().getBottomPanel().setPlanFileName(planFile.getName());
					});
				}
			});
			loadData.start();
		}
	}
}
