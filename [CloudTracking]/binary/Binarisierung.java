package binary;

import ij.ImagePlus;
import ij.process.ColorProcessor;
import ij.process.ImageProcessor;

import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;

public class Binarisierung {

	/**
	 * Erzeugen eines Bin‰rbildes mit true = weiﬂ --> Wolke und false = schwarz
	 * --> Himmel
	 * 
	 * @param HSBArray
	 * @return
	 */
	public static ImageProcessor binaryPicture(ImageProcessor proc) {

		Image image = proc.createImage();

		// Cast von Image in BufferedImage
		BufferedImage img = (BufferedImage) image;

		// Erzeugen des ColorArray
		Color[][] colorImage = new Color[img.getWidth()][img.getHeight()];

		// Umwandlung des Image in ein ColorArray
		for (int i = 0; i < colorImage.length; i++) {
			for (int j = 0; j < colorImage[i].length; j++) {
				int colorAsInt = img.getRGB(i, j);
				int red = (colorAsInt & 0x00ff0000) >> 16;
				int green = (colorAsInt & 0x0000ff00) >> 8;
				int blue = colorAsInt & 0x000000ff;

				colorImage[i][j] = new Color(red, green, blue);
			}
		}

		float[][][] HSBArray = RGBToHSB.pixelArray(colorImage);

		
		
		/*Histogrammausgleich
		ImageProcessor HistProcessor = new ColorProcessor(HSBArray.length, HSBArray[0].length);
		for ( int x = 0; x < HSBArray.length; x++){
			for(int y = 0; y < HSBArray[0].length; y ++){
			
				HistProcessor.setf(x, y, HSBArray[x][y][0]);
			}
		}
		
		new ImagePlus("Brightness", HistProcessor.convertToByteProcessor()).show();
		
		HistProcessor = HistAusgleich.run(HistProcessor);
		
		for ( int x = 0; x < HSBArray.length; x++){
			for(int y = 0; y < HSBArray[0].length; y ++){
			 
				HSBArray[x][y][0] = HistProcessor.getf(x, y);
			}
		}
		
		//Ausgabe des Histogrammausgeliches auf Brightness
		new ImagePlus("HistBrightness", HistProcessor.convertToByteProcessor()).show();
		*/
		
		
		// Binarisierung
		for (int i = 0; i < HSBArray.length; i++) {
			for (int j = 0; j < HSBArray[i].length; j++) {

				// Testen auf nicht Himmel (Wolke)
				
				//Mit Histogrammausgleich
				//if (HSBArray[i][j][1] < 0.3 && HSBArray[i][j][0] < 200) {
				
				if (HSBArray[i][j][1] < 0.2){
					
					//System.out.println("weiss");
					proc.setf(i, j, -1);
				} else {
					//System.out.println("schwarz");
					proc.setf(i, j, 0);
				}
			}
		}

		return proc;
	}
}
