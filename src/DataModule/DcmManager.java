package DataModule;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.pixelmed.dicom.Attribute;
import com.pixelmed.dicom.AttributeList;

import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

// Contains static methods to work witch DICOM files, like loading and saving
public class DcmManager {
	
	static String lastPath = null;
	
	public static File getDcmFile(Stage stage, String fileType) {
		FileChooser chooser = new FileChooser();
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(fileType + " files (*.dcm)", "*.dcm");
		chooser.getExtensionFilters().add(extFilter);
		if(lastPath != null)
			chooser.setInitialDirectory(new File(lastPath));
		else
			chooser.setInitialDirectory(new File(System.getProperty("user.home")));
		File selectedFile = chooser.showOpenDialog(stage);
	    if(selectedFile != null) {
	    	lastPath = selectedFile.getParent();
	    	return selectedFile;
	    }
	    return null;
	}
	
	public static List<File> getDcmFiles(Stage stage, String fileType) {
		FileChooser chooser = new FileChooser();
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(fileType + " files (*.dcm)", "*.dcm");
		chooser.getExtensionFilters().add(extFilter);
		if(lastPath != null)
			chooser.setInitialDirectory(new File(lastPath));
		else
			chooser.setInitialDirectory(new File(System.getProperty("user.home")));
		List<File> selectedFiles = chooser.showOpenMultipleDialog(stage);
	    if(selectedFiles != null) {
	    	lastPath = selectedFiles.get(0).getParent();
	    	return selectedFiles;
	    }
	    return null;
	}
	
	public static Color getDoseColorScale(double doseValue, double maxValue){
		//if(doseValue == 0)
		//	return Color.BLACK;
		//double colorValue = doseValue/maxDoseValue;
		////double colorValue = doseValue / DcmData.getMaxDoseValue();
		//return new Color(colorValue, colorValue, colorValue, 1.0);
		
		double hue = Color.BLUE.getHue() + (Color.RED.getHue() - Color.BLUE.getHue()) * doseValue / maxValue ;
		return Color.hsb(hue, 1.0, 1.0);
	}
	
	public static boolean saveDoseDataToFile(Stage stage, Point[][] doseMatrix) {
		FileChooser chooser = new FileChooser();
		chooser.setInitialDirectory(new File(System.getProperty("user.dir")));
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

	public static double getRelativeAngle(double x1, double y1, double x2, double y2) {
		double angle = Math.atan2(x1*y2-x2*y1, x1*x2+y1*y2);
		if(angle < 0)
			 return (angle + 2*Math.PI);
		return angle;
	}
	
}
