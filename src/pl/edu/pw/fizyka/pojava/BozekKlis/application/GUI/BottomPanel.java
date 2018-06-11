package pl.edu.pw.fizyka.pojava.BozekKlis.application.GUI;

import pl.edu.pw.fizyka.pojava.BozekKlis.application.Preferences;
import pl.edu.pw.fizyka.pojava.BozekKlis.application.DicomDataModule.DcmData;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

//Bottom panel
public class BottomPanel extends HBox {
	GUI gui;
	
	//Label doseFileName;
	Label planFileName;
	Label structurFileName;
	Label doseCalculatedLabel;
	Label doseFilesLoadedLabel;
	Label currentframeNumberLabel;
	Label maxframeNumberLabel;
	
	public BottomPanel(GUI gui) {
		this.gui = gui;
		
		Label doseCalculatedTextLabel = new Label(Preferences.getLabel("doseCalculated")+": ");
		Label doseFilesLoadedTextLabel = new Label(Preferences.getLabel("doseFilesLoaded")+": ");
		doseCalculatedLabel = new Label((DcmData.isDoseCalculated() ? Preferences.getLabel("yes") : Preferences.getLabel("no")));
		doseFilesLoadedLabel = new Label(String.valueOf(DcmData.getDoseFilesLoaded()));
		VBox left = new VBox(new HBox(doseCalculatedTextLabel, doseCalculatedLabel), new HBox(doseFilesLoadedTextLabel, doseFilesLoadedLabel));
		left.getStyleClass().add("LeftBox");
		
		Region region1 = new Region();
		HBox.setHgrow(region1, Priority.ALWAYS);
		
		
		currentframeNumberLabel = new Label(String.valueOf(gui.getCenterPanel().getDrawingPanel().getCurrentFrame()));
		maxframeNumberLabel = new Label(String.valueOf(DcmData.getNumberOfFrames()));
		
		VBox center = new VBox(new HBox(currentframeNumberLabel, new Label(" / "), maxframeNumberLabel));
		center.getStyleClass().add("CenterBox");
		
		
		Region region2 = new Region();
		HBox.setHgrow(region2, Priority.ALWAYS);
		
		
		Label doseFileTextLabel = new Label(Preferences.getLabel("planFile") + ": ");
		Label structurFileTextLabel = new Label(Preferences.getLabel("structurFile") + ": ");
		VBox textColumn = new VBox(new HBox(doseFileTextLabel), new HBox(structurFileTextLabel));
		
		//doseFileName = new Label(DcmData.getDoseFileName());
		planFileName = new Label(DcmData.getPlanFileName());
		structurFileName = new Label(DcmData.getStructurFileName());
		VBox nameColumn = new VBox(new HBox(planFileName), new HBox(structurFileName));
		
		HBox right = new HBox(textColumn, nameColumn);
		right.getStyleClass().add("RightBox");
		
		getChildren().addAll(left, region1, center, region2, right);
		
	}
	
	public void resetData() {
		structurFileName.setText("-");
		planFileName.setText("-");
		doseFilesLoadedLabel.setText("0");
		doseCalculatedLabel.setText(Preferences.getLabel("no"));
		currentframeNumberLabel.setText("0");
		maxframeNumberLabel.setText("0");
	}

	public void setStructurFileName(String fileName) {
		structurFileName.setText(fileName);
	}
	
	public void setPlanFileName(String fileName) {
		planFileName.setText(fileName);
	}

	public void setDoseCalculatedLabel(String value) {
		doseCalculatedLabel.setText(value);
	}

	public void setDoseFilesLoadedLabel(String value) {
		doseFilesLoadedLabel.setText(value);
	}
	
	public void setCurrentFrameNumberLabel(int currentframeNumberLabel) {
		this.currentframeNumberLabel.setText(String.valueOf(currentframeNumberLabel));
	}

	public void setMaxFrameNumberLabel(int maxframeNumberLabel) {
		this.maxframeNumberLabel.setText(String.valueOf(maxframeNumberLabel));
	}
	
}
