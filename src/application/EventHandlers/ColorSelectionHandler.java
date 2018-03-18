package application.EventHandlers;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.paint.Color;

import javax.swing.JColorChooser;

import application.Preferences;

public class ColorSelectionHandler implements EventHandler<ActionEvent> {

	@Override
	public void handle(ActionEvent arg0) {
		int r = (int) Preferences.getSelectionColor().getRed();
		int g = (int) Preferences.getSelectionColor().getGreen();
		int b = (int) Preferences.getSelectionColor().getBlue();
		
		java.awt.Color result = JColorChooser.showDialog(null, "Choose color", new java.awt.Color(r,g,b));
		Color newColor = Color.rgb(result.getRed(), result.getGreen(), result.getBlue());
		Preferences.setSelectionColor(newColor);
	}
}
