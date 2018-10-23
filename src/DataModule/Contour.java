package DataModule;

import java.util.ArrayList;
import java.util.List;

import com.pixelmed.dicom.AttributeTag;
import com.pixelmed.dicom.DicomException;
import com.pixelmed.dicom.SequenceItem;

// Stores information about single contour
public class Contour {
	private int numberOfPoints;
	private List<Point> data;
	private double z;
	private int id;
	private double centerX, centerY;
	private double maxValue;
	
	public Contour(SequenceItem item, int id) {
		this.id = id;
		this.maxValue = 0.0;
		double[] rawData = null;
		
		try {
			this.numberOfPoints = item.getAttributeList().get(new AttributeTag("0x3006,0x0046")).getSingleIntegerValueOrDefault(0);
			rawData = item.getAttributeList().get(new AttributeTag("0x3006,0x0050")).getDoubleValues();
		} catch (DicomException e) {
			e.printStackTrace();
		}
		
		this.data = new ArrayList<Point>();
		if(this.numberOfPoints > 0)
			this.z = rawData[2];
		else
			this.z = DcmData.EMPTY;
		
		for(int i = 0; i < this.numberOfPoints; ++i)
			this.data.add(new Point(rawData[i*3], rawData[i*3+1]));
	}
	
	public Contour(int z) {
		this.numberOfPoints = 0;
		this.data = new ArrayList<Point>();
		this.z = z;
		this.id = DcmData.EMPTY;
	}

	public int getId() {
		return id;
	}
	public double getZ() {
		return z;
	}
	public int getNumberOfPoints() {
		return numberOfPoints;
	}
	public List<Point> getData() {
		return data;
	}

	public void calculateCenter() {
		centerX = 0.0;
		centerY = 0.0;
		for(Point p : data) {
			centerX += p.getX();
			centerY += p.getY();
		}
		centerX /= numberOfPoints;
		centerY /= numberOfPoints;
	}

	public double getCenterX() {
		return centerX;
	}

	public double getCenterY() {
		return centerY;
	}

	public double getMaxValue() {
		return maxValue;
	}

	public void setMaxValue(double maxValue) {
		this.maxValue = maxValue;
	}
	
}
