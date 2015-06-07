import ij.ImagePlus;
import ij.ImageStack;
import ij.measure.ResultsTable;
import ij.plugin.FolderOpener;
import ij.plugin.PlugIn;
import ij.plugin.filter.Analyzer;
import ij.process.ImageProcessor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import cloud.Cloud;
import cloud.CloudPair;
import cloud.Vec2;

public class CloudTracking_ implements PlugIn {

	private FolderOpener opener;
	private ImagePlus clouds;
	
	private ImageProcessor ref;
	private ImageProcessor corr;
	private String dirWorkFolder = ".../cloudFolder";
	private String dirImgRepo = ".../cloudImgRepo";
	private String filename = "cloud0";
	private String ending = ".png";
	private String commandDelete;
	private String commandLoad;
	private int counter;
	private ArrayList<Cloud> referenceList;
	private ArrayList<Cloud> correspondenceList;
	private ArrayList<CloudPair> pairList;
	private ResultsTable results;
	
	private Process p;
	private int numOfIteration = 1;
	
	
	public void run(String arg) {

	while(numOfIteration > 0){	
		
		ImageStack cloudStack = loadImagesIntoStack();
		
		// an dieser Stelle erfolgt die Binarisierung des Stacks
		makeBinaryStack();
		
		// aus dem Stack wird das Referenzbild sowie das Korrespondenzbild extrahiert
		ref = cloudStack.getProcessor(1);
		corr = cloudStack.getProcessor(2);
		
		
		// Für die Bewegungsprognose wird aus dem vorherigen Durchlauf der jeweilige Vektor und die Richtung aus der ResultsTable ausgelesen
		
		// Wenn keine Bewegungsprognose aus der vorherigen Iteration vorhanden ist bzw. gleich Null ist,
		// wird die findBorders-Methode aufgerufen, welche jeweils eine List mit Cloud-Objekten zurück gibt.
		referenceList = findBorders(ref);
		correspondenceList = findBorders(corr);

		
		// Wenn eine Prognose aus dem vorherigen Durchlauf vorhanden ist, dann wir das Tracking durchgeführt.
		// Tracking
		
		pairList = findCloudPairs(referenceList, correspondenceList);
		
		// Der ResultsTable wird am Ende des Durchlaufs die Wolke inkl. Prognose hinzugefügt und innerhalb von ImageJ als Auflistung angezeigt
		// welche Methode hierzu benutzt wird, ist noch unklar, vielleicht muss diese noch implementiert werden
		
		
		ResultsTable rt = Analyzer.getResultsTable();
		if (rt == null) {
		        rt = new ResultsTable();
		        Analyzer.setResultsTable(rt);
		}
		
		for (int i = 0; i < pairList.size(); i++){
			
		rt.incrementCounter();
		
		rt.addValue("NumberOfIteration", numOfIteration);
		rt.addValue("CloudNumber", -1);
		rt.addValue("Time until Sun is reached (sec)", pairList.get(i).timeUntilSunIsReached(30, 30));
		rt.addValue("Cloud velocity (pix per sec)", pairList.get(i).calculateVelocity());
		rt.addValue("Reference Cloud Height", pairList.get(i).getReference().getHeight());
		rt.addValue("Reference Cloud Width", pairList.get(i).getReference().getWidth());
		rt.addValue("Reference Cloud Center_X", pairList.get(i).getReference().getX());
		rt.addValue("Reference Cloud Center_Y", pairList.get(i).getReference().getY());
		rt.addValue("Correspondence Cloud Height", pairList.get(i).getCorrespondence().getHeight());
		rt.addValue("Correspondence Cloud Width", pairList.get(i).getCorrespondence().getWidth());
		rt.addValue("Correspondence Cloud Center_X", pairList.get(i).getCorrespondence().getX());
		rt.addValue("Correspondence Cloud Center_Y", pairList.get(i).getCorrespondence().getY());
		
		}
		
		rt.show("Results");
		
		// Die folgende Methode bereitet das Verzeichnis für den nächsten Durchlauf vor
		//prepareNextIteration();
		
		numOfIteration++;
		
		
		// Hier wird die Aufnahme der Kamera simuliert
		try {
			Thread.sleep(3000);
		} catch(InterruptedException ex) {
		    Thread.currentThread().interrupt();
		}
		
	}
	}
	
	private ImageStack loadImagesIntoStack(){
		

		opener = new FolderOpener();
		clouds = new ImagePlus();
		clouds = opener.openFolder(dirWorkFolder);
		
		return clouds.getImageStack();
			
	}
	
	private void makeBinaryStack(){};
	
	private ArrayList<Cloud> findBorders(ImageProcessor binary){

		binary.autoThreshold();
		
		ArrayList<Cloud> cloudList = new ArrayList<Cloud>();
		
		int white = -1;
		int width = binary.getWidth();
		int height = binary.getHeight();
	
		byte [] pixels = (byte[]) binary.getPixels();
		
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
					
					Cloud tmp = new Cloud(centerX,centerY,cloudWidth,cloudHeight);

					if(tmp.getHeight() != 0 && tmp.getWidth() != 0){
						cloudList.add(tmp);
					}
				}
			}
		}
		
		return cloudList;
	}
	
	private ArrayList<CloudPair> findCloudPairs(ArrayList<Cloud> referenceList, ArrayList<Cloud> correspondenceList){
	
		ArrayList<CloudPair> pairs = new ArrayList<CloudPair>();
		
		for(int i = 0; i < referenceList.size(); i++){
			
			Cloud ref = referenceList.get(i);
			for(int j = 0; j < correspondenceList.size(); j++){
				
				Cloud corr = correspondenceList.get(j);
				if(ref.findMatchingCloud(corr)){
					
					Vec2 tmp = ref.computeCenterDistance(corr);
					
					CloudPair pairTmp = new CloudPair(ref, corr, tmp, numOfIteration);
					pairs.add(pairTmp);		
				}
			}	
		}
		return pairs;	
	}
	
	private void prepareNextIteration(){
		// unix-shell command
		commandDelete = "rm " + dirWorkFolder + "/" + filename + numOfIteration +  ending;	
		
		// Vorerst werden Bilder aus einem externen Ordner geladen, 
		// in einer späteren Umsetzung liefert hier die Kamera Bilder
		
		commandLoad = "mv " + dirImgRepo + "/" + filename + (numOfIteration + 2) + ending;
		
		try {
			p = Runtime.getRuntime().exec(commandDelete);
			p = Runtime.getRuntime().exec(commandLoad);
			System.out.println(commandDelete);
			System.out.println(commandLoad);
			
		} catch (IOException e) {
		// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	}

}
