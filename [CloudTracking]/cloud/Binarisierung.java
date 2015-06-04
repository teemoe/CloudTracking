package cloud;

public class Binarisierung {
	
	/**
	 * Erzeugen eines Binärbildes
	 * mit true = weiß --> Wolke
	 * und false = schwarz --> Himmel
	 * @param HSBArray
	 * @return
	 */
	public static boolean[][] binaryPicture (float[][][] HSBArray){
		
		boolean[][] binaryPicture =  new boolean[HSBArray.length][HSBArray[0].length];
		
		for(int i = 0; i < HSBArray.length; i++){
			for(int j = 0; j < HSBArray[i].length; j++){
				
				//Testen auf nicht Himmel (Wolke)
				 if(HSBArray[i][j][1] < 0.3){
					 binaryPicture[i][j] = true;
				 }
			}
		}
		
		return binaryPicture;
	}
	
}
