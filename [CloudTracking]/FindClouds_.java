import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import cloud.Cloud;
import cloud.CloudPair;
import ij.ImagePlus;
import ij.ImageStack;
import ij.measure.ResultsTable;
import ij.plugin.FolderOpener;
import ij.plugin.PlugIn;
import ij.plugin.filter.Analyzer;
import ij.process.ImageProcessor;


public class FindClouds_ implements PlugIn {

	private FolderOpener opener;
	private ImagePlus clouds;
	
	private ImageProcessor ref;
	private ImageProcessor corr;
	
	private String dirWorkFolder = "../combinationI/";

	private ArrayList<Cloud> referenceList;
	private ArrayList<Cloud> correspondenceList;
	private ArrayList<CloudPair> pairList;

	
	private Process p;
	
	
	
	@Override
	public void run(String arg0) {
		
		ImageStack cloudStack = loadImagesIntoStack();
		
		
		ref = cloudStack.getProcessor(1);
		corr = cloudStack.getProcessor(2);
		
		referenceList = findBorders(ref);
		correspondenceList = findBorders(corr);
		
		ResultsTable rt = Analyzer.getResultsTable();
		if (rt == null) {
		        rt = new ResultsTable();
		        Analyzer.setResultsTable(rt);
		}

		
		for( int i = 0; i < referenceList.size(); i++){
		
		Cloud tmp = referenceList.get(i);
			
		rt.incrementCounter();
		
		rt.addValue("CloudHeight", tmp.getHeight());
		rt.addValue("CloudWidth", tmp.getWidth());
		rt.addValue("Center_X", tmp.getX());
		rt.addValue("Center_Y", tmp.getY());
		
		}
		
		for( int i = 0; i < correspondenceList.size(); i++){
			
		Cloud tmp = correspondenceList.get(i);
			
		rt.incrementCounter();
		
		rt.addValue("CloudHeight", tmp.getHeight());
		rt.addValue("CloudWidth", tmp.getWidth());
		rt.addValue("Center_X", tmp.getX());
		rt.addValue("Center_Y", tmp.getY());
		
		}
		
//		ref.autoThreshold();
//		
//		byte[] pixels = (byte[]) ref.getPixels();
//		
//		for(int i = 0; i < pixels.length; i++){
//			
//			rt.incrementCounter();
//			rt.addValue("Value", pixels[i]);
//		}
		
		rt.show("Results");
		
		
	}

	
	private ImageStack loadImagesIntoStack(){
		

		opener = new FolderOpener();
		clouds = new ImagePlus();
		clouds = opener.openFolder(dirWorkFolder);
		
		return clouds.getImageStack();
			
	}
	
	private ArrayList<Cloud> findBorders(ImageProcessor binary){
		
	
		binary.autoThreshold();
		
		ArrayList<Cloud> cloudList = new ArrayList<Cloud>();
		
		int white = -1;
		int width = binary.getWidth();
		int height = binary.getHeight();
	// (short[], short[], float[] or int[]) 
		int [] pixels = (int[]) binary.getPixels();
		
		int length = pixels.length;
		
		boolean [] marker = new boolean[pixels.length];
		
		
		for(int y = 0; y < height; y++ ){
			for(int x = 0; x < width; x++){
				if(pixels[y*width + x] == white && marker[y*width + x] == false){
					
					int[] xy={x,y};
					
					int centerX = 0, centerY = 0, cloudWidth = 0, cloudHeight = 0;
					int minX = x, minY = y, maxX = x, maxY = y;
					
					Queue <int[]> fifo = new LinkedList();
					fifo.add(xy);
					
					while(fifo.isEmpty() == false){
						
						int [] coordXY = fifo.poll();
						
						if(coordXY[1] * width + coordXY[0] - 1 > 0){
						
							if(pixels[coordXY[1] * width + coordXY[0] - 1] == white && marker[coordXY[1] * width + coordXY[0] - 1]==false){
							
								int[] xyL={coordXY[0] - 1,coordXY[1]};
								fifo.add(xyL);
								if(xyL[0]<minX)
									minX=xyL[0];
								
								marker[coordXY[1] * width + coordXY[0]] = true;
								}
							
							
						}
						
						if(coordXY[1] * width + coordXY[0] + 1 < length){
						
							if(pixels[coordXY[1] * width + coordXY[0] + 1]== white && marker[coordXY[1] * width + coordXY[0] + 1]==false){
							
								int[] xyR={coordXY[0] + 1, coordXY[1]};
								fifo.add(xyR);
								if(xyR[0]>maxX)
									maxX=xyR[0];
								
								marker[coordXY[1] * width + coordXY[0]] = true;
								}
						}
						
						if((coordXY[1] - 1) * width + coordXY[0] > 0){
						
							if(pixels[(coordXY[1]-1) * width + coordXY[0]] == white && marker[(coordXY[1]-1) * width + coordXY[0]] == false){
							
								int[] xyO={coordXY[0],coordXY[1]-1};
								fifo.add(xyO);
								if(xyO[1] * width < minY)
									minY=xyO[1];
							
								marker[coordXY[1] * width + coordXY[0]] = true;
								}
						}
						
						
						if((coordXY[1] + 1) * width + coordXY[0] < length){
							
							if(pixels[(coordXY[1]+1) * width + coordXY[0]] == white && marker[(coordXY[1]+1) * width + coordXY[0]]==false){
								
								int[] xyU={coordXY[0],coordXY[1]+1};
								fifo.add(xyU);
								if(xyU[1]* width >maxY)
									maxY=xyU[1];
								
								marker[coordXY[1] * width + coordXY[0]] = true;
								}
						}
					
					
					}
					
					centerX=minX + (maxX-minX)/2;
					centerY= minY + (maxY-minY)/2;
					cloudWidth=maxX-minX;
					cloudHeight=maxY-minY;
					
					Cloud tmp = new Cloud(centerX,centerY,cloudWidth,cloudHeight,0);

					if(tmp.getHeight() != 0 && tmp.getWidth() != 0){
						cloudList.add(tmp);
					}
				}
			}
		}
		
		return cloudList;
	}


	
}
