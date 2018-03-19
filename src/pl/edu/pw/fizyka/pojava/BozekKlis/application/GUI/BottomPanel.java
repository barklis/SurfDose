package pl.edu.pw.fizyka.pojava.BozekKlis.application.GUI;

import pl.edu.pw.fizyka.pojava.BozekKlis.application.Preferences;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class BottomPanel extends HBox {
	GUI gui;
	
	Label doseTextLabel;
	Label doseLabel;
	
	public BottomPanel(GUI gui) {
		this.gui = gui;
		
		doseTextLabel = new Label(Preferences.getLabel("dose")+": ");
		//doseLabel = new Label(doseValue);
		doseLabel = new Label("0.0");
		
		getChildren().addAll(new VBox(doseTextLabel), new VBox(doseLabel));
	}
}
