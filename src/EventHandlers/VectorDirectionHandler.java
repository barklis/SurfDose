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
	static Stage window = null;
	GUI gui;
	
	public VectorDirectionHandler(GUI gui) {
		this.gui = gui;
	}
	
	@Override
	public void handle(ActionEvent event) {
		if(window != null)
			window.close();
		window = new Stage();
		window.setTitle(Preferences.getLabel("vectorAngleWindowTitle"));
		
		double currentFirstAngle = DcmManager.getRelativeAngle(0, -1, DcmData.getuVector()[0], DcmData.getuVector()[1])*180/Math.PI;
		double currentSecondAngle = DcmData.getAngularWidth()*180/Math.PI;
		TextField firstAngleField = new TextField(String.valueOf(currentFirstAngle));
		TextField secondAngleField = new TextField(String.valueOf(currentSecondAngle));
		
		Button okButton = new Button(Preferences.getLabel("okButton"));
		Button applyButton = new Button(Preferences.getLabel("applyButton"));
		Button cancelButton = new Button(Preferences.getLabel("cancelButton"));
		
		Label errorLabel = new Label("");
		errorLabel.getStyleClass().add("errorLabel");
		
		cancelButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				window.close();
				window = null;
			}
		});
		
		okButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				try {
					double recievedFirstAngle = Double.parseDouble(firstAngleField.getText());
					double recievedSecondAngle = Double.parseDouble(secondAngleField.getText());
					
					if(recievedSecondAngle > 360 || recievedSecondAngle < 0)
						throw new IllegalArgumentException(Preferences.getLabel("angularWidthOutOfBounds"));
					
					recievedFirstAngle *= Math.PI/180;
					recievedSecondAngle *= Math.PI/180;
					
					double firstVectorX = Math.sin(recievedFirstAngle);
					double firstVectorY = -Math.cos(recievedFirstAngle);
					
					if(firstVectorX != DcmData.getuVector()[0] || firstVectorY != DcmData.getuVector()[1] || recievedSecondAngle != DcmData.getAngularWidth()) {
						DcmData.setuVector(firstVectorX, firstVectorY);
						DcmData.setAngularWidth(recievedSecondAngle);
						DcmData.recalculateAngle();
						gui.getCenterPanel().getDrawingPanel().redraw();
					}
					
					window.close();
					window = null;
				} catch(NumberFormatException e) {
					errorLabel.setText(Preferences.getLabel("invalidAngle"));
				} catch(IllegalArgumentException e) {
					errorLabel.setText(e.getMessage());
				}
			}
		});
		
		applyButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				try {
					double recievedFirstAngle = Double.parseDouble(firstAngleField.getText());
					double recievedSecondAngle = Double.parseDouble(secondAngleField.getText());
					
					if(recievedSecondAngle > 360 || recievedSecondAngle < 0)
						throw new IllegalArgumentException(Preferences.getLabel("angularWidthOutOfBounds"));
					
					errorLabel.setText("");
					
					recievedFirstAngle *= Math.PI/180;
					recievedSecondAngle *= Math.PI/180;
				
					DcmData.setuVector(Math.sin(recievedFirstAngle), -Math.cos(recievedFirstAngle));
					DcmData.setAngularWidth(recievedSecondAngle);
					DcmData.recalculateAngle();
					gui.getCenterPanel().getDrawingPanel().redraw();
				
				} catch(NumberFormatException e) {
					errorLabel.setText(Preferences.getLabel("invalidAngle"));
				} catch(IllegalArgumentException e) {
					errorLabel.setText(e.getMessage());
				}
			}
		});
		
		VBox left = new VBox(new HBox(new Label(Preferences.getLabel("firstAngleWindowLabel")+":")), new HBox(new Label(Preferences.getLabel("secondAngleWindowLabel")+":")));
		VBox right = new VBox(new HBox(firstAngleField), new HBox(secondAngleField));
		
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
		window.show();
	}

	public static Stage getWindow() {
		return window;
	}
	
}
