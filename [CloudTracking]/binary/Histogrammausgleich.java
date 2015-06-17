package binary;

import ij.process.ImageProcessor;

public class Histogrammausgleich {

	public void histAusgleich(ImageProcessor proc) {
		int w = proc.getWidth();
		int h = proc.getHeight();
		int anzahlPixel = w * h; 
		int möglicheWerte = 256; 
	
		
		int[] RGBpicture = proc.getHistogram();
		for (int j = 1; j < RGBpicture.length; j++) {
			RGBpicture[j] = RGBpicture[j-1] + RGBpicture[j];
		}
	

		for (int v = 0; v < h; v++) {
			for (int u = 0; u < w; u++) {
				int a = proc.getPixel(u, v);
				int b = RGBpicture[a] * (möglicheWerte-1) / anzahlPixel;
				proc.putPixel(u, v, b);
			}
		}
	}
}