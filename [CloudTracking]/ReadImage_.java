import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import cloud.*;
import ij.*;
import ij.measure.ResultsTable;
import ij.plugin.FolderOpener;
import ij.plugin.PlugIn;
import ij.process.ImageProcessor;

public class ReadImage_ implements PlugIn {

	private FolderOpener opener;
	//private ImagePlus imgToShow;
	private ImagePlus clouds;
	
	private ImageProcessor ref;
	private ImageProcessor corr;
	
	private String dir = "/Users/boelf/Desktop/cloudFolder";
	private String filename = "cloud0";
	private String ending = ".png";
	private String command;
	private int counter;
	private ArrayList<Cloud> referenceList;
	private ArrayList<Cloud> correspondenceList;
	private ArrayList<CloudPair> pairList;
	private ResultsTable results;
	
	private Process p;
	private int numOfIteration = 1;
	
	
	public void run(String arg) {
		
		// counter für die Bildnummer des Bildes, welches am Ende des Durchlauf aus dem Verzeichnis gelöscht wird
		counter = 1;


		ImageStack cloudStack = loadImagesIntoStack();
		
		// an dieser Stelle erfolgt die Binarisierung des Stacks
		makeBinaryStack();
		
		// aus dem Stack wird das Referenzbild sowie das Korrespondenzbild extrahiert
		ref = cloudStack.getProcessor(1);
		corr = cloudStack.getProcessor(2);
		
		// Wenn keine Bewegungsprognose aus der vorherigen Iteration vorhanden ist,
		// wird die findBorders-Methode aufgerufen, welche jeweils eine List mit Cloud-Objekten zurück gibt.
		referenceList = findBorders(ref);
		correspondenceList = findBorders(corr);

		
		// Wenn eine Prognose aus dem vorherigen Durchlauf vorhanden ist, dann wir das Tracking durchgeführt.
		// Tracking
		
		pairList = findCloudPairs(referenceList, correspondenceList);
		
		// Der ResultsTable wird am Ende des Durchlaufs die Wolke inkl. Prognose hinzugefügt und innerhalb von ImageJ als Auflistung angezeigt
		// welche Methode hierzu benutzt wird, ist noch unklar, vielleicht muss diese noch implementiert werden
		results.addResults();
		


		// Die folgende Methode bereitet das Verzeichnis für den nächsten Durchlauf vor
		prepareNextIteration();
		
		numOfIteration++;
	}
	
	
	private ImageStack loadImagesIntoStack(){
		

		opener = new FolderOpener();
		clouds = new ImagePlus();
		clouds = opener.openFolder(dir);
		
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
		return null;	
	}
	
	private void prepareNextIteration(){
		// unix-shell command
		// command = "rm " + dir + "/" + filename + counter +  ending;		
		
		
//		try {
//		p = Runtime.getRuntime().exec(command);
//		System.out.println(command);
//		counter++;
//	} catch (IOException e) {
//		// TODO Auto-generated catch block
//		e.printStackTrace();
//	}
	}

}
