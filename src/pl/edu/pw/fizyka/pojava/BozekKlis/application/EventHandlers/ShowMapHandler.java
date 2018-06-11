package pl.edu.pw.fizyka.pojava.BozekKlis.application.EventHandlers;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import pl.edu.pw.fizyka.pojava.BozekKlis.application.Preferences;
import pl.edu.pw.fizyka.pojava.BozekKlis.application.DicomDataModule.DcmData;
import pl.edu.pw.fizyka.pojava.BozekKlis.application.GUI.GUI;

public class ShowMapHandler implements EventHandler<ActionEvent> {
	
	GUI gui;
	
	public ShowMapHandler(GUI gui) {
		this.gui = gui;
	}
	
	@Override
	public void handle(ActionEvent event) {
		if(DcmData.isDoseCalculated()) {
			gui.getCenterPanel().getDrawingPanel().placeMap();
			gui.getCenterPanel().getDrawingPanel().getMapPanel().setContainerSize();
			gui.getCenterPanel().getDrawingPanel().getMapPanel().drawMap();
			
			gui.getMenuBarClass().getShowHistogramItem().setSelected(false);
			gui.getMenuBarClass().getShowFilesContentItem().setSelected(false);
			gui.getMenuBarClass().getShowMapItem().setSelected(true);
		}
		else {
			gui.getMenuBarClass().getShowMapItem().setSelected(false);
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle(Preferences.getLabel("notCalculatedInformationTitle"));
			alert.setContentText(Preferences.getLabel("notCalculatedInformationContent"));
			alert.setHeaderText("");
			alert.showAndWait();
		}
	}

}
