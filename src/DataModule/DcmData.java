package DataModule;

import java.io.File;
import java.io.IOException;
import java.util.*;

import com.pixelmed.dicom.Attribute;
import com.pixelmed.dicom.AttributeTag;
import com.pixelmed.dicom.DicomException;
import com.pixelmed.dicom.SequenceAttribute;

import Exceptions.ErrorHandler;
import Exceptions.MissingTagException;
import Exceptions.NotEnoughContoursException;
import application.ContourCenterContainer;
import application.Preferences;
import javafx.application.Platform;

// Contains loaded and calculated data
public class DcmData {
	private static List<DcmFrame> dcmFrames = new ArrayList<DcmFrame>();
	private static int numberOfCols = 0;
	private static int numberOfRows = 0;
	private static int numberOfFrames = 0;
	private static int doseFilesLoaded = 0;
	private static double maxSumDoseValue = 0;
	private static double x0 = 0.0;
	private static double y0 = 0.0;
	private static double z0 = 0.0;
	private static double colsPixelSpacing = 0.0;
	private static double rowsPixelSpacing = 0.0;
	private static double contourSpacing = 0.0;
	private static boolean doseLoaded = false;
	private static boolean contourLoaded = false;
	private static boolean planLoaded = false;
	private static boolean doseCalculated = false;
	private static String planFileName = "-";
	private static String doseFileName = "-";
	private static String structurFileName = "-";
	private static int numberOfFractions = 0;
	private static int numberOfBeams = 0;
	private static double[] isocenterPosition = null;
	private static double[] gantryAngles = null;
	private static double uVector[] = {-1, 0};
	private static double angularWidth = Math.PI*2;
	private static int currentContourId = 0;
	private static int maxContourId = 0;
	private static int verticalVoxels = 0;
	private static double[] frameOffset = null;
	private static double[] integerData = null;
	private static boolean dosePreLoaded = false;
	
	public static final int EMPTY = 1010101010;
	
	public static void calculateDose(int frameIndex) {
		if(!doseLoaded || !contourLoaded)
			return;
		
		getFrame(frameIndex).sumDose();
		
		for(Contour contour : getFrame(frameIndex).getContours()) {
			for(Point point : contour.getData()) {	
				point.setValue( getFrame(frameIndex).getInterpolatedDose(point.getX(), point.getY()) );
				if(contour.getMaxValue() < point.getValue())
					contour.setMaxValue(point.getValue());
			}
		}
		
		doseCalculated = true;
	}
	
	public static void recalculateAngle() {
		for(DcmFrame frame : dcmFrames) {
			for(Contour contour : frame.getContours()) {
				for(Point point : contour.getData()) {	
					point.calculateAngle(uVector, contour.getCenterX(), contour.getCenterY());
				}
			}
		}
	}
	
	public static void resetData() {
		DcmFrame.frameCounter = 0;
		dcmFrames.clear();
		numberOfCols = 0;
		numberOfRows = 0;
		numberOfFrames = 0;
		doseFilesLoaded = 0;
		maxSumDoseValue = 0;
		x0 = 0.0;
		y0 = 0.0;
		z0 = 0.0;
		contourSpacing = 0.0;
		colsPixelSpacing = 0.0;
		rowsPixelSpacing = 0.0;
		doseLoaded = false;
		contourLoaded = false;
		planLoaded = false;
		doseCalculated = false;
		planFileName = "-";
		structurFileName = "-";
		numberOfFractions = 0;
		numberOfBeams = 0;
		isocenterPosition = null;
		gantryAngles = null;
		currentContourId = 0;
		maxContourId = 0;
		verticalVoxels = 0;
		integerData = null;
		dosePreLoaded = false;
		frameOffset = null;
	}
	
	public static double getMaxValueByContourId(int id) {
		double maxValue = 0.0;
		for(DcmFrame frame : dcmFrames) {
			for(Contour contour : frame.getContours()) {
				if(id == contour.getId()) {
					for(Point p : contour.getData()) {
						if(maxValue < p.getValue())
							maxValue = p.getValue();
					}
					break;
				}
			}
		}
		return maxValue;
	}
	
	public synchronized static boolean setDoseData(File dcmFile) {
		
		short[] shortData = null;
		double scalingFactor = 0;

		try {
			AttributeListExcept list = new AttributeListExcept();
			list.read(dcmFile);
			Attribute data = list.getPixelData();
			numberOfRows = list.getSafe(new AttributeTag("0x0028,0x0010")).getSingleIntegerValueOrDefault(0);
			numberOfCols = list.getSafe(new AttributeTag("0x0028,0x0011")).getSingleIntegerValueOrDefault(0);
			verticalVoxels = list.getSafe(new AttributeTag("0x0028,0x0008")).getSingleIntegerValueOrDefault(0);
			frameOffset = list.getSafe(new AttributeTag("0x3004,0x000c")).getDoubleValues();
			
			if(frameOffset[0] != 0.0) {
				double diff = frameOffset[0];
				for(double i : frameOffset) {
					i -= diff;
				}
			}
			
			double[] offset = list.getSafe(new AttributeTag("0x0020,0x0032")).getDoubleValues();
			x0 = offset[0];
			y0 = offset[1];
			z0 = offset[2];
			
			double[] pixelSpacing = list.getSafe(new AttributeTag("0x0028,0x0030")).getDoubleValues();
			rowsPixelSpacing = pixelSpacing[0];
			colsPixelSpacing = pixelSpacing[1];
			
			scalingFactor = list.getSafe(new AttributeTag("0x3004,0x000e")).getSingleDoubleValueOrDefault(0.0);
			
			shortData = data.getShortValues();
		} catch (DicomException | IOException e) {
			e.printStackTrace();
			return false;
		} catch (MissingTagException e) {
			Platform.runLater(() -> new ErrorHandler(Preferences.getLabel("missingTag") + " RTDOSE: " + e.getTag()).showDialog());
		}
		
		if(integerData == null) {
			integerData = new double[shortData.length/2];
		
			for(int i = 0; i < shortData.length/2; i++) {
				double tmp = (getUnsigned(shortData[2*i+1]) << 16) | getUnsigned(shortData[2*i]);
				integerData[i] = tmp*scalingFactor*100;
				if(maxSumDoseValue < integerData[i])
					maxSumDoseValue = integerData[i];
			}
		}
		else {
			for(int i = 0; i < shortData.length/2; i++) {
				double tmp = (getUnsigned(shortData[2*i+1]) << 16) | getUnsigned(shortData[2*i]);
				integerData[i] += tmp*scalingFactor*100;
				if(maxSumDoseValue < integerData[i])
					maxSumDoseValue = integerData[i];
			}
		}
		
		shortData = null;
		setDosePreLoaded(true);
		
		++doseFilesLoaded;
		if(contourLoaded)
			return finishLoadingDose();
		return false;
	}
	
	public static boolean finishLoadingDose() {
		if(integerData == null)
			return false;

		for(int f = 0; f < numberOfFrames; f++)
			dcmFrames.get(f).setDoseData(integerData, x0, y0, z0, colsPixelSpacing, rowsPixelSpacing, verticalVoxels, frameOffset);
		
		setDoseLoaded(true);
		integerData = null;
		return true;
	}
	
	private static long getUnsigned(short signed) {
		if(signed >= 0)
			return (long) signed;
		signed ^= (1<<15);
		long u = signed;
		return (u ^ (1<<15));
	}
	
	public static void setContourCenterData(ContourCenterContainer centerData) {
		int startFrameIndex =getFrameByZ(centerData.getStartZ()).getFrameNumber();
		int dataLength = centerData.getDataLength();
		int currentId = getCurrentContourId();
		int localStartFrameIndex = DcmManager.getStartingFrame(currentId);
		int localEndFrameIndex = DcmManager.getEndingFrame(currentId);
		
		int diff = localStartFrameIndex - startFrameIndex;
		
		for(int i = localStartFrameIndex; i <= localEndFrameIndex; ++i) {
			Contour c = DcmData.getFrame(i).getContourById(currentId);
			if(i <= startFrameIndex) {
				c.setCenterX(centerData.getInitialX());
				c.setCenterY(centerData.getInitialY());
			}
			else if(i >= startFrameIndex + dataLength-1) {
				c.setCenterX(centerData.getEndingX());
				c.setCenterY(centerData.getEndingY());
			}
			else {
				c.setCenterX(centerData.getX(i + diff - localStartFrameIndex));
				c.setCenterY(centerData.getY(i + diff - localStartFrameIndex));
			}
			for(Point point : c.getData())
				point.calculateAngle(uVector, c.getCenterX(), c.getCenterY());
		}
		
	}
	
	public static DcmFrame getFrame(int index) {
		return dcmFrames.get(index);
	}
	
	public static DcmFrame getFrameByZ(double zCoord) {
		return Collections.min(dcmFrames, new Comparator<DcmFrame>() {
			@Override
			public int compare(DcmFrame o1, DcmFrame o2) {
				double d1 = Math.abs(o1.getZ()-zCoord);
				double d2 = Math.abs(o2.getZ()-zCoord);
				
				if(d1 < d2)
					return -1;
				else if(d2 > d1)
					return 1;
				return 0;
			}
		});
	}
	
	public synchronized static boolean setContourData(File dcmFile) {
		
		SequenceAttribute sequenceOfMainContours = null;
		List<List<Contour>> listPackage = new ArrayList<List<Contour>>();
		
		if(dcmFrames.size() != 0)
			dcmFrames.clear();
		
		try {
			AttributeListExcept list = new AttributeListExcept();
			list.read(dcmFile);
			sequenceOfMainContours = (SequenceAttribute) list.getSafe(new AttributeTag("0x3006,0x0039"));
			
			maxContourId = sequenceOfMainContours.getNumberOfItems()-1;
					
			for(int i = 0; i < sequenceOfMainContours.getNumberOfItems(); ++i) {
				SequenceAttribute itemsOfData = (SequenceAttribute) AttributeListExcept.getInnerInstance(sequenceOfMainContours.getItem(i).getAttributeList()).getSafe(new AttributeTag("0x3006,0x0040"));
				
				List<Contour> multiContours = new ArrayList<Contour>();
				for(int x = 0; x < itemsOfData.getNumberOfItems(); ++x) {
					Contour tempContour = new Contour(itemsOfData.getItem(x), i);
					if(!(x > 0 && tempContour.getZ() == multiContours.get(multiContours.size()-1).getZ()))
						multiContours.add(tempContour);
				}
				
				listPackage.add(multiContours);
			}
			
		
		} catch (DicomException | IOException e) {
			e.printStackTrace();
			return false;
		} catch (MissingTagException e) {
			Platform.runLater(() -> new ErrorHandler(Preferences.getLabel("missingTag") + " RTSTRUCTUR: " + e.getTag()).showDialog());
		}
		
		try {
			if(listPackage.size() == 0)
				throw new NotEnoughContoursException();
			
			List<Contour> nonZeroList = null;
			for(List<Contour> list : listPackage) {
				if(list.size() > 1) {
					nonZeroList = list;
					break;
				}
			}
			
			if(nonZeroList == null)
				throw new NotEnoughContoursException();
			
			contourSpacing = Math.abs(nonZeroList.get(1).getZ() - nonZeroList.get(0).getZ());
			
		} catch (NotEnoughContoursException e) {
			new ErrorHandler(e.getMessage()).showDialog();
		}
		
		int[] bounds = new int[listPackage.size()];
		for(int x = 0; x < bounds.length; ++x)
			bounds[x] = 0;
		
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
					column.add(new Contour(EMPTY));
				}
			}
			if(!inserted)
				break;
			
			double minCoord = Collections.min(column, (a, b) ->(a.getZ() > b.getZ()) ? 1 : 0).getZ();
			List<Contour> subList = new ArrayList<Contour>();
			
			if(minCoord != DcmData.EMPTY) {
				for(int x = 0; x < column.size(); ++x) {
					if(column.get(x).getZ() == minCoord) {
						subList.add(column.get(x));
						bounds[x]++;
					}
				}
			}
			
			dcmFrames.add(new DcmFrame());
			dcmFrames.get(frameCounter).setContours(subList);
			dcmFrames.get(frameCounter).setZ(subList.get(0).getZ());
			++frameCounter;
		}
		
		numberOfFrames = dcmFrames.size();
		
		for(DcmFrame frame : DcmData.getDcmFrames())
			for(Contour contour : frame.getContours()) {
				contour.calculateCenter();
				for(Point point : contour.getData())
					point.calculateAngle(uVector, contour.getCenterX(), contour.getCenterY());
			}
		
		setContourLoaded(true);
		structurFileName = dcmFile.getName();
		if(!doseLoaded)
			finishLoadingDose();
		return true;
	}
	
	public synchronized static boolean setPlanData(File dcmFile) {
		try {
			AttributeListExcept list = new AttributeListExcept();
			list.read(dcmFile);
			SequenceAttribute fractionGroupSequence = (SequenceAttribute) list.getSafe(new AttributeTag("0x300a,0x0070"));
			numberOfFractions = AttributeListExcept.getInnerInstance(fractionGroupSequence.getItem(0).getAttributeList()).getSafe(new AttributeTag("0x300a,0x0078")).getSingleIntegerValueOrDefault(0);
			numberOfBeams = AttributeListExcept.getInnerInstance(fractionGroupSequence.getItem(0).getAttributeList()).getSafe(new AttributeTag("0x300a,0x0080")).getSingleIntegerValueOrDefault(0);
			
			gantryAngles = new double[numberOfBeams];
			
			SequenceAttribute beamSequence = (SequenceAttribute) list.getSafe(new AttributeTag("0x300a,0x00b0"));
			for(int i = 0; i < beamSequence.getNumberOfItems(); ++i) {
				
				SequenceAttribute controlPointSequence = (SequenceAttribute) AttributeListExcept.getInnerInstance(beamSequence.getItem(i).getAttributeList()).getSafe(new AttributeTag("0x300a,0x0111"));
				gantryAngles[i] = AttributeListExcept.getInnerInstance(controlPointSequence.getItem(0).getAttributeList()).getSafe(new AttributeTag("0x300a,0x011e")).getSingleDoubleValueOrDefault(0.0);
				gantryAngles[i] *= Math.PI/180;
				
				if(i == 0) {
					isocenterPosition = AttributeListExcept.getInnerInstance(controlPointSequence.getItem(0).getAttributeList()).getSafe(new AttributeTag("0x300a,0x012c")).getDoubleValues();
				}
			}
			
		} catch (IOException | DicomException e) {
			e.printStackTrace();
			return false;
		} catch (MissingTagException e) {
			Platform.runLater(() -> new ErrorHandler(Preferences.getLabel("missingTag") + " RTPLAN: " + e.getTag()).showDialog());
		}
		
		setPlanLoaded(true);
		planFileName = dcmFile.getName();
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

	public static int getNumberOfFractions() {
		return numberOfFractions;
	}

	public static int getNumberOfBeams() {
		return numberOfBeams;
	}

	public static int getMaxContourId() {
		return maxContourId;
	}

	public static double[] getuVector() {
		return uVector;
	}

	public static int getCurrentContourId() {
		return currentContourId;
	}

	public static void setCurrentContourId(int currentContourId) {
		DcmData.currentContourId = currentContourId;
	}

	public static double[] getIsocenterPosition() {
		return isocenterPosition;
	}

	public static boolean isPlanLoaded() {
		return planLoaded;
	}

	public static void setPlanLoaded(boolean planLoaded) {
		DcmData.planLoaded = planLoaded;
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

	public static int getDoseFilesLoaded() {
		return doseFilesLoaded;
	}

	public static double getMaxSumDoseValue() {
		return maxSumDoseValue;
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

	public static void setMaxSumDoseValue(double maxSumDoseValue) {
		DcmData.maxSumDoseValue = maxSumDoseValue;
	}

	public static String getPlanFileName() {
		return planFileName;
	}

	public static String getStructurFileName() {
		return structurFileName;
	}

	public static void setuVector(double vectorX, double vectorY) {
		DcmData.uVector[0] = vectorX;
		DcmData.uVector[1] = vectorY;
	}

	public static double getAngularWidth() {
		return angularWidth;
	}

	public static void setAngularWidth(double angularWidth) {
		DcmData.angularWidth = angularWidth;
	}

	public static double[] getGantryAngles() {
		return gantryAngles;
	}

	public static double getContourSpacing() {
		return contourSpacing;
	}

	public static double getZ0() {
		return z0;
	}

	public static int getVerticalVoxels() {
		return verticalVoxels;
	}

	public static boolean isDosePreLoaded() {
		return dosePreLoaded;
	}

	public static void setDosePreLoaded(boolean dosePreLoaded) {
		DcmData.dosePreLoaded = dosePreLoaded;
	}

	public static double[] getFrameOffset() {
		return frameOffset;
	}
	
}
