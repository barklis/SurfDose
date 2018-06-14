package application;

import org.w3c.dom.Document;

import javafx.scene.paint.Color;

// Stores user preferences, and allows to load text in chosen language
public class Preferences {
	private static Document languageDocument;
	private static Document configDocument;
	
	private static String language;
	private static double contourLineWidth;
	private static Color contourLineColor;
	private static int pixelsInCol;
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
			contourLineColor = Color.valueOf(XmlHandler.getLabel(configDocument, "contourLineColor"));
			pixelsInCol = Integer.parseInt(XmlHandler.getLabel(configDocument, "pixelsInCol"));
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
	
		String valStr = String.format( "#%02X%02X%02X",
				(int)( contourColor.getRed() * 255 ),
				(int)( contourColor.getGreen() * 255 ),
				(int)( contourColor.getBlue() * 255 ) );
		
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
}
