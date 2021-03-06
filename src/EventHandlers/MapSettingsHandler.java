package EventHandlers;

import GUI.GUI;
import GUI.PointersPanel;
import application.Preferences;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MapSettingsHandler implements EventHandler<ActionEvent> {

	private static Stage window = null;

	@Override
	public void handle(ActionEvent event) {
		if(window != null)
			window.close();
		window = new Stage();
		window.setTitle(Preferences.getLabel("mapSettingsWindowTitle"));
		
		TextField pixelsInColField = new TextField(String.valueOf(Preferences.getPixelsInCol()));
		TextField pixelsInRowField = new TextField(String.valueOf(Preferences.getPixelsInRow()));
		TextField radiusField = new TextField(String.valueOf(Preferences.getInterpolationRadius()));
		
		CheckBox rowPointerCheckbox = new CheckBox(Preferences.getLabel("rowPointerCheckboxLabel"));
		if(Preferences.isRowPointerEnabled())
			rowPointerCheckbox.setSelected(true);
		
		Label errorLabel = new Label("");
		errorLabel.getStyleClass().add("errorLabel");
		
		Button okButton = new Button(Preferences.getLabel("okButton"));
		okButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				try {
					int retrievedPixelInColValue = Integer.parseInt(pixelsInColField.getText());
					int retrievedPixelInRowValue = Integer.parseInt(pixelsInRowField.getText());
					double retrievedRadiusValue = Double.parseDouble(radiusField.getText());
					
					if(retrievedPixelInColValue == Preferences.getPixelsInCol() && retrievedPixelInRowValue == Preferences.getPixelsInRow() && retrievedRadiusValue == Preferences.getInterpolationRadius()) {
						window.close();
						window = null;
						return;
					}
					
					if(retrievedPixelInColValue > 0)
						Preferences.setPixelsInCol(retrievedPixelInColValue);
					else
						throw new IllegalArgumentException(Preferences.getLabel("negativeNumber"));
					
					if(retrievedPixelInRowValue > 0)
						Preferences.setPixelsInRow(retrievedPixelInRowValue);
					else
						throw new IllegalArgumentException(Preferences.getLabel("negativeNumber"));
					
					if(retrievedRadiusValue > 0.0)
						Preferences.setInterpolationRadius(retrievedRadiusValue);
					else
						throw new IllegalArgumentException(Preferences.getLabel("negativeNumber"));
					
					window.close();
					window = null;
					if(GUI.instance().getDrawingPanel().isMapEmbeded())
						GUI.instance().getMapPanel().drawMap();
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
					int retrievedPixelInColValue = Integer.parseInt(pixelsInColField.getText());
					int retrievedPixelInRowValue = Integer.parseInt(pixelsInRowField.getText());
					double retrievedRadiusValue = Double.parseDouble(radiusField.getText());
					
					if(retrievedPixelInColValue == Preferences.getPixelsInCol() && retrievedPixelInRowValue == Preferences.getPixelsInRow() && retrievedRadiusValue == Preferences.getInterpolationRadius())
						return;
					
					if(retrievedPixelInColValue > 0)
						Preferences.setPixelsInCol(retrievedPixelInColValue);
					else
						throw new IllegalArgumentException(Preferences.getLabel("negativeNumber"));
					
					if(retrievedPixelInRowValue > 0)
						Preferences.setPixelsInRow(retrievedPixelInRowValue);
					else
						throw new IllegalArgumentException(Preferences.getLabel("negativeNumber"));
					
					if(retrievedRadiusValue > 0.0)
						Preferences.setInterpolationRadius(retrievedRadiusValue);
					else
						throw new IllegalArgumentException(Preferences.getLabel("negativeNumber"));
					
					if(GUI.instance().getDrawingPanel().isMapEmbeded())
						GUI.instance().getMapPanel().drawMap();
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
				window.close();
				window = null;
			}
		});
		
		rowPointerCheckbox.selectedProperty().addListener(new ChangeListener<Boolean>() {
			public void changed(ObservableValue<? extends Boolean> ov, Boolean oldVal, Boolean newVal) {
				if(newVal) {
					Preferences.setRowPointerEnabled(true);
					if(GUI.instance().getDrawingPanel().isMapEmbeded()) {
						GUI.instance().getBottomPanel().setExtendedZCoordLabel();
						GUI.instance().getMapPanel().getPointersPanel().drawLines();
					}
				}
				else {
					Preferences.setRowPointerEnabled(false);
					if(GUI.instance().getDrawingPanel().isMapEmbeded()) {
						GUI.instance().getMapPanel().getPointersPanel().eraseLines();
						
						PointersPanel pPanel = GUI.instance().getMapPanel().getPointersPanel();
						GUI.instance().getBottomPanel().showMapMode(pPanel.getStartingFrame(), pPanel.getEndingFrame());
					}
				}
			}
		});
		
		VBox left = new VBox(new HBox(new Label(Preferences.getLabel("pixelsInColLabel")+":")),
							 new HBox(new Label(Preferences.getLabel("pixelsInRowLabel")+":")),
							 new HBox(new Label(Preferences.getLabel("interpolationRadiusLabel")+":")),
							 new HBox(rowPointerCheckbox));
		
		VBox right = new VBox(new HBox(pixelsInColField), 
							  new HBox(pixelsInRowField),
							  new HBox(radiusField));
		
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
		
		window.setScene(scene);
		window.setResizable(false);
		window.show();
	}

	public static Stage getWindow() {
		return window;
	}

}
