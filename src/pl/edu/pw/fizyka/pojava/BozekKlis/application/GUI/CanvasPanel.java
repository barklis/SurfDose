package pl.edu.pw.fizyka.pojava.BozekKlis.application.GUI;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import pl.edu.pw.fizyka.pojava.BozekKlis.application.Preferences;
import pl.edu.pw.fizyka.pojava.BozekKlis.application.DicomDataModule.Contour;
import pl.edu.pw.fizyka.pojava.BozekKlis.application.DicomDataModule.DcmData;

public class CanvasPanel extends Canvas {
	GUI gui;
	int pixelSize;
	
	double containerWidth;
	double containerHeight;

	public CanvasPanel(GUI gui, double width, double height) {
		this.gui = gui;
		setHeight(height);
		setWidth(width);
	}
	
public void drawFrame() {
		
		if(!DcmData.isDoseLoaded())
			return;
		
		int currentFrame = gui.getCenterPanel().getDrawingPanel().getCurrentFrame();
		GraphicsContext gc = getGraphicsContext2D();
		gc.setFill(Color.WHITE);
		gc.fillRect(0, 0, DcmData.getNumberOfCols()*pixelSize, DcmData.getNumberOfRows()*pixelSize);
		
		for(int r = 0; r < DcmData.getNumberOfRows(); r++) {
			for(int c = 0; c < DcmData.getNumberOfCols(); c++) {
				gc.setFill(
					DcmData.getDcmFrames().get(currentFrame).getDoseColorScale(
						DcmData.getDcmFrames().get(currentFrame).getDoseData()[c][r].getValue()
					)
				);
				gc.fillRect(c*pixelSize, r*pixelSize, pixelSize, pixelSize);
			}
		}
		
		if(!DcmData.isContourLoaded())
			return;
		
		gc.setStroke(Preferences.getContourLineColor());
		gc.setLineWidth(Preferences.getContourLineWidth());
		gc.beginPath();
		for(Contour contour : DcmData.getDcmFrames().get(currentFrame).getContours()) {
			gc.moveTo((contour.getData().get(0).getX()-DcmData.getX0())*pixelSize/DcmData.getColsPixelSpacing(), (contour.getData().get(0).getY()-DcmData.getY0())*pixelSize/DcmData.getRowsPixelSpacing());
			for(int i = 1; i < contour.getNumberOfPoints(); i++) {
				gc.lineTo((contour.getData().get(i).getX()-DcmData.getX0())*pixelSize/DcmData.getColsPixelSpacing(), (contour.getData().get(i).getY()-DcmData.getY0())*pixelSize/DcmData.getRowsPixelSpacing());
			}
			gc.lineTo((contour.getData().get(contour.getNumberOfPoints()-1).getX()-DcmData.getX0())*pixelSize/DcmData.getColsPixelSpacing(), (contour.getData().get(contour.getNumberOfPoints()-1).getY()-DcmData.getY0())*pixelSize/DcmData.getColsPixelSpacing());
			gc.stroke();
		}
		gc.closePath();
	}

	public void setPixelSize() {
		double containerRatio = containerHeight/containerWidth;
		double dataRatio = (double)(DcmData.getNumberOfRows())/DcmData.getNumberOfCols();
		if(dataRatio > containerRatio)
			pixelSize = (int) (containerHeight/ DcmData.getNumberOfRows());
		else	
			pixelSize = (int) (containerWidth/ DcmData.getNumberOfCols());
		setWidth(pixelSize*DcmData.getNumberOfCols());
		setHeight(pixelSize*DcmData.getNumberOfRows());
	}

	public void setContainerSize() {
		containerWidth = gui.getScene().getWidth();
		containerHeight = gui.getScene().getHeight()-gui.getBottomPanel().getHeight()-gui.getMenuBarClass().getHeight();
		if(DcmData.isDoseLoaded())
			setPixelSize();
	}
}
