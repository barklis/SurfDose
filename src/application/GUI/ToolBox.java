package application.GUI;

import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class ToolBox extends VBox {
	GUI gui;
	
	Button hideToolBoxButton;
	Button undoButton;
	Button selectionColorButton;
	
	public ToolBox(GUI gui) {
		this.gui = gui;
		
		hideToolBoxButton = new Button("<<<");
		hideToolBoxButton.getStyleClass().add("hideToolBoxButton");
		
		undoButton = new Button("");
		undoButton.getStyleClass().add("undoButton");
		
		selectionColorButton = new Button("");
		selectionColorButton.getStyleClass().add("selectionColorButton");
		
		getChildren().add(new HBox(hideToolBoxButton));
		getChildren().add(new HBox(undoButton));
		getChildren().add(new HBox(selectionColorButton));
	}
}
