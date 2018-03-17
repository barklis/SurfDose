package application.GUI;

import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import application.Preferences;

public class GUI{
	Stage mainWindow;
	
	MenuBarClass menuBarClass;
	CenterPanel centerPanel;
	BottomPanel bottomPanel;
	
	public GUI(Stage primaryStage) {
		mainWindow = primaryStage;
		
		init();
	}
	
	public void init() {
		Preferences.loadConfigFiles();
		
		menuBarClass = new MenuBarClass(this);
		centerPanel = new CenterPanel(this);
		bottomPanel = new BottomPanel(this);
		
		BorderPane root = new BorderPane();
		root.setTop(menuBarClass);
		root.setCenter(centerPanel);
		root.setBottom(bottomPanel);
		
		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("/Resources/application.css").toExternalForm());
		
		mainWindow.close();
		mainWindow = new Stage();
		mainWindow.setScene(scene);
		mainWindow.setTitle("Analyzer");
		mainWindow.show();
	}
	
}
