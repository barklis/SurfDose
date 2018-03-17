package application.GUI;

import javafx.scene.image.ImageView;

public class ImagePanel extends ImageView {
	
	GUI gui;
	
	public ImagePanel(GUI gui) {
		this.gui = gui;
	}
	
	/*
	Image image = SwingFXUtils.toFXImage(bufferedImage, null);
	setImage(image);
	setFitWidth(100);
	setPreserveRatio(true);
	setSmooth(true);
	setCache(true);
	*/
}
