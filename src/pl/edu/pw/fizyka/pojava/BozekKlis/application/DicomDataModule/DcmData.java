package pl.edu.pw.fizyka.pojava.BozekKlis.application.DicomDataModule;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.pixelmed.dicom.Attribute;
import com.pixelmed.dicom.AttributeList;
import com.pixelmed.dicom.AttributeTag;
import com.pixelmed.dicom.DicomException;
import com.pixelmed.dicom.SequenceAttribute;

// Contains loaded and calculated data
public class DcmData {
	static List<DcmFrame> dcmFrames = new ArrayList<DcmFrame>();
	static int numberOfCols = 0;
	static int numberOfRows = 0;
	static int numberOfFrames = 0;
	static double maxDoseValue = 0;
	static double x0 = 0.0;
	static double y0 = 0.0;
	static double colsPixelSpacing = 0.0;
	static double rowsPixelSpacing = 0.0;
	static boolean doseLoaded = false;
	static boolean contourLoaded = false;
	static boolean doseCalculated = false;
	static String doseFileName = "-";
	static String structurFileName = "-";
	
	public static void calculateDose(int frameIndex) {
		if(!doseLoaded || !contourLoaded)
			return;
		
		for(Contour contour : dcmFrames.get(frameIndex).getContours()) {
			for(Point point : contour.getData()) {	
				point.setValue(
					dcmFrames.get(frameIndex).getInterpolatedDose( point.getX(), point.getY() )
				);
			}
		}
		
		doseCalculated = true;
	}
	
	public synchronized static boolean setDoseData(File dcmFile) {
		
		short[] shortData = null;
		double scalingFactor = 0;
		try {
			AttributeList list = new AttributeList();
			list.read(dcmFile);
			Attribute data = list.getPixelData();
			numberOfRows = list.get(new AttributeTag("0x0028,0x0010")).getSingleIntegerValueOrDefault(0);
			numberOfCols = list.get(new AttributeTag("0x0028,0x0011")).getSingleIntegerValueOrDefault(0);
			numberOfFrames = list.get(new AttributeTag("0x0028,0x0008")).getSingleIntegerValueOrDefault(0);
			
			double[] offset = list.get(new AttributeTag("0x0020,0x0032")).getDoubleValues();
			x0 = offset[0];
			y0 = offset[1];
			
			double[] pixelSpacing = list.get(new AttributeTag("0x0028,0x0030")).getDoubleValues();
			rowsPixelSpacing = pixelSpacing[0];
			colsPixelSpacing = pixelSpacing[1];
			
			scalingFactor = list.get(new AttributeTag("0x3004,0x000e")).getSingleDoubleValueOrDefault(0.0)*1000;
			
			shortData = data.getShortValues();
		} catch (DicomException | IOException e) {
			e.printStackTrace();
			return false;
		}
		
		double[] integerData = new double[shortData.length/2];
		for(int i = 0; i < shortData.length/2; i++) {
			
			integerData[i] = (getUnsigned(shortData[2*i+1]) << 16) | getUnsigned(shortData[2*i]);
			integerData[i] = (long) (integerData[i]*scalingFactor);
			if(integerData[i] > maxDoseValue)
				maxDoseValue = integerData[i];
		}
		
		shortData = null;
		
		if(dcmFrames.size() == 0) {
			for(int f = 0; f < numberOfFrames; ++f) {
				dcmFrames.add(new DcmFrame());
			}
		}
		
		for(int f = 0; f < numberOfFrames; f++) {
			dcmFrames.get(f).setDoseData(integerData, x0, y0, colsPixelSpacing, rowsPixelSpacing);
		}
		
		setDoseLoaded(true);
		doseFileName = dcmFile.getName();
		return true;
	}
	private static long getUnsigned(short signed) {
		if(signed >= 0)
			return (long) signed;
		signed ^= (1<<15);
		long u = signed;
		return (u ^ (1<<15));
	}
	
	public synchronized static boolean setContourData(File dcmFile) {
		
		SequenceAttribute sequenceOfMainContours = null;
		List<List<Contour>> listPackage = new ArrayList<List<Contour>>();
		
		try {
			AttributeList list = new AttributeList();
			list.read(dcmFile);
			sequenceOfMainContours = (SequenceAttribute) list.get(new AttributeTag("0x3006,0x0039"));
			
			for(int i = 0; i < sequenceOfMainContours.getNumberOfItems(); ++i) {
				SequenceAttribute itemsOfData = (SequenceAttribute) sequenceOfMainContours.getItem(i).getAttributeList().get(new AttributeTag("0x3006,0x0040"));
				
				List<Contour> multiContours = new ArrayList<Contour>();
				for(int x = 0; x < itemsOfData.getNumberOfItems(); ++x) {
					Contour tempContour = new Contour(itemsOfData.getItem(x));
					if(!(x > 0 && tempContour.getZ() == multiContours.get(multiContours.size()-1).getZ()))
						multiContours.add(tempContour);
				}
				
				listPackage.add(multiContours);
			}
			
		
		} catch (DicomException | IOException e) {
			e.printStackTrace();
			return false;
		}
		
		int[] bounds = new int[listPackage.size()];
		for(int x : bounds)
			x = 0;
		
		int frameCounter = 0;
		while(true) {
			boolean inserted = false;
			List<Contour> column = new ArrayList<Contour>();
			for(int i = 0; i < bounds.length; ++i) {
				if(bounds[i] < listPackage.get(i).size()) {
					column.add(listPackage.get(i).get(bounds[i]));
					inserted = true;
				}
				else {
					column.add(new Contour(10000000));
				}
			}
			if(!inserted)
				break;
			
			double minCoord = Collections.min(column, (a, b) ->(a.getZ() > b.getZ()) ? 1 : 0).getZ();
			List<Contour> subList = new ArrayList<Contour>();
			for(int x = 0; x < column.size(); ++x) {
				if(column.get(x).getZ() == minCoord) {
					subList.add(column.get(x));
					bounds[x]++;
				}
			}
			if(!isDoseLoaded())
				dcmFrames.add(new DcmFrame());
			dcmFrames.get(frameCounter).setContours(subList);
			++frameCounter;
		}
		
		numberOfFrames = dcmFrames.size();
		
		setContourLoaded(true);
		structurFileName = dcmFile.getName();
		return true;
	}

	public static List<DcmFrame> getDcmFrames() {
		return dcmFrames;
	}

	public static int getNumberOfCols() {
		return numberOfCols;
	}

	public static int getNumberOfRows() {
		return numberOfRows;
	}

	public static int getNumberOfFrames() {
		return numberOfFrames;
	}

	public static double getX0() {
		return x0;
	}

	public static double getY0() {
		return y0;
	}

	public static double getColsPixelSpacing() {
		return colsPixelSpacing;
	}

	public static double getRowsPixelSpacing() {
		return rowsPixelSpacing;
	}

	public static double getMaxDoseValue() {
		return maxDoseValue;
	}

	public static boolean isDoseLoaded() {
		return doseLoaded;
	}

	public static void setDoseLoaded(boolean doseLoaded) {
		DcmData.doseLoaded = doseLoaded;
	}

	public static boolean isContourLoaded() {
		return contourLoaded;
	}

	public static void setContourLoaded(boolean contourLoaded) {
		DcmData.contourLoaded = contourLoaded;
	}

	public static boolean isDoseCalculated() {
		return doseCalculated;
	}

	public static void setDoseCalculated(boolean doseCalculated) {
		DcmData.doseCalculated = doseCalculated;
	}

	public static String getDoseFileName() {
		return doseFileName;
	}

	public static String getStructurFileName() {
		return structurFileName;
	}
	
}
