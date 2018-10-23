package EventHandlers;

import DataModule.DcmData;
import GUI.GUI;
import application.Preferences;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

// Allow to calculate dose
public class CalculateDoseHandler implements EventHandler<ActionEvent> {

	@Override
	public void handle(ActionEvent event) {
		if(DcmData.isContourLoaded() && DcmData.isDoseLoaded()) {
			GUI.instance().getBottomPanel().setDoseCalculatedLabel(Preferences.getLabel("calculating"));
			Thread calc = new Thread(new Runnable() {
				@Override
				public void run() {
					for(int i = 0; i < DcmData.getNumberOfFrames(); ++i)
						DcmData.calculateDose(i);
					
					Platform.runLater(()->{
						GUI.instance().getBottomPanel().setDoseCalculatedLabel(Preferences.getLabel("yes"));
					});
				}
			});
			calc.start();
		}
		else {	
			String message = Preferences.getLabel("missingFiles") + ":\n";	
			if(!DcmData.isDosePreLoaded())
				message += "\nRTDOSE";
			if(!DcmData.isContourLoaded())
				message += "\nRTSTRUCTUR";
			
			GUI.instance().showInformationDialog(Preferences.getLabel("cannotCalculateTitle"), message);
		}
	}

}
