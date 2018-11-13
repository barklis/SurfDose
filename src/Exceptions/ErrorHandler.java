package Exceptions;

import application.Preferences;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class ErrorHandler extends Exception{
	private String message;

	public ErrorHandler(String message) {
		this.message = message;
	}
	
	public void showDialog() {
		Stage stage = new Stage();
		
		Button okButton = new Button("OK");
		okButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				stage.close();
			}
		});
		
		BorderPane root = new BorderPane();
		root.setCenter(new Label(message));
		HBox bottom = new HBox(okButton);
		bottom.setAlignment(Pos.CENTER);
		bottom.setPadding(new Insets(10, 20, 20, 20));
		root.setBottom(bottom);
		
		Scene scene = new Scene(root, 250, 120);
		stage.setScene(scene);
		stage.setTitle(Preferences.getLabel("error"));
		stage.show();
	}
	
}
