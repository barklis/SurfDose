package DataModule;

import java.util.ArrayList;
import java.util.List;

// Stores data of single frame
public class DcmFrame {
	private List<Contour> contours;
	private List<Point[][]> doseData;
	private Point[][] sumDoseData;
	private int frameNumber;
	private double maxDoseValue;
	private double maxSumDoseValue;
	private double z;
	
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
		sumDoseData = null;
		sumDoseData = new Point[DcmData.getNumberOfCols()][DcmData.getNumberOfRows()];
		for(int r = 0; r < DcmData.getNumberOfRows(); r++) {
			for(int c = 0; c < DcmData.getNumberOfCols(); c++) {
				sumDoseData[c][r] = doseData.get(0)[c][r].getCopy();
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

	public void setDoseData(double[] doseValues, double x0, double y0, double z0, double colsPixelSpacing, double rowsPixelSpacing, double height) {
		if(sumDoseData == null)
			sumDoseData = new Point[DcmData.getNumberOfCols()][DcmData.getNumberOfRows()];
		this.doseData.add(new Point[DcmData.getNumberOfCols()][DcmData.getNumberOfRows()]);
		
		if(this.z < z0-rowsPixelSpacing/2 || this.z > z0+(height-0.5)*rowsPixelSpacing) {
			for(int r = 0; r < DcmData.getNumberOfRows(); r++) {
				for(int c = 0; c < DcmData.getNumberOfCols(); c++) {
					this.doseData.get(doseData.size()-1)[c][r] = new Point(
						x0 + c*colsPixelSpacing,
						y0 + r*rowsPixelSpacing,
						0.0
					);
					if(sumDoseData[c][r] == null) {
						sumDoseData[c][r] = new Point(
							x0 + c*colsPixelSpacing,
							y0 + r*rowsPixelSpacing,
							0.0
						);
					}
				}
			}
			return;
		}
		
		int layerNumber = (int) ((this.z-z0+rowsPixelSpacing/2)/rowsPixelSpacing);
		//System.out.println(layerNumber*rowsPixelSpacing + " - " + (layerNumber+1)*rowsPixelSpacing + " = " + (this.z-z0+rowsPixelSpacing/2));
		
		for(int r = 0; r < DcmData.getNumberOfRows(); r++) {
			for(int c = 0; c < DcmData.getNumberOfCols(); c++) {
				double valuePoint = doseValues[DcmData.getNumberOfRows()*DcmData.getNumberOfCols()*layerNumber+DcmData.getNumberOfCols()*r+c];
				
				if(valuePoint > maxDoseValue)
					maxDoseValue = valuePoint;
				
				this.doseData.get(doseData.size()-1)[c][r] = new Point(
					x0 + c*colsPixelSpacing,
					y0 + r*rowsPixelSpacing,
					valuePoint
				);
				if(sumDoseData[c][r] == null) {
					sumDoseData[c][r] = new Point(
						x0 + c*colsPixelSpacing,
						y0 + r*rowsPixelSpacing,
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
		if(	cX < DcmData.getX0()-DcmData.getColsPixelSpacing()/2 || cX > DcmData.getX0()+DcmData.getColsPixelSpacing()*(DcmData.getNumberOfCols()-0.5) || 
			cY < DcmData.getY0()-DcmData.getRowsPixelSpacing()/2 || cY > DcmData.getY0()+DcmData.getRowsPixelSpacing()*(DcmData.getNumberOfRows()-0.5) )
			return 0.0;
		
		int dX = (int) ((cX-DcmData.getX0())/DcmData.getColsPixelSpacing());
		int dY = (int) ((cY-DcmData.getY0())/DcmData.getRowsPixelSpacing());
		
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

	public double getZ() {
		return z;
	}

	public void setZ(double z) {
		this.z = z;
	}
	
}
