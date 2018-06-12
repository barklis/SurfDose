package pl.edu.pw.fizyka.pojava.BozekKlis.application.DicomDataModule;

import java.util.ArrayList;
import java.util.List;

// Stores data of single frame
public class DcmFrame {
	List<Contour> contours;
	List<Point[][]> doseData;
	Point[][] sumDoseData;
	int frameNumber;
	double maxDoseValue;
	double maxSumDoseValue;
	
	static int frameCounter = 0;
	
	public DcmFrame() {
		contours = new ArrayList<Contour>();
		doseData = new ArrayList<Point[][]>();
		sumDoseData = null;
		frameNumber = frameCounter;
		frameCounter++;
		maxDoseValue = 0.0;
		maxSumDoseValue = 0.0;
	}
	
	public Contour getContourById(int id) {
		for(int i = 0; i < contours.size(); ++i) {
			if(id == contours.get(i).getId()) {
				return contours.get(i);
			}
		}
		return new Contour(DcmData.EMPTY);
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
	public List<Point[][]> getDoseData() {
		return doseData;
	}
	public Point[][] getSumDoseData(){
		return sumDoseData;
	}
	public double getMaxDoseValue() {
		return maxDoseValue;
	}
	public double getMaxSumDoseValue() {
		return maxSumDoseValue;
	}
	
	public void sumDose() {
		sumDoseData = new Point[DcmData.getNumberOfCols()][DcmData.getNumberOfRows()];
		for(int r = 0; r < DcmData.getNumberOfRows(); r++) {
			for(int c = 0; c < DcmData.getNumberOfCols(); c++) {
				sumDoseData[c][r] = doseData.get(0)[c][r];
				for(int l = 1; l < doseData.size(); ++l) {
					sumDoseData[c][r].addValue(doseData.get(l)[c][r].getValue());
				}
				if(sumDoseData[c][r].getValue() > maxSumDoseValue) {
					maxSumDoseValue = sumDoseData[c][r].getValue();
					if(maxSumDoseValue > DcmData.getMaxSumDoseValue())
						DcmData.setMaxSumDoseValue(maxSumDoseValue);
				}
			}
		}
	}

	public void setDoseData(double[] doseValues, double x0, double y0, double colsPixelSpacing, double rowsPixelSpacing) {
		if(DcmData.getDoseFilesLoaded() == 0)
			sumDoseData = new Point[DcmData.getNumberOfCols()][DcmData.getNumberOfRows()];
		
		this.doseData.add(new Point[DcmData.getNumberOfCols()][DcmData.getNumberOfRows()]);
		for(int r = 0; r < DcmData.getNumberOfRows(); r++) {
			for(int c = 0; c < DcmData.getNumberOfCols(); c++) {
				double valuePoint = doseValues[DcmData.getNumberOfRows()*DcmData.getNumberOfCols()*frameNumber+DcmData.getNumberOfCols()*r+c];
				if(valuePoint > maxDoseValue)
					maxDoseValue = valuePoint;
				this.doseData.get(doseData.size()-1)[c][r] = new Point(
					x0 + c*colsPixelSpacing+colsPixelSpacing/2,
					y0 + r*rowsPixelSpacing+rowsPixelSpacing/2,
					valuePoint
				);
				if(DcmData.getDoseFilesLoaded() == 0) {
					sumDoseData[c][r] = new Point(
						x0 + c*colsPixelSpacing+colsPixelSpacing/2,
						y0 + r*rowsPixelSpacing+rowsPixelSpacing/2,
						valuePoint
					);
				}
				else {
					sumDoseData[c][r].addValue(valuePoint);
				}
				if(sumDoseData[c][r].getValue() > maxSumDoseValue) {
					maxSumDoseValue = sumDoseData[c][r].getValue();
					if(maxSumDoseValue > DcmData.getMaxSumDoseValue())
						DcmData.setMaxSumDoseValue(maxSumDoseValue);
				}
			}
		}
	}
	
	public double getInterpolatedDose(double cX, double cY) {
		if(	cX < DcmData.getX0() || cX > DcmData.getX0()+DcmData.getColsPixelSpacing()*DcmData.getNumberOfCols() || 
			cY < DcmData.getY0() || cY > DcmData.getY0()+DcmData.getRowsPixelSpacing()*DcmData.getNumberOfRows() )
			return 0.0;
		
		int dX = (int) ((cX-DcmData.getX0()-DcmData.getColsPixelSpacing()/2)/DcmData.getColsPixelSpacing());
		int dY = (int) ((cY-DcmData.getY0()-DcmData.getRowsPixelSpacing()/2)/DcmData.getRowsPixelSpacing());
		
		if(dY > DcmData.getNumberOfRows()-1) {
			dY = DcmData.getNumberOfRows()-1;
		}
		if(dX > DcmData.getNumberOfCols()-1) {
			dX = DcmData.getNumberOfCols()-1;
		}
		
		if(cY < sumDoseData[0][0].getY() || cY >= sumDoseData[0][DcmData.getNumberOfRows()-1].getY()) {
			
			if(cX < sumDoseData[0][0].getX() || cX >= sumDoseData[DcmData.getNumberOfCols()-1][0].getX()) {
				return sumDoseData[dX][dY].getValue();
			}
			else {
				return interpolateFrom2XPoints(sumDoseData[dX][dY], sumDoseData[dX+1][dY], cX);
			}
		}
		else if(cX < sumDoseData[0][0].getX() || cX >= sumDoseData[DcmData.getNumberOfCols()-1][0].getX()) {
			return interpolateFrom2YPoints(sumDoseData[dX][dY], sumDoseData[dX][dY+1], cY);
		}
		else{
			return interpolateFrom4Points(sumDoseData[dX][dY], sumDoseData[dX+1][dY], sumDoseData[dX][dY+1], sumDoseData[dX+1][dY+1], cX, cY);
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
