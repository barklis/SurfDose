package pl.edu.pw.fizyka.pojava.BozekKlis.application.GUI;

import pl.edu.pw.fizyka.pojava.BozekKlis.application.EventHandlers.ColorSelectionHandler;
import pl.edu.pw.fizyka.pojava.BozekKlis.application.EventHandlers.DeleteSelectionHandler;
import pl.edu.pw.fizyka.pojava.BozekKlis.application.EventHandlers.ResizeImageHandler;
import pl.edu.pw.fizyka.pojava.BozekKlis.application.EventHandlers.RestoreSelectionHandler;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class ToolBox extends VBox {
	GUI gui;
	
	Button hideToolBoxButton;
	Button undoButton;
	Button redoButton;
	Button selectionColorButton;
	Button resizeImageButton;
	
	public ToolBox(GUI gui) {
		this.gui = gui;
		
		hideToolBoxButton = new Button("<<<");
		hideToolBoxButton.getStyleClass().add("hideToolBoxButton");
		
		undoButton = new Button("");
		undoButton.getStyleClass().add("undoButton");
		undoButton.setOnAction(new DeleteSelectionHandler(gui));
		
		redoButton = new Button("");
		redoButton.getStyleClass().add("redoButton");
		redoButton.setOnAction(new RestoreSelectionHandler(gui));
		
		selectionColorButton = new Button("");
		selectionColorButton.getStyleClass().add("selectionColorButton");
		selectionColorButton.setOnAction(new ColorSelectionHandler(gui));
		
		resizeImageButton = new Button("");
		resizeImageButton.getStyleClass().add("resizeImageButton");
		resizeImageButton.setOnAction(new ResizeImageHandler(gui));
		
		getChildren().add(new HBox(hideToolBoxButton));
		getChildren().add(new HBox(undoButton));
		getChildren().add(new HBox(redoButton));
		getChildren().add(new HBox(selectionColorButton));
		getChildren().add(new HBox(resizeImageButton));
	}
}
