package application.GUI;

import javafx.scene.layout.BorderPane;

public class CenterPanel extends BorderPane {
	GUI gui;
	
	ToolBox toolbox;
	DrawingPanel drawingPanel;
	
	public CenterPanel(GUI gui) {
		this.gui = gui;
		
		toolbox = new ToolBox(gui);
		drawingPanel = new DrawingPanel(gui);
		
		setLeft(toolbox);
		setCenter(drawingPanel);
		
	}

	public ToolBox getToolbox() {
		return toolbox;
	}

	public DrawingPanel getDrawingPanel() {
		return drawingPanel;
	}
}
