package tracking;

import java.awt.List;
import java.util.ArrayList;

public class ArrayConv {

	
	public static int[] two2one(int[][] arr) {
		
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
}