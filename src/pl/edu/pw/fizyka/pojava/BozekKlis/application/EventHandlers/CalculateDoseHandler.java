package pl.edu.pw.fizyka.pojava.BozekKlis.application.EventHandlers;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import pl.edu.pw.fizyka.pojava.BozekKlis.application.Preferences;
import pl.edu.pw.fizyka.pojava.BozekKlis.application.DicomDataModule.DcmData;
import pl.edu.pw.fizyka.pojava.BozekKlis.application.GUI.GUI;

public class CalculateDoseHandler implements EventHandler<ActionEvent> {

	GUI gui;
	
	public CalculateDoseHandler(GUI gui) {
		this.gui = gui;
	}

	@Override
	public void handle(ActionEvent event) {
		if(DcmData.isContourLoaded() && DcmData.isDoseLoaded()) {
			gui.getBottomPanel().setDoseLabel(Preferences.getLabel("calculating"));
			Thread calc = new Thread(new Runnable() {
				@Override
				public void run() {
					for(int i = 0; i < DcmData.getNumberOfFrames(); ++i)
						DcmData.calculateDose(i);
					gui.getCenterPanel().getDrawingPanel().getChartPanel().initChartList();
					Platform.runLater(()->{
						gui.getBottomPanel().setDoseLabel(Preferences.getLabel("calculated"));
					});
				}
			});
			calc.start();
		}
	}

}
