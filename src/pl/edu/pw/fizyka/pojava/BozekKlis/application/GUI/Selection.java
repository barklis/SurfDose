package pl.edu.pw.fizyka.pojava.BozekKlis.application.GUI;

import java.util.ArrayList;
import java.util.List;

import pl.edu.pw.fizyka.pojava.BozekKlis.application.Preferences;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Selection {
	static List<Selection> selectionList = new ArrayList<Selection>();
	static List<Selection> deletedSelectionList = new ArrayList<Selection>();
	static Selection currentSelection;
	static double canvasWidth;
	static double canvasHeight;
	
	double x1, x2, y1, y2;
	double selectionLineWidth;
	Color selectionColor;
	
	boolean exist = false;
	
	public Selection() {
		x1 = 0;
		x2 = 0;
		y1 = 0;
		y2 = 0;
		currentSelection = null;
		this.selectionColor = Preferences.getSelectionColor();
		this.selectionLineWidth = Preferences.getSelectionLineWidth();
	}
	
	public static void removeLast() {
		if(selectionList.size() > 0) {
			deletedSelectionList.add(selectionList.get(selectionList.size()-1));
			selectionList.remove(selectionList.size()-1);
		}
		if(currentSelection != null)
			currentSelection = null;
	}
	
	public static void restoreSelection() {
		if(deletedSelectionList.size() > 0) {
			selectionList.add(deletedSelectionList.get(deletedSelectionList.size()-1));
			deletedSelectionList.remove(deletedSelectionList.size()-1);
		}
	}
	
	public void drawOnCanvas(GraphicsContext gContext, double currentCanvasWidth, double currentCanvasHeight) {
		if(!exist)
			return;
		gContext.setStroke(selectionColor);
		gContext.setLineWidth(selectionLineWidth);
		gContext.strokeRect(x1*currentCanvasWidth, y1*currentCanvasHeight, (x2-x1)*currentCanvasWidth, (y2-y1)*currentCanvasHeight);
	}
	
	public void drawCurrentOnCanvas(GraphicsContext gContext, double currentCanvasWidth, double currentCanvasHeight) {
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
		
		gContext.setStroke(selectionColor);
		gContext.setLineWidth(selectionLineWidth);
		gContext.strokeRect(newX1*currentCanvasWidth, newY1*currentCanvasHeight, (newX2-newX1)*currentCanvasWidth, (newY2-newY1)*currentCanvasHeight);
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
		}
	}

	public static void initSelection(double x, double y, double canvasWidth, double canvasHeight) {
		deletedSelectionList.clear();
		currentSelection = new Selection();
		Selection.canvasHeight = canvasHeight;
		Selection.canvasWidth = canvasWidth;
		
		currentSelection.setFirstCorner(x, y);
	}
	
	public boolean isExist() {
		return exist;
	}

	public void setExist(boolean exist) {
		this.exist = exist;
	}

	public static void setGlobalLineWidth(Double value) {
		for(Selection x : selectionList)
			x.selectionLineWidth = value;
	}

	public static void setCurrentSelectionColor(Color newColor) {
		currentSelection.selectionColor = newColor;
	}

	public static void setCurrentSelectionLineWidth(Double value) {
		currentSelection.selectionLineWidth = value;
	}
}
