package GUI;

import EventHandlers.CalculateDoseHandler;
import EventHandlers.ChangeLanguageHandler;
import EventHandlers.ContourLineColorHandler;
import EventHandlers.ContourLineWidthHandler;
import EventHandlers.ExitProgramHandler;
import EventHandlers.MapSettingsHandler;
import EventHandlers.OpenRTdoseFileHandler;
import EventHandlers.OpenRTplanFileHandler;
import EventHandlers.OpenRTstucturFileHandler;
import EventHandlers.ResetDataHandler;
import EventHandlers.SetCurrentContourHandler;
import EventHandlers.SetScaleGridsHandler;
import EventHandlers.ShowDocumentationHandler;
import EventHandlers.ShowHistogramHandler;
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
	GUI gui;
	
	Menu fileMenu;
	Menu editMenu;
	Menu optionsMenu;
	Menu helpMenu;
	
	CheckMenuItem showFilesContentItem;
	CheckMenuItem showHistogramItem;
	CheckMenuItem showMapItem;
	
	public MenuBarClass(GUI gui) {
		this.gui = gui;
		
		fileMenu = new Menu(Preferences.getLabel("file"));
			MenuItem openRTstructurFileItem = new MenuItem(Preferences.getLabel("openRTstructurFile"));
				openRTstructurFileItem.setOnAction(new OpenRTstucturFileHandler(gui));
			MenuItem openRTdoseFileItem = new MenuItem(Preferences.getLabel("openRTdoseFile"));
				openRTdoseFileItem.setOnAction(new OpenRTdoseFileHandler(gui));
			MenuItem openRTplanFileItem = new MenuItem(Preferences.getLabel("openRTplanFile"));
				openRTplanFileItem.setOnAction(new OpenRTplanFileHandler(gui));
			MenuItem exitItem = new MenuItem(Preferences.getLabel("exit"));
				exitItem.setOnAction(new ExitProgramHandler());
				
			fileMenu.getItems().addAll(openRTstructurFileItem, openRTdoseFileItem, openRTplanFileItem, new SeparatorMenuItem(), exitItem);
		
		editMenu = new Menu(Preferences.getLabel("programMenu"));
			showFilesContentItem = new CheckMenuItem(Preferences.getLabel("showFilesContentItem"));
			showHistogramItem = new CheckMenuItem(Preferences.getLabel("showHistogramItem"));
			showMapItem = new CheckMenuItem(Preferences.getLabel("showMapItem"));
				showFilesContentItem.setOnAction(new ShowLoadedDataHandler(gui));
				showHistogramItem.setOnAction(new ShowHistogramHandler(gui));
				showMapItem.setOnAction(new ShowMapHandler(gui));
			
			MenuItem calculateDoseItem = new MenuItem(Preferences.getLabel("calculateDoseMenu"));
				calculateDoseItem.setOnAction(new CalculateDoseHandler(gui));
		
			editMenu.getItems().addAll(showFilesContentItem, showHistogramItem, showMapItem, new SeparatorMenuItem(), calculateDoseItem);
		
		optionsMenu = new Menu(Preferences.getLabel("options"));
			MenuItem contourColorItem = new MenuItem(Preferences.getLabel("contourLineColor"));
				contourColorItem.setOnAction(new ContourLineColorHandler(gui));
			MenuItem contourLineWidthItem = new MenuItem(Preferences.getLabel("contourLineWidth"));
				contourLineWidthItem.setOnAction(new ContourLineWidthHandler(gui));
			MenuItem mapSettingsItem = new MenuItem(Preferences.getLabel("pixelRowsNumber"));
				mapSettingsItem.setOnAction(new MapSettingsHandler(gui));
			MenuItem setCurrentContour = new MenuItem(Preferences.getLabel("currentContour"));
				setCurrentContour.setOnAction(new SetCurrentContourHandler(gui));
			MenuItem setVectorDirectionItem = new MenuItem(Preferences.getLabel("vectorDirection"));
				setVectorDirectionItem.setOnAction(new VectorDirectionHandler(gui));
			MenuItem setScaleItem = new MenuItem(Preferences.getLabel("doseScaleResolution"));
				setScaleItem.setOnAction(new SetScaleGridsHandler(gui));
			MenuItem resetDataItem = new MenuItem(Preferences.getLabel("resetData"));
				resetDataItem.setOnAction(new ResetDataHandler(gui));
				
			Menu languageMenu = new Menu(Preferences.getLabel("language"));
				MenuItem polishLanguageItem = new MenuItem(Preferences.getLabel("PolishLanguage"));
					polishLanguageItem.setOnAction(new ChangeLanguageHandler("Polish", gui));
				MenuItem englishLanguageItem = new MenuItem(Preferences.getLabel("EnglishLanguage"));
					englishLanguageItem.setOnAction(new ChangeLanguageHandler("English", gui));
				languageMenu.getItems().addAll(polishLanguageItem, englishLanguageItem);
				
				optionsMenu.getItems().addAll(contourColorItem, contourLineWidthItem, mapSettingsItem, setCurrentContour,
						setVectorDirectionItem, setScaleItem, new SeparatorMenuItem(), resetDataItem, new SeparatorMenuItem(), languageMenu);
		
		helpMenu = new Menu(Preferences.getLabel("help"));
			MenuItem documantationItem = new MenuItem(Preferences.getLabel("showDocumentation"));
				documantationItem.setOnAction(new ShowDocumentationHandler());
			helpMenu.getItems().add(documantationItem);
		
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
