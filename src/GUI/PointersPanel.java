package GUI;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class PointersPanel extends Canvas {
	
	private int rowCounter;
	private int colCounter;
	private int pixelSize;
	private int pixelsInCol;
	private int pixelsInRow;
	
	public PointersPanel(double width, double height, int row, int col) {
		setWidth(width);
		setHeight(height);
		
		this.rowCounter = row;
		this.colCounter = col;
		
		pixelSize = 0;
		pixelsInCol = 0;
		pixelsInRow = 0;
	}
	
	public void movePointer(int delta) {
		if(rowCounter - delta >= 1 && rowCounter - delta <= pixelsInCol)
			rowCounter -= delta;
	}
	
	public void setVariables(int pxSize, int pxsInCol, int pxsInRow, double width, double height) {
		pixelSize = pxSize;
		pixelsInCol = pxsInCol;
		pixelsInRow = pxsInRow;
		
		setWidth(width);
		setHeight(height);
	}
	
	public void drawLines() {
		GraphicsContext g = getGraphicsContext2D();
		getGraphicsContext2D().clearRect(0, 0, getWidth(), getHeight());
		
		g.setStroke(Color.BLACK);
		g.setLineWidth(1);
		
		double yPos = (rowCounter-0.5)*pixelSize;
		g.strokeLine(0, yPos, getWidth(), yPos);
	}
	
}
