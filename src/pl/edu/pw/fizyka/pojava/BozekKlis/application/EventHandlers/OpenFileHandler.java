package pl.edu.pw.fizyka.pojava.BozekKlis.application.EventHandlers;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import com.pixelmed.dicom.AttributeList;
import com.pixelmed.dicom.DicomException;
import com.pixelmed.display.ConsumerFormatImageMaker;
import com.pixelmed.display.SourceImage;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.FileChooser;
import pl.edu.pw.fizyka.pojava.BozekKlis.application.GUI.GUI;

public class OpenFileHandler implements EventHandler<ActionEvent> {

	GUI gui;
	
	public OpenFileHandler(GUI gui) {
		this.gui = gui;
	}

	@Override
	public void handle(ActionEvent arg0) {
		FileChooser chooser = new FileChooser();
		chooser.setInitialDirectory(new File("src/pl/edu/pw/fizyka/pojava/BozekKlis/Resources/"));
		File selectedFile = null;
		selectedFile = chooser.showOpenDialog(null);
		if(selectedFile != null) {
			AttributeList list = new AttributeList();
			
			try {
				list.read(selectedFile);
			} catch (IOException | DicomException e) {
				e.printStackTrace();
			}
			
			SourceImage dcmImage = null;
			
			try {
				dcmImage = new SourceImage(list);
			} catch (DicomException e) {
				e.printStackTrace();
			}
			
			System.out.println("Width: " + dcmImage.getWidth());
			System.out.println("Height: " + dcmImage.getHeight());
			System.out.println("Frames: " + dcmImage.getNumberOfFrames());
			
			try {
				BufferedImage[] images = ConsumerFormatImageMaker.makeEightBitImages(list);
				gui.getCenterPanel().getDrawingPanel().getImagePanel().loadImage(images[0]);
			} catch (DicomException e1) {
				e1.printStackTrace();
			}
		}
	}

}
