package tracking;

import ij.ImagePlus;
import ij.process.ByteProcessor;
import ij.process.ImageProcessor;

import java.util.ArrayList;

import cloud.Cloud;

public class startTracking {
	
	private ArrayList<Cloud> startTracking(ImageProcessor binary){

		ArrayList<Cloud> cloudList = new ArrayList<Cloud>();
		
		int white = -1;
		int widthP = binary.getWidth();
		int heightP = binary.getHeight();
		//counter für grauwert der einzelnen wolken
		byte hueC=10;
		//counter für nummerierung der wolken
		byte nC=0;
		
		//binärbild anzeigen
		new ImagePlus(" ",binary).show();
	
		byte [] pixels = (byte[]) binary.getPixels();
		
		int length = pixels.length;
		
		//merkerbild
		boolean [] marker = new boolean[pixels.length];
		//kontrollbild zur anzeige
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
					
					
					}//end while
					
					centerX=minX + (maxX-minX)/2;
					centerY= minY + (maxY-minY)/2;
					cloudWidth=maxX-minX;
					cloudHeight=maxY-minY;
					
					Cloud tmp = new Cloud(centerX,centerY,cloudWidth,cloudHeight);
					//Cloud tmp = new Cloud(centerX,centerY,cloudWidth,cloudHeight,nC);

					if(tmp.getHeight() != 0 && tmp.getWidth() != 0){
						cloudList.add(tmp);
					}
					
					hueC+=10; //>128 ausschließen
					nC=1;
					
				}//end for
			}//end for
			
			
		}//end find borders
		
		//resultat anzeigen   
		
		ImageProcessor ip= new ByteProcessor(widthP,heightP, show);
		ImagePlus imp =new ImagePlus(" ",ip);
		imp.show();
		
		return cloudList;
	}
	

}
