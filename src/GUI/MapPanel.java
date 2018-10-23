package GUI;

import java.util.ArrayList;
import java.util.List;

import DataModule.DcmData;
import DataModule.DcmManager;
import DataModule.Point;
import application.Preferences;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class MapPanel extends Canvas {
	PointersPanel pointersPanel;
	
	private double containerWidth;
	private double containerHeight;
	
	private double[][] matrix;
	
	public MapPanel(double width, double height) {
		setHeight(height);
		setWidth(width);
		
		pointersPanel = new PointersPanel(width, height, 1, 1);
		
		matrix = null;
	}
	
	public void drawMap() {
		GUI.instance().getScalePanel().setMaxDoseAndReload(DcmData.getMaxValueByContourId(DcmData.getCurrentContourId()));
		int currentId = DcmData.getCurrentContourId();
		
		if(!DcmData.isDoseCalculated())
			return;
		
		int startingFrame = DcmManager.getStartingFrame(currentId);
		int endingFrame = DcmManager.getEndingFrame(currentId);
		
		int pixelsInCol = Preferences.getPixelsInCol();
		int pixelsInRow = Preferences.getPixelsInRow();
		
		int verticalPixelSize = (int)(containerHeight/pixelsInCol);
		int horizontalPixelSize = (int)(containerWidth/pixelsInRow);
		
		int pixelSize;
		
		if(verticalPixelSize < horizontalPixelSize)
			pixelSize = verticalPixelSize;
		else
			pixelSize = horizontalPixelSize;
		
		setWidth(pixelsInRow*pixelSize);
		setHeight(pixelsInCol*pixelSize);
		matrix = new double[pixelsInRow][pixelsInCol];
		
		double framePixelSize = getHeight()/(endingFrame-startingFrame);
		double radius = Preferences.getInterpolationRadius()*framePixelSize;
		
		GraphicsContext gc = getGraphicsContext2D();
		gc.clearRect(0, 0, getWidth(), getHeight());
		gc.setFill(Color.BLACK);
		
		for(int h = 0; h < pixelsInCol; ++h) {
			int minFrameIndex = (int)Math.ceil(startingFrame + ((h+0.5)*pixelSize-radius)/framePixelSize);
			int maxFrameIndex = (int)Math.floor(startingFrame + ((h+0.5)*pixelSize+radius)/framePixelSize);
			
			for(int w = 0; w < pixelsInRow; ++w) {
				Point pixelCoord = new Point((w+0.5)*pixelSize, (h+0.5)*pixelSize);
				List<Point> neighbours = new ArrayList<Point>();
				
				for(int f = minFrameIndex; f <= maxFrameIndex; ++f) {
					if(f < startingFrame || f > endingFrame)
						continue;
					for(Point p : DcmData.getDcmFrames().get(f).getContourById(currentId).getData()) {
						Point setPoint = new Point(p.getAngle()*getWidth()/DcmData.getAngularWidth(), (f-startingFrame)*framePixelSize);
						if(Point.distance(pixelCoord, setPoint) < radius)
							neighbours.add(p);
					}
				}
				gc.setFill(getMeanValueColor(neighbours, currentId, w, pixelsInCol-1-h));
				gc.fillRect(w*pixelSize, (pixelsInCol-1-h)*pixelSize, pixelSize, pixelSize);
			}
		}
		
		pointersPanel.setVariables(pixelSize, pixelsInCol, pixelsInRow, startingFrame, endingFrame, getWidth(), getHeight());
		GUI.instance().getBottomPanel().showMapMode(startingFrame, endingFrame);
		
		if(Preferences.isRowPointerEnabled()) {
			pointersPanel.drawLines();
		}
		
	}
	
	public Color getMeanValueColor(List<Point> points, int id, int w, int h) {
		if(points.size() == 0) {
			matrix[w][h] = 0.0;
			return DcmManager.getDoseColorScale(0.0, DcmData.getMaxValueByContourId(id));
		}
		double sum = 0.0;
		for(Point p : points)
			sum += p.getValue();
		matrix[w][h] = sum/points.size();
		return DcmManager.getDoseColorScale(matrix[w][h], DcmData.getMaxValueByContourId(id));
	}

	public void setContainerSize() {
		containerWidth = GUI.instance().getScene().getWidth()-GUI.instance().getScalePanel().getWidth();
		containerHeight = GUI.instance().getScene().getHeight()-GUI.instance().getBottomPanel().getHeight()-GUI.instance().getMenuBarClass().getHeight();
	}

	public double[][] getMatrix() {
		return matrix;
	}

	public PointersPanel getPointersPanel() {
		return pointersPanel;
	}

	public void resetMatrix() {
		matrix = null;
	}
	
}
