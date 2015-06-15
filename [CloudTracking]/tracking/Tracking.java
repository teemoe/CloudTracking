package tracking;

import ij.ImagePlus;
import ij.measure.ResultsTable;
import ij.process.ImageProcessor;

import java.util.ArrayList;

import cloud.Cloud;

public class Tracking {

	
	//auf punkte aus results zugreifen
		private ArrayList<Cloud> Tracking (ResultsTable rt, ImageProcessor binary){
			
			
			ArrayList<Cloud> corr = new ArrayList<Cloud>();
			ArrayList<Cloud> cloudList = new ArrayList<Cloud>();
			
			int white = -1;
			int widthP = binary.getWidth();
			int heightP = binary.getHeight();
			byte counter=10;
		
			byte [] pixels = (byte[]) binary.getPixels();
			byte [] show = new byte[widthP*heightP];
			
			int length = pixels.length;
					
			
			// durchlaufe alle wolken
			while (corr.size()!=0){
			
			Cloud c= corr.remove(corr.size()-1); //poll
					
			int height=c.getHeight();
			int width=c.getWidth();
			int x=c.getX();
			int y=c.getY();
			
			int incrY=height/10;
			int[]possibleY= new int[10];
			for(int i=-5; i<5; i++){
				possibleY[i]=y+i*incrY;
			}
			
			int incrX=height/10;
			int[]possibleX= new int[10];
			for(int i=-5; i<5; i++){
				possibleX[i]=x+i*incrX;
			}
			
			
			// spätere randpunkte
			int minX=x, minY=y, maxX=x, maxY=y;
			int minXtemp=x, minYtemp=y, maxXtemp=x, maxYtemp=y;
			
			// laufvariablen
			int xl=x, xr=x, yu=y, yo=y;
			
			//x
			for(int i=0; i<10; i++){
			
			y=possibleY[i];
				
			// teste wie lange vom mittelpunkt der alten wolke in jede richtung gegangen werden kann
			while(pixels[y*widthP+xl]==white && pixels[y*widthP+xl]>0 && y*widthP+xl%widthP != 0){
				xl=xl-1;
				minXtemp=xl;
				show[y*widthP+xl]=counter;
			}
			
			while(pixels[y*widthP+xr]==white && pixels[y*widthP+xr]<widthP-1 && y*widthP+xr != (widthP-1)){
				xr=xr+1;
				maxXtemp=xr;
				show[y*widthP+xr]=counter;
			}
			
			if (minXtemp<minX)
				minX=minXtemp;
			
			if (maxXtemp<maxX)
				maxX=maxXtemp;
			
			}
			
			
			//y
			for(int i=0; i<10; i++){
			
				x=possibleX[i];
				
			
			
			while(pixels[yo*widthP+x]==white && pixels[y*widthP+xr]>0 && yo*widthP+x != 0){
				yo=yo-1;
				minY=yo;
				show[yo*widthP+x]=counter;
			}
			
			while(pixels[yu*widthP+x]==white && pixels[y*widthP+xr]<heightP-1 && yu*widthP+x != (widthP-1)){
				yu=yu+1;
				maxY=yu;
				show[yu*widthP+x]=counter;
			}
			
			if (minYtemp<minY)
				minY=minYtemp;
			
			if (maxYtemp<maxY)
				maxY=maxYtemp;
			
			}
			
			//neue wolke
			x=(minX+maxX)/2;
			y=(minY+maxY)/2;
			width=maxX-minX;
			height=maxY-minY;
			
			Cloud cn=new Cloud(x,y,width,height);
			
			cloudList.add(cn);
			
			} // ende while
			
			//bild anzeigen lassen:
			
			
			
			new ImagePlus().show();

			return cloudList;
			
		} // ende tracking
		
}