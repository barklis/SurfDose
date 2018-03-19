package pl.edu.pw.fizyka.pojava.BozekKlis.application.GUI;

import pl.edu.pw.fizyka.pojava.BozekKlis.application.Preferences;
import pl.edu.pw.fizyka.pojava.BozekKlis.application.EventHandlers.DrawSelectionListener;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;

public class SelectionPanel extends Canvas {
	
	GUI gui;
	double width, height;
	GraphicsContext gContext;
	
	
	double mouseX, mouseY;
	
	public SelectionPanel(GUI gui, double width, double height) {
		this.gui = gui;
		this.width = width;
		this.height = height;
		
		setWidth(width);
		setHeight(height);
		gContext = getGraphicsContext2D();
		
		addEventHandler(MouseEvent.MOUSE_PRESSED, new DrawSelectionListener(this));
		addEventHandler(MouseEvent.MOUSE_DRAGGED, new DrawSelectionListener(this));
        addEventHandler(MouseEvent.MOUSE_RELEASED, new DrawSelectionListener(this));
		
		draw();
	}
	
	public void draw() {
		gContext.clearRect(0, 0, width, height);
		gContext.setLineWidth(Preferences.getSelectionLineWidth());
		for(Selection selection : Selection.getSelectionList()) {
			selection.drawOnCanvas(gContext, getWidth(), getHeight());
		}
	}
	
	public void drawCurrentSelection() {
		Selection.getCurrentSelection().drawCurrentOnCanvas(gContext, getWidth(), getHeight());
	}
	
	public void resize(double width, double height) {
		this.width = width;
		this.height = height;
		setWidth(width);
		setHeight(height);
	}

}
