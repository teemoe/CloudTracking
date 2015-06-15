import java.awt.Color;

import ij.ImagePlus;
import ij.gui.Arrow;
import ij.gui.Line;
import ij.gui.Overlay;
import ij.gui.Roi;
import ij.plugin.filter.PlugInFilter;
import ij.process.ImageProcessor;


public class DrawLine_ implements PlugInFilter {

	@Override
	public void run(ImageProcessor ip) {
	
		int height = ip.getHeight();
		int width = ip.getWidth();
		
		int size = height*width;
		
		Overlay overlay = new Overlay();
		Roi roi = new Arrow(10, 10, 15, 15);
		System.out.println(roi.getColor());
		Color c = new Color(255,255,255);
		overlay.setStrokeColor(c);
		
		
		
		
		ImagePlus imp= new ImagePlus("Line added", ip);
		imp.setOverlay(overlay);
		
		imp.setRoi(roi, true);
		imp.show();
		
		System.out.println(roi.isVisible());
				
	}

	@Override
	public int setup(String arg0, ImagePlus arg1) {
		
		return DOES_8G;
	}

}
