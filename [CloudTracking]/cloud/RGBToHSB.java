package cloud;

import java.awt.Color;


public class RGBToHSB {
	
	public static float[][][] pixelArray (Color RGBArray[][]){
		
		float[][][] HSBArray = new float[RGBArray.length][RGBArray[0].length][3];
		
		//Iteration über alle Pixel im RGBBild
		for(int i = 0; i < RGBArray.length; i++){
			for(int j = 0; j < RGBArray[i].length; j++){
				
				//Konvertierung in den HSB Farbarum
				HSBArray[i][j] = Color.RGBtoHSB(RGBArray[i][j].getRed(), RGBArray[i][j].getGreen(), RGBArray[i][j].getBlue(), HSBArray[i][j]);
			}
		}
		return HSBArray;
	}
}
