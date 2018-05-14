package pl.edu.pw.fizyka.pojava.BozekKlis.application.EventHandlers;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.CheckMenuItem;
import pl.edu.pw.fizyka.pojava.BozekKlis.application.DicomDataModule.DcmData;
import pl.edu.pw.fizyka.pojava.BozekKlis.application.GUI.GUI;

public class ShowLoadedDataHandler implements EventHandler<ActionEvent> {

	GUI gui;
	CheckMenuItem data;
	CheckMenuItem chart;
	
	public ShowLoadedDataHandler(GUI gui, CheckMenuItem data, CheckMenuItem chart) {
		this.gui = gui;
		this.data = data;
		this.chart = chart;
	}

	@Override
	public void handle(ActionEvent event) {
		if(DcmData.isDoseLoaded()) {
			gui.getCenterPanel().getDrawingPanel().placeCanvas();
			gui.getCenterPanel().getDrawingPanel().getCanvasPanel().setContainerSize();
			gui.getCenterPanel().getDrawingPanel().getCanvasPanel().setPixelSize();
			gui.getCenterPanel().getDrawingPanel().getCanvasPanel().drawFrame();
			chart.setSelected(false);
			data.setSelected(true);
		}
		else
			data.setSelected(false);
	}

}
