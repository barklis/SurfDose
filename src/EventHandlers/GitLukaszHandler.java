package EventHandlers;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import GUI.GUI;
import application.Preferences;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class GitLukaszHandler implements EventHandler<ActionEvent> {

	@Override
	public void handle(ActionEvent event) {
		try {
			Desktop.getDesktop().browse(new URL("https://github.com/xxlukasz11").toURI());
		} catch (IOException | URISyntaxException e) {
			String message = Preferences.getLabel("linkHasExpired");
			GUI.instance().showInformationDialog(Preferences.getLabel("error"), message);
			e.printStackTrace();
		}
	}

}
