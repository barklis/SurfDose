package EventHandlers;

import GUI.GUI;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

//handle window maximize event
public class WindowMaximizeHandler implements ChangeListener<Boolean> {
	GUI gui;
	
	public WindowMaximizeHandler(GUI gui) {
		this.gui = gui;
	}
	
	@Override
	public void changed(ObservableValue<? extends Boolean> arg0, Boolean arg1, Boolean arg2) {
		Platform.runLater(()-> {
			gui.getCenterPanel().getDrawingPanel().getCanvasPanel().setContainerSize();
			gui.getCenterPanel().getDrawingPanel().getMapPanel().setContainerSize();
			
			if(gui.getCenterPanel().getDrawingPanel().isCanvasEmbeded())
				gui.getCenterPanel().getDrawingPanel().getCanvasPanel().drawFrame();
			else if(gui.getCenterPanel().getDrawingPanel().isMapEmbeded())
				gui.getCenterPanel().getDrawingPanel().getMapPanel().drawMap();
		});
	}

}
