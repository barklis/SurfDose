package application;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import Exceptions.InvalidCenterCoordsFile;

public class ContourCenterContainer {
	private double startZ;
	private double endZ;
	private List<double[]> coords;
	
	private boolean decoded;
	
	public ContourCenterContainer() {
		startZ = 0;
		endZ = 0;
		coords = new ArrayList<double[]>();
		decoded = false;
	}
	
	public void readFile(File file) throws IOException, InvalidCenterCoordsFile {
		Scanner reader = new Scanner(file);
		
		if(reader.hasNext())
			startZ = Double.parseDouble(reader.next());
		else
			throw new InvalidCenterCoordsFile();
		
		if(reader.hasNext())
			endZ = Double.parseDouble(reader.next());
		else
			throw new InvalidCenterCoordsFile();
		
		coords.clear();
		while(reader.hasNext()) {
			double x = Double.parseDouble(reader.next());
			if(reader.hasNext())
				coords.add(new double[]{x, Double.parseDouble(reader.next())});
			else
				throw new InvalidCenterCoordsFile();
		}
		
		if(coords.size() < 2) {
			throw new InvalidCenterCoordsFile();
		}
		
		decoded = false;
		reader.close();
	}
	
	public void decode(double[] isocenterPosition) {
		if(decoded)
			return;
		if(isocenterPosition.length < 3)
			throw new IllegalArgumentException("Array has only " + isocenterPosition.length + " elements while 3 are required.");
		
		startZ += isocenterPosition[2];
		endZ += isocenterPosition[2];
		for(double[] coord : coords) {
			coord[0] += isocenterPosition[0];
			coord[1] += isocenterPosition[1];
		}
		
		decoded = true;
	}
	
	public int getDataLength() {
		return coords.size();
	}
	
	public double getX(int index) {
		return coords.get(index)[0];
	}
	
	public double getY(int index) {
		return coords.get(index)[1];
	}
	
	public double getInitialX() {
		return coords.get(0)[0];
	}
	
	public double getInitialY() {
		return coords.get(0)[1];
	}
	
	public double getEndingX() {
		return coords.get(coords.size()-1)[0];
	}
	
	public double getEndingY() {
		return coords.get(coords.size()-1)[1];
	}

	public double getStartZ() {
		return startZ;
	}

	public double getEndZ() {
		return endZ;
	}

	public List<double[]> getCoords() {
		return coords;
	}
	
	
	
}
