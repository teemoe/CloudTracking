package cloud;

import ij.process.ImageProcessor;

import java.util.ArrayList;

public class Tracking {

	private ArrayList<Cloud> tracking(ArrayList<Cloud> corr,
			ImageProcessor binary) {

		// ? binary.autoThreshold();

		ArrayList<Cloud> cloudList = new ArrayList<Cloud>();

		int white = -1;
		int widthP = binary.getWidth();
		int heightP = binary.getHeight();

		byte[] pixels = (byte[]) binary.getPixels();

		// int length = pixels.length();

		// durchlaufe alle wolken
		while (corr.size() != 0) {

			Cloud c = corr.remove(cloudList.size() - 1);

			heightP = c.getHeight();
			widthP = c.getWidth();
			int x = c.getX();
			int y = c.getY();

			// spätere randpunkte
			int minX = x, minY = y, maxX = x, maxY = y;

			// laufvariablen
			int xl = x, xr = x, yu = y, yo = y;

			// teste wie lange vom mittelpunkt der alten wolke in jede richtung
			// gegangen werden kann
			while (pixels[y * widthP + xl] == white
					&& pixels[y * widthP + xl] > 0) {
				xl = xl - 1;
				minX = xl;
			}

			while (pixels[y * widthP + xr] == white
					&& pixels[y * widthP + xr] < widthP - 1) {
				xr = xr + 1;
				maxX = xr;
			}

			while (pixels[yo * widthP + x] == white
					&& pixels[y * widthP + xr] > 0) {
				yo = yo - 1;
				minY = yo;
			}

			while (pixels[yu * widthP + x] == white
					&& pixels[y * widthP + xr] < heightP - 1) {
				yu = yu + 1;
				maxY = yu;
			}

			// neue wolke
			x = (minX + maxX) / 2;
			y = (minY + maxY) / 2;
			widthP = maxX - minX;
			heightP = maxY - minY;

			Cloud cn = new Cloud(x, y, widthP, heightP);

			cloudList.add(cn);

		} // ende while

		return cloudList;

	} // ende tracking

}
