package GUI;

import DataModule.DcmData;
import application.Preferences;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

//Bottom panel
public class BottomPanel extends HBox {
	private Label planFileName;
	private Label structurFileName;
	private Label doseCalculatedLabel;
	private Label doseFilesLoadedLabel;
	private Label currentframeNumberLabel;
	private Label maxframeNumberLabel;
	private Label separatorLabel;
	private Label zCoord;
	
	public BottomPanel(GUI gui) {
		Label doseCalculatedTextLabel = new Label(Preferences.getLabel("doseCalculated")+": ");
		Label doseFilesLoadedTextLabel = new Label(Preferences.getLabel("doseFilesLoaded")+": ");
		doseCalculatedLabel = new Label((DcmData.isDoseCalculated() ? Preferences.getLabel("yes") : Preferences.getLabel("no")));
		doseFilesLoadedLabel = new Label(String.valueOf(DcmData.getDoseFilesLoaded()));
		VBox left = new VBox(new HBox(doseCalculatedTextLabel, doseCalculatedLabel), new HBox(doseFilesLoadedTextLabel, doseFilesLoadedLabel));
		left.getStyleClass().add("LeftBox");
		
		Region region1 = new Region();
		HBox.setHgrow(region1, Priority.ALWAYS);
		
		
		separatorLabel = new Label(" / ");
		currentframeNumberLabel = new Label(String.valueOf(gui.getDrawingPanel().getCurrentFrame()));
		maxframeNumberLabel = new Label(String.valueOf(DcmData.getNumberOfFrames()));
		
		VBox center = new VBox(new HBox(currentframeNumberLabel, separatorLabel, maxframeNumberLabel));
		center.getStyleClass().add("CenterBox");
		
		
		Region region2 = new Region();
		HBox.setHgrow(region2, Priority.ALWAYS);
		
		zCoord = new Label("-");
		VBox zCoordsBox = new VBox (new HBox(new Label("Z: "), zCoord));
		zCoordsBox.getStyleClass().add("ZBox");
		
		Region region3 = new Region();
		HBox.setHgrow(region3, Priority.ALWAYS);
		
		Label doseFileTextLabel = new Label(Preferences.getLabel("planFile") + ": ");
		Label structurFileTextLabel = new Label(Preferences.getLabel("structurFile") + ": ");
		VBox textColumn = new VBox(new HBox(doseFileTextLabel), new HBox(structurFileTextLabel));
		
		//doseFileName = new Label(DcmData.getDoseFileName());
		planFileName = new Label(DcmData.getPlanFileName());
		structurFileName = new Label(DcmData.getStructurFileName());
		VBox nameColumn = new VBox(new HBox(planFileName), new HBox(structurFileName));
		
		HBox right = new HBox(textColumn, nameColumn);
		right.getStyleClass().add("RightBox");
		
		getChildren().addAll(left, region1, center, region2, zCoordsBox, region3, right);
		
	}
	
	public void showMapMode(int startingFrame, int endingFrame) {
		currentframeNumberLabel.setText(String.valueOf(startingFrame+1));
		maxframeNumberLabel.setText(String.valueOf(endingFrame+1));
		separatorLabel.setText(" - ");
		
		if(Preferences.isRowPointerEnabled() && GUI.instance().getDrawingPanel().isMapEmbeded())
			zCoord.setText(DcmData.getDcmFrames().get(startingFrame).getZ() + " / " + GUI.instance().getMapPanel().getPointersPanel().getZPos()[1] + " / " + DcmData.getDcmFrames().get(endingFrame).getZ());
		else
			zCoord.setText(DcmData.getDcmFrames().get(startingFrame).getZ() + " / " + DcmData.getDcmFrames().get(endingFrame).getZ());
	}
	
	public void showFrameCountMode() {
		currentframeNumberLabel.setText(String.valueOf(GUI.instance().getDrawingPanel().getCurrentFrame()));
		maxframeNumberLabel.setText(String.valueOf(DcmData.getNumberOfFrames()));
		separatorLabel.setText(" / ");
		
		zCoord.setText(String.valueOf(DcmData.getDcmFrames().get(GUI.instance().getDrawingPanel().getCurrentFrame()).getZ()));
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

	public void setzCoordLabel(double value) {
		this.zCoord.setText(String.valueOf(value));
	}
	
	public void setExtendedZCoordLabel() {
		double[] zCoords = GUI.instance().getMapPanel().getPointersPanel().getZPos();
		this.zCoord.setText(zCoords[0] + " / " + Math.round(zCoords[1]*100)/100.0 + " / " + zCoords[2]);
	}
	
}
