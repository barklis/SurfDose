package GUI;

import DataModule.DcmManager;
import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class ScalePanel extends Canvas {
	GUI gui;
	double maxDose;
	int grids;
	
	double width;
	double containerHeight;

	public ScalePanel(GUI gui) {
		this.gui = gui;
		maxDose = 1.0;
		
		grids = 30;
		width = 80;
		
		setWidth(width);
	}
	
	public void drawScale() {
		GraphicsContext gc = getGraphicsContext2D();
		setContainerSize();
		
		gc.setFill(Color.BLACK);
		gc.fillRect(0, 0, width, containerHeight);
		gc.setStroke(Color.VIOLET);
		gc.setLineWidth(2);
		gc.strokeLine(width, 0, width, containerHeight);
		
		int gridHeight = (int) (containerHeight/(grids+3));
		
		gc.setStroke(Color.WHITE);
		gc.setLineWidth(1);
		gc.setTextBaseline(VPos.CENTER);
		gc.strokeText("[GY]", width*0.5, gridHeight);
		for(int i = 0; i <= grids; ++i) {
			double value = (grids-i)*maxDose/grids;
			gc.setFill(DcmManager.getDoseColorScale(value, maxDose));
			gc.fillRect(width*0.125, 2*gridHeight + gridHeight*i, width*0.25, gridHeight);
			
			gc.strokeText(getShortenedString(value), width*0.5, 2*gridHeight + gridHeight*(i+0.5));
		}
	}
	
	private String getShortenedString(double value) {
		String str = String.valueOf(value);
		int index = str.indexOf(".");
		if(index != -1 && str.length() > index+3)
			return str.substring(0, index+3);
		return str;
	}

	public void setContainerSize() {
		containerHeight = gui.getScene().getHeight()-gui.getBottomPanel().getHeight()-gui.getMenuBarClass().getHeight();
		setHeight(containerHeight);
	}

	public void setMaxDoseAndReload(double maxDose) {
		this.maxDose = maxDose/100.0;
		drawScale();
	}

	public int getGrids() {
		return grids;
	}

	public void setGridsAndReload(int grids) {
		this.grids = grids;
		drawScale();
	}
	
}
