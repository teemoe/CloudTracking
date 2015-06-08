package cloud;

import ij.process.ImageProcessor;

import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;

public class Binarisierung {

	/**
	 * Erzeugen eines Binärbildes mit true = weiß --> Wolke und false = schwarz
	 * --> Himmel
	 * 
	 * @param HSBArray
	 * @return
	 */
	public static void binaryPicture(ImageProcessor proc) {

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

				colorImage[j][i] = new Color(red, green, blue);
			}
		}
		
		float[][][] HSBArray = RGBToHSB.pixelArray(colorImage);

		
		//Binarisierung
		for (int i = 0; i < HSBArray.length; i++) {
			for (int j = 0; j < HSBArray[i].length; j++) {

				// Testen auf nicht Himmel (Wolke)
				if (HSBArray[i][j][1] < 0.3) {
					
					proc.putPixel(i, j, 255);
				} else {

					proc.putPixel(i, j, 0);
				}
			}
		}
	}
}
