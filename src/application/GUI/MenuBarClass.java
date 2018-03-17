package application.GUI;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;

import application.Preferences;
import application.EventHandlers.ChangeLanguageHandler;

public class MenuBarClass extends MenuBar {
	GUI gui;
	
	Menu fileMenu;
	Menu editMenu;
	Menu optionsMenu;
	Menu helpMenu;
	
	public MenuBarClass(GUI gui) {
		this.gui = gui;
		
		fileMenu = new Menu(Preferences.getLabel("file"));
			MenuItem openFileItem = new MenuItem(Preferences.getLabel("openFile"));
			MenuItem exitItem = new MenuItem(Preferences.getLabel("exit"));
			fileMenu.getItems().addAll(openFileItem, new SeparatorMenuItem(), exitItem);
		
		editMenu = new Menu(Preferences.getLabel("edit"));
			MenuItem selectionUndoItem = new MenuItem(Preferences.getLabel("undoSelection"));
			editMenu.getItems().addAll(selectionUndoItem);
		
		optionsMenu = new Menu(Preferences.getLabel("options"));
			MenuItem selectionColorItem = new MenuItem(Preferences.getLabel("selectionColor"));
			MenuItem selectionWidthItem = new MenuItem(Preferences.getLabel("selectionWidth"));
			
			Menu languageMenu = new Menu(Preferences.getLabel("language"));
				MenuItem polishLanguageItem = new MenuItem(Preferences.getLabel("PolishLanguage"));
					polishLanguageItem.setOnAction(new ChangeLanguageHandler("Polish", gui));
				MenuItem englishLanguageItem = new MenuItem(Preferences.getLabel("EnglishLanguage"));
					englishLanguageItem.setOnAction(new ChangeLanguageHandler("English", gui));
				languageMenu.getItems().addAll(polishLanguageItem, englishLanguageItem);
				
			optionsMenu.getItems().addAll(selectionColorItem, selectionWidthItem, new SeparatorMenuItem(), languageMenu);
		
		helpMenu = new Menu(Preferences.getLabel("help"));
			MenuItem documantationItem = new MenuItem(Preferences.getLabel("showDocumentation"));
			helpMenu.getItems().add(documantationItem);
		
		getMenus().addAll(fileMenu, editMenu, optionsMenu, helpMenu);
	}
}
