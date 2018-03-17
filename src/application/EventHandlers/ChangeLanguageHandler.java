package application.EventHandlers;

import java.util.Optional;

import application.Preferences;
import application.GUI.GUI;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;

public class ChangeLanguageHandler implements EventHandler<ActionEvent> {
	String language;
	GUI root;
	
	public ChangeLanguageHandler(String language, GUI root) {
		this.language = language;
		this.root = root;
	}

	@Override
	public void handle(ActionEvent arg0) {
		Preferences.setLanguage(language);
		
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle(Preferences.getLabel("languageConformationTitle"));
		alert.setContentText(Preferences.getLabel("languageConformationContent"));
		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK){
			root.init();
		}
	}

}
