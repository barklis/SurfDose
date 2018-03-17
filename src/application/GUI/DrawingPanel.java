package application.GUI;

import javafx.scene.layout.StackPane;

public class DrawingPanel extends StackPane {
	GUI gui;
	
	ImagePanel imagePanel;
	SelectionPanel selectionPanel;
	
	double width, height;
	
	public DrawingPanel(GUI gui) {
		this.gui = gui;
		
		width = 700;
		height = 500;
		setPrefSize(width, height);
		
		imagePanel = new ImagePanel(gui);
		selectionPanel = new SelectionPanel(gui);
		getChildren().add(imagePanel);
		getChildren().add(selectionPanel);
	}
}
