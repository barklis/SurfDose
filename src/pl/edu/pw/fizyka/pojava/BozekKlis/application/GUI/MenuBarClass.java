package pl.edu.pw.fizyka.pojava.BozekKlis.application.GUI;

import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;

import pl.edu.pw.fizyka.pojava.BozekKlis.application.Preferences;
import pl.edu.pw.fizyka.pojava.BozekKlis.application.EventHandlers.CalculateDoseHandler;
import pl.edu.pw.fizyka.pojava.BozekKlis.application.EventHandlers.ChangeLanguageHandler;
import pl.edu.pw.fizyka.pojava.BozekKlis.application.EventHandlers.ContourLineColorHandler;
import pl.edu.pw.fizyka.pojava.BozekKlis.application.EventHandlers.ContourLineWidthHandler;
import pl.edu.pw.fizyka.pojava.BozekKlis.application.EventHandlers.ExitProgramHandler;
import pl.edu.pw.fizyka.pojava.BozekKlis.application.EventHandlers.MapSettingsHandler;
import pl.edu.pw.fizyka.pojava.BozekKlis.application.EventHandlers.OpenRTdoseFileHandler;
import pl.edu.pw.fizyka.pojava.BozekKlis.application.EventHandlers.OpenRTplanFileHandler;
import pl.edu.pw.fizyka.pojava.BozekKlis.application.EventHandlers.OpenRTstucturFileHandler;
import pl.edu.pw.fizyka.pojava.BozekKlis.application.EventHandlers.ResetDataHandler;
import pl.edu.pw.fizyka.pojava.BozekKlis.application.EventHandlers.SetCurrentContourHandler;
import pl.edu.pw.fizyka.pojava.BozekKlis.application.EventHandlers.ShowDocumentationHandler;
import pl.edu.pw.fizyka.pojava.BozekKlis.application.EventHandlers.ShowHistogramHandler;
import pl.edu.pw.fizyka.pojava.BozekKlis.application.EventHandlers.ShowLoadedDataHandler;
import pl.edu.pw.fizyka.pojava.BozekKlis.application.EventHandlers.ShowMapHandler;

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
			MenuItem resetDataItem = new MenuItem(Preferences.getLabel("resetData"));
				resetDataItem.setOnAction(new ResetDataHandler(gui));
				
			Menu languageMenu = new Menu(Preferences.getLabel("language"));
				MenuItem polishLanguageItem = new MenuItem(Preferences.getLabel("PolishLanguage"));
					polishLanguageItem.setOnAction(new ChangeLanguageHandler("Polish", gui));
				MenuItem englishLanguageItem = new MenuItem(Preferences.getLabel("EnglishLanguage"));
					englishLanguageItem.setOnAction(new ChangeLanguageHandler("English", gui));
				languageMenu.getItems().addAll(polishLanguageItem, englishLanguageItem);
				
				optionsMenu.getItems().addAll(contourColorItem, contourLineWidthItem, mapSettingsItem, setCurrentContour, new SeparatorMenuItem(), resetDataItem, new SeparatorMenuItem(), languageMenu);
		
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
