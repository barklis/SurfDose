package pl.edu.pw.fizyka.pojava.BozekKlis.application;

import org.w3c.dom.Document;

import javafx.scene.paint.Color;

public class Preferences {
	private static Document languageDocument;
	private static Document configDocument;
	
	private static String language;
	private static int selectionLineWidth;
	private static Color selectionColor;
	
	public static void loadConfigFiles(){
		configDocument = XmlHandler.loadXMLDocument("config.xml");
		languageDocument = XmlHandler.loadXMLDocument(XmlHandler.getLabel(configDocument, "language")+".xml");
		
		language = XmlHandler.getLabel(configDocument, "language");
		selectionLineWidth = Integer.parseInt(XmlHandler.getLabel(configDocument, "selectionLineWidth"));
		selectionColor = Color.valueOf(XmlHandler.getLabel(configDocument, "selectionColor"));
	}
	
	public static int getSelectionLineWidth() {
		return selectionLineWidth;
	}


	public static void setSelectionLineWidth(int selectionLineWidth) {
		Preferences.selectionLineWidth = selectionLineWidth;
		XmlHandler.changeSelectorValue("selectionLineWidth", String.valueOf(selectionLineWidth), configDocument, "config.xml");
	}

	public static Color getSelectionColor() {
		return selectionColor;
	}
	
	public static void setSelectionColor(Color selectionColor) {
		Preferences.selectionColor = selectionColor;

		String valStr = String.format( "#%02X%02X%02X",
				(int)( selectionColor.getRed() * 255 ),
				(int)( selectionColor.getGreen() * 255 ),
				(int)( selectionColor.getBlue() * 255 ) );
		
		XmlHandler.changeSelectorValue("selectionColor", valStr, configDocument, "config.xml");
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
