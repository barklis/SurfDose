package application.GUI;

import java.util.ArrayList;
import java.util.List;

import application.Preferences;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Selection {
	static List<Selection> selectionList = new ArrayList<Selection>();
	static List<Selection> deletedSelectionList = new ArrayList<Selection>();
	static Selection currentSelection;
	Color selectionColor;
	
	double x1 = 0;
	double x2 = 0;
	double y1 = 0;
	double y2 = 0;
	
	static double canvasWidth;
	static double canvasHeight;
	
	boolean exist = false;
	
	public Selection() {
		currentSelection = null;
		this.selectionColor = Preferences.getSelectionColor();
	}
	
	public static void removeLast() {
		if(selectionList.size() > 0) {
			deletedSelectionList.add(selectionList.get(selectionList.size()-1));
			selectionList.remove(selectionList.size()-1);
		}
	}
	
	public static void restoreSelection() {
		if(deletedSelectionList.size() > 0) {
			selectionList.add(deletedSelectionList.get(deletedSelectionList.size()-1));
			deletedSelectionList.remove(deletedSelectionList.size()-1);
		}
	}
	
	
	public boolean isExist() {
		return exist;
	}

	public void setExist(boolean exist) {
		this.exist = exist;
	}
	
	public void drawOnCanvas(GraphicsContext gc, double currentCanvasWidth, double currentCanvasHeight) {
		if(!exist)
			return;
		gc.setStroke(selectionColor);
		gc.strokeRect(x1*currentCanvasWidth, y1*currentCanvasHeight, (x2-x1)*currentCanvasWidth, (y2-y1)*currentCanvasHeight);
	}
	
	public void drawCurrentOnCanvas(GraphicsContext gc, double currentCanvasWidth, double currentCanvasHeight) {
		double newX1 = x1;
		double newX2 = x2;
		double newY1 = y1;
		double newY2 = y2;
		
		if(x2 < x1) {
			newX1 = x2;
			newX2 = x1;
		}
		if(y2 < y1) {
			newY1 = y2;
			newY2 = y1;
		}
		
		gc.setStroke(selectionColor);
		gc.strokeRect(newX1*currentCanvasWidth, newY1*currentCanvasHeight, (newX2-newX1)*currentCanvasWidth, (newY2-newY1)*currentCanvasHeight);
	}

	public static List<Selection> getSelectionList() {
		return selectionList;
	}
	
	public void setFirstCorner(double x, double y) {
		this.x1 = x/canvasWidth;
		this.y1 = y/canvasHeight;
	}
	
	public void setSecondCorner(double x, double y) {
		exist = true;
		this.x2 = x/canvasWidth;
		this.y2 = y/canvasHeight;
	}
	
	public static Selection getCurrentSelection() {
		return currentSelection;
	}
	public void finalizeSelection() {
		if(currentSelection != null) {
			if(x2 < x1) {
				double tmp = x1;
				x1 = x2;
				x2 = tmp;
			}
			if(y2 < y1) {
				double tmp = y1;
				y1 = y2;
				y2 = tmp;
			}
			selectionList.add(currentSelection);
			currentSelection = null;
		}
		deletedSelectionList.clear();
	}

	public static void initSelection(double x, double y, double canvasWidth, double canvasHeight) {
		currentSelection = new Selection();
		Selection.canvasHeight = canvasHeight;
		Selection.canvasWidth = canvasWidth;
		
		currentSelection.setFirstCorner(x, y);
	}
}
