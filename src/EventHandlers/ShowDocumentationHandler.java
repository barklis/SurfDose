package EventHandlers;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;

import application.ErrorHandler;
import application.Preferences;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class ShowDocumentationHandler implements EventHandler<ActionEvent> {
	@Override
	public void handle(ActionEvent event) {
		File htmlFile = new File("manual/manual.html");
		try {
			Desktop.getDesktop().browse(htmlFile.toURI());
		} catch (IOException e) {
			ErrorHandler handler = new ErrorHandler(Preferences.getLabel("noDocumentationFileContent"));
			handler.showDialog();
			e.printStackTrace();
		}
	}
}
