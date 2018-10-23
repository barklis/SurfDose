package EventHandlers;

import DataModule.DcmData;
import GUI.GUI;
import application.Preferences;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class ShowMapHandler implements EventHandler<ActionEvent> {
	
	@Override
	public void handle(ActionEvent event) {
		if(DcmData.isDoseCalculated()) {
			GUI.instance().getDrawingPanel().placeMap();
			GUI.instance().getMapPanel().setContainerSize();
			GUI.instance().getMapPanel().drawMap();
			
			GUI.instance().getMenuBarClass().getShowHistogramItem().setSelected(false);
			GUI.instance().getMenuBarClass().getShowFilesContentItem().setSelected(false);
			GUI.instance().getMenuBarClass().getShowMapItem().setSelected(true);
		}
		else {
			GUI.instance().getMenuBarClass().getShowMapItem().setSelected(false);
			GUI.instance().showInformationDialog(Preferences.getLabel("notCalculatedInformationTitle"), Preferences.getLabel("notCalculatedInformationContent"));
		}
	}

}
