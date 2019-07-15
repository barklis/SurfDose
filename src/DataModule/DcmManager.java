package DataModule;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import com.pixelmed.dicom.AttributeList;

import GUI.GUI;
import GUI.ChartPanel;
import application.Preferences;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

// Contains static methods to work witch DICOM files, like loading and saving
public class DcmManager {
	
	private static String lastPath = System.getProperty("user.home");
	
	public static File getDcmFile(Stage stage, String fileType, String extension) {
		FileChooser chooser = new FileChooser();
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(fileType + " files (*." + extension + ")", "*." + extension);
		chooser.getExtensionFilters().add(extFilter);
		chooser.setInitialDirectory(new File(lastPath));
		File selectedFile = chooser.showOpenDialog(stage);
	    if(selectedFile != null) {
	    	lastPath = selectedFile.getParent();
	    	return selectedFile;
	    }
	    return null;
	}
	
	public static List<File> getDcmFiles(Stage stage, String fileType, String extension) {
		FileChooser chooser = new FileChooser();
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(fileType + " files (*." + extension + ")", "*." + extension);
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

	private static PrintWriter createPrintWriter(String fileTypeLabel, String extension, String initialFileName){
		String dotExtension = "." + extension;
		String pattern = "*" + dotExtension;
		String bracedPattern = "(" + dotExtension + ")";

		FileChooser chooser = new FileChooser();
		chooser.setInitialDirectory(new File(lastPath));
		chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(fileTypeLabel + " " + bracedPattern, pattern));
		chooser.setInitialFileName(initialFileName);
		File outputFile = chooser.showSaveDialog(GUI.instance().getMainWindow());

		if(outputFile == null) {
			return null;
		}

		if (!outputFile.getName().contains(".")) {
			outputFile.renameTo(new File(outputFile.getName() + dotExtension));
		}

		PrintWriter writer;
		try {
			writer = new PrintWriter(outputFile.getAbsolutePath(), "UTF-8");
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			GUI.instance().showErrorDialog(Preferences.getLabel("error"), Preferences.getLabel("cannotCreateFile"));
			return null;
		}
		return writer;
	}
	
	public static Color getDoseColorScale(double doseValue, double maxValue){
		double hue = Color.BLUE.getHue() + (Color.RED.getHue() - Color.BLUE.getHue()) * doseValue / maxValue ;
		return Color.hsb(hue, 1.0, 1.0);
	}
	
	public static void saveDataToFile(Stage stage, double[][] matrix) {
		PrintWriter writer = createPrintWriter(Preferences.getLabel("textFile"), "txt", "surface_data");
		if(writer == null){
			return;
		}

		// grid size - (row col)
		writer.println(Preferences.getPixelsInRow() + " " + Preferences.getPixelsInCol());
		double angle = getRelativeAngle(0, -1, DcmData.getuVector()[0], DcmData.getuVector()[1]);
		writer.println(toDegrees(angle) + " " + toDegrees(DcmData.getAngularWidth()));
		
		// data
    	for(int r = 0; r < matrix[0].length; r++) {
    		for(int c = 0; c < matrix.length; c++)
    			writer.print(matrix[c][r] + " ");
    		writer.println();
    	}
    	
    	writer.close();
	}
	
	public static double toDegrees(double radians) {
		return 180.0/Math.PI*radians;
	}
	
	public static int getStartingFrame(int id) {
		for(int f = 0; f < DcmData.getDcmFrames().size(); ++f) {
			DcmFrame frame = DcmData.getDcmFrames().get(f);
			for(int x = 0; x < frame.getContours().size(); ++x)
				if(frame.getContours().get(x).getId() == id)
					return f;
		}
		return 0;
	}
	
	public static int getEndingFrame(int id) {
		for(int f = DcmData.getDcmFrames().size()-1; f >= 0; --f) {
			DcmFrame frame = DcmData.getDcmFrames().get(f);
			for(int x = 0; x < frame.getContours().size(); ++x)
				if(frame.getContours().get(x).getId() == id)
					return f;
		}
		return 0;
	}
	
	public static void saveContourCenter() {
		PrintWriter writer = createPrintWriter(Preferences.getLabel("textFile"), "txt", "contour_center");
		if(writer == null){
			return;
		}

		int currentId = DcmData.getCurrentContourId();
		int startingFrame = getStartingFrame(currentId);
		int endingFrame = getEndingFrame(currentId);
			
		// relative centers of contours - [X:Y]
		double isoX = DcmData.getIsocenterPosition()[0];
		double isoY = DcmData.getIsocenterPosition()[1];
		double isoZ = DcmData.getIsocenterPosition()[2];
		
		// Z coord of contour - (min max)
		writer.println((DcmData.getFrame(startingFrame).getZ() - isoZ) + " " + (DcmData.getFrame(endingFrame).getZ() - isoZ));
		
		for(int i = startingFrame; i <= endingFrame; ++i){
			DcmFrame frame = DcmData.getFrame(i);
			Contour c = frame.getContourById(currentId);
			writer.println((c.getCenterX() - isoX) + " " + (c.getCenterY() - isoY));
		}
		writer.close();
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
	
	public static void saveAttributeListToFile(AttributeList list) {
		PrintWriter writer = createPrintWriter("text file", "txt", "attribute_list");
		if(writer == null){
			return;
		}

		writer.println(list);
		writer.close();
	}

	public static double getRelativeAngle(double x1, double y1, double x2, double y2) {
		double angle = Math.atan2(x1*y2-x2*y1, x1*x2+y1*y2);
		if(angle < 0)
			 return (angle + 2*Math.PI);
		return angle;
	}

	public static void exportPixelWidth() {
		/* TODO
		int currentId = DcmData.getCurrentContourId();
		int startingFrame = DcmManager.getStartingFrame(currentId);
		int endingFrame = DcmManager.getEndingFrame(currentId);

		for(int i = startingFrame; i <= endingFrame; ++i){

		}
		*/
	}

	public static void exportChart(int frameNumber) {
		int currentId = DcmData.getCurrentContourId();

		PrintWriter writer = createPrintWriter(Preferences.getLabel("textFile"), "txt", "chart_"+frameNumber);
		if(writer == null)
			return;

		List<Point> data = DcmData.getFrame(frameNumber).getContourById(currentId).getData();
		double z_coord = DcmData.getFrame(frameNumber).getZ();

		int nOfPoints = data.size();
		writer.print("Z: ");
		writer.println(z_coord);
		writer.println();
		writer.println("Angle [°]\tDose [Gy]");

		BiConsumer<Integer, PrintWriter> writeXY = (i, writer_local) -> {
			if (data.get(i).getAngle() > DcmData.getAngularWidth())
				return;

			double x_data = data.get(i).getAngle() * 180 / Math.PI;
			double y_data = data.get(i).getValue() / 100;
			writer_local.print(x_data);
			writer_local.print("\t");
			writer_local.print(y_data);
			writer_local.println();
		};

		int startingIndex = ChartPanel.getStartingIndex(data);
		for (int i = startingIndex; i < nOfPoints; ++i) {
			writeXY.accept(i, writer);
		}
		for (int i = 0; i < startingIndex; ++i) {
			writeXY.accept(i, writer);
		}

		writer.close();
	}

}
