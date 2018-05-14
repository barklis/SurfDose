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
import pl.edu.pw.fizyka.pojava.BozekKlis.application.EventHandlers.OpenRTdoseFileHandler;
import pl.edu.pw.fizyka.pojava.BozekKlis.application.EventHandlers.OpenRTstucturFileHandler;
import pl.edu.pw.fizyka.pojava.BozekKlis.application.EventHandlers.ShowHistogramHandler;
import pl.edu.pw.fizyka.pojava.BozekKlis.application.EventHandlers.ShowLoadedDataHandler;

public class MenuBarClass extends MenuBar {
	GUI gui;
	
	Menu fileMenu;
	Menu editMenu;
	Menu optionsMenu;
	Menu helpMenu;
	
	public MenuBarClass(GUI gui) {
		this.gui = gui;
		
		fileMenu = new Menu(Preferences.getLabel("file"));
			MenuItem openRTstructurFileItem = new MenuItem(Preferences.getLabel("openRTstructurFile"));
				openRTstructurFileItem.setOnAction(new OpenRTstucturFileHandler(gui));
			MenuItem openRTdoseFileItem = new MenuItem(Preferences.getLabel("openRTdoseFile"));
				openRTdoseFileItem.setOnAction(new OpenRTdoseFileHandler(gui));
			MenuItem exitItem = new MenuItem(Preferences.getLabel("exit"));
				exitItem.setOnAction(new ExitProgramHandler());
				
			fileMenu.getItems().addAll(openRTstructurFileItem, openRTdoseFileItem, new SeparatorMenuItem(), exitItem);
		
		editMenu = new Menu(Preferences.getLabel("programMenu"));
			CheckMenuItem showFilesContentItem = new CheckMenuItem(Preferences.getLabel("showFilesContentItem"));
			CheckMenuItem showHistogramItem = new CheckMenuItem(Preferences.getLabel("showHistogramItem"));
				showFilesContentItem.setOnAction(new ShowLoadedDataHandler(gui, showFilesContentItem, showHistogramItem));
				showHistogramItem.setOnAction(new ShowHistogramHandler(gui, showFilesContentItem, showHistogramItem));
			
			MenuItem calculateDoseItem = new MenuItem(Preferences.getLabel("calculateDoseMenu"));
				calculateDoseItem.setOnAction(new CalculateDoseHandler(gui));
		
			editMenu.getItems().addAll(showFilesContentItem, showHistogramItem, new SeparatorMenuItem(), calculateDoseItem);
		
		optionsMenu = new Menu(Preferences.getLabel("options"));
			MenuItem contourColorItem = new MenuItem(Preferences.getLabel("contourLineColor"));
				contourColorItem.setOnAction(new ContourLineColorHandler(gui));
			MenuItem contourLineWidthItem = new MenuItem(Preferences.getLabel("contourLineWidth"));
				contourLineWidthItem.setOnAction(new ContourLineWidthHandler(gui));
			
			Menu languageMenu = new Menu(Preferences.getLabel("language"));
				MenuItem polishLanguageItem = new MenuItem(Preferences.getLabel("PolishLanguage"));
					polishLanguageItem.setOnAction(new ChangeLanguageHandler("Polish", gui));
				MenuItem englishLanguageItem = new MenuItem(Preferences.getLabel("EnglishLanguage"));
					englishLanguageItem.setOnAction(new ChangeLanguageHandler("English", gui));
				languageMenu.getItems().addAll(polishLanguageItem, englishLanguageItem);
				
				optionsMenu.getItems().addAll(contourColorItem, contourLineWidthItem, new SeparatorMenuItem(), languageMenu);
		
		helpMenu = new Menu(Preferences.getLabel("help"));
			MenuItem documantationItem = new MenuItem(Preferences.getLabel("showDocumentation"));
			helpMenu.getItems().add(documantationItem);
		
		getMenus().addAll(fileMenu, editMenu, optionsMenu, helpMenu);
	}
}
