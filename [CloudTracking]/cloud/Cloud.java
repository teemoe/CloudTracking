package cloud;

public class Cloud implements Comparable<Cloud> {
	
	private int x,y,width,height,numberOfIteration,cloudNumberInPicture;
	
	//Threshold fürs matching
	private int thresh = 3;
	 
	public Cloud(int x, int y, int width, int height, int iter) {

		this.x=x;
		this.y=y;
		this.width = width;
		this.height = height;
		this.numberOfIteration = iter;

	}
	



	public Vec2 computeCenterDistance(Cloud corr){
				
		int vecX = corr.getX() - this.getX();
		int vecY = corr.getY() - this.getY();
		
		return new Vec2(vecX, vecY);	
	
	}
	
	
	//Noch zu überdenken: Schwelle für übereinstimmende Größen (Variable thresh)
	
	
	public boolean findMatchingCloud(Cloud corr){
		
		if(Math.abs(corr.getHeight() - this.getHeight()) <= thresh && Math.abs(corr.getWidth() - this.getWidth()) <= thresh)
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
	
	public int getNumberOfIteration() {
		return numberOfIteration;
	}


	public void setNumberOfIteration(int numberOfIteration) {
		this.numberOfIteration = numberOfIteration;
	}


	public int getThresh() {
		return thresh;
	}


	public void setThresh(int thresh) {
		this.thresh = thresh;
	}


//vergleich nach fläche; 
	public int compareTo(Cloud other) {
		
//		if(this.height*this.width > other.height*other.width)
//			return 1;
//		
//		else if (this.height*this.width < other.height*other.width)
//			return -1;
//			
//		else 	
//			return 0;
		
		return this.height*this.width - other.height*other.width;
	}
	
//berechnet abstand zweier wolken
	public double getDist(Cloud other){
		Vec2 v=new Vec2(this.getX()-other.getX(),this.getY()-other.getY());
		double dist=v.computeVectorLength();
		return dist;
	}


	
}
