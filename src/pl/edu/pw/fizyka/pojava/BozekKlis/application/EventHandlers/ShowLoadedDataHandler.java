package pl.edu.pw.fizyka.pojava.BozekKlis.application.EventHandlers;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Alert.AlertType;
import pl.edu.pw.fizyka.pojava.BozekKlis.application.Preferences;
import pl.edu.pw.fizyka.pojava.BozekKlis.application.DicomDataModule.DcmData;
import pl.edu.pw.fizyka.pojava.BozekKlis.application.GUI.GUI;

//shows data stored in loaded DICOM files
public class ShowLoadedDataHandler implements EventHandler<ActionEvent> {

	GUI gui;
	CheckMenuItem data;
	CheckMenuItem chart;
	CheckMenuItem map;
	
	public ShowLoadedDataHandler(GUI gui, CheckMenuItem data, CheckMenuItem chart, CheckMenuItem map) {
		this.gui = gui;
		this.data = data;
		this.chart = chart;
		this.map = map;
	}

	@Override
	public void handle(ActionEvent event) {
		if(DcmData.isDoseLoaded() && DcmData.isPlanLoaded()) {
			gui.getCenterPanel().getDrawingPanel().placeCanvas();
			gui.getCenterPanel().getDrawingPanel().getCanvasPanel().setContainerSize();
			gui.getCenterPanel().getDrawingPanel().getCanvasPanel().drawFrame();
			
			chart.setSelected(false);
			map.setSelected(false);
			data.setSelected(true);
		}
		else {
			data.setSelected(false);
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle(Preferences.getLabel("cannotDisplayLoadedDataTitle"));
			
			String message = Preferences.getLabel("missingFiles") + ":\n";	
			if(!DcmData.isDoseLoaded())
				message += "\nRTDOSE";
			if(!DcmData.isPlanLoaded())
				message += "\nRTPLAN";
			
			alert.setContentText(message);
			alert.setHeaderText("");
			alert.showAndWait();
		}
	}

}
