package EventHandlers;

import GUI.GUI;
import application.Preferences;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MapSettingsHandler implements EventHandler<ActionEvent> {

	GUI gui;
	private static Stage mapSettingsWindow = null;
	
	public MapSettingsHandler(GUI gui) {
		this.gui = gui;
	}

	@Override
	public void handle(ActionEvent event) {
		if(mapSettingsWindow != null)
			mapSettingsWindow.close();
		mapSettingsWindow = new Stage();
		mapSettingsWindow.setTitle(Preferences.getLabel("mapSettingsWindowTitle"));
		
		TextField numberOfPixelsField = new TextField(String.valueOf(Preferences.getPixelsInCol()));
		TextField radiusField = new TextField(String.valueOf(Preferences.getInterpolationRadius()));
		
		Label errorLabel = new Label("");
		errorLabel.getStyleClass().add("errorLabel");
		
		Button okButton = new Button(Preferences.getLabel("okButton"));
		okButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				try {
					int retrievedPixelValue = Integer.parseInt(numberOfPixelsField.getText());
					double retrievedRadiusValue = Double.parseDouble(radiusField.getText());
					if(retrievedPixelValue == Preferences.getPixelsInCol() && retrievedRadiusValue == Preferences.getInterpolationRadius()) {
						mapSettingsWindow.close();
						mapSettingsWindow = null;
						return;
					}
					
					if(retrievedPixelValue > 0)
						Preferences.setPixelsInCol(retrievedPixelValue);
					else
						throw new IllegalArgumentException(Preferences.getLabel("negativeNumber"));
					
					if(retrievedRadiusValue > 0.0)
						Preferences.setInterpolationRadius(retrievedRadiusValue);
					else
						throw new IllegalArgumentException(Preferences.getLabel("negativeNumber"));
					
					mapSettingsWindow.close();
					mapSettingsWindow = null;
					if(gui.getCenterPanel().getDrawingPanel().isMapEmbeded())
						gui.getCenterPanel().getDrawingPanel().getMapPanel().drawMap();
				} catch (NumberFormatException e) {
					errorLabel.setText(Preferences.getLabel("invalidExpression"));
				} catch (IllegalArgumentException e) {
					errorLabel.setText(e.getMessage());
				}
			}
		});
		
		Button applyButton = new Button(Preferences.getLabel("applyButton"));
		applyButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				try {
					int retrievedPixelValue = Integer.parseInt(numberOfPixelsField.getText());
					double retrievedRadiusValue = Double.parseDouble(radiusField.getText());
					if(retrievedPixelValue == Preferences.getPixelsInCol() && retrievedRadiusValue == Preferences.getInterpolationRadius()) 
						return;
					
					if(retrievedPixelValue > 0)
						Preferences.setPixelsInCol(retrievedPixelValue);
					else
						throw new IllegalArgumentException(Preferences.getLabel("negativeNumber"));
					
					if(retrievedRadiusValue > 0.0)
						Preferences.setInterpolationRadius(retrievedRadiusValue);
					else
						throw new IllegalArgumentException(Preferences.getLabel("negativeNumber"));
					
					if(gui.getCenterPanel().getDrawingPanel().isMapEmbeded())
						gui.getCenterPanel().getDrawingPanel().getMapPanel().drawMap();
				} catch (NumberFormatException e) {
					errorLabel.setText(Preferences.getLabel("invalidExpression"));
				} catch (IllegalArgumentException e) {
					errorLabel.setText(e.getMessage());
				}
			}
		});
		
		Button cancelButton = new Button(Preferences.getLabel("cancelButton"));
		cancelButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				mapSettingsWindow.close();
				mapSettingsWindow = null;
			}
		});
		
		VBox left = new VBox(new HBox(new Label(Preferences.getLabel("pixelsInColLabel")+":")), new HBox(new Label(Preferences.getLabel("interpolationRadiusLabel")+":")));
		VBox right = new VBox(new HBox(numberOfPixelsField), new HBox(radiusField));
		
		left.getStyleClass().add("tableVbox");
		right.getStyleClass().add("tableVbox");
		
		HBox hboxTop = new HBox(left, right);
		
		HBox hboxCenter = new HBox(errorLabel);
		hboxCenter.setAlignment(Pos.CENTER);
		
		HBox hboxBottom = new HBox();
		hboxBottom.getChildren().addAll(okButton, cancelButton, applyButton);
		hboxBottom.setAlignment(Pos.CENTER);
		
		BorderPane root = new BorderPane();
		root.setTop(hboxTop);
		root.setCenter(hboxCenter);
		root.setBottom(hboxBottom);
		
		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("/Stylesheets/optionWindowStylesheet.css").toExternalForm());
		
		mapSettingsWindow.setScene(scene);
		mapSettingsWindow.setResizable(false);
		mapSettingsWindow.show();
	}

	public static Stage getMapSettingsWindow() {
		return mapSettingsWindow;
	}

}
