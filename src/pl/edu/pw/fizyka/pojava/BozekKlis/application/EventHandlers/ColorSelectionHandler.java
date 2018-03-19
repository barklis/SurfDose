package pl.edu.pw.fizyka.pojava.BozekKlis.application.EventHandlers;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.paint.Color;

import javax.swing.JColorChooser;

import pl.edu.pw.fizyka.pojava.BozekKlis.application.Preferences;

public class ColorSelectionHandler implements EventHandler<ActionEvent> {

	@Override
	public void handle(ActionEvent arg0) {
		int r = (int) (255*Preferences.getSelectionColor().getRed());
		int g = (int) (255*Preferences.getSelectionColor().getGreen());
		int b = (int) (255*Preferences.getSelectionColor().getBlue());
		
		java.awt.Color result = JColorChooser.showDialog(null, "Choose color", new java.awt.Color(r,g,b));
		if(result != null) {
			Color newColor = Color.rgb(result.getRed(), result.getGreen(), result.getBlue());
			Preferences.setSelectionColor(newColor);
		}
	}
}
