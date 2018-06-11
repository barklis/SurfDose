package pl.edu.pw.fizyka.pojava.BozekKlis.application.EventHandlers;

import java.io.File;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import pl.edu.pw.fizyka.pojava.BozekKlis.application.DicomDataModule.DcmData;
import pl.edu.pw.fizyka.pojava.BozekKlis.application.DicomDataModule.DcmManager;
import pl.edu.pw.fizyka.pojava.BozekKlis.application.GUI.GUI;

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
