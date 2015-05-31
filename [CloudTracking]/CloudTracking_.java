import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import cloud.*;
import ij.*;
import ij.measure.ResultsTable;
import ij.plugin.FolderOpener;
import ij.plugin.PlugIn;
import ij.plugin.filter.Analyzer;
import ij.process.ImageProcessor;

public class CloudTracking_ implements PlugIn {

	private FolderOpener opener;
	private ImagePlus clouds;
	
	private ImageProcessor ref;
	private ImageProcessor corr;
	
	private String dirWorkFolder = "/Users/boelf/Desktop/cloudFolder";
	private String dirImgRepo = "/Users/boelf/Desktop/cloudImgRepo";
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
		
		//pairList = findCloudPairs(referenceList, correspondenceList);
		
		// Der ResultsTable wird am Ende des Durchlaufs die Wolke inkl. Prognose hinzugefügt und innerhalb von ImageJ als Auflistung angezeigt
		// welche Methode hierzu benutzt wird, ist noch unklar, vielleicht muss diese noch implementiert werden
		
		
		ResultsTable rt = Analyzer.getResultsTable();
		if (rt == null) {
		        rt = new ResultsTable();
		        Analyzer.setResultsTable(rt);
		}

		rt.incrementCounter();
		
		rt.addValue("NumberOfIteration", -1);
		rt.addValue("CloudNumber", -1);
		rt.addValue("Time until Sun is reached (sec)", -1);
		rt.addValue("Cloud velocity (pix per sec)", -1);
		
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
		return null;
	}

	private Cloud fillRegion(int x, int y, boolean[][]img, boolean[][]marker){
		return null;	
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
