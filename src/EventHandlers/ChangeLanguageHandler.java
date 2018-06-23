package EventHandlers;

import GUI.GUI;
import application.Preferences;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ButtonType;

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
		
		if(gui.showConfirmationDialog(
				Preferences.getLabel("languageConformationTitle"),
				Preferences.getLabel("languageConformationContent")
			) == ButtonType.OK){
			gui.restart();
		}
	}

}
