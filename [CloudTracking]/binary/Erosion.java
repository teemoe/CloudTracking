package binary;

import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;

import ij.*;
import ij.process.ImageProcessor;

public class Erosion {
	//das hatte ich letzte woche iwo bei den "java bibliotheken" gesehen aber das ist anscheinend rekursiv
	/*public static ImageProcessor erode(ImageProcessor temp, int [][] filter){
ImageProcessor result= erode(temp, filter);

return result;*/
	public static ImageProcessor erode (ImageProcessor proc){
		Image image=proc.createImage();
		// Cast von Image in BufferedImage
		BufferedImage img = (BufferedImage) image;
		// Erzeugen des binArray
		int [][] binImage= new int [img.getWidth()][img.getHeight()];
		for (int i=0; i<binImage.length;i++){
			for( int j=0; j<binImage[i].length; j++){
				binImage [i][j]= img.getRGB(i, j);
			}
		}
		//public static int[][] erosion( int array[][]){
		int[][] newarray = new int[binImage.length][binImage[0].length]; 

		for(int i = 1; i < binImage.length; i++){
			for (int j=1; j<binImage[i].length;j++){
				if (binImage[i-1][j-1]==0 &&binImage[i][j-1]==255 && binImage[i+1][j-1]==255
						&& binImage[i-1][j]==0 && binImage[i][j]==255 && binImage[i+1][j]==255
						&& binImage [i-1][j+1]==0 &&binImage[i][j+1]==255&&binImage[i+1][j+1]==255){
					newarray[i][j]=255;
				}
				else newarray[i][j]=0;	
			}
		}
		for (int x=0; x<binImage.length;x++){
			for (int y=0; y<binImage[x].length;y++){
				proc.putPixel(x, y, newarray[x][y]);
			}
		}
		return proc;	
	}
}