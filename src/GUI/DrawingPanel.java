package GUI;

import DataModule.DcmData;
import application.Preferences;
import javafx.application.Platform;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.StackPane;

public class DrawingPanel extends StackPane {
	GUI gui;
	
	CanvasPanel canvasPanel;
	ChartPanel chartPanel;
	MapPanel mapPanel;
	
	boolean chartEmbeded;
	boolean canvasEmbeded;
	boolean mapEmbeded;
	
	Integer currentFrame;
	
	double width, height;
	
	public DrawingPanel(GUI gui) {
		this.gui = gui;
		this.width = 1000;
		this.height = 500;
		this.currentFrame = 0;
		setPrefSize(width, height);
		
		canvasPanel = new CanvasPanel(gui, width, height);
		chartPanel = new ChartPanel(gui);
		mapPanel = new MapPanel(gui, width, height);
		
		this.chartEmbeded = false;
		this.canvasEmbeded = false;
		this.mapEmbeded = false;
		
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
						changeChart();
						gui.getBottomPanel().setzCoordLabel(DcmData.getDcmFrames().get(currentFrame).getZ());
					}
				}
				
				if(mapEmbeded && Preferences.isRowPointerEnabled()) {
					getMapPanel().getPointersPanel().movePointer(sc);
					getMapPanel().getPointersPanel().drawLines();
					
					gui.getBottomPanel().setExtendedZCoordLabel();
				}
			});
		});
		
	}
	
	public void unEmbedToReset() {
		if(isCanvasEmbeded() || isChartEmbeded())
			getChildren().remove(0);
		if(isMapEmbeded()) {
			getChildren().remove(0);
			getChildren().remove(0);
		}
			
		mapEmbeded = false;
		chartEmbeded = false;
		canvasEmbeded = false;
		currentFrame = 0;
	}
	
	public void unEmbed() {
		if(isCanvasEmbeded() || isChartEmbeded())
			getChildren().remove(0);
		if(isMapEmbeded()) {
			getChildren().remove(0);
			getChildren().remove(0);
		}
		mapEmbeded = false;
		chartEmbeded = false;
		canvasEmbeded = false;
	}
	
	public void placeCanvas() {
		unEmbed();
		getChildren().add(canvasPanel);
		mapEmbeded = false;
		chartEmbeded = false;
		canvasEmbeded = true;
	}
	
	public void placeChart() {
		unEmbed();
		getChildren().add(chartPanel.getChart());
		canvasEmbeded = false;
		mapEmbeded = false;
		chartEmbeded = true;
	}
	
	public void placeMap() {
		unEmbed();
		getChildren().add(mapPanel);
		getChildren().add(mapPanel.getPointersPanel());
		canvasEmbeded = false;
		chartEmbeded = false;
		mapEmbeded = true;
	}
	
	public void changeChart() {
		if(chartEmbeded) {
			getChildren().remove(0);
			getChildren().add(chartPanel.getChart());
		}
	}
	
	public void redraw() {
		if(canvasEmbeded)
			canvasPanel.drawFrame();
		else if(mapEmbeded)
			mapPanel.drawMap();
		else if(chartEmbeded) {
			getChildren().remove(0);
			chartPanel.initChartList();
			getChildren().add(chartPanel.getChart());
		}
	}
	
	public boolean isMapEmbeded() {
		return mapEmbeded;
	}

	public void setMapEmbeded(boolean mapEmbeded) {
		this.mapEmbeded = mapEmbeded;
	}

	public MapPanel getMapPanel() {
		return mapPanel;
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
