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
		
		imagePanel = new ImagePanel(gui, width, height);
		selectionPanel = new SelectionPanel(gui, imagePanel.width, imagePanel.height);
		
		getChildren().add(imagePanel);
		getChildren().add(selectionPanel);
	}
	
	public ImagePanel getImagePanel() {
		return imagePanel;
	}

	public SelectionPanel getSelectionPanel() {
		return selectionPanel;
	}

	public void resize() {
		width = getWidth();
		height = getHeight();
		imagePanel.resize(width, height);
		selectionPanel.resize(imagePanel.width, imagePanel.height);
		selectionPanel.draw();
	}
}
