package application.GUI;

import application.Preferences;
import application.EventHandlers.DrawSelectionListener;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

public class SelectionPanel extends Canvas {
	
	GUI gui;
	double width, height;
	GraphicsContext gc;
	Color selectionColor;
	
	double mouseX, mouseY;
	
	public SelectionPanel(GUI gui, double width, double height) {
		this.gui = gui;
		this.width = width;
		this.height = height;
		this.selectionColor = Preferences.getSelectionColor();
		
		setWidth(width);
		setHeight(height);
		gc = getGraphicsContext2D();
		
		addEventHandler(MouseEvent.MOUSE_PRESSED, new DrawSelectionListener(this));
		addEventHandler(MouseEvent.MOUSE_DRAGGED, new DrawSelectionListener(this));
        addEventHandler(MouseEvent.MOUSE_RELEASED, new DrawSelectionListener(this));
		
		draw();
	}
	
	public void draw() {
		gc.clearRect(0, 0, width, height);
		gc.setStroke(selectionColor);
		gc.setLineWidth(Preferences.getSelectionWidth());
		for(Selection selection : Selection.getSelectionList()) {
			selection.drawOnCanvas(gc);
		}
	}
	
	public void drawCurrentSelection() {
		Selection.getCurrentSelection().drawCurrentOnCanvas(gc);
	}
	
	public void resize(double width, double height) {
		this.width = width;
		this.height = height;
		setWidth(width);
		setHeight(height);
	}

}
