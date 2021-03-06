import ij.ImagePlus;
import ij.ImageStack;
import ij.measure.ResultsTable;
import ij.plugin.PlugIn;
import ij.plugin.filter.Analyzer;
import ij.process.ImageProcessor;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

import tracking.Tracking;
import binary.Binarisierung;
//import binary.Binarisierung;
//import binary.Erosion;
import cloud.Cloud;
import cloud.CloudPair;
import cloud.Vec2;

public class CloudTracking_ implements PlugIn {

	
	private ImageProcessor ref;
	private ImageProcessor corr;
	
	private String dir = "../combinationI/"; // Pfad zum Ordner, in dem die Aufnahmen der Wolken sind
	private String name = "cloud";
	private String format = ".jpg";
	
	private ArrayList<Cloud> referenceList;
	private ArrayList<Cloud> correspondenceList;
	private ArrayList<CloudPair> pairListPrevious;		// wichtig fürs Tracking
	private ArrayList<CloudPair> pairListCurrent;
	private ArrayList<Cloud> correspondenceListPrevious;
	

	private int numberOfImagesToProcess = new File(dir).listFiles().length - 1 ;
	private int numOfIteration = 1;
	
	public void run(String arg) {

		//Ueberpruefung der Bildanzahl im Ordner
		//System.out.println(numberOfImagesToProcess);

	while(numOfIteration < numberOfImagesToProcess){	
		
		ImageStack cloudStack = loadImages();
		
		//Anzeige des CloudStacks
		//ImagePlus imgToShow = new ImagePlus("CloudStack No. " + numOfIteration, cloudStack);
		//imgToShow.show();

		
		// an dieser Stelle erfolgt die Binarisierung des Stacks
		makeBinaryStack(cloudStack);
		
		// anschließend erodieren
		//erodeBinaryStack(cloudStack);
		
		
		// aus dem Stack wird das Referenzbild sowie das Korrespondenzbild extrahiert
		ref = cloudStack.getProcessor(1);
		corr = cloudStack.getProcessor(2);
		
		//binarisierung
//		ref.autoThreshold();
//		corr.autoThreshold();
		
		
		// Ausgabe des binarisierten Bildes
		new ImagePlus("ref", ref.convertToByteProcessor()).show();
		new ImagePlus("corr", corr.convertToByteProcessor()).show();
		
		
		// Wenn keine Bewegungsprognose aus der vorherigen Iteration vorhanden ist bzw. gleich Null ist,
		// wird die findBorders-Methode aufgerufen, welche jeweils eine List mit Cloud-Objekten zurück gibt.
		if(pairListPrevious == null || pairListPrevious.size() == 0 || correspondenceListPrevious == null || correspondenceListPrevious.size() == 0){
		
		//referenceList = startTracking.startTracking(ref, numOfIteration);
		//correspondenceList = startTracking.startTracking(corr, numOfIteration);
		referenceList = Tracking.startTracking(ref, numOfIteration);
		correspondenceList = Tracking.startTracking(corr, numOfIteration);
		
		pairListCurrent = findCloudPairs(referenceList, correspondenceList);
		
		}
		
		// Wenn Prognosen/Referenzdaten aus dem vorherigen Durchlauf vorhanden sind, dann wir das Tracking durchgeführt.
		else{
			
			
			referenceList = correspondenceListPrevious;
			
			//test
			correspondenceList = Tracking.startTracking(corr, numOfIteration);
			pairListCurrent = findCloudPairs(referenceList, correspondenceList);
			
			
//			correspondenceList = Tracking.Tracking(pairListPrevious, corr, numOfIteration);
			
		}
		
		//kontrollanzeige
//		ImagePlus refShow = new ImagePlus("ref", ref);
//		refShow.show();
//		ImagePlus corrShow = new ImagePlus("crr", corr);
//		corrShow.show();
		
		//pairListCurrent = findCloudPairs(referenceList, correspondenceList);
		//pairListCurrent = findCloudpairsStart.findCloudPairs(referenceList, correspondenceList, numOfIteration);
		
		
		// Zeige den Bewegungsvektor im Referenz- bzw. Korrespondenzbild
//		if(pairListCurrent.size()!=0){
//		for(int i = 0; i < pairListCurrent.size(); i++){
//			pairListCurrent.get(i).showVec(ref);
//			System.out.println("leer");
//		}}
		
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
		correspondenceListPrevious = correspondenceList;
		
		
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
	
//	private void erodeBinaryStack(ImageStack cloudStack){
//		Erosion.erode(cloudStack.getProcessor(1));
//		Erosion.erode(cloudStack.getProcessor(1));
//	}
	
	private ArrayList<CloudPair> findCloudPairs(ArrayList<Cloud> referenceList, ArrayList<Cloud> correspondenceList){
	
	
		byte th=4;//wieviele stellen im corr array werden getestet
 
		
		ArrayList<CloudPair> pairs = new ArrayList<CloudPair>();
		
		int sizeR=referenceList.size();
		int sizeC=correspondenceList.size();
		int dif=sizeR- sizeC;
		
		
		Collections.sort(referenceList);
		Collections.sort(correspondenceList);
		
				
		//ref>cor
		if(dif<0){
			
			for(int i = 0; i < referenceList.size(); i++){
				Cloud ref = referenceList.get(i);
				Cloud cor = correspondenceList.get(i);
				double minDis=ref.getDist(correspondenceList.get(i));
				
			for(int j=0; j<correspondenceList.size(); j++){
				
				if(Math.abs(i-j)<=th){
									
							
					if(ref.getDist(correspondenceList.get(j))<minDis){
					minDis=ref.getDist(correspondenceList.get(j));
					cor=correspondenceList.get(j);
					}
					
					Vec2 tmp = ref.computeCenterDistance(cor);	
						
						CloudPair pairTmp = new CloudPair(ref, cor, tmp, numOfIteration);
						pairs.add(pairTmp);
					
					
				}//if
			}//for
			}//for
			}//if
			
		
		//ziel: richtige zuordnung der wolken ohne dopplung
			//corr>ref
		if(dif>=0){
				
				for(int i = 0; i < correspondenceList.size(); i++){
					Cloud cor = correspondenceList.get(i);
					Cloud ref = referenceList.get(i);
					double minDis=cor.getDist(referenceList.get(i));
					
				for(int j=0; j<referenceList.size(); j++){
					
					if(Math.abs(i-j)<=th){
										
								
						if(cor.getDist(referenceList.get(j))<minDis){
						minDis=cor.getDist(referenceList.get(j));
						ref=referenceList.get(j);
						}
						
						Vec2 tmp = ref.computeCenterDistance(cor);	
							
							CloudPair pairTmp = new CloudPair(ref, cor, tmp, numOfIteration);
							pairs.add(pairTmp);
						
						
					}//if
				}//for
				}//for
				
			}
		
		return pairs;	
	}
	
	private ArrayList<CloudPair> findCloudPairsAlt(ArrayList<Cloud> referenceList, ArrayList<Cloud> correspondenceList){
		System.out.println("findCloudPairs");
		
		ArrayList<CloudPair> pairs = new ArrayList<CloudPair>();
		
		for(int i = 0; i < referenceList.size(); i++){
			
			Cloud ref = referenceList.get(i);
			for(int j = 0; j < correspondenceList.size(); j++){
				
				Cloud corr = correspondenceList.get(j);
				if(ref.findMatchingCloud(corr)){
					
					Vec2 v = ref.computeCenterDistance(corr);
					
					//System.out.println(tmp.getX());
					//System.out.println(tmp.getY());
					
					CloudPair pairTmp = new CloudPair(ref, corr, v, numOfIteration);
					pairs.add(pairTmp);
				}
			}	
		}
		return pairs;	
	}
	
	


}
