package pl.edu.pw.fizyka.pojava.BozekKlis.application.EventHandlers;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import pl.edu.pw.fizyka.pojava.BozekKlis.application.GUI.GUI;

public class WindowMaximizeHandler implements ChangeListener<Boolean> {
	GUI gui;
	
	public WindowMaximizeHandler(GUI gui) {
		this.gui = gui;
	}
	
	@Override
	public void changed(ObservableValue<? extends Boolean> arg0, Boolean arg1, Boolean arg2) {
		Platform.runLater(()-> {
			gui.getCenterPanel().getDrawingPanel().resizeImage();
		});
	}

}
