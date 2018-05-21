package pl.edu.pw.fizyka.pojava.BozekKlis.application.DicomDataModule;

import java.util.ArrayList;
import java.util.List;

import com.pixelmed.dicom.AttributeTag;
import com.pixelmed.dicom.DicomException;
import com.pixelmed.dicom.SequenceItem;

// Stores information about single contour
public class Contour {
	int numberOfPoints;
	List<Point> data;
	double z;
	
	public Contour(SequenceItem item) {
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
			this.z = 0;
		
		for(int i = 0; i < this.numberOfPoints; ++i)
			this.data.add(new Point(rawData[i*3], rawData[i*3+1]));
	}
	
	public Contour(int z) {
		this.numberOfPoints = 0;
		this.data = new ArrayList<Point>();
		this.z = z;
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
	
}
