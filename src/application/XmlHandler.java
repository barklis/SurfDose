package application;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class XmlHandler {
	
	public static String getLabel(Document doc, String name) {
		return ((Element) doc.getElementsByTagName(name).item(0)).getTextContent();
	}
	
	public static Document loadXMLDocument(String fileName) {
		Document doc = null;
		try {
			DocumentBuilder dBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			doc = dBuilder.parse(new File("src/XML/" + fileName));
			doc.getDocumentElement().normalize();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return doc;
	}
	
	public static void changeSelectorValue(String selector, String value, Document doc, String fileName) {
		try {
			Node node = doc.getElementsByTagName(selector).item(0);
			node.setTextContent(value);
			
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File("src/XML/"+fileName));
			transformer.transform(source, result);
		} catch (TransformerException e) {
			e.printStackTrace();
		}
	}
}
