package pl.edu.pw.fizyka.pojava.BozekKlis.application.EventHandlers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import pl.edu.pw.fizyka.pojava.BozekKlis.application.GUI.GUI;

public class WindowResizedHandler implements ChangeListener<Number> {
	GUI gui;
		
	public WindowResizedHandler(GUI gui) {
		this.gui = gui;
	}

	@Override
	public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
		gui.setResizeEventManaged(false);
	}
	
}
