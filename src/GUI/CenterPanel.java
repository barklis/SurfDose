package GUI;

import javafx.scene.layout.BorderPane;

public class CenterPanel extends BorderPane {
	
	DrawingPanel drawingPanel;
	ScalePanel scalePanel;
	
	public CenterPanel(GUI gui) {
		
		drawingPanel = new DrawingPanel(gui);
		scalePanel = new ScalePanel(gui);
		
		setCenter(drawingPanel);
		setLeft(scalePanel);
	}

	public DrawingPanel getDrawingPanel() {
		return drawingPanel;
	}

	public ScalePanel getScalePanel() {
		return scalePanel;
	}
	
}
