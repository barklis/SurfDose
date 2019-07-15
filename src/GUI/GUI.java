package GUI;

import EventHandlers.ContourSettingsHandler;
import EventHandlers.MapSettingsHandler;
import EventHandlers.PlanDataSettingsHandler;
import EventHandlers.SetCurrentContourHandler;
import EventHandlers.VectorDirectionHandler;
import EventHandlers.WindowMaximizeHandler;
import EventHandlers.WindowResizedHandler;
import application.Preferences;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

//contains all interface classes
public class GUI{
	private static GUI ins = null;
	
	private Stage mainWindow;
	private Scene scene;
	
	private MenuBarClass menuBarClass;
	private CenterPanel centerPanel;
	private BottomPanel bottomPanel;
	
	private boolean resizeEventManaged;
	
	private GUI(Stage stage) {
		mainWindow = stage;
		resizeEventManaged = true;
		
		init();
	}
	
	public static GUI instance(Stage stage) {
		if(ins == null)
			ins = new GUI(stage);
		return ins;
	}
	
	public static GUI instance() {
		return ins;
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
		
		menuBarClass = new MenuBarClass();
		centerPanel = new CenterPanel(this);
		bottomPanel = new BottomPanel(this);
		
		BorderPane root = new BorderPane();
		root.setTop(menuBarClass);
		root.setCenter(centerPanel);
		root.setBottom(bottomPanel);
		
		scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("/Stylesheets/stylesheet.css").toExternalForm()); 
		
		mainWindow.maximizedProperty().addListener(new WindowMaximizeHandler());
		
		WindowResizedHandler resizeHandler = new WindowResizedHandler(this);
		mainWindow.widthProperty().addListener(resizeHandler);
		mainWindow.heightProperty().addListener(resizeHandler);
		
		mainWindow.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent arg0) {
				if(ContourSettingsHandler.getWindow() != null)
					ContourSettingsHandler.getWindow().close();
				if(SetCurrentContourHandler.getWindow() != null)
					SetCurrentContourHandler.getWindow().close();
				if(MapSettingsHandler.getWindow() != null)
					MapSettingsHandler.getWindow().close();
				if(VectorDirectionHandler.getWindow() != null)
					VectorDirectionHandler.getWindow().close();
				if(PlanDataSettingsHandler.getWindow() != null)
					PlanDataSettingsHandler.getWindow().close();
			}
		});
		
		mainWindow.setScene(scene);
		mainWindow.setTitle("Surfdose");
		mainWindow.show();
	}
	
	public ButtonType showConfirmationDialog(String title, String content) {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle(title);
		alert.setContentText(content);
		alert.setHeaderText("");
		return alert.showAndWait().get();
	}
	
	public void showInformationDialog(String title, String content) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle(title);
		alert.setContentText(content);
		alert.setHeaderText("");
		alert.showAndWait();
	}

	public void showErrorDialog(String title, String content) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle(title);
		alert.setContentText(content);
		alert.setHeaderText("");
		alert.showAndWait();
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
	
	public CanvasPanel getCanvasPanel() {
		return centerPanel.drawingPanel.canvasPanel;
	}
	
	public MapPanel getMapPanel() {
		return centerPanel.drawingPanel.mapPanel;
	}
	
	public ChartPanel getChartPanel() {
		return centerPanel.drawingPanel.chartPanel;
	}
	
	public DrawingPanel getDrawingPanel() {
		return centerPanel.drawingPanel;
	}
	
	public ScalePanel getScalePanel() {
		return centerPanel.scalePanel;
	}
}
