package GUI;

import DataModule.DcmData;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class PointersPanel extends Canvas {
	
	GUI gui;
	
	private int rowCounter;
	private int colCounter;
	private int pixelSize;
	private int pixelsInCol;
	private int pixelsInRow;
	private int startingFrame;
	private int endingFrame;
	
	public PointersPanel(double width, double height, int row, int col, GUI gui) {
		setWidth(width);
		setHeight(height);
		
		this.rowCounter = row;
		this.colCounter = col;
		
		pixelSize = 0;
		pixelsInCol = 0;
		pixelsInRow = 0;
		
		this.gui = gui;
	}
	
	public void movePointer(int delta) {
		if(rowCounter - delta >= 1 && rowCounter - delta <= pixelsInCol)
			rowCounter -= delta;
	}
	
	public void setVariables(int pxSize, int pxsInCol, int pxsInRow, int strtFrame, int endFrame, double width, double height) {
		pixelSize = pxSize;
		pixelsInCol = pxsInCol;
		pixelsInRow = pxsInRow;
		startingFrame = strtFrame;
		endingFrame = endFrame;
		
		setWidth(width);
		setHeight(height);
		
		rowCounter = 1;
	}
	
	public void drawLines() {
		GraphicsContext g = getGraphicsContext2D();
		g.clearRect(0, 0, getWidth(), getHeight());
		
		g.setStroke(Color.BLACK);
		g.setLineWidth(1);
		
		double yPos = (rowCounter-0.5)*pixelSize;
		g.strokeLine(0, yPos, getWidth(), yPos);
	}

	public double[] getZPos() {
		double startZ = DcmData.getDcmFrames().get(startingFrame).getZ();
		double endZ = DcmData.getDcmFrames().get(endingFrame).getZ();
		return new double[] {startZ, (endZ - startZ)*(rowCounter-0.5)/pixelsInCol + startZ, endZ};
	}

	public void eraseLines() {
		getGraphicsContext2D().clearRect(0, 0, getWidth(), getHeight());
	}

	public int getStartingFrame() {
		return startingFrame;
	}

	public int getEndingFrame() {
		return endingFrame;
	}
	
}
