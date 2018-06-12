package pl.edu.pw.fizyka.pojava.BozekKlis.application.EventHandlers;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import pl.edu.pw.fizyka.pojava.BozekKlis.application.Preferences;
import pl.edu.pw.fizyka.pojava.BozekKlis.application.GUI.GUI;

// Allows to choose contour line width
public class ContourLineWidthHandler implements EventHandler<ActionEvent> {

	public static Stage optionsWindow = null;
	GUI gui;
	
	public ContourLineWidthHandler(GUI gui) {
		this.gui = gui;
	}
	
	@Override
	public void handle(ActionEvent event) {
		if(optionsWindow != null)
			optionsWindow.close();
		optionsWindow = new Stage();
		optionsWindow.setTitle(Preferences.getLabel("contourWidthWindowTitle"));
		
		Spinner<Double> lineWidthField = new Spinner<Double>();
		SpinnerValueFactory<Double> factory = new SpinnerValueFactory.DoubleSpinnerValueFactory(0.1, 100.0, Preferences.getContourLineWidth(), 0.1);
		lineWidthField.setValueFactory(factory);
		
		Button okButton = new Button(Preferences.getLabel("okButton"));
		okButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				if(lineWidthField.getValue() == Preferences.getContourLineWidth()) {
					optionsWindow.close();
					optionsWindow = null;
					return;
				}
				Preferences.setContourLineWidth(lineWidthField.getValue());
				
				optionsWindow.close();
				optionsWindow = null;
				if(gui.getCenterPanel().getDrawingPanel().isCanvasEmbeded())
					gui.getCenterPanel().getDrawingPanel().getCanvasPanel().drawFrame();
			}
		});
		
		Button applyButton = new Button(Preferences.getLabel("applyButton"));
		applyButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if(lineWidthField.getValue() == Preferences.getContourLineWidth()) {
					return;
				}
				Preferences.setContourLineWidth(lineWidthField.getValue());
				if(gui.getCenterPanel().getDrawingPanel().isCanvasEmbeded())
					gui.getCenterPanel().getDrawingPanel().getCanvasPanel().drawFrame();
			}
		});
		
		Button cancelButton = new Button(Preferences.getLabel("cancelButton"));
		cancelButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				optionsWindow.close();
				optionsWindow = null;
			}
		});
		
		HBox hboxTop = new HBox();
		hboxTop.getChildren().add(new VBox(new Label(Preferences.getLabel("contourWidthWindowLabel"))));
		hboxTop.getChildren().add(lineWidthField);
		
		HBox hboxBottom = new HBox();
		hboxBottom.getChildren().addAll(okButton, cancelButton, applyButton);
		hboxBottom.setAlignment(Pos.CENTER);
		
		BorderPane root = new BorderPane();
		root.setTop(hboxTop);
		root.setBottom(hboxBottom);
		
		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("/pl/edu/pw/fizyka/pojava/BozekKlis/Resources/optionWindowStylesheet.css").toExternalForm());
		
		optionsWindow.setScene(scene);
		optionsWindow.setResizable(false);
		optionsWindow.show();
	}

	public static Stage getOptionsWindow() {
		return optionsWindow;
	}

}
