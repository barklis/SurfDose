package EventHandlers;

import GUI.GUI;
import application.Preferences;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ButtonType;

// Allows to change language od the application
public class ChangeLanguageHandler implements EventHandler<ActionEvent> {
	String language;
	
	public ChangeLanguageHandler(String language) {
		this.language = language;
	}

	@Override
	public void handle(ActionEvent arg0) {
		Preferences.setLanguage(language);
		
		if(GUI.instance().showConfirmationDialog(
				Preferences.getLabel("languageConformationTitle"),
				Preferences.getLabel("languageConformationContent")
			) == ButtonType.OK){
			GUI.instance().restart();
		}
	}

}
