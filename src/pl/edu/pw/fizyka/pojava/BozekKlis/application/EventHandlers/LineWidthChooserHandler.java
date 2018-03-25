package pl.edu.pw.fizyka.pojava.BozekKlis.application.EventHandlers;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import pl.edu.pw.fizyka.pojava.BozekKlis.application.Preferences;
import pl.edu.pw.fizyka.pojava.BozekKlis.application.GUI.GUI;
import pl.edu.pw.fizyka.pojava.BozekKlis.application.GUI.Selection;

public class LineWidthChooserHandler implements EventHandler<ActionEvent> {

	public static Stage optionsWindow = null;
	GUI gui;
	
	public LineWidthChooserHandler(GUI gui) {
		this.gui = gui;
	}

	@Override
	public void handle(ActionEvent arg0) {
		if(optionsWindow != null)
			optionsWindow.close();
		optionsWindow = new Stage();
		optionsWindow.setTitle(Preferences.getLabel("lineWidthWindowTitle"));
		
		Spinner<Double> lineWidthField = new Spinner<Double>();
		SpinnerValueFactory<Double> factory = new SpinnerValueFactory.DoubleSpinnerValueFactory(0.1, 100.0, Preferences.getSelectionLineWidth(), 0.1);
		lineWidthField.setValueFactory(factory);
		
		CheckBox applyToAll = new CheckBox(Preferences.getLabel("applyToAllCheckbox"));
		
		Button okButton = new Button(Preferences.getLabel("okButton"));
		okButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				Preferences.setSelectionLineWidth(lineWidthField.getValue());
				
				if(applyToAll.isSelected()) {
					Selection.setGlobalLineWidth(lineWidthField.getValue());
					gui.getCenterPanel().getDrawingPanel().getSelectionPanel().draw();
				}
				else if(Selection.getCurrentSelection() != null) {
					Selection.setCurrentSelectionLineWidth(lineWidthField.getValue());
					gui.getCenterPanel().getDrawingPanel().getSelectionPanel().draw();
				}
				
				optionsWindow.close();
				optionsWindow = null;
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
		hboxTop.getChildren().add(new VBox(new Label(Preferences.getLabel("lineWidthWindowLabel"))));
		hboxTop.getChildren().add(lineWidthField);
		
		HBox hboxBottom = new HBox();
		hboxBottom.getChildren().addAll(okButton, cancelButton);
		hboxBottom.setAlignment(Pos.CENTER);
		
		BorderPane root = new BorderPane();
		root.setTop(hboxTop);
		root.setCenter(new HBox(applyToAll));
		root.setBottom(hboxBottom);
		
		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("/pl/edu/pw/fizyka/pojava/BozekKlis/Resources/lineWidthWindowStylesheet.css").toExternalForm());
		
		optionsWindow.setScene(scene);
		optionsWindow.show();
		
	}

}
