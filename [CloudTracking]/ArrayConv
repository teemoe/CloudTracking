package tracking;

import ij.process.ImageProcessor;

import java.util.ArrayList;

 
public class ArrayConv {

	
	public static byte[] two2oneB(int[][] arr) {
		
	    ArrayList <Integer> list = new ArrayList<Integer>();
	    for (int i = 0; i < arr.length; i++) {
	       	        for (int j = 0; j < arr[0].length; j++) { 
	                  list.add(arr[i][j]); 
	        }
	    }

	    byte[] vector = new byte[list.size()];
	    for (int i = 0; i < vector.length; i++) {
	    	int v=list.get(i);
	    //int vn=new Integer(v);
	    //	byte b=(byte)vn;
	        vector[i] = (byte)v;
	    }
	    return vector;
	}
	
	
public static int[] two2oneI(int[][] arr) {
		
	    ArrayList <Integer> list = new ArrayList<Integer>();
	    for (int i = 0; i < arr.length; i++) {
	       	        for (int j = 0; j < arr[i].length; j++) { 
	                  list.add(arr[i][j]); 
	        }
	    }

	    int[] vector = new int[list.size()];
	    for (int i = 0; i < vector.length; i++) {
	    	vector[i] = list.get(i);
	    }
	    return vector;
	}


public static int[] getPix(ImageProcessor binary){
	int [] pixelsB = (int[]) binary.getPixels();
	return pixelsB;
}



}
