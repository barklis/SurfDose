package GUI;

import EventHandlers.CalculateDoseHandler;
import EventHandlers.ChangeLanguageHandler;
import EventHandlers.ContourSettingsHandler;
import EventHandlers.ExitProgramHandler;
import EventHandlers.ExportCentersOfContoursHandler;
import EventHandlers.ExportSurfaceDataHandler;
import EventHandlers.GitBartekHandler;
import EventHandlers.GitLukaszHandler;
import EventHandlers.MapSettingsHandler;
import EventHandlers.OpenCenterCoordsFile;
import EventHandlers.OpenRTdoseFileHandler;
import EventHandlers.OpenRTplanFileHandler;
import EventHandlers.OpenRTstucturFileHandler;
import EventHandlers.PlanDataSettingsHandler;
import EventHandlers.ResetDataHandler;
import EventHandlers.SetCurrentContourHandler;
import EventHandlers.SetScaleGridsHandler;
import EventHandlers.ShowChartHandler;
import EventHandlers.ShowDocumentationHandler;
import EventHandlers.ShowLoadedDataHandler;
import EventHandlers.ShowMapHandler;
import EventHandlers.VectorDirectionHandler;
import application.Preferences;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;

//Menu Bar
public class MenuBarClass extends MenuBar {
	
	private Menu fileMenu;
	private Menu editMenu;
	private Menu optionsMenu;
	private Menu helpMenu;
	
	private CheckMenuItem showFilesContentItem;
	private CheckMenuItem showHistogramItem;
	private CheckMenuItem showMapItem;
	
	public MenuBarClass() {
		
		fileMenu = new Menu(Preferences.getLabel("file"));
			MenuItem openRTstructurFileItem = new MenuItem(Preferences.getLabel("openRTstructurFile"));
				openRTstructurFileItem.setOnAction(new OpenRTstucturFileHandler());
			MenuItem openRTdoseFileItem = new MenuItem(Preferences.getLabel("openRTdoseFile"));
				openRTdoseFileItem.setOnAction(new OpenRTdoseFileHandler());
			MenuItem openRTplanFileItem = new MenuItem(Preferences.getLabel("openRTplanFile"));
				openRTplanFileItem.setOnAction(new OpenRTplanFileHandler());
			MenuItem exportSurfaceDataItem = new MenuItem(Preferences.getLabel("exportSurfaceData"));
				exportSurfaceDataItem.setOnAction(new ExportSurfaceDataHandler());
			MenuItem exportContourCentersItem = new MenuItem(Preferences.getLabel("exportContourCenterData"));
				exportContourCentersItem.setOnAction(new ExportCentersOfContoursHandler());
			MenuItem importContourCentersItem = new MenuItem(Preferences.getLabel("importContourCenterData"));
				importContourCentersItem.setOnAction(new OpenCenterCoordsFile());
			MenuItem exitItem = new MenuItem(Preferences.getLabel("exit"));
				exitItem.setOnAction(new ExitProgramHandler());
				
			fileMenu.getItems().addAll(
				openRTstructurFileItem, openRTdoseFileItem, openRTplanFileItem, new SeparatorMenuItem(),
				exportSurfaceDataItem, exportContourCentersItem, importContourCentersItem, new SeparatorMenuItem(), exitItem
			);
		
		editMenu = new Menu(Preferences.getLabel("programMenu"));
			showFilesContentItem = new CheckMenuItem(Preferences.getLabel("showFilesContentItem"));
			showHistogramItem = new CheckMenuItem(Preferences.getLabel("showHistogramItem"));
			showMapItem = new CheckMenuItem(Preferences.getLabel("showMapItem"));
				showFilesContentItem.setOnAction(new ShowLoadedDataHandler());
				showHistogramItem.setOnAction(new ShowChartHandler());
				showMapItem.setOnAction(new ShowMapHandler());
			
			MenuItem calculateDoseItem = new MenuItem(Preferences.getLabel("calculateDoseMenu"));
				calculateDoseItem.setOnAction(new CalculateDoseHandler());
		
			editMenu.getItems().addAll(showFilesContentItem, showHistogramItem, showMapItem, new SeparatorMenuItem(), calculateDoseItem);
		
		optionsMenu = new Menu(Preferences.getLabel("options"));
			MenuItem contourSettingsItem = new MenuItem(Preferences.getLabel("contourSettings"));
			contourSettingsItem.setOnAction(new ContourSettingsHandler());
			MenuItem mapSettingsItem = new MenuItem(Preferences.getLabel("pixelRowsNumber"));
				mapSettingsItem.setOnAction(new MapSettingsHandler());
			MenuItem PlanDataSettingsItem = new MenuItem(Preferences.getLabel("planDataSettings"));
				PlanDataSettingsItem.setOnAction(new PlanDataSettingsHandler());
			MenuItem setCurrentContour = new MenuItem(Preferences.getLabel("currentContour"));
				setCurrentContour.setOnAction(new SetCurrentContourHandler());
			MenuItem setVectorDirectionItem = new MenuItem(Preferences.getLabel("vectorDirection"));
				setVectorDirectionItem.setOnAction(new VectorDirectionHandler());
			MenuItem setScaleItem = new MenuItem(Preferences.getLabel("doseScaleResolution"));
				setScaleItem.setOnAction(new SetScaleGridsHandler());
			MenuItem resetDataItem = new MenuItem(Preferences.getLabel("resetData"));
				resetDataItem.setOnAction(new ResetDataHandler());
				
			Menu languageMenu = new Menu(Preferences.getLabel("language"));
				MenuItem polishLanguageItem = new MenuItem(Preferences.getLabel("PolishLanguage"));
					polishLanguageItem.setOnAction(new ChangeLanguageHandler("Polish"));
				MenuItem englishLanguageItem = new MenuItem(Preferences.getLabel("EnglishLanguage"));
					englishLanguageItem.setOnAction(new ChangeLanguageHandler("English"));
				languageMenu.getItems().addAll(polishLanguageItem, englishLanguageItem);
				
				optionsMenu.getItems().addAll(
					contourSettingsItem, mapSettingsItem, PlanDataSettingsItem, setCurrentContour, setVectorDirectionItem,
					setScaleItem, new SeparatorMenuItem(), resetDataItem, new SeparatorMenuItem(), languageMenu
				);
		
		helpMenu = new Menu(Preferences.getLabel("help"));
			MenuItem documentationItem = new MenuItem(Preferences.getLabel("showDocumentation"));
				documentationItem.setOnAction(new ShowDocumentationHandler());
			MenuItem gitBartekItem = new MenuItem(Preferences.getLabel("gitBartek"));
				gitBartekItem.setOnAction(new GitBartekHandler());
			MenuItem gitLukaszItem = new MenuItem(Preferences.getLabel("gitLukasz"));
				gitLukaszItem.setOnAction(new GitLukaszHandler());
			helpMenu.getItems().addAll(documentationItem, new SeparatorMenuItem(), gitBartekItem, gitLukaszItem);
		
		getMenus().addAll(fileMenu, editMenu, optionsMenu, helpMenu);
	}
	
	public void reset() {
		showFilesContentItem.setSelected(false);
		showHistogramItem.setSelected(false);
		showMapItem.setSelected(false);
	}

	public CheckMenuItem getShowFilesContentItem() {
		return showFilesContentItem;
	}

	public CheckMenuItem getShowHistogramItem() {
		return showHistogramItem;
	}

	public CheckMenuItem getShowMapItem() {
		return showMapItem;
	}
	
}
