package pl.edu.pw.fizyka.pojava.BozekKlis.application.GUI;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;

import pl.edu.pw.fizyka.pojava.BozekKlis.application.Preferences;
import pl.edu.pw.fizyka.pojava.BozekKlis.application.EventHandlers.ChangeLanguageHandler;
import pl.edu.pw.fizyka.pojava.BozekKlis.application.EventHandlers.ColorSelectionHandler;
import pl.edu.pw.fizyka.pojava.BozekKlis.application.EventHandlers.DeleteSelectionHandler;
import pl.edu.pw.fizyka.pojava.BozekKlis.application.EventHandlers.ExitProgramHandler;
import pl.edu.pw.fizyka.pojava.BozekKlis.application.EventHandlers.LineWidthChooserHandler;
import pl.edu.pw.fizyka.pojava.BozekKlis.application.EventHandlers.RestoreSelectionHandler;

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
				exitItem.setOnAction(new ExitProgramHandler());
			fileMenu.getItems().addAll(openFileItem, new SeparatorMenuItem(), exitItem);
		
		editMenu = new Menu(Preferences.getLabel("edit"));
			MenuItem selectionUndoItem = new MenuItem(Preferences.getLabel("undoSelection"));
				selectionUndoItem.setOnAction(new DeleteSelectionHandler(gui));
			MenuItem selectionRedoItem = new MenuItem(Preferences.getLabel("redoSelection"));
				selectionRedoItem.setOnAction(new RestoreSelectionHandler(gui));
			editMenu.getItems().addAll(selectionUndoItem, selectionRedoItem);
		
		optionsMenu = new Menu(Preferences.getLabel("options"));
			MenuItem selectionColorItem = new MenuItem(Preferences.getLabel("selectionColor"));
				selectionColorItem.setOnAction(new ColorSelectionHandler(gui));
			MenuItem selectionWidthItem = new MenuItem(Preferences.getLabel("selectionWidth"));
				selectionWidthItem.setOnAction(new LineWidthChooserHandler(gui));
			
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
