package EventHandlers;

import DataModule.DcmData;
import DataModule.DcmManager;
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

public class VectorDirectionHandler implements EventHandler<ActionEvent> {
	static Stage angleWindow = null;
	GUI gui;
	
	public VectorDirectionHandler(GUI gui) {
		this.gui = gui;
	}
	
	@Override
	public void handle(ActionEvent event) {
		if(angleWindow != null)
			angleWindow.close();
		angleWindow = new Stage();
		angleWindow.setTitle(Preferences.getLabel("vectorAngleWindowTitle"));
		
		double curentAngle = DcmManager.getRelativeAngle(0, -1, DcmData.getuVector()[0], DcmData.getuVector()[1])*180/Math.PI;
		TextField angleField = new TextField(String.valueOf(curentAngle));
		
		Button okButton = new Button(Preferences.getLabel("okButton"));
		Button applyButton = new Button(Preferences.getLabel("applyButton"));
		Button cancelButton = new Button(Preferences.getLabel("cancelButton"));
		
		cancelButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				angleWindow.close();
				angleWindow = null;
			}
		});
		
		Label errorLabel = new Label("");
		errorLabel.getStyleClass().add("errorLabel");
		
		HBox hboxTop = new HBox();
		hboxTop.getChildren().add(new VBox(new Label(Preferences.getLabel("vectorAngleWindowLabel")+": ")));
		hboxTop.getChildren().add(angleField);
		
		HBox hboxBottom = new HBox();
		hboxBottom.getChildren().addAll(okButton, cancelButton, applyButton);
		hboxBottom.setAlignment(Pos.CENTER);
		
		HBox hboxCenter = new HBox(errorLabel);
		hboxCenter.setAlignment(Pos.CENTER);
		
		okButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				try {
					double recievedAngle = Double.parseDouble(angleField.getText());
					recievedAngle *= Math.PI/180;
					
					double vectorX = Math.sin(recievedAngle);
					double vectorY = -Math.cos(recievedAngle);
					
					if(vectorX != DcmData.getuVector()[0] || vectorY != DcmData.getuVector()[1]) {
						DcmData.setuVector(vectorX, vectorY);
						DcmData.recalculateAngle();
						gui.getCenterPanel().getDrawingPanel().redraw();
					}
					
					angleWindow.close();
					angleWindow = null;
				} catch(NumberFormatException e) {
					errorLabel.setText(Preferences.getLabel("invalidAngle"));
				}
			}
		});
		
		applyButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				try {
				double recievedAngle = Double.parseDouble(angleField.getText());
				recievedAngle *= Math.PI/180;
				
				DcmData.setuVector(Math.sin(recievedAngle), -Math.cos(recievedAngle));
				DcmData.recalculateAngle();
				gui.getCenterPanel().getDrawingPanel().redraw();
				
				} catch(NumberFormatException e) {
					errorLabel.setText(Preferences.getLabel("invalidAngle"));
				}
			}
		});
		
		BorderPane root = new BorderPane();
		root.setTop(hboxTop);
		root.setCenter(hboxCenter);
		root.setBottom(hboxBottom);
		
		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("/Stylesheets/optionWindowStylesheet.css").toExternalForm());
		
		angleWindow.setScene(scene);
		angleWindow.show();
	}

	public static Stage getAngleWindow() {
		return angleWindow;
	}
	
}
