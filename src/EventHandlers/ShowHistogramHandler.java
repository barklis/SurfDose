package EventHandlers;

import DataModule.DcmData;
import GUI.GUI;
import application.Preferences;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

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
			
			gui.getBottomPanel().showFrameCountMode();
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
