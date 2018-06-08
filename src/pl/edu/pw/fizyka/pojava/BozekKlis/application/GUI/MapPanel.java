package pl.edu.pw.fizyka.pojava.BozekKlis.application.GUI;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import pl.edu.pw.fizyka.pojava.BozekKlis.application.DicomDataModule.Contour;
import pl.edu.pw.fizyka.pojava.BozekKlis.application.DicomDataModule.DcmData;
import pl.edu.pw.fizyka.pojava.BozekKlis.application.DicomDataModule.DcmFrame;
import pl.edu.pw.fizyka.pojava.BozekKlis.application.DicomDataModule.DcmManager;
import pl.edu.pw.fizyka.pojava.BozekKlis.application.DicomDataModule.Point;

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
		int myId = 2;
		
		if(!DcmData.isDoseCalculated())
			return;
		GraphicsContext gc = getGraphicsContext2D();
		gc.clearRect(0, 0, containerWidth, containerHeight);
		gc.setFill(Color.BLACK);
		
		int startingFrame = getStartingFrame(myId);
		int endingFrame = getEndingFrame(myId);
		
		int pixelSize = (int)(containerHeight/(endingFrame-startingFrame));
		int iterations = (int) (containerWidth/pixelSize);
		
		double radius = 1.5*pixelSize;
		int interpolatedFrames = (int) Math.ceil(radius/pixelSize);
		
		for(int f = startingFrame; f < endingFrame; ++f) {
			Contour contour = DcmData.getDcmFrames().get(f).getContourById(myId);
			
			if(contour.getData().size() == 0)
				continue;
			
			for(int i = 0; i < iterations; ++i) {
				Point pixelCoord = new Point((i+0.5)*pixelSize, (f-startingFrame+0.5)*pixelSize);
				List<Point> neighbours = new ArrayList<Point>();
				
				for(int x = -interpolatedFrames; x < interpolatedFrames; ++x) {
					if(f+interpolatedFrames < startingFrame || f+interpolatedFrames > endingFrame)
						continue;
					
					for(Point p : DcmData.getDcmFrames().get(f+x).getContourById(myId).getData()) {
						Point setPoint = new Point(p.getAngle()*containerWidth/Math.PI*0.5, (f-startingFrame)*pixelSize);
						if(Point.distance(pixelCoord, setPoint) < radius)
							neighbours.add(p);
					}
				}
				
				gc.setFill(getMeanValueColor(neighbours, myId));
				gc.fillRect(i*pixelSize, (f-startingFrame)*pixelSize, pixelSize, pixelSize);
			}
			
			Point pixelCoord = new Point((iterations*pixelSize+containerWidth)*0.5, (f-startingFrame+0.5)*pixelSize);
			List<Point> neighbours = new ArrayList<Point>();
			
			for(int x = -interpolatedFrames; x < interpolatedFrames; ++x) {
				if(f+interpolatedFrames < startingFrame || f+interpolatedFrames > endingFrame)
					continue;
				
				for(Point p : DcmData.getDcmFrames().get(f+x).getContourById(myId).getData()) {
					Point setPoint = new Point(p.getAngle()*containerWidth/Math.PI*0.5, (f-startingFrame)*pixelSize);
					if(Point.distance(pixelCoord, setPoint) < radius)
						neighbours.add(p);
				}
			}
			gc.setFill(getMeanValueColor(neighbours, myId));
			gc.fillRect(iterations*pixelSize, (f-startingFrame)*pixelSize, containerWidth-iterations*pixelSize, pixelSize);
			
			// draw dots
			//for(int i = startingIndex; i < contour.getData().size(); ++i)
			//	gc.fillOval(contour.getData().get(i).getAngle()*containerWidth/Math.PI*0.5, containerHeight*(f-startingFrame)/(endingFrame-startingFrame), 2, 2);
			//for(int i = 0; i < startingIndex; ++i)
			//	gc.fillOval(contour.getData().get(i).getAngle()*containerWidth/Math.PI*0.5, containerHeight*(f-startingFrame)/(endingFrame-startingFrame), 2, 2);
			
		} // frames
		
		
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
		containerWidth = gui.getScene().getWidth();
		containerHeight = gui.getScene().getHeight()-gui.getBottomPanel().getHeight()-gui.getMenuBarClass().getHeight();
		setWidth(containerWidth);
		setHeight(containerHeight);
	}
}
