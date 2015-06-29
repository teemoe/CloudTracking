package binary;

import ij.process.ImageProcessor;

public class HistAusgleich {

	public static ImageProcessor run(ImageProcessor picture) {
		int w = picture.getWidth();
		int h = picture.getHeight();
		int numberOfPixel = w * h;
		int n = 256; // moegliche Werte

		// eigentlicher Ausgleich
		int[] hist = picture.getHistogram();
		for (int i = 1; i < hist.length; i++) {
			hist[i] = hist[i - 1] + hist[i];
		}

		// Neues Bild
		for (int j = 0; j < h; j++) {
			for (int k = 0; k < w; k++) {
				int a = (int)picture.getPixelValue(k, j);
				int b = hist[a] * (n - 1) / numberOfPixel;

				picture.putPixel(k, j, b);
			}
		}

		return picture;
	}
}
