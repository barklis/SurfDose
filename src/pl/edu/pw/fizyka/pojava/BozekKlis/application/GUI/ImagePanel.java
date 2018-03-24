package pl.edu.pw.fizyka.pojava.BozekKlis.application.GUI;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ImagePanel extends ImageView {
	
	GUI gui;
	
	double width, height;
	BufferedImage bufferedImage;
	
	public ImagePanel(GUI gui, double width, double height) {
		this.gui = gui;
		this.width = width;
		this.height = height;
		
		loadImage();
	}
	
	void loadImage() {
		try {
			bufferedImage = ImageIO.read(new File("src/pl/edu/pw/fizyka/pojava/BozekKlis/Resources/grafika.png"));
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		resizeImage(width, height);
	}
	
	public void resizeImage(double width, double height) {
		Image image = SwingFXUtils.toFXImage(bufferedImage, null);
		this.setImage(image);
		this.setFitWidth(width);
		this.setFitHeight(height);
		this.setPreserveRatio(true);
		this.setSmooth(true);
		this.setCache(true);
		
		double imageRatio = image.getHeight() / image.getWidth();
		double panelRatio = height / width;
		if(imageRatio < panelRatio) {
			this.width = width;
			this.height = width*imageRatio;
		}
		else {
			this.height = height;
			this.width = height/imageRatio;
		}
	}
}
