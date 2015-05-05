package cloud;

import java.util.Vector;

public class Cloud {
	
	private int x,y,width,height;
	
	//Threshold fürs matching
	private int thresh = 3;
	 
	public Cloud(int x, int y, int width, int height) {

		this.x=x;
		this.y=y;
		this.width = width;
		this.height = height;
	}
	

	public Vector computeCenterDistance(Cloud ref){
				
		int vecX = ref.getX() - this.getX();
		int vecY = ref.getY() - this.getY();
		
		return new Vector(vecX, vecY);	
	
	}
	
	
	//Noch zu überdenken: Schwelle für übereinstimmende Größen (Variable thresh)
	
	
	public boolean findMatchingCloud(Cloud ref){
		
		if(Math.abs(ref.getHeight() - this.getHeight()) <= thresh && Math.abs(ref.getWidth() - this.getWidth()) <= thresh)
			return true;
		
		else 
			return false;
		
	}
	
	
	
	
	public int getX() {
		return x;
	}


	public void setX(int x) {
		this.x = x;
	}


	public int getY() {
		return y;
	}


	public void setY(int y) {
		this.y = y;
	}


	public int getWidth() {
		return width;
	}


	public void setWidth(int width) {
		this.width = width;
	}


	public int getHeight() {
		return height;
	}


	public void setHeight(int height) {
		this.height = height;
	}


	
}
# CloudTracking
