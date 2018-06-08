package pl.edu.pw.fizyka.pojava.BozekKlis.application.GUI;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import pl.edu.pw.fizyka.pojava.BozekKlis.application.Preferences;
import pl.edu.pw.fizyka.pojava.BozekKlis.application.DicomDataModule.Contour;
import pl.edu.pw.fizyka.pojava.BozekKlis.application.DicomDataModule.DcmData;
import pl.edu.pw.fizyka.pojava.BozekKlis.application.DicomDataModule.DcmFrame;
import pl.edu.pw.fizyka.pojava.BozekKlis.application.DicomDataModule.DcmManager;
import pl.edu.pw.fizyka.pojava.BozekKlis.application.DicomDataModule.Point;

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
		// drawing dose
		if(!DcmData.isDoseLoaded())
			return;
		
		if(!gui.isResizeEventManaged()) {
			setContainerSize();
			gui.setResizeEventManaged(true);
		}
		
		int currentFrame = gui.getCenterPanel().getDrawingPanel().getCurrentFrame();
		GraphicsContext gc = getGraphicsContext2D();
		gc.setFill(Color.WHITE);
		gc.fillRect(0, 0, DcmData.getNumberOfCols()*pixelSize, DcmData.getNumberOfRows()*pixelSize);
		
		for(int r = 0; r < DcmData.getNumberOfRows(); r++) {
			for(int c = 0; c < DcmData.getNumberOfCols(); c++) {
				gc.setFill(
					DcmManager.getDoseColorScale(
						DcmData.getDcmFrames().get(currentFrame).getSumDoseData()[c][r].getValue(),
						DcmData.getMaxSumDoseValue()
					)
				);
				gc.fillRect(c*pixelSize, r*pixelSize, pixelSize, pixelSize);
			}
		}
		
		// drawing contours
		if(!DcmData.isContourLoaded())
			return;
		
		gc.setStroke(Preferences.getContourLineColor());
		gc.setLineWidth(Preferences.getContourLineWidth());
		for(Contour contour : DcmData.getDcmFrames().get(currentFrame).getContours()) {
			gc.beginPath();
			gc.moveTo(getLocalX(contour.getData().get(0).getX()), getLocalY(contour.getData().get(0).getY()));
			for(int i = 1; i < contour.getNumberOfPoints(); i++) {
				gc.lineTo(getLocalX(contour.getData().get(i).getX()), getLocalY(contour.getData().get(i).getY()));
			}
			gc.lineTo(getLocalX(contour.getData().get(contour.getNumberOfPoints()-1).getX()), getLocalY(contour.getData().get(contour.getNumberOfPoints()-1).getY()));
			gc.stroke();
			gc.closePath();
		}
		
		// drawing isocentre
		Point[] ds = { new Point(-12.4, 15.8), new Point(DcmData.getIsocenterPosition()[0], DcmData.getIsocenterPosition()[1])};
		double[] z0 = {-5.9, DcmData.getIsocenterPosition()[2]};
		for(int i = 0; i < ds.length; ++i) {
			DcmFrame frame = DcmData.getDcmFrames().get(0);
			for(DcmFrame f : DcmData.getDcmFrames()) {
				if(Math.abs(f.getContours().get(0).getZ() - z0[i]) < Math.abs(frame.getContours().get(0).getZ()-z0[i])) {
					frame = f;
				}
			}
			if(DcmData.getDcmFrames().get(currentFrame).getContours().get(0).getZ() == frame.getContours().get(0).getZ())
				gc.setFill(Color.DARKVIOLET);
			else
				gc.setFill(Color.GRAY);
			gc.fillOval(getLocalX(ds[i].getX()), getLocalY(ds[i].getY()), 2*pixelSize, 2*pixelSize);
		}
		
		// drawing starting vector
		Contour c = DcmData.getDcmFrames().get(currentFrame).getContourById(2);
		if(c.getData().size() != 0) {
			gc.setFill(Color.BLUEVIOLET);
			gc.fillOval(getLocalX(c.getCenterX())-2, getLocalY(c.getCenterY())-2, 4, 4);
			gc.setStroke(Color.BLUEVIOLET);
			gc.strokeLine(getLocalX(c.getCenterX()), getLocalY(c.getCenterY()),
					getLocalX(c.getCenterX()) + DcmData.getuVector()[0]*containerWidth,
					getLocalY(c.getCenterY()) + DcmData.getuVector()[1]*containerHeight);
		}
		
	}

	public double getLocalX(double x) {
		return (x-DcmData.getX0())*pixelSize/DcmData.getColsPixelSpacing();
	}
	
	public double getLocalY(double y) {
		return (y-DcmData.getY0())*pixelSize/DcmData.getRowsPixelSpacing();
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
