package EventHandlers;

import GUI.GUI;
import application.Preferences;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ContourSettingsHandler implements EventHandler<ActionEvent> {

	GUI gui;
	private static Stage window = null;
	
	public ContourSettingsHandler(GUI gui) {
		this.gui = gui;
	}

	@Override
	public void handle(ActionEvent event) {
		if(window != null)
			window.close();
		window = new Stage();
		window.setTitle(Preferences.getLabel("windowTitle"));
		
		ColorPicker lineColorField = new ColorPicker(Preferences.getContourLineColor());
		ColorPicker activeLineColorField = new ColorPicker(Preferences.getActiveContourLineColor());
		
		Spinner<Double> lineWidthField = new Spinner<Double>();
		SpinnerValueFactory<Double> factoryOne = new SpinnerValueFactory.DoubleSpinnerValueFactory(0.1, 100.0, Preferences.getContourLineWidth(), 0.1);
		lineWidthField.setValueFactory(factoryOne);
		
		Spinner<Double> activeLineWidthField = new Spinner<Double>();
		SpinnerValueFactory<Double> factoryTwo = new SpinnerValueFactory.DoubleSpinnerValueFactory(0.1, 100.0, Preferences.getActiveContourLineWidth(), 0.1);
		activeLineWidthField.setValueFactory(factoryTwo);
		
		Button okButton = new Button(Preferences.getLabel("okButton"));
		
		VBox left = new VBox(new HBox(new Label(Preferences.getLabel("contourColorLabel")+":")),
					new HBox(new Label(Preferences.getLabel("activeContourColorLabel")+":")),
					new HBox(new Label(Preferences.getLabel("contourWidthLabel")+":")),
					new HBox(new Label(Preferences.getLabel("activeContourWidthLabel")+":")));
		
		VBox right = new VBox(new HBox(lineColorField),
					 new HBox(activeLineColorField),
					 new HBox(lineWidthField),
					 new HBox(activeLineWidthField));
		
		left.getStyleClass().add("tableVbox");
		right.getStyleClass().add("tableVbox");
		
		HBox hboxTop = new HBox(left, right);
		
		HBox hboxBottom = new HBox();
		hboxBottom.getChildren().add(okButton);
		hboxBottom.setAlignment(Pos.CENTER);
		
		BorderPane root = new BorderPane();
		root.setTop(hboxTop);
		root.setBottom(hboxBottom);
		
		okButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				window.close();
				window = null;
			}
		});
		
		lineColorField.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Preferences.setContourLineColor(lineColorField.getValue());
				if(gui.getCenterPanel().getDrawingPanel().isCanvasEmbeded())
					gui.getCenterPanel().getDrawingPanel().getCanvasPanel().drawFrame();
			}
		});
		
		activeLineColorField.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Preferences.setActiveContourLineColor(activeLineColorField.getValue());
				if(gui.getCenterPanel().getDrawingPanel().isCanvasEmbeded())
					gui.getCenterPanel().getDrawingPanel().getCanvasPanel().drawFrame();
			}
		});
		
		lineWidthField.valueProperty().addListener((obs, oldValue, newValue) -> {
			Preferences.setContourLineWidth(lineWidthField.getValue());
			if(gui.getCenterPanel().getDrawingPanel().isCanvasEmbeded())
				gui.getCenterPanel().getDrawingPanel().getCanvasPanel().drawFrame();
		});
		
		activeLineWidthField.valueProperty().addListener((obs, oldValue, newValue) -> {
			Preferences.setActiveContourLineWidth(activeLineWidthField.getValue());
			if(gui.getCenterPanel().getDrawingPanel().isCanvasEmbeded())
				gui.getCenterPanel().getDrawingPanel().getCanvasPanel().drawFrame();
		});
		
		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("/Stylesheets/optionWindowStylesheet.css").toExternalForm());
		
		window.setScene(scene);
		window.setResizable(false);
		window.show();
	}

	public static Stage getWindow() {
		return window;
	}

}
