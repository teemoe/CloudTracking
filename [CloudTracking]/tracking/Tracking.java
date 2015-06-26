package tracking;

import ij.ImagePlus;
import ij.process.ByteProcessor;
import ij.process.ImageProcessor;

import java.util.ArrayList;

import cloud.Cloud;
import cloud.CloudPair;
import cloud.Vec2;

public class Tracking {

	
	public static ArrayList<Cloud> Tracking (ArrayList<CloudPair> ref, ImageProcessor binary, int numOfIteration){
		
		ArrayList<Cloud> currentCorrespondence = new ArrayList<Cloud>();
		
		int white = -1;
		int [] pixels = (int[]) binary.getPixels();
		int length = pixels.length;
		int binWidth  = binary.getWidth();
		//int binHeight = binary.getHeight();  //unnötig
		
		
		for(int i = 0; i < ref.size(); i++)
		{
			
			CloudPair previousPair = ref.get(i);
			Cloud correspondence = previousPair.getCorrespondence();
			int corrHeight = correspondence.getHeight();
			int corrWidth = correspondence.getWidth();
			int corrCenterX = correspondence.getX();
			int corrCenterY = correspondence.getY();
			Vec2 motion = previousPair.getMotionVec();
			int dirX = motion.getX();
			int dirY = motion.getY();
			//String direction = previousPair.getVecDirection(); //unnötig
			
			int estimatedCenterX;
			int estimatedCenterY;
			int heightTop = 0;
			int heightBottom = 0;
			int widthLeft = 0;
			int widthRight = 0;
			
			// Pruefe, ob das geschätzte neue Zentrum der Wolke innerhalb des Bildes liegt
			if(corrCenterX + dirX + corrCenterY + dirY * binWidth >= 0 && corrCenterX + dirX + corrCenterY + dirY * binWidth < length){
				
				estimatedCenterX = corrCenterX + dirX;
				estimatedCenterY = corrCenterY + dirY;
				
				//Pruefe ob das Pixle im Binärbild weiß ist, also ob es ein Wolkenpixel ist
				if(pixels[estimatedCenterX + binWidth * estimatedCenterY] == white){
					
					
					//Dann laufe der Höhe und Breite entsprechend vom Zentrum ausgehend durch das "pixels"-Array und bestimme die neue Höhe und Breite
					
					//oben
					
					for(int j = 0; j < corrHeight / 2; j++){
						
					//Abfrage, ob Arraygrenzen nicht überschritten werden
						if(estimatedCenterX + binWidth * (estimatedCenterY - j) >= 0 && estimatedCenterX + binWidth * (estimatedCenterY - j) < length){
							if(pixels[estimatedCenterX + binWidth * (estimatedCenterY - j)] != white)
								heightTop = j;
							else 
								heightTop++;
						}
						else
							break;
					}	
					
					//unten
					
					for(int g = 0; g < corrHeight / 2; g++){
						
					
					//Abfrage, ob Arraygrenzen nicht überschritten werden
						if(estimatedCenterX + binWidth * (estimatedCenterY + g) >= 0 && estimatedCenterX + binWidth * (estimatedCenterY + g) < length){
						
							if(pixels[estimatedCenterX + binWidth * (estimatedCenterY + g)] != white)
								heightBottom = g;
							else
								heightBottom++;
						}
						else
							break;
					}
					
					//links
					
					for(int h = 0; h < corrWidth / 2; h++){
						
					//Abfrage, ob Arraygrenzen nicht überschritten werden
						if(estimatedCenterX - h + binWidth * estimatedCenterY >= 0 && estimatedCenterX - h + binWidth * estimatedCenterY < length){	
						
							if(pixels[estimatedCenterX - h + binWidth * estimatedCenterY] != white)
								widthLeft = h;
							else 
								widthLeft++;
						}
						else
							break;
					}
					
					//rechts
					 
					for(int k = 0; k < corrWidth / 2; k++){
						
					//Abfrage, ob Arraygrenzen nicht überschritten werden
						if(estimatedCenterX + k + binWidth * estimatedCenterY >= 0 && estimatedCenterX + k + binWidth * estimatedCenterY < length){		
						
							if(pixels[estimatedCenterX + k + binWidth * estimatedCenterY] != white)
								widthRight = k;
							else 
								widthRight++;
						}
						else
							break;
					}
					
					
					int tmpWidth = Math.abs(widthLeft) + Math.abs(widthRight);
					int tmpHeight = Math.abs(heightTop) + Math.abs(heightBottom);
					
					Cloud toAdd = new Cloud(estimatedCenterX, estimatedCenterY, tmpWidth, tmpHeight,numOfIteration);
					currentCorrespondence.add(toAdd);
				}
				
			}
			
			
			//Andernfalls liegt das Zentrum nicht mehr im Bild
			else
				continue;
			
		}
		
		
		return currentCorrespondence;
	}

	public static ArrayList<Cloud> startTracking(ImageProcessor binary, int numOfIteration){

		ArrayList<Cloud> cloudList = new ArrayList<Cloud>();
		
		int white = -1;
		int widthP = binary.getWidth();
		int heightP = binary.getHeight();
		//counter für grauwert der einzelnen wolken
		byte hueC=10;
		//counter für nummerierung der wolken
		byte nC=0;
		
		//binärbild anzeigen
		ImagePlus ipb=new ImagePlus("binary",binary);
		//ipb.show();
	
		int [] pixels = (int[]) binary.getPixels();
		
		int length = pixels.length;
		
		//merkerbild
		boolean [] marker = new boolean[pixels.length];
		//kontrollbild zur anzeige
		byte [] show = new byte[length];
		
		
		ArrayList <int[]> fifo = new ArrayList<int[]>();
		
		
		for(int y = 0; y < heightP; y++ ){
			for(int x = 0; x < widthP; x++){
				
				//startpunkt gefunden
				if(pixels[y*widthP + x] == white && !marker[y*widthP + x]){
					
					hueC+=10;
					nC+=1;
					
					int[] xy={x,y};
					
					int centerX = 0, centerY = 0, cloudWidth = 0, cloudHeight = 0;
					int minX = x, minY = y, maxX = x, maxY = y;
					
					fifo.add(xy);
					
					marker[y * widthP + x] = true;
					show[y * widthP + x] = hueC;
					
					while(!fifo.isEmpty()){
						
						
						
						
						int [] coordXY = fifo.remove(fifo.size()-1);
								
						//links		
						if(coordXY[1] * widthP + coordXY[0] - 1 > 0 && coordXY[1] % widthP != 0){
						
							if(pixels[coordXY[1] * widthP + coordXY[0] - 1] == white && !marker[coordXY[1] * widthP + coordXY[0] - 1]){
							
								int[] xyL={coordXY[0] - 1,coordXY[1]};
								fifo.add(xyL);
								if(xyL[0]<minX)
									minX=xyL[0];
								
								marker[coordXY[1] * widthP + coordXY[0]] = true;
								show[coordXY[1] * widthP + coordXY[0]] = hueC;
								}
							 
							 
						}
						
						//rechts
						if(coordXY[1] * widthP + coordXY[0] + 1 < length && coordXY[1] % widthP != (widthP-1)){
						
							if(pixels[coordXY[1] * widthP + coordXY[0] + 1]== white && !marker[coordXY[1] * widthP + coordXY[0] + 1]){
							
								int[] xyR={coordXY[0] + 1, coordXY[1]};
								fifo.add(xyR);
								if(xyR[0]>maxX)
									maxX=xyR[0];
								
								marker[coordXY[1] * widthP + coordXY[0]] = true;
								show[coordXY[1] * widthP + coordXY[0]] = hueC;
								}
						}
						
						//oben
						if((coordXY[1] - 1) * widthP + coordXY[0] > 0 && coordXY[0] != 0){
						
							if(pixels[(coordXY[1]-1) * widthP + coordXY[0]] == white && !marker[(coordXY[1]-1) * widthP + coordXY[0]]){
							
								int[] xyO={coordXY[0],coordXY[1]-1};
								fifo.add(xyO);
								if(xyO[1] * widthP < minY)
									minY=xyO[1];
							
								marker[coordXY[1] * widthP + coordXY[0]] = true;
								show[coordXY[1] * widthP + coordXY[0]] = hueC;
								}
						}
						
						//unten
						if((coordXY[1] + 1) * widthP + coordXY[0] < length && coordXY[0] != (heightP-1)){
							
							if(pixels[(coordXY[1]+1) * widthP + coordXY[0]] == white && !marker[(coordXY[1]+1) * widthP + coordXY[0]]){
								
								int[] xyU={coordXY[0],coordXY[1]+1};
								fifo.add(xyU);
								if(xyU[1]* widthP >maxY)
									maxY=xyU[1];
								
								marker[coordXY[1] * widthP + coordXY[0]] = true;
								show[coordXY[1] * widthP + coordXY[0]] = hueC;
								}
						}
					
						
					//	hueC+=10; //wann stoppen?
					//	nC+=1;
						
					
					}//end while
					
					
					
					centerX=minX + (maxX-minX)/2;
					centerY= minY + (maxY-minY)/2;
					cloudWidth=maxX-minX;
					cloudHeight=maxY-minY;
					
					Cloud tmp = new Cloud(centerX,centerY,cloudWidth,cloudHeight, numOfIteration);
					//Cloud tmp = new Cloud(centerX,centerY,cloudWidth,cloudHeight,nC);

					if(tmp.getHeight() != 0 && tmp.getWidth() != 0){
						cloudList.add(tmp);
					}
					
					
					
					
				}//end for
			}//end for
			
			
		}//end find borders
		
		//resultat anzeigen   
		
		ImageProcessor ip= new ByteProcessor(widthP,heightP, show);
		ImagePlus imp =new ImagePlus("show",ip);
		//imp.show();
		
		return cloudList;
	}
	
	
	public static ArrayList<Cloud> Tracking3 (ArrayList<CloudPair> ref, ImageProcessor binary, int numOfIteration){
		
		
		ArrayList<Cloud> cloudList = new ArrayList<Cloud>();
					
		
			
			int white = -1;//255
			int widthP = binary.getWidth();
			int heightP = binary.getHeight();
			byte counter=10;
		
			byte [] pixels = (byte[]) binary.getPixels();
			byte [] show = new byte[widthP*heightP];
			
			int length = pixels.length;
					
			
			// durchlaufe alle wolken
			while (ref.size()!=0){
			
			CloudPair cp= ref.remove(ref.size()-1); //poll
			Cloud c=cp.getCorrespondence();
					
			int height=c.getHeight();
			int width=c.getWidth();
			int x=c.getX();
			int y=c.getY();
			Vec2 motion=cp.getMotionVec();
			int dirX = motion.getX();
			int dirY = motion.getY();
			int estimatedCenterX;
			int estimatedCenterY;
			
			//noch zu tun:nähe zum rand prüfen?
			
			
if(x + dirX + y + dirY * widthP >= 0 && x + dirX + y + dirY * widthP < length){
				
			estimatedCenterX = x + dirX;
			estimatedCenterY = y + dirY;
			
if(pixels[estimatedCenterX + widthP * estimatedCenterY] != white){
	continue; //raus aus dem durchlauf
}	
}


			
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
			
			Cloud cn=new Cloud(x,y,width,height,numOfIteration);
			
			cloudList.add(cn);
			
			} // ende while
			
			
			
//			ImageProcessor ip= new ByteProcessor(widthP,heightP, show);
//			ImagePlus imp =new ImagePlus("show",ip);
//			imp.show();
			
			return cloudList;
			
		} // ende tracking
	
}
}
