package pl.edu.pw.fizyka.pojava.BozekKlis.application.GUI;

import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import pl.edu.pw.fizyka.pojava.BozekKlis.application.Preferences;
import pl.edu.pw.fizyka.pojava.BozekKlis.application.EventHandlers.ContourLineWidthHandler;
import pl.edu.pw.fizyka.pojava.BozekKlis.application.EventHandlers.WindowMaximizeHandler;

public class GUI{
	Stage mainWindow;
	Scene scene;
	
	MenuBarClass menuBarClass;
	CenterPanel centerPanel;
	BottomPanel bottomPanel;
	
	public GUI(Stage stage) {
		mainWindow = stage;
		
		init();
	}
	
	public void restart() {
		getCenterPanel().getDrawingPanel().setChartEmbeded(false);
		getCenterPanel().getDrawingPanel().setCanvasEmbeded(false);
		mainWindow.close();
		mainWindow = new Stage();
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
		
		scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("/pl/edu/pw/fizyka/pojava/BozekKlis/Resources/stylesheet.css").toExternalForm()); 
		
		mainWindow.maximizedProperty().addListener(new WindowMaximizeHandler(this));
		mainWindow.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent arg0) {
				if(ContourLineWidthHandler.optionsWindow != null)
					ContourLineWidthHandler.optionsWindow.close();
				System.exit(0);
			}
		});
		mainWindow.setScene(scene);
		mainWindow.setTitle("DICOM Analyzer");
		mainWindow.show();
	}
	
	public Stage getMainWindow() {
		return mainWindow;
	}

	public MenuBarClass getMenuBarClass() {
		return menuBarClass;
	}

	public CenterPanel getCenterPanel() {
		return centerPanel;
	}

	public BottomPanel getBottomPanel() {
		return bottomPanel;
	}
	public Scene getScene() {
		return scene;
	}
}
