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

public class SetScaleGridsHandler implements EventHandler<ActionEvent> {
	
	static Stage scaleGridWindow = null;
	
	@Override
	public void handle(ActionEvent event) {
		if(scaleGridWindow != null)
			scaleGridWindow.close();
		scaleGridWindow = new Stage();
		scaleGridWindow.setTitle(Preferences.getLabel("scaleGridWindowTitle"));
		
		TextField scaleField = new TextField(String.valueOf(GUI.instance().getScalePanel().getGrids()));
		
		Button okButton = new Button(Preferences.getLabel("okButton"));
		Button applyButton = new Button(Preferences.getLabel("applyButton"));
		Button cancelButton = new Button(Preferences.getLabel("cancelButton"));
		
		cancelButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				scaleGridWindow.close();
				scaleGridWindow = null;
			}
		});
		
		Label errorLabel = new Label("");
		errorLabel.getStyleClass().add("errorLabel");
		
		HBox hboxTop = new HBox();
		hboxTop.getChildren().add(new VBox(new Label(Preferences.getLabel("scaleGridWindowLabel") + ": ")));
		hboxTop.getChildren().add(scaleField);
		
		HBox hboxBottom = new HBox();
		hboxBottom.getChildren().addAll(okButton, cancelButton, applyButton);
		hboxBottom.setAlignment(Pos.CENTER);
		
		HBox hboxCenter = new HBox(errorLabel);
		hboxCenter.setAlignment(Pos.CENTER);
		
		okButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				try {
					int recievedScale = Integer.parseInt(scaleField.getText());
					if(recievedScale < 0)
						throw new IndexOutOfBoundsException();
					
					GUI.instance().getScalePanel().setGridsAndReload(recievedScale);
					scaleGridWindow.close();
					scaleGridWindow = null;
				} catch(NumberFormatException e) {
					errorLabel.setText(Preferences.getLabel("invalidScale"));
				} catch(IndexOutOfBoundsException e) {
					errorLabel.setText(Preferences.getLabel("valueOutOfBounds"));
				}
			}
		});
		
		applyButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				try {
					int recievedScale = Integer.parseInt(scaleField.getText());
					if(recievedScale < 0)
						throw new IndexOutOfBoundsException();
					
					GUI.instance().getScalePanel().setGridsAndReload(recievedScale);
				} catch(NumberFormatException e) {
					errorLabel.setText(Preferences.getLabel("invalidScale"));
				} catch(IndexOutOfBoundsException e) {
					errorLabel.setText(Preferences.getLabel("valueOutOfBounds"));
				}
			}
		});
		
		BorderPane root = new BorderPane();
		root.setTop(hboxTop);
		root.setCenter(hboxCenter);
		root.setBottom(hboxBottom);
		
		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("/Stylesheets/optionWindowStylesheet.css").toExternalForm());
		
		scaleGridWindow.setScene(scene);
		scaleGridWindow.show();
	}

	public static Stage getScaleGridWindow() {
		return scaleGridWindow;
	}
	
}