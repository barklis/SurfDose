package application;

import org.w3c.dom.Document;

import Exceptions.ErrorHandler;
import javafx.scene.paint.Color;

// Stores user preferences, and allows to load text in chosen language
public class Preferences {
	private static Document languageDocument;
	private static Document configDocument;
	
	private static String language;
	private static double contourLineWidth;
	private static double activeContourLineWidth;
	private static Color contourLineColor;
	private static Color activeContourLineColor;
	private static int pixelsInCol;
	private static int pixelsInRow;
	private static double interpolationRadius;
	
	public static boolean loadConfigFiles(){
		try {
			configDocument = XmlHandler.loadXMLDocument("config.xml");
		} catch (Exception e) {
			new ErrorHandler("Cannot find config.xml").showDialog();
			return false;
		}
		try {
			languageDocument = XmlHandler.loadXMLDocument(XmlHandler.getLabel(configDocument, "language")+".xml");
			
			language = XmlHandler.getLabel(configDocument, "language");
			contourLineWidth = Double.parseDouble(XmlHandler.getLabel(configDocument, "contourLineWidth"));
			activeContourLineWidth = Double.parseDouble(XmlHandler.getLabel(configDocument, "activeContourLineWidth"));
			contourLineColor = Color.valueOf(XmlHandler.getLabel(configDocument, "contourLineColor"));
			activeContourLineColor = Color.valueOf(XmlHandler.getLabel(configDocument, "activeContourLineColor"));
			pixelsInCol = Integer.parseInt(XmlHandler.getLabel(configDocument, "pixelsInCol"));
			pixelsInRow = Integer.parseInt(XmlHandler.getLabel(configDocument, "pixelsInRow"));
			interpolationRadius = Double.parseDouble(XmlHandler.getLabel(configDocument, "interpolationRadius"));
		} catch (Exception e) {
			new ErrorHandler("Cannot find "+XmlHandler.getLabel(configDocument, "language")+".xml").showDialog();
			return false;
		}
		return true;
	}
	
	public static double getContourLineWidth() {
		return contourLineWidth;
	}

	public static void setContourLineWidth(double contourLineWidth) {
		Preferences.contourLineWidth = contourLineWidth;
		XmlHandler.changeSelectorValue("contourLineWidth", String.valueOf(contourLineWidth), configDocument, "config.xml");
	}

	public static Color getContourLineColor() {
		return contourLineColor;
	}
	
	public static double getInterpolationRadius() {
		return interpolationRadius;
	}

	public static void setInterpolationRadius(double interpolationRadius) {
		Preferences.interpolationRadius = interpolationRadius;
		XmlHandler.changeSelectorValue("interpolationRadius", String.valueOf(interpolationRadius), configDocument, "config.xml");
	}

	public static void setContourLineColor(Color contourColor) {
		Preferences.contourLineColor = contourColor;
		String valStr = getColorString(contourColor);
		XmlHandler.changeSelectorValue("contourLineColor", valStr, configDocument, "config.xml");
	}


	public static int getPixelsInCol() {
		return pixelsInCol;
	}

	public static void setPixelsInCol(int pixelsInCol) {
		Preferences.pixelsInCol = pixelsInCol;
		XmlHandler.changeSelectorValue("pixelsInCol", String.valueOf(Preferences.pixelsInCol), configDocument, "config.xml");
	}

	public static String getLabel(String name) {
		return XmlHandler.getLabel(languageDocument, name);
	}
	
	public static String getLanguage() {
		return language;
	}
	
	public static void setLanguage(String language) {
		Preferences.language = language;
		XmlHandler.changeSelectorValue("language", language, configDocument, "config.xml");
	}

	public static int getPixelsInRow() {
		return pixelsInRow;
	}

	public static void setPixelsInRow(int pixelsInRow) {
		Preferences.pixelsInRow = pixelsInRow;
		XmlHandler.changeSelectorValue("pixelsInRow", String.valueOf(Preferences.pixelsInRow), configDocument, "config.xml");
	}

	public static double getActiveContourLineWidth() {
		return activeContourLineWidth;
	}

	public static void setActiveContourLineWidth(double activeContourLineWidth) {
		Preferences.activeContourLineWidth = activeContourLineWidth;
		XmlHandler.changeSelectorValue("activeContourLineWidth", String.valueOf(activeContourLineWidth), configDocument, "config.xml");
	}

	public static Color getActiveContourLineColor() {
		return activeContourLineColor;
	}

	public static void setActiveContourLineColor(Color activeContourLineColor) {
		Preferences.activeContourLineColor = activeContourLineColor;
		String valStr = getColorString(activeContourLineColor);
		XmlHandler.changeSelectorValue("activeContourLineColor", valStr, configDocument, "config.xml");
	}
	
	private static String getColorString(Color color) {
		return String.format( "#%02X%02X%02X",
				(int)( color.getRed() * 255 ),
				(int)( color.getGreen() * 255 ),
				(int)( color.getBlue() * 255 ) );
	}
	
}
