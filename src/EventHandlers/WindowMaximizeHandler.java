package EventHandlers;

import GUI.GUI;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

//handle window maximize event
public class WindowMaximizeHandler implements ChangeListener<Boolean> {
	
	@Override
	public void changed(ObservableValue<? extends Boolean> arg0, Boolean arg1, Boolean arg2) {
		Platform.runLater(()-> {
			GUI.instance().getCanvasPanel().setContainerSize();
			GUI.instance().getMapPanel().setContainerSize();
			GUI.instance().getScalePanel().drawScale();
			
			if(GUI.instance().getDrawingPanel().isCanvasEmbeded())
				GUI.instance().getCanvasPanel().drawFrame();
			else if(GUI.instance().getDrawingPanel().isMapEmbeded())
				GUI.instance().getMapPanel().drawMap();
		});
	}

}
