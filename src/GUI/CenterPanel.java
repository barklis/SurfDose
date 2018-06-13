package GUI;

import javafx.scene.layout.BorderPane;

public class CenterPanel extends BorderPane {
	GUI gui;
	
	DrawingPanel drawingPanel;
	
	public CenterPanel(GUI gui) {
		this.gui = gui;
		
		drawingPanel = new DrawingPanel(gui);
		
		setCenter(drawingPanel);
		
	}

	public DrawingPanel getDrawingPanel() {
		return drawingPanel;
	}
}
