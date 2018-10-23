package EventHandlers;

import GUI.GUI;
import application.Preferences;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class PlanDataSettingsHandler implements EventHandler<ActionEvent> {
	private static Stage window = null;
	
	private CheckBox axesCheckBox;
	ColorPicker axesColorField;
	
	CheckBox isocenterCheckBox;
	ColorPicker isocenterColorField;
	ColorPicker activeIsocenterColorField;
	Spinner<Integer> isocenterDotRadiusField;
	
	Label axesLabel;
	Label isocenterLabel;
	Label activeIsocenterLabel;
	Label isocenterDotDiameterLabel;

	@Override
	public void handle(ActionEvent event) {
		if(window != null)
			window.close();
		window = new Stage();
		window.setTitle(Preferences.getLabel("planDataSettingsWindowTitle"));
		
		axesCheckBox = new CheckBox(Preferences.getLabel("axesCheckboxLabel"));
		axesColorField = new ColorPicker(Preferences.getAxesColor());
		
		isocenterCheckBox = new CheckBox(Preferences.getLabel("isocenterCheckboxLabel"));
		isocenterColorField = new ColorPicker(Preferences.getIsocenterColor());
		activeIsocenterColorField = new ColorPicker(Preferences.getActiveIsocenterColor());
		
		isocenterDotRadiusField = new Spinner<Integer>();
		SpinnerValueFactory<Integer> factoryOne = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 500, Preferences.getIsocenterDotRadius(), 1);
		isocenterDotRadiusField.setValueFactory(factoryOne);
		
		axesLabel = new Label(Preferences.getLabel("axesColorLabel") + ":");
		isocenterLabel = new Label(Preferences.getLabel("isocenterColorLabel") + ":");
		activeIsocenterLabel = new Label(Preferences.getLabel("activeIsocenterColorLabel") + ":");
		isocenterDotDiameterLabel = new Label(Preferences.getLabel("isocenterDotRadiusLabel") + ":");
		
		Button okButton = new Button(Preferences.getLabel("okButton"));
		
		if(Preferences.isAxesVisible())
			axesCheckBox.setSelected(true);
		else
			setAxesSectionEnabled(false);
		
		if(Preferences.isIsocenterVisible())
			isocenterCheckBox.setSelected(true);
		else
			setIsoenterSectionEnabled(false);
		
		axesColorField.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Preferences.setAxesColor(axesColorField.getValue());
				if(GUI.instance().getDrawingPanel().isCanvasEmbeded())
					GUI.instance().getCanvasPanel().drawFrame();
			}
		});
		
		isocenterColorField.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Preferences.setIsocenterColor(isocenterColorField.getValue());
				if(GUI.instance().getDrawingPanel().isCanvasEmbeded())
					GUI.instance().getCanvasPanel().drawFrame();
			}
		});
		
		activeIsocenterColorField.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Preferences.setActiveIsocenterColor(activeIsocenterColorField.getValue());
				if(GUI.instance().getDrawingPanel().isCanvasEmbeded())
					GUI.instance().getCanvasPanel().drawFrame();
			}
		});
		
		axesCheckBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
			public void changed(ObservableValue<? extends Boolean> ov, Boolean oldVal, Boolean newVal) {
				setAxesSectionEnabled(newVal);
				if(GUI.instance().getDrawingPanel().isCanvasEmbeded())
					GUI.instance().getCanvasPanel().drawFrame();
			}
		});
		
		isocenterCheckBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
			public void changed(ObservableValue<? extends Boolean> ov, Boolean oldVal, Boolean newVal) {
				setIsoenterSectionEnabled(newVal);
				if(GUI.instance().getDrawingPanel().isCanvasEmbeded())
					GUI.instance().getCanvasPanel().drawFrame();
			}
		});
		
		isocenterDotRadiusField.valueProperty().addListener((obs, oldValue, newValue) -> {
			Preferences.setIsocenterDotRadius(isocenterDotRadiusField.getValue());
			if(GUI.instance().getDrawingPanel().isCanvasEmbeded())
				GUI.instance().getCanvasPanel().drawFrame();
		});
		
		okButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				window.close();
				window = null;
			}
		});
		
		VBox topLeft = new VBox(new HBox(axesLabel));
		VBox topRight = new VBox(new HBox(axesColorField));
		
		VBox middleLeft = new VBox(new HBox(isocenterLabel),
								   new HBox(activeIsocenterLabel),
								   new HBox(isocenterDotDiameterLabel));
		VBox middleRight = new VBox(new HBox(isocenterColorField),
									new HBox(activeIsocenterColorField),
									new HBox(isocenterDotRadiusField));
		
		topLeft.getStyleClass().add("tableVbox");
		topRight.getStyleClass().add("tableVbox");
		middleLeft.getStyleClass().add("tableVbox");
		middleRight.getStyleClass().add("tableVbox");
		
		VBox vboxTop = new VBox(axesCheckBox, new HBox(topLeft, topRight));
		VBox vboxMiddle = new VBox(isocenterCheckBox, new HBox(middleLeft, middleRight));
		
		vboxTop.getStyleClass().add("bottomPadding");
		vboxMiddle.getStyleClass().add("bottomPadding");
		
		HBox hboxBottom = new HBox(okButton);
		hboxBottom.setAlignment(Pos.CENTER);
		
		BorderPane root = new BorderPane();
		root.setTop(vboxTop);
		root.setCenter(vboxMiddle);
		root.setBottom(hboxBottom);
		
		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("/Stylesheets/optionWindowStylesheet.css").toExternalForm());
		
		window.setScene(scene);
		window.setResizable(false);
		window.show();
	}
	
	private void setIsoenterSectionEnabled(boolean flag) {
		Color textColor = flag ? Color.BLACK : Color.GRAY;
		
		isocenterColorField.setDisable(!flag);
		activeIsocenterColorField.setDisable(!flag);
		isocenterDotRadiusField.setDisable(!flag);
			
		isocenterLabel.setTextFill(textColor);
		activeIsocenterLabel.setTextFill(textColor);
		isocenterDotDiameterLabel.setTextFill(textColor);
		
		Preferences.setIsocenterVisible(flag);
	}
	
	private void setAxesSectionEnabled(boolean flag){
		Color textColor = flag ? Color.BLACK : Color.GRAY;
		
		axesColorField.setDisable(!flag);
		axesLabel.setTextFill(textColor);
		
		Preferences.setAxesVisible(flag);
	}
	
	public static Stage getWindow() {
		return window;
	}

}
