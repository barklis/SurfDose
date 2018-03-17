package application;

import org.w3c.dom.Document;

import javafx.scene.paint.Color;

public class Preferences {
	private static Document languageDocument;
	private static Document configDocument;
	
	private static String language;
	private static int selectionWidth;
	private static Color selectionColor;
	
	public static void loadConfigFiles(){
		configDocument = XmlHandler.loadXMLDocument("config.xml");
		languageDocument = XmlHandler.loadXMLDocument(XmlHandler.getLabel(configDocument, "language")+".xml");
		
		language = XmlHandler.getLabel(configDocument, "language");
		selectionWidth = Integer.parseInt(XmlHandler.getLabel(configDocument, "selectionWidth"));
		selectionColor = Color.valueOf(XmlHandler.getLabel(configDocument, "selectionColor"));
	}
	
	public static int getSelectionWidth() {
		return selectionWidth;
	}


	public static void setSelectionWidth(int selectionWidth) {
		Preferences.selectionWidth = selectionWidth;
		XmlHandler.changeSelectorValue("selectionWidth", String.valueOf(selectionWidth), configDocument, "config.xml");
	}

	public static Color getSelectionColor() {
		return selectionColor;
	}
	
	public static void setSelectionColor(Color selectionColor) {
		Preferences.selectionColor = selectionColor;
		
		String valStr = "#";
		valStr += Integer.toHexString((int) selectionColor.getRed());
		valStr += Integer.toHexString((int) selectionColor.getGreen());
		valStr += Integer.toHexString((int) selectionColor.getBlue());
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
