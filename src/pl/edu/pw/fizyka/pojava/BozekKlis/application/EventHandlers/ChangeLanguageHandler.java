package pl.edu.pw.fizyka.pojava.BozekKlis.application.EventHandlers;

import java.util.Optional;

import pl.edu.pw.fizyka.pojava.BozekKlis.application.Preferences;
import pl.edu.pw.fizyka.pojava.BozekKlis.application.GUI.GUI;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;

// Allows to change language od the application
public class ChangeLanguageHandler implements EventHandler<ActionEvent> {
	String language;
	GUI gui;
	
	public ChangeLanguageHandler(String language, GUI gui) {
		this.language = language;
		this.gui = gui;
	}

	@Override
	public void handle(ActionEvent arg0) {
		Preferences.setLanguage(language);
		
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle(Preferences.getLabel("languageConformationTitle"));
		alert.setContentText(Preferences.getLabel("languageConformationContent"));
		alert.setHeaderText("");
		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK){
			gui.restart();
		}
	}

}
