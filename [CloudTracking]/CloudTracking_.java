import java.io.File;
import java.util.ArrayList;
import binary.Binarisierung;
import cloud.*;
import ij.*;
import ij.measure.ResultsTable;
import ij.plugin.PlugIn;
import ij.plugin.filter.Analyzer;
import ij.process.ByteProcessor;
import ij.process.ImageProcessor;

public class CloudTracking_ implements PlugIn {

	
	private ImageProcessor ref;
	private ImageProcessor corr;
	
	private String dir = ""; // Pfad zum Ordner, in dem die Aufnahmen der Wolken sind
	private String name = "cloud";
	private String format = ".jpg";
	
	private ArrayList<Cloud> referenceList;
	private ArrayList<Cloud> correspondenceList;
	private ArrayList<CloudPair> pairListPrevious;		// wichtig fürs Tracking
	private ArrayList<CloudPair> pairListCurrent;
	
	
	private int numberOfImagesToProcess = new File(dir).listFiles().length - 1 ;
	private int numOfIteration = 1;
	
	public void run(String arg) {

		System.out.println(numberOfImagesToProcess);

	while(numOfIteration < numberOfImagesToProcess / 2){	
		
		
		ImageStack cloudStack = loadImages();
		
		//Anzeige des CloudStacks
		//ImagePlus imgToShow = new ImagePlus("CloudStack No. " + numOfIteration, cloudStack);
		//imgToShow.show();

		
		// an dieser Stelle erfolgt die Binarisierung des Stacks
		makeBinaryStack(cloudStack);
		
		
		// aus dem Stack wird das Referenzbild sowie das Korrespondenzbild extrahiert
		ref = cloudStack.getProcessor(1);
		corr = cloudStack.getProcessor(2);
		
		
		// Ausgabe des binarisierten Bildes
		//new ImagePlus("ref", ref).show();
		//new ImagePlus("corr", corr).show();
		
		
		// Wenn keine Bewegungsprognose aus der vorherigen Iteration vorhanden ist bzw. gleich Null ist,
		// wird die findBorders-Methode aufgerufen, welche jeweils eine List mit Cloud-Objekten zurück gibt.
		referenceList = findBorders(ref);
		correspondenceList = findBorders(corr);

		
		// Wenn eine Prognose aus dem vorherigen Durchlauf vorhanden ist, dann wir das Tracking durchgeführt.
		// Tracking
		
		pairListCurrent = findCloudPairs(referenceList, correspondenceList);
		
		
		// Zeige den Bewegungsvektor im Referenz- bzw. Korrespondenzbild
		
//		for(int i = 0; i < pairListCurrent.size(); i++){
//			pairListCurrent.get(i).showVec(ref);
//		}
		
		// Der ResultsTable wird am Ende des Durchlaufs die Wolke inkl. Prognose hinzugefügt und innerhalb von ImageJ als Auflistung angezeigt
		// welche Methode hierzu benutzt wird, ist noch unklar, vielleicht muss diese noch implementiert werden
		
		
		ResultsTable rt = Analyzer.getResultsTable();
		if (rt == null) {
		        rt = new ResultsTable();
		        Analyzer.setResultsTable(rt);
		}
		
		for (int i = 0; i < pairListCurrent.size(); i++){
			
		rt.incrementCounter();
		
		rt.addValue("NumberOfIteration", numOfIteration);
		rt.addValue("CloudNumber", -1);
		rt.addValue("Time until Sun is reached (sec)", pairListCurrent.get(i).timeUntilSunIsReached(30, 30));
		rt.addValue("Cloud velocity (pix per sec)", pairListCurrent.get(i).calculateVelocity());
		rt.addValue("Reference Cloud Height", pairListCurrent.get(i).getReference().getHeight());
		rt.addValue("Reference Cloud Width", pairListCurrent.get(i).getReference().getWidth());
		rt.addValue("Reference Cloud Center_X", pairListCurrent.get(i).getReference().getX());
		rt.addValue("Reference Cloud Center_Y", pairListCurrent.get(i).getReference().getY());
		rt.addValue("Correspondence Cloud Height", pairListCurrent.get(i).getCorrespondence().getHeight());
		rt.addValue("Correspondence Cloud Width", pairListCurrent.get(i).getCorrespondence().getWidth());
		rt.addValue("Correspondence Cloud Center_X", pairListCurrent.get(i).getCorrespondence().getX());
		rt.addValue("Correspondence Cloud Center_Y", pairListCurrent.get(i).getCorrespondence().getY());
		
		}
		
		rt.show("Results");
		
		// Die folgende Methode bereitet das Verzeichnis für den nächsten Durchlauf vor
		//prepareNextIteration();
		
		numOfIteration++;
		pairListPrevious = pairListCurrent;
		
		
		// Hier wird die Aufnahme der Kamera simuliert
		try {
			Thread.sleep(3000);
		} catch(InterruptedException ex) {
		    Thread.currentThread().interrupt();
		}
		
	}
	}
	
	private ImageStack loadImages(){
		
		String fileNameNumberRef = new String();
		String fileNameNumberCorr = new String();
		
		if(numOfIteration < 10){
			fileNameNumberRef = "000" + numOfIteration;
			fileNameNumberCorr = "000" + (numOfIteration + 1);
		}
		if(numOfIteration >= 10 && numOfIteration < 100){
			fileNameNumberRef = "00" + numOfIteration;
			fileNameNumberCorr = "00" + (numOfIteration + 1);
		}
		if(numOfIteration >= 100 && numOfIteration < 1000){
			fileNameNumberRef = "0" + numOfIteration;
			fileNameNumberCorr = "0" + (numOfIteration + 1);
		}
		if(numOfIteration >= 1000){
			fileNameNumberRef = String.valueOf(numOfIteration);
			fileNameNumberCorr = String.valueOf(numOfIteration + 1);
		}
		
		ImagePlus ref = new ImagePlus(dir + name + fileNameNumberRef + format);
		ImagePlus corr = new ImagePlus(dir + name + (fileNameNumberCorr) + format);
		ImageStack tmp = ImageStack.create(ref.getWidth(), ref.getHeight(), 2, ref.getBitDepth());
		
		
		tmp.setPixels(ref.getProcessor().getPixels(),1);
		tmp.setPixels(corr.getProcessor().getPixels(),2);
		
		return tmp;
		
		
	}
		
	private void makeBinaryStack(ImageStack cloudStack){
		
		cloudStack.setProcessor(
				Binarisierung.binaryPicture(cloudStack.getProcessor(1)), 1);
		cloudStack.setProcessor(
				Binarisierung.binaryPicture(cloudStack.getProcessor(2)), 2);		
	}
	
	private ArrayList<Cloud> findBorders(ImageProcessor binary){

		ArrayList<Cloud> cloudList = new ArrayList<Cloud>();
		
		int white = -1;
		int widthP = binary.getWidth();
		int heightP = binary.getHeight();
		byte counter=10;
		
		new ImagePlus("",binary).show();
	
		int [] pixels = (int[]) binary.getPixels();
		
		int length = pixels.length;
		
		boolean [] marker = new boolean[pixels.length];
		byte [] show = new byte[length];
		
		
		ArrayList <int[]> fifo = new ArrayList<int[]>();
		
		
		for(int y = 0; y < heightP; y++ ){
			for(int x = 0; x < widthP; x++){
				
				if(pixels[y*widthP + x] == white && !marker[y*widthP + x]){
					
					int[] xy={x,y};
					
					int centerX = 0, centerY = 0, cloudWidth = 0, cloudHeight = 0;
					int minX = x, minY = y, maxX = x, maxY = y;
					
					
					fifo.add(xy);
					
					marker[y * widthP + x] = true;
					show[y * widthP + x] = counter;
					
					while(!fifo.isEmpty()){
						
						int [] coordXY = fifo.remove(fifo.size()-1);
								
								
						if(coordXY[1] * widthP + coordXY[0] - 1 > 0 && coordXY[1] % widthP != 0){
						
							if(pixels[coordXY[1] * widthP + coordXY[0] - 1] == white && !marker[coordXY[1] * widthP + coordXY[0] - 1]){
							
								int[] xyL={coordXY[0] - 1,coordXY[1]};
								fifo.add(xyL);
								if(xyL[0]<minX)
									minX=xyL[0];
								
								marker[coordXY[1] * widthP + coordXY[0]] = true;
								show[coordXY[1] * widthP + coordXY[0]] = counter;
								}
							 
							 
						}
						
						if(coordXY[1] * widthP + coordXY[0] + 1 < length && coordXY[1] % widthP != (widthP-1)){
						
							if(pixels[coordXY[1] * widthP + coordXY[0] + 1]== white && !marker[coordXY[1] * widthP + coordXY[0] + 1]){
							
								int[] xyR={coordXY[0] + 1, coordXY[1]};
								fifo.add(xyR);
								if(xyR[0]>maxX)
									maxX=xyR[0];
								
								marker[coordXY[1] * widthP + coordXY[0]] = true;
								show[coordXY[1] * widthP + coordXY[0]] = counter;
								}
						}
						
						if((coordXY[1] - 1) * widthP + coordXY[0] > 0 && coordXY[0] != 0){
						
							if(pixels[(coordXY[1]-1) * widthP + coordXY[0]] == white && !marker[(coordXY[1]-1) * widthP + coordXY[0]]){
							
								int[] xyO={coordXY[0],coordXY[1]-1};
								fifo.add(xyO);
								if(xyO[1] * widthP < minY)
									minY=xyO[1];
							
								marker[coordXY[1] * widthP + coordXY[0]] = true;
								show[coordXY[1] * widthP + coordXY[0]] = counter;
								}
						}
						
						
						if((coordXY[1] + 1) * widthP + coordXY[0] < length && coordXY[0] != (heightP-1)){
							
							if(pixels[(coordXY[1]+1) * widthP + coordXY[0]] == white && !marker[(coordXY[1]+1) * widthP + coordXY[0]]){
								
								int[] xyU={coordXY[0],coordXY[1]+1};
								fifo.add(xyU);
								if(xyU[1]* widthP >maxY)
									maxY=xyU[1];
								
								marker[coordXY[1] * widthP + coordXY[0]] = true;
								show[coordXY[1] * widthP + coordXY[0]] = counter;
								}
						}
					
					
					}//end while
					
					centerX=minX + (maxX-minX)/2;
					centerY= minY + (maxY-minY)/2;
					cloudWidth=maxX-minX;
					cloudHeight=maxY-minY;
					
					Cloud tmp = new Cloud(centerX,centerY,cloudWidth,cloudHeight);

					if(tmp.getHeight() != 0 && tmp.getWidth() != 0){
						cloudList.add(tmp);
					}
					
					counter+=10; //>128 ausschließen
					
				}//end for
			}//end for
			
			
		}//end find borders
		
		//resultat anzeigen
		
		ByteProcessor ip= new ByteProcessor(widthP,heightP, show);
		new ImagePlus("",ip).show();
		
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
	


}
