package binary;

import ij.process.ImageProcessor;

public class HistAusgleich {

	public void run(ImageProcessor picture) {
		int w = picture.getWidth();
		int h = picture.getHeight();
		int numberOfPixel = w * h;
		int n = 256; //moegliche Werte

		// compute the cumulative histogram:
		int[] hist = picture.getHistogram();
		for (int i = 1; i < hist.length; i++) {
			hist[i] = hist[i - 1] + hist[i];
		}

		// equalize the image:
		for (int j = 0; j < h; j++) {
			for (int k = 0; k < w; k++) {
				int a = picture.getPixel(k, j);
				int b = hist[a] * (n - 1) / numberOfPixel;
				picture.putPixel(k, j, b);
			}
		}
	}

}
