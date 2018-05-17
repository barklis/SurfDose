package pl.edu.pw.fizyka.pojava.BozekKlis.application.EventHandlers;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Alert.AlertType;
import pl.edu.pw.fizyka.pojava.BozekKlis.application.Preferences;
import pl.edu.pw.fizyka.pojava.BozekKlis.application.DicomDataModule.DcmData;
import pl.edu.pw.fizyka.pojava.BozekKlis.application.GUI.GUI;

//Show chart of calculated dose
public class ShowHistogramHandler implements EventHandler<ActionEvent> {

	GUI gui;
	CheckMenuItem data;
	CheckMenuItem chart;
	
	public ShowHistogramHandler(GUI gui, CheckMenuItem data, CheckMenuItem chart) {
		this.gui = gui;
		this.data = data;
		this.chart = chart;
	}

	@Override
	public void handle(ActionEvent event) {
		if(DcmData.isDoseCalculated()) {
			if(!gui.getCenterPanel().getDrawingPanel().getChartPanel().isListInited())
				gui.getCenterPanel().getDrawingPanel().getChartPanel().initChartList();
			
			gui.getCenterPanel().getDrawingPanel().placeChart();
			chart.setSelected(true);
			data.setSelected(false);
		}
		else {
			chart.setSelected(false);
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle(Preferences.getLabel("notCalculatedInformationTitle"));
			alert.setContentText(Preferences.getLabel("notCalculatedInformationContent"));
			alert.setHeaderText("");
			alert.showAndWait();
		}
	}

}
