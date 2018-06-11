package pl.edu.pw.fizyka.pojava.BozekKlis.application.EventHandlers;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import pl.edu.pw.fizyka.pojava.BozekKlis.application.Preferences;
import pl.edu.pw.fizyka.pojava.BozekKlis.application.DicomDataModule.DcmData;
import pl.edu.pw.fizyka.pojava.BozekKlis.application.GUI.GUI;

//Show chart of calculated dose
public class ShowHistogramHandler implements EventHandler<ActionEvent> {

	GUI gui;
	
	public ShowHistogramHandler(GUI gui) {
		this.gui = gui;
	}

	@Override
	public void handle(ActionEvent event) {
		if(DcmData.isDoseCalculated()) {
			if(!gui.getCenterPanel().getDrawingPanel().getChartPanel().isListInited())
				gui.getCenterPanel().getDrawingPanel().getChartPanel().initChartList();
			
			gui.getCenterPanel().getDrawingPanel().placeChart();
			
			gui.getMenuBarClass().getShowMapItem().setSelected(false);
			gui.getMenuBarClass().getShowFilesContentItem().setSelected(false);
			gui.getMenuBarClass().getShowHistogramItem().setSelected(true);
		}
		else {
			gui.getMenuBarClass().getShowHistogramItem().setSelected(false);
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle(Preferences.getLabel("notCalculatedInformationTitle"));
			alert.setContentText(Preferences.getLabel("notCalculatedInformationContent"));
			alert.setHeaderText("");
			alert.showAndWait();
		}
	}

}
