package pl.edu.pw.fizyka.pojava.BozekKlis.application.GUI;

import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import pl.edu.pw.fizyka.pojava.BozekKlis.application.Preferences;
import pl.edu.pw.fizyka.pojava.BozekKlis.application.EventHandlers.ContourLineWidthHandler;
import pl.edu.pw.fizyka.pojava.BozekKlis.application.EventHandlers.MapSettingsHandler;
import pl.edu.pw.fizyka.pojava.BozekKlis.application.EventHandlers.SetCurrentContourHandler;
import pl.edu.pw.fizyka.pojava.BozekKlis.application.EventHandlers.WindowMaximizeHandler;
import pl.edu.pw.fizyka.pojava.BozekKlis.application.EventHandlers.WindowResizedHandler;

//contains all interface classes
public class GUI{
	Stage mainWindow;
	Scene scene;
	
	MenuBarClass menuBarClass;
	CenterPanel centerPanel;
	BottomPanel bottomPanel;
	
	boolean resizeEventManaged;
	
	public GUI(Stage stage) {
		mainWindow = stage;
		resizeEventManaged = true;
		
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
		
		if(!Preferences.loadConfigFiles())
			return;
		
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
		
		WindowResizedHandler resizeHandler = new WindowResizedHandler(this);
		mainWindow.widthProperty().addListener(resizeHandler);
		mainWindow.heightProperty().addListener(resizeHandler);
		
		mainWindow.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent arg0) {
				if(ContourLineWidthHandler.getOptionsWindow() != null)
					ContourLineWidthHandler.getOptionsWindow().close();
				if(SetCurrentContourHandler.getContourWindow() != null)
					SetCurrentContourHandler.getContourWindow().close();
				if(MapSettingsHandler.getMapSettingsWindow() != null)
					MapSettingsHandler.getMapSettingsWindow().close();
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

	public boolean isResizeEventManaged() {
		return resizeEventManaged;
	}

	public void setResizeEventManaged(boolean resizeEventManaged) {
		this.resizeEventManaged = resizeEventManaged;
	}

	public BottomPanel getBottomPanel() {
		return bottomPanel;
	}
	public Scene getScene() {
		return scene;
	}
}
