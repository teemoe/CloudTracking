package cloud;

public class Cloud {
	
	private int x,y,width,height,numberOfIteration,cloudNumberInPicture;
	
	//Threshold fürs matching
	private int thresh = 3;
	 
	public Cloud(int x, int y, int width, int height, int iter, int num) {

		this.x=x;
		this.y=y;
		this.width = width;
		this.height = height;
		this.numberOfIteration = iter;
		this.cloudNumberInPicture = num;
	}
	

	public int getNumberOfIteration() {
		return numberOfIteration;
	}


	public void setNumberOfIteration(int numberOfIteration) {
		this.numberOfIteration = numberOfIteration;
	}


	public int getCloudNumberInPicture() {
		return cloudNumberInPicture;
	}


	public void setCloudNumberInPicture(int cloudNumberInPicture) {
		this.cloudNumberInPicture = cloudNumberInPicture;
	}


	public int getThresh() {
		return thresh;
	}


	public void setThresh(int thresh) {
		this.thresh = thresh;
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


	
}
