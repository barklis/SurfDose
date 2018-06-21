package GUI;

import java.util.ArrayList;
import java.util.List;

import DataModule.DcmData;
import DataModule.DcmFrame;
import DataModule.DcmManager;
import DataModule.Point;
import application.Preferences;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class MapPanel extends Canvas {
	GUI gui;
	
	double containerWidth;
	double containerHeight;
	
	public MapPanel(GUI gui, double width, double height) {
		this.gui = gui;
		setHeight(height);
		setWidth(width);
	}
	
	public void drawMap() {
		gui.getCenterPanel().getScalePanel().setMaxDoseAndReload(DcmData.getMaxValueByContourId(DcmData.getCurrentContourId()));
		int currentId = DcmData.getCurrentContourId();
		
		if(!DcmData.isDoseCalculated())
			return;
		GraphicsContext gc = getGraphicsContext2D();
		gc.clearRect(0, 0, containerWidth, containerHeight);
		gc.setFill(Color.BLACK);
		
		int startingFrame = getStartingFrame(currentId);
		int endingFrame = getEndingFrame(currentId);
		gui.getBottomPanel().showMapMode(startingFrame, endingFrame);
		
		int pixelsInCol = Preferences.getPixelsInCol();
		int pixelSize = (int)(containerHeight/pixelsInCol);
		int iterations = (int) (containerWidth/pixelSize);
		
		double framePixelSize = containerHeight/(endingFrame-startingFrame);
		double radius = Preferences.getInterpolationRadius()*framePixelSize;
		
		// Main rectangle
		for(int h = 0; h < pixelsInCol; ++h) {
			int minFrameIndex = (int)Math.ceil(startingFrame + ((h+0.5)*pixelSize-radius)/framePixelSize);
			int maxFrameIndex = (int)Math.floor(startingFrame + ((h+0.5)*pixelSize+radius)/framePixelSize);
			
			for(int w = 0; w < iterations; ++w) {
				Point pixelCoord = new Point((w+0.5)*pixelSize, (h+0.5)*pixelSize);
				List<Point> neighbours = new ArrayList<Point>();
				
				for(int f = minFrameIndex; f <= maxFrameIndex; ++f) {
					if(f < startingFrame || f > endingFrame)
						continue;
					for(Point p : DcmData.getDcmFrames().get(f).getContourById(currentId).getData()) {
						Point setPoint = new Point(p.getAngle()*containerWidth/DcmData.getAngularWidth(), (f-startingFrame)*framePixelSize);
						if(Point.distance(pixelCoord, setPoint) < radius)
							neighbours.add(p);
					}
				}
				gc.setFill(getMeanValueColor(neighbours, currentId));
				gc.fillRect(w*pixelSize, h*pixelSize, pixelSize, pixelSize);
			}
			//Right remaining rectangle
			Point pixelCoord = new Point((iterations*pixelSize+containerWidth)*0.5, (h+0.5)*pixelSize);
			List<Point> neighbours = new ArrayList<Point>();
			for(int f = minFrameIndex; f <= maxFrameIndex; ++f) {
				if(f < startingFrame || f > endingFrame)
					continue;
				for(Point p : DcmData.getDcmFrames().get(f).getContourById(currentId).getData()) {
					Point setPoint = new Point(p.getAngle()*containerWidth/Math.PI*0.5, (f-startingFrame)*framePixelSize);
					if(Point.distance(pixelCoord, setPoint) < radius)
						neighbours.add(p);
				}
			}
			gc.setFill(getMeanValueColor(neighbours, currentId));
			gc.fillRect(iterations*pixelSize, h*pixelSize, (int)(containerWidth-iterations*pixelSize), pixelSize);
		}
		//Bottom remaining rectangle
		int remainingHeight = (int) (containerHeight-pixelsInCol*pixelSize);
		int minFrameIndex = (int)Math.ceil(startingFrame + (containerHeight-remainingHeight*0.5-radius)/framePixelSize);
		int maxFrameIndex = (int)Math.floor(startingFrame + (containerHeight-remainingHeight*0.5+radius)/framePixelSize);
		for(int w = 0; w < iterations; ++w) {
			Point pixelCoord = new Point((w+0.5)*pixelSize, containerHeight-remainingHeight*0.5);
			List<Point> neighbours = new ArrayList<Point>();
			
			for(int f = minFrameIndex; f <= maxFrameIndex; ++f) {
				if(f < startingFrame || f > endingFrame)
					continue;
				for(Point p : DcmData.getDcmFrames().get(f).getContourById(currentId).getData()) {
					Point setPoint = new Point(p.getAngle()*containerWidth/Math.PI*0.5, (f-startingFrame)*framePixelSize);
					if(Point.distance(pixelCoord, setPoint) < radius)
						neighbours.add(p);
				}
			}
			gc.setFill(getMeanValueColor(neighbours, currentId));
			gc.fillRect(w*pixelSize, (int)(containerHeight-remainingHeight), pixelSize, pixelSize);
		}
		// Bottom-right pixel
		Point pixelCoord = new Point((iterations*pixelSize+containerWidth)*0.5, containerHeight-remainingHeight*0.5);
		List<Point> neighbours = new ArrayList<Point>();
		for(int f = minFrameIndex; f <= maxFrameIndex; ++f) {
			if(f < startingFrame || f > endingFrame)
				continue;
			for(Point p : DcmData.getDcmFrames().get(f).getContourById(currentId).getData()) {
				Point setPoint = new Point(p.getAngle()*containerWidth/Math.PI*0.5, (f-startingFrame)*framePixelSize);
				if(Point.distance(pixelCoord, setPoint) < radius)
					neighbours.add(p);
			}
		}
		gc.setFill(getMeanValueColor(neighbours, currentId));
		gc.fillRect(iterations*pixelSize, (int)(containerHeight-remainingHeight), (int)(containerWidth-iterations*pixelSize), pixelSize);
		
		
		
	}
	
	public Color getMeanValueColor(List<Point> points, int id) {
		if(points.size() == 0)
			return DcmManager.getDoseColorScale(0.0, DcmData.getMaxValueByContourId(id));
		double sum = 0.0;
		for(Point p : points)
			sum += p.getValue();
		return DcmManager.getDoseColorScale(sum/points.size(), DcmData.getMaxValueByContourId(id));
	}
	
	public int getStartingFrame(int id) {
		for(int f = 0; f < DcmData.getDcmFrames().size(); ++f) {
			DcmFrame frame = DcmData.getDcmFrames().get(f);
			for(int x = 0; x < frame.getContours().size(); ++x)
				if(frame.getContours().get(x).getId() == id)
					return f;
		}
		return 0;
	}
	
	public int getEndingFrame(int id) {
		for(int f = DcmData.getDcmFrames().size()-1; f >= 0; --f) {
			DcmFrame frame = DcmData.getDcmFrames().get(f);
			for(int x = 0; x < frame.getContours().size(); ++x)
				if(frame.getContours().get(x).getId() == id)
					return f;
		}
		return 0;
	}

	public void setContainerSize() {
		containerWidth = gui.getScene().getWidth()-gui.getCenterPanel().getScalePanel().getWidth();
		containerHeight = gui.getScene().getHeight()-gui.getBottomPanel().getHeight()-gui.getMenuBarClass().getHeight();
		setWidth(containerWidth);
		setHeight(containerHeight);
	}
}
