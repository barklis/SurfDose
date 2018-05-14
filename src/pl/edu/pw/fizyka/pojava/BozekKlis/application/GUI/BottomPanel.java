package pl.edu.pw.fizyka.pojava.BozekKlis.application.GUI;

import pl.edu.pw.fizyka.pojava.BozekKlis.application.Preferences;
import pl.edu.pw.fizyka.pojava.BozekKlis.application.DicomDataModule.DcmData;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class BottomPanel extends HBox {
	GUI gui;
	
	Label doseFileName;
	Label structurFileName;
	Label doseLabel;
	
	public BottomPanel(GUI gui) {
		this.gui = gui;
		
		Label doseTextLabel = new Label(Preferences.getLabel("dose")+": ");
		doseLabel = new Label((DcmData.isDoseCalculated() ? Preferences.getLabel("calculated") : Preferences.getLabel("notCalculated")));
		HBox left = new HBox(new VBox(doseTextLabel), new VBox(doseLabel));
		left.getStyleClass().add("LeftHBox");
		
		
		Region region1 = new Region();
		HBox.setHgrow(region1, Priority.ALWAYS);
		
		
		Label doseFileTextLabel = new Label(Preferences.getLabel("doseFile") + ": ");
		Label structurFileTextLabel = new Label(Preferences.getLabel("structurFile") + ": ");
		VBox textColumn = new VBox(new HBox(doseFileTextLabel), new HBox(structurFileTextLabel));
		
		doseFileName = new Label(DcmData.getDoseFileName());
		structurFileName = new Label(DcmData.getStructurFileName());
		VBox nameColumn = new VBox(new HBox(doseFileName), new HBox(structurFileName));
		
		HBox right = new HBox(textColumn, nameColumn);
		right.getStyleClass().add("RightHBox");
		
		getChildren().addAll(left, region1, right);
		
	}

	public void setDoseFileName(String fileName) {
		doseFileName.setText(fileName);
	}

	public void setStructurFileName(String fileName) {
		structurFileName.setText(fileName);
	}

	public void setDoseLabel(String value) {
		doseLabel.setText(value);
	}
	
}
