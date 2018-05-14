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
			
			shortData = data.getShortValues();
		} catch (DicomException | IOException e) {
			e.printStackTrace();
			return false;
		}
		
		long[] integerData = new long[shortData.length/2];
		for(int i = 0; i < shortData.length/2; i++) {
			
			integerData[i] = (getUnsigned(shortData[2*i+1]) << 16) | getUnsigned(shortData[2*i]);
			
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
		
		SequenceAttribute itemsOfData = null;
		try {
			AttributeList list = new AttributeList();
			list.read(dcmFile);
			Attribute attr = list.get(new AttributeTag("0x3006,0x0039"));
			SequenceAttribute r1 = (SequenceAttribute) attr;
			itemsOfData = (SequenceAttribute) r1.getItem(0).getAttributeList().get(new AttributeTag("0x3006,0x0040"));
		} catch (DicomException | IOException e) {
			e.printStackTrace();
			return false;
		}
		
		List<Contour> multiContours = new ArrayList<Contour>();
		for(int i = 0; i < itemsOfData.getNumberOfItems(); i++) {
			multiContours.add(new Contour(itemsOfData.getItem(i)));
		}
		
		Collections.sort(multiContours, (a, b) -> (a.getZ() > b.getZ()) ? 1 : 0);
		
		List<List<Contour>> listPackage = new ArrayList<List<Contour>>();
		for(int i = 0; i < multiContours.size()-1; i++) {
			List<Contour> subList = new ArrayList<Contour>();
			subList.add(multiContours.get(i));
			while(multiContours.get(i).getZ() == multiContours.get(i+1).getZ()) {
				subList.add(multiContours.get(i+1));
				++i;
			}
			listPackage.add(subList);
			if(i == multiContours.size()-2) {
				List<Contour> sub = new ArrayList<Contour>();
				sub.add(multiContours.get(multiContours.size()-1));
				listPackage.add(sub);
			}
		}
		multiContours = null;
		
		numberOfFrames = listPackage.size();
		if(dcmFrames.size() == 0) {
			for(int i = 0; i < numberOfFrames; ++i) {
				dcmFrames.add(new DcmFrame());
			}
		}
		
		for(int i = 0; i < numberOfFrames; ++i) {
			dcmFrames.get(i).setContours(listPackage.get(i));
		}
		
		
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
