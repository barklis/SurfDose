package pl.edu.pw.fizyka.pojava.BozekKlis.application.EventHandlers;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import pl.edu.pw.fizyka.pojava.BozekKlis.application.ErrorHandler;
import pl.edu.pw.fizyka.pojava.BozekKlis.application.Preferences;

public class ShowDocumentationHandler implements EventHandler<ActionEvent> {
	@Override
	public void handle(ActionEvent event) {
		File htmlFile = new File("manual.html");
		try {
			Desktop.getDesktop().browse(htmlFile.toURI());
		} catch (IOException e) {
			ErrorHandler handler = new ErrorHandler(Preferences.getLabel("noDocumentationFileContent"));
			handler.showDialog();
			e.printStackTrace();
		}
	}
}
