package pl.edu.pw.fizyka.pojava.BozekKlis.application.EventHandlers;

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
import pl.edu.pw.fizyka.pojava.BozekKlis.application.Preferences;
import pl.edu.pw.fizyka.pojava.BozekKlis.application.DicomDataModule.DcmData;
import pl.edu.pw.fizyka.pojava.BozekKlis.application.GUI.GUI;

public class SetCurrentContourHandler implements EventHandler<ActionEvent> {
	
	static Stage contourWindow = null;
	GUI gui;
	
	public SetCurrentContourHandler(GUI gui) {
		this.gui = gui;
	}
	
	@Override
	public void handle(ActionEvent event) {
		if(contourWindow != null)
			contourWindow.close();
		contourWindow = new Stage();
		contourWindow.setTitle(Preferences.getLabel("currentContourWindowTitle"));
		
		TextField contourIdField = new TextField(String.valueOf(DcmData.getCurrentContourId()));
		
		Button okButton = new Button(Preferences.getLabel("okButton"));
		Button nextButton = new Button("->");
		Button prevButton = new Button("<-");
		
		Button cancelButton = new Button(Preferences.getLabel("cancelButton"));
		cancelButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				contourWindow.close();
				contourWindow = null;
			}
		});
		
		Label errorLabel = new Label("");
		errorLabel.getStyleClass().add("errorLabel");
		
		HBox hboxTop = new HBox();
		hboxTop.getChildren().add(new VBox(new Label(Preferences.getLabel("currentContourWindowLabel") + " (0 - " + DcmData.getMaxContourId() + ") ")));
		hboxTop.getChildren().add(contourIdField);
		
		HBox hboxBottom = new HBox();
		hboxBottom.getChildren().addAll(okButton, cancelButton, prevButton, nextButton);
		hboxBottom.setAlignment(Pos.CENTER);
		
		HBox hboxCenter = new HBox(errorLabel);
		hboxCenter.setAlignment(Pos.CENTER);
		
		okButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				try {
					int recievedId = Integer.parseInt(contourIdField.getText());
					if(recievedId < 0 || recievedId > DcmData.getMaxContourId())
						throw new IndexOutOfBoundsException();
					DcmData.setCurrentContourId(recievedId);
					gui.getCenterPanel().getDrawingPanel().redraw();
					contourWindow.close();
					contourWindow = null;
				} catch(NumberFormatException e) {
					errorLabel.setText(Preferences.getLabel("invalidCurrentContourId"));
				} catch(IndexOutOfBoundsException e) {
					errorLabel.setText(Preferences.getLabel("valueOutOfBounds"));
				}
			}
		});
		
		nextButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				int current = DcmData.getCurrentContourId();
				if(current+1 <= DcmData.getMaxContourId()) {
					errorLabel.setText("");
					contourIdField.setText(String.valueOf(current+1));
					DcmData.setCurrentContourId(current+1);
					gui.getCenterPanel().getDrawingPanel().redraw();
				}
			}
		});
		
		prevButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				int current = DcmData.getCurrentContourId();
				if(current-1 >= 0) {
					errorLabel.setText("");
					contourIdField.setText(String.valueOf(current-1));
					DcmData.setCurrentContourId(current-1);
					gui.getCenterPanel().getDrawingPanel().redraw();
				}
			}
		});
		
		BorderPane root = new BorderPane();
		root.setTop(hboxTop);
		root.setCenter(hboxCenter);
		root.setBottom(hboxBottom);
		
		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("/pl/edu/pw/fizyka/pojava/BozekKlis/Resources/optionWindowStylesheet.css").toExternalForm());
		
		contourWindow.setScene(scene);
		contourWindow.show();
	}

	public static Stage getContourWindow() {
		return contourWindow;
	}
	
}
