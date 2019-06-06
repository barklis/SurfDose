package GUI;

import DataModule.Contour;
import DataModule.DcmData;
import DataModule.DcmFrame;
import DataModule.DcmManager;
import DataModule.Point;
import application.Preferences;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class CanvasPanel extends Canvas {
	private int pixelSize;
	
	private double containerWidth;
	private double containerHeight;

	public CanvasPanel(double width, double height) {
		setHeight(height);
		setWidth(width);
	}
	
public void drawFrame() {
		// drawing dose
		
		if(!GUI.instance().isResizeEventManaged()) {
			setContainerSize();
			GUI.instance().setResizeEventManaged(true);
		}
		
		GUI.instance().getBottomPanel().setzCoordLabel(DcmData.getDcmFrames().get(GUI.instance().getDrawingPanel().getCurrentFrame()).getZ());
		
		GraphicsContext gc = getGraphicsContext2D();
		gc.setFill(Color.WHITE);
		gc.fillRect(0, 0, DcmData.getNumberOfCols()*pixelSize, DcmData.getNumberOfRows()*pixelSize);
		
		// drawing dose
		drawDose(gc);
		
		// drawing contours
		if(DcmData.isContourLoaded()) {
			drawContours(gc);
		}
		
		// drawing beam lines
		if(DcmData.isPlanLoaded()) {
			if(Preferences.isAxesVisible())
				drawBeamLines(gc);
		}
		
		// drawing isocentre
		if(DcmData.isPlanLoaded() && DcmData.isContourLoaded()) {
			if(Preferences.isIsocenterVisible())
				drawIsocenter(gc);
		}
		
		// drawing restraining vectors
		if(DcmData.isContourLoaded()) {
			drawVectors(gc);
		}
	}

	private void drawDose(GraphicsContext gc) {
		int currentFrame = GUI.instance().getDrawingPanel().getCurrentFrame();
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
	}
	private void drawContours(GraphicsContext gc) {
		int currentFrame = GUI.instance().getDrawingPanel().getCurrentFrame();
		gc.setLineWidth(Preferences.getContourLineWidth());
		gc.setStroke(Color.WHITE);
		
		Contour selectedContour = null;
		for(Contour contour : DcmData.getDcmFrames().get(currentFrame).getContours()) {
			if(contour.getId() == DcmData.getCurrentContourId()) {
				selectedContour = contour;
				continue;
			}
			
			if(contour.getData().size() == 0)
				return;
			
			gc.beginPath();
			gc.moveTo(getLocalX(contour.getData().get(0).getX()), getLocalY(contour.getData().get(0).getY()));
			for(int i = 1; i < contour.getNumberOfPoints(); i++) {
				gc.lineTo(getLocalX(contour.getData().get(i).getX()), getLocalY(contour.getData().get(i).getY()));
			}
			gc.lineTo(getLocalX(contour.getData().get(contour.getNumberOfPoints()-1).getX()), getLocalY(contour.getData().get(contour.getNumberOfPoints()-1).getY()));
			gc.stroke();
			gc.closePath();
		}
		if(selectedContour != null) {
			gc.setStroke(Preferences.getActiveContourLineColor());
			gc.setLineWidth(Preferences.getActiveContourLineWidth());
			gc.beginPath();
			gc.moveTo(getLocalX(selectedContour.getData().get(0).getX()), getLocalY(selectedContour.getData().get(0).getY()));
			for(int i = 1; i < selectedContour.getNumberOfPoints(); i++) {
				gc.lineTo(getLocalX(selectedContour.getData().get(i).getX()), getLocalY(selectedContour.getData().get(i).getY()));
			}
			gc.lineTo(getLocalX(selectedContour.getData().get(selectedContour.getNumberOfPoints()-1).getX()), getLocalY(selectedContour.getData().get(selectedContour.getNumberOfPoints()-1).getY()));
			gc.stroke();
			gc.closePath();
		}
	}

	private void drawIsocenter(GraphicsContext gc) {
		int currentFrame = GUI.instance().getDrawingPanel().getCurrentFrame();
		Point isoPoint = new Point(DcmData.getIsocenterPosition()[0], DcmData.getIsocenterPosition()[1]);
		double z0 = DcmData.getIsocenterPosition()[2];
		
		DcmFrame frame = DcmData.getDcmFrames().get(0);
		for(DcmFrame f : DcmData.getDcmFrames()) {
			if(Math.abs(f.getZ() - z0) < Math.abs(frame.getZ()-z0)) {
				frame = f;
			}
		}
		if(DcmData.getDcmFrames().get(currentFrame).getZ() == frame.getZ())
			gc.setFill(Preferences.getActiveIsocenterColor());
		else
			gc.setFill(Preferences.getIsocenterColor());
		
		int dotRadius = Preferences.getIsocenterDotRadius();
		gc.fillOval(getLocalX(isoPoint.getX())-dotRadius, getLocalY(isoPoint.getY())-dotRadius, 2*dotRadius, 2*dotRadius);
	}
	
	private void drawBeamLines(GraphicsContext gc) {
		double isoX = getLocalX(DcmData.getIsocenterPosition()[0]);
		double isoY = getLocalY(DcmData.getIsocenterPosition()[1]);
		
		gc.setStroke(Preferences.getAxesColor());
		gc.setLineWidth(2);
		for(int i = 0; i < DcmData.getNumberOfBeams(); ++i) {
			gc.strokeLine(isoX, isoY, isoX + Math.sin(DcmData.getGantryAngles()[i])*containerWidth, isoY - Math.cos(DcmData.getGantryAngles()[i])*containerHeight);
		}
	}
	
	private void drawVectors(GraphicsContext gc) {
		Contour c = DcmData.getDcmFrames().get(GUI.instance().getCenterPanel().getDrawingPanel().getCurrentFrame()).getContourById(DcmData.getCurrentContourId());
		if(c.getData().size() != 0) {
			
			// center of the contour
			gc.setFill(Color.BLUEVIOLET);
			gc.fillOval(getLocalX(c.getCenterX())-2, getLocalY(c.getCenterY())-2, 4, 4);
			
			// angle vector
			double newAngle = DcmData.getAngularWidth() + DcmManager.getRelativeAngle(0, -1, DcmData.getuVector()[0], DcmData.getuVector()[1]);
			gc.setStroke(Color.BLUEVIOLET);
			gc.setLineWidth(2);
			gc.strokeLine(getLocalX(c.getCenterX()), getLocalY(c.getCenterY()),
					getLocalX(c.getCenterX()) + Math.sin(newAngle)*containerWidth,
					getLocalY(c.getCenterY()) + -Math.cos(newAngle)*containerHeight);
			
			// main vector
			gc.setStroke(Color.BROWN);
			gc.strokeLine(getLocalX(c.getCenterX()), getLocalY(c.getCenterY()),
					getLocalX(c.getCenterX()) + DcmData.getuVector()[0]*containerWidth,
					getLocalY(c.getCenterY()) + DcmData.getuVector()[1]*containerHeight);
		}
	}

	public double getLocalX(double x) {
		return (x-DcmData.getX0()+DcmData.getColsPixelSpacing()/2)*pixelSize/DcmData.getColsPixelSpacing();
	}
	
	public double getLocalY(double y) {
		return (y-DcmData.getY0()+DcmData.getRowsPixelSpacing()/2)*pixelSize/DcmData.getRowsPixelSpacing();
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
		containerWidth = GUI.instance().getScene().getWidth()-GUI.instance().getScalePanel().getWidth();
		containerHeight = GUI.instance().getScene().getHeight()-GUI.instance().getBottomPanel().getHeight()-GUI.instance().getMenuBarClass().getHeight();
		if(DcmData.isDoseLoaded())
			setPixelSize();
	}
}
