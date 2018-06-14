package EventHandlers;

import java.io.File;

import DataModule.DcmData;
import DataModule.DcmManager;
import GUI.GUI;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class OpenRTplanFileHandler implements EventHandler<ActionEvent> {
	
	GUI gui;
	
	public OpenRTplanFileHandler(GUI gui) {
		this.gui = gui;
	}
	
	@Override
	public void handle(ActionEvent event) {
		File planFile = DcmManager.getDcmFile(gui.getMainWindow(), "RTPLAN");
		if(planFile != null) {
			gui.getBottomPanel().setPlanFileName("Processing...");
			Thread loadData = new Thread(new Runnable() {	
				@Override
				public void run() {
					DcmData.setPlanData(planFile);
					Platform.runLater(()->{
						gui.getBottomPanel().setPlanFileName(planFile.getName());
					});
				}
			});
			loadData.start();
		}
	}
}
