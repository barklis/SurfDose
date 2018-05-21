package pl.edu.pw.fizyka.pojava.BozekKlis.application.DicomDataModule;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.paint.Color;

// Stores data of single frame
public class DcmFrame {
	List<Contour> contours;
	Point[][] doseData;
	int frameNumber;
	double maxDoseValue;
	
	static int frameCounter = 0;
	
	public DcmFrame() {
		contours = new ArrayList<Contour>();
		frameNumber = frameCounter;
		frameCounter++;
		maxDoseValue = 0.0;
	}
	
	public List<Contour> getContours() {
		return contours;
	}
	public void setContours(List<Contour> contours) {
		this.contours = contours;
	}
	public int getFrameNumber() {
		return frameNumber;
	}
	public Point[][] getDoseData() {
		return doseData;
	}
	public double getMaxDoseValue() {
		return maxDoseValue;
	}

	public void setDoseData(double[] doseValues, double x0, double y0, double colsPixelSpacing, double rowsPixelSpacing) {
		this.doseData = new Point[DcmData.getNumberOfCols()][DcmData.getNumberOfRows()];
		for(int r = 0; r < DcmData.getNumberOfRows(); r++) {
			for(int c = 0; c < DcmData.getNumberOfCols(); c++) {
				double valuePoint = doseValues[DcmData.getNumberOfRows()*DcmData.getNumberOfCols()*frameNumber+DcmData.getNumberOfCols()*r+c];
				if(valuePoint > maxDoseValue)
					maxDoseValue = valuePoint;
				this.doseData[c][r] = new Point(
					x0 + c*colsPixelSpacing+colsPixelSpacing/2,
					y0 + r*rowsPixelSpacing+rowsPixelSpacing/2,
					valuePoint
				);
			}
		}
	}
	
	public Color getDoseColorScale(double doseValue){
		if(maxDoseValue == 0 || doseValue == 0)
			return Color.BLACK;
		//double colorValue = doseValue/maxDoseValue;
		////double colorValue = doseValue / DcmData.getMaxDoseValue();
		//return new Color(colorValue, colorValue, colorValue, 1.0);
		
		double hue = Color.BLUE.getHue() + (Color.RED.getHue() - Color.BLUE.getHue()) * doseValue / maxDoseValue ;
		return Color.hsb(hue, 1.0, 1.0);
	}
	
	public double getInterpolatedDose(double cX, double cY) {
		int dX = (int) ((cX-DcmData.getX0()-DcmData.getColsPixelSpacing()/2)/DcmData.getColsPixelSpacing());
		int dY = (int) ((cY-DcmData.getY0()-DcmData.getRowsPixelSpacing()/2)/DcmData.getRowsPixelSpacing());
		
		if(dY > DcmData.getNumberOfRows()-1) {
			dY = DcmData.getNumberOfRows()-1;
		}
		if(dX > DcmData.getNumberOfCols()-1) {
			dX = DcmData.getNumberOfCols()-1;
		}
		
		if(cY < doseData[0][0].getY() || cY >= doseData[0][DcmData.getNumberOfRows()-1].getY()) {
			
			if(cX < doseData[0][0].getX() || cX >= doseData[DcmData.getNumberOfCols()-1][0].getX()) {
				return doseData[dX][dY].getValue();
			}
			else {
				return interpolateFrom2XPoints(doseData[dX][dY], doseData[dX+1][dY], cX);
			}
		}
		else if(cX < doseData[0][0].getX() || cX >= doseData[DcmData.getNumberOfCols()-1][0].getX()) {
			return interpolateFrom2YPoints(doseData[dX][dY], doseData[dX][dY+1], cY);
		}
		else{
			return interpolateFrom4Points(doseData[dX][dY], doseData[dX+1][dY], doseData[dX][dY+1], doseData[dX+1][dY+1], cX, cY);
		}
	}
	
	private double interpolateFrom2XPoints(Point p1, Point p2, double x) {
		return (p2.getValue()-p1.getValue())/(p2.getX()-p1.getX())*(x-p1.getX())+p1.getValue();
	}
	
	private double interpolateFrom2YPoints(Point p1, Point p2, double y) {
		return (p2.getValue()-p1.getValue())/(p2.getY()-p1.getY())*(y-p1.getY())+p1.getValue();
	}
	
	private double interpolateFrom4Points(Point p1, Point p2, Point p3, Point p4, double x, double y) {
		double fxy1 = (p2.getX() - x)/(p2.getX() - p1.getX())*p1.getValue() + (x - p1.getX())/(p2.getX() - p1.getX())*p2.getValue();
		double fxy2 = (p2.getX() - x)/(p2.getX() - p1.getX())*p3.getValue() + (x - p1.getX())/(p2.getX() - p1.getX())*p4.getValue();
		return (p3.getY() - y)/(p3.getY() - p1.getY())*fxy1 + (y - p1.getY())/(p3.getY() - p1.getY())*fxy2;
	}
	
}
