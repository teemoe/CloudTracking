import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Vector;

import ij.*;
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
	
	private Process p;
	
	
	public void run(String arg) {
		
		counter = 1;
		command = "rm " + dir + "/" + filename + counter +  ending;

		ImageStack cloudStack = loadImagesIntoStack();

		Vector motionVec;
		
		ref = cloudStack.getProcessor(1);
		corr = cloudStack.getProcessor(2);
		
		referenceList = findBorders(ref);
		correspondenceList = findBorders(corr);
		
		Cloud referenceCloud;
		Cloud correspondenceCloud;
		
		ArrayList<Vector> vecs = new ArrayList<Vector>();
		Vector mean = new Vector(0,0);
		
		for(int i = 0; i < referenceList.size(); i++){
			
			referenceCloud = referenceList.get(i);
			
			for(int j = 0; j < correspondenceList.size(); j++){
				
				correspondenceCloud = correspondenceList.get(j);
				
				
				if(referenceCloud.findMatchingCloud(correspondenceCloud)){
										
					vecs.add(referenceCloud.computeCenterDistance(correspondenceCloud));					
				}
			}			
		}
		
		for(int i = 0; i < vecs.size(); i++){
			
		}
		

//			try {
//				p = Runtime.getRuntime().exec(command);
//				System.out.println(command);
//				counter++;
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}

		
		
	}
	
	
	private ImageStack loadImagesIntoStack(){
		

		opener = new FolderOpener();
		clouds = new ImagePlus();
		clouds = opener.openFolder(dir);
		
		return clouds.getImageStack();
			
	}
	
	
	
	private void binary(){};
	

	public static ArrayList<Cloud> findBorders(ImageProcessor binary){
		
		ArrayList<Cloud> clList=new ArrayList<Cloud>();
		
		
		byte [] picBin = (byte[]) binary.getPixels();
		boolean [] marker = new boolean [picBin.length];

		for(int i=0; i<marker.length;i++){

				marker[i]=false;	
		}
	
		
		// durchlaufe Eingangsbild
		// wenn pixel = 1 beginn region zu füllen
		
		
//		for(int i=0; i<img.length;i++){
//			for(int j=0; j<img[0].length;j++){
//			
//			if(img[i][j]==true & marker[i][j]==false){
//				
//								
//				Cloud cl=fillRegion(i,j,img,marker);
//				
//				clList.add(cl);
//			}
//			
//		}
//		}
	
				
		return clList;
	}
	
	public static Cloud fillRegion(int x, int y, boolean[][]img, boolean[][]marker){
		
		int[] xy={x,y};
			
		int cmX=0,cmY=0,width=0,height=0;
		int minX=x, minY=y, maxX=x, maxY=y;
		
		Queue <int[]> fifo = new LinkedList(); //anderer typ
		
				
		fifo.add(xy);
		
		while(fifo.isEmpty()==false){
			
		int[] xyB=fifo.poll();
			
		
		if(img[xyB[0]-1][xyB[1]]==true & marker[xyB[0]-1][xyB[1]]==false){
		
		int[] xyL={xyB[0]-1,xyB[1]};
		fifo.add(xyL);
		if(xyL[0]<minX)
			minX=xyL[0];
		}
		

		if(img[xyB[0]+1][xyB[1]]==true & marker[xyB[0]+1][xyB[1]]==false){
		
		int[] xyR={xyB[0]+1,xyB[1]};
		fifo.add(xyR);
		if(xyR[0]>maxX)
			maxX=xyR[0];
		}
		

		if(img[xyB[0]][xyB[1]-1]==true & marker[xyB[0]][xyB[1]-1]==false){
		
		int[] xyO={xyB[0],xyB[1]-1};
		fifo.add(xyO);
	
		}
		

		if(img[xyB[0]][xyB[1]+1]==true & marker[xyB[0]][xyB[1]+1]==false){
		
		int[] xyU={xyB[0]-1,xyB[1]+1};
		fifo.add(xyU);
		if(xyU[0]>maxY)
			maxY=xyU[0];
		}
					
		
		}
		//end while
		
		
		
		cmX=(minX+maxX)/2;
		cmY=(minY+maxY)/2;
		width=maxX-minX;
		height=maxY-minY;
		
		Cloud cl=new Cloud(cmX,cmY,width,height);

		
		return cl;
		
		
	}
	
		
	//Voraussetzung zur Berechung konstanter Zeitabstand zwischen den Aufnahmen (3s)
	//Angabe in Pixel pro Sekunde	
	private int Zeitprognose(){ 
		return 0;}

}
