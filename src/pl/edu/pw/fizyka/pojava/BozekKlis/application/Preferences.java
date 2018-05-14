package pl.edu.pw.fizyka.pojava.BozekKlis.application;

import org.w3c.dom.Document;

import javafx.scene.paint.Color;

public class Preferences {
	private static Document languageDocument;
	private static Document configDocument;
	
	private static String language;
	private static double contourLineWidth;
	private static Color contourLineColor;
	
	public static void loadConfigFiles(){
		configDocument = XmlHandler.loadXMLDocument("config.xml");
		languageDocument = XmlHandler.loadXMLDocument(XmlHandler.getLabel(configDocument, "language")+".xml");
		
		language = XmlHandler.getLabel(configDocument, "language");
		contourLineWidth = Double.parseDouble(XmlHandler.getLabel(configDocument, "contourLineWidth"));
		contourLineColor = Color.valueOf(XmlHandler.getLabel(configDocument, "contourLineColor"));
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
	
	public static void setContourLineColor(Color contourColor) {
		Preferences.contourLineColor = contourColor;
	
		String valStr = String.format( "#%02X%02X%02X",
				(int)( contourColor.getRed() * 255 ),
				(int)( contourColor.getGreen() * 255 ),
				(int)( contourColor.getBlue() * 255 ) );
		
		XmlHandler.changeSelectorValue("contourLineColor", valStr, configDocument, "config.xml");
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
