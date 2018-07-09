package EventHandlers;

import DataModule.DcmData;
import GUI.GUI;
import application.Preferences;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

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
			gui.showInformationDialog(Preferences.getLabel("notCalculatedInformationTitle"), Preferences.getLabel("notCalculatedInformationContent"));
		}
	}

}
