package EventHandlers;

import GUI.GUI;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

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
