package pl.edu.pw.fizyka.pojava.BozekKlis.application.DicomDataModule;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.pixelmed.dicom.Attribute;
import com.pixelmed.dicom.AttributeList;

import javafx.stage.FileChooser;
import javafx.stage.Stage;

// Contains static methods to work witch DICOM files, like loading and saving
public class DcmManager {
	
	public static File getDcmFile(Stage stage, String fileType) {
		FileChooser chooser = new FileChooser();
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(fileType + " files (*.dcm)", "*.dcm");
		chooser.getExtensionFilters().add(extFilter);
		chooser.setInitialDirectory(new File(System.getProperty("user.dir")));
		File selectedFile = chooser.showOpenDialog(null);
	    if(selectedFile != null)
	    	return selectedFile;
	    return null;
	}
	
	public static boolean saveDoseDataToFile(Stage stage, Point[][] doseMatrix) {
		FileChooser chooser = new FileChooser();
		chooser.setInitialDirectory(new File(System.getProperty("user.home") + "/Desktop"));
		File outputFile = chooser.showSaveDialog(stage);
	    if(outputFile != null) {
	    	PrintWriter writer = null;
			try {
				writer = new PrintWriter(outputFile.getAbsolutePath(), "UTF-8");
			} catch (FileNotFoundException | UnsupportedEncodingException e) {
				e.printStackTrace();
				return false;
			}
	    	for(int r = 0; r < doseMatrix[0].length; r++) {
	    		for(int c = 0; c < doseMatrix.length; c++)
	    			writer.print(doseMatrix[c][r].getValue() + ", ");
	    		writer.println();
	    	}
	    	writer.close();
	    	
	    	return true;
	    }
	    return false;
	}
	
	public static boolean saveRecievedDoseData(Stage stage, List<DcmFrame> list) {
		FileChooser chooser = new FileChooser();
		chooser.setInitialDirectory(new File(System.getProperty("user.home") + "/Desktop"));
		File outputFile = chooser.showSaveDialog(stage);
	    if(outputFile != null) {
	    	PrintWriter writer = null;
			try {
				writer = new PrintWriter(outputFile.getAbsolutePath(), "UTF-8");
			} catch (FileNotFoundException | UnsupportedEncodingException e) {
				e.printStackTrace();
				return false;
			}
			
			
			List<Double> sumFrame = new ArrayList<Double>();
			List<Double> sumContour = new ArrayList<Double>();
			writer.println("Max dose value: " + DcmData.getMaxDoseValue());
			double maxRecieved = 0;
			for(DcmFrame f : DcmData.getDcmFrames()) {
				sumFrame.add(0.0);
				for(Contour c : f.getContours()) {
					sumContour.add(0.0);
					for(Point p : c.getData()) {
						sumFrame.set(sumFrame.size()-1, sumFrame.get(sumFrame.size()-1) + p.getValue());
						sumContour.set(sumContour.size()-1, sumContour.get(sumContour.size()-1) + p.getValue());
						if(p.getValue() > maxRecieved)
							maxRecieved = p.getValue();
					}
				}
			}
			writer.println("Max recieved dose value: " + DcmData.getMaxDoseValue());
			writer.println();
			
	    	for(int f = 0; f < list.size(); f++) {
	    		writer.println("Frame: " + (f+1) + "   ||   Total dose: " + sumFrame.get(f));
	    		for(int c = 0; c < list.get(f).getContours().size(); c++) {
	    			writer.println("Contour : " + (c+1) + "   ||   Total dose: " + sumContour.get(f+c));
	    			writer.print("[ ");
	    			for(int d = 0; d < list.get(f).getContours().get(c).getNumberOfPoints(); d++) {
	    				writer.print(Math.round(list.get(f).getContours().get(c).getData().get(d).getValue()*10)/10.0 + "; ");
	    			}
	    			writer.println("]");
	    		}
	    		writer.println();
	    	}
	    	writer.close();
	    	
	    	return true;
	    }
	    return false;
	}
	
	public static boolean saveAttributeListToFile(Stage stage, AttributeList list) {
		FileChooser chooser = new FileChooser();
		chooser.setInitialDirectory(new File(System.getProperty("user.home") + "/Desktop"));
		File outputFile = chooser.showSaveDialog(stage);
	    if(outputFile != null) {
	    	PrintWriter writer = null;
			try {
				writer = new PrintWriter(outputFile.getAbsolutePath(), "UTF-8");
			} catch (FileNotFoundException | UnsupportedEncodingException e) {
				e.printStackTrace();
				return false;
			}
	    	writer.println(list);
	    	writer.close();
	    	
	    	return true;
	    }
	    return false;
	}
	
	public static boolean saveMarkedAttributesToFile(Stage stage, Collection<Attribute> list) {
		FileChooser chooser = new FileChooser();
		chooser.setInitialDirectory(new File(System.getProperty("user.home") + "/Desktop"));
		File outputFile = chooser.showSaveDialog(stage);
	    if(outputFile != null) {
	    	PrintWriter writer = null;
			try {
				writer = new PrintWriter(outputFile.getAbsolutePath(), "UTF-8");
			} catch (FileNotFoundException | UnsupportedEncodingException e) {
				e.printStackTrace();
				return false;
			}
			int i = 1;
			for(Attribute x : list) {
				writer.println("\n[XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX: " + i + "]\n" + x);
				i++;
			}
	    	writer.close();
	    	
	    	return true;
	    }
	    return false;
	}
	
}
