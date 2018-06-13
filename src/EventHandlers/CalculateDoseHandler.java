package EventHandlers;

import DataModule.DcmData;
import GUI.GUI;
import application.Preferences;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

// Allow to calculate dose
public class CalculateDoseHandler implements EventHandler<ActionEvent> {

	GUI gui;
	
	public CalculateDoseHandler(GUI gui) {
		this.gui = gui;
	}

	@Override
	public void handle(ActionEvent event) {
		if(DcmData.isContourLoaded() && DcmData.isDoseLoaded() && DcmData.isPlanLoaded()) {
			gui.getBottomPanel().setDoseCalculatedLabel(Preferences.getLabel("calculating"));
			Thread calc = new Thread(new Runnable() {
				@Override
				public void run() {
					for(int i = 0; i < DcmData.getNumberOfFrames(); ++i)
						DcmData.calculateDose(i);
					gui.getCenterPanel().getDrawingPanel().getChartPanel().initChartList();
					Platform.runLater(()->{
						gui.getBottomPanel().setDoseCalculatedLabel(Preferences.getLabel("yes"));
						System.out.println();
					});
				}
			});
			calc.start();
		}
		else {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle(Preferences.getLabel("cannotCalculateTitle"));
			
			String message = Preferences.getLabel("missingFiles") + ":\n";	
			if(!DcmData.isDoseLoaded())
				message += "\nRTDOSE";
			if(!DcmData.isContourLoaded())
				message += "\nRTSTRUCTUR";
			if(!DcmData.isPlanLoaded())
				message += "\nRTPLAN";
			
			alert.setContentText(message);
			alert.setHeaderText("");
			alert.showAndWait();
		}
	}

}
