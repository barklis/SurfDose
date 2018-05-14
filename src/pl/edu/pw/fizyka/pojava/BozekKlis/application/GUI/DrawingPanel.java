package pl.edu.pw.fizyka.pojava.BozekKlis.application.GUI;

import javafx.scene.layout.StackPane;

public class DrawingPanel extends StackPane {
	GUI gui;
	
	//ImagePanel imagePanel;
	CanvasPanel canvasPanel;
	ChartPanel chartPanel;
	
	boolean chartEmbeded;
	boolean canvasEmbeded;
	
	double width, height;
	
	public DrawingPanel(GUI gui) {
		this.gui = gui;
		this.width = 800;
		this.height = 500;
		setPrefSize(width, height);
		
		//imagePanel = new ImagePanel(gui, width, height);
		canvasPanel = new CanvasPanel(gui, width, height);
		chartPanel = new ChartPanel(gui);
		
		this.chartEmbeded = false;
		this.canvasEmbeded = false;
		
		
		//getChildren().add(imagePanel);
		
	}
	
	//public ImagePanel getImagePanel() {
	//	return imagePanel;
	//}
	
	public void placeCanvas() {
		if(chartEmbeded || canvasEmbeded)
			getChildren().remove(0);
		getChildren().add(canvasPanel);
		canvasEmbeded = true;
		chartEmbeded = false;
	}
	
	public void placeChart() {
		if(chartEmbeded || canvasEmbeded)
			getChildren().remove(0);
		getChildren().add(chartPanel.getChart());
		canvasEmbeded = false;
		chartEmbeded = true;
	}
	
	public void changeChart() {
		if(chartEmbeded) {
			getChildren().remove(0);
			getChildren().add(chartPanel.getChart());
		}
	}
	
	public CanvasPanel getCanvasPanel() {
		return canvasPanel;
	}

	public ChartPanel getChartPanel() {
		return chartPanel;
	}

	public boolean isChartEmbeded() {
		return chartEmbeded;
	}
	
	public boolean isCanvasEmbeded() {
		return canvasEmbeded;
	}

	public void setChartEmbeded(boolean chartEmbeded) {
		this.chartEmbeded = chartEmbeded;
	}

	public void setCanvasEmbeded(boolean canvasEmbeded) {
		this.canvasEmbeded = canvasEmbeded;
	}
	
}
