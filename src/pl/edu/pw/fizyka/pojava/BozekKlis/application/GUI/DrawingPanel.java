package pl.edu.pw.fizyka.pojava.BozekKlis.application.GUI;

import javafx.application.Platform;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.StackPane;
import pl.edu.pw.fizyka.pojava.BozekKlis.application.DicomDataModule.DcmData;

public class DrawingPanel extends StackPane {
	GUI gui;
	
	CanvasPanel canvasPanel;
	ChartPanel chartPanel;
	
	boolean chartEmbeded;
	boolean canvasEmbeded;
	
	Integer currentFrame;
	
	double width, height;
	
	public DrawingPanel(GUI gui) {
		this.gui = gui;
		this.width = 900;
		this.height = 500;
		this.currentFrame = 0;
		setPrefSize(width, height);
		
		canvasPanel = new CanvasPanel(gui, width, height);
		chartPanel = new ChartPanel(gui);
		
		this.chartEmbeded = false;
		this.canvasEmbeded = false;
		
		Platform.runLater(() -> {
			gui.getScene().setOnScroll((ScrollEvent e) -> {
				int sc = (e.getDeltaY() > 0) ? 1 : -1;
				if(this.currentFrame + sc >= 0 && this.currentFrame + sc < DcmData.getNumberOfFrames()) {
					
					if(canvasEmbeded) {
						this.currentFrame += sc;
						gui.getBottomPanel().setCurrentFrameNumberLabel(currentFrame+1);
						canvasPanel.drawFrame();
					}
					else if(chartEmbeded) {
						this.currentFrame += sc;
						gui.getBottomPanel().setCurrentFrameNumberLabel(currentFrame+1);
						gui.getCenterPanel().getDrawingPanel().changeChart();
					}
				}
			});
		});
		
		
	}
	
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

	public Integer getCurrentFrame() {
		return currentFrame;
	}

	public void setCurrentFrame(Integer currentFrame) {
		this.currentFrame = currentFrame;
	}
	
}
