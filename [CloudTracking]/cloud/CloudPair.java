package cloud;



public class CloudPair {
	
	private Cloud reference;
	private Cloud correspondence;
	
	private Vec2 motionVec; // Pixels per second
	private String vecDirection; // direction relative to image plane
	
	private int numOfIteration;
	

	public CloudPair(Cloud ref, Cloud corr, Vec2 motion, int iter){
		
		
		reference = ref;
		correspondence = corr;
		motionVec = motion;
		numOfIteration = iter;
		vecDirection = this.determineVectorDirection();
	}
	
	
	public double calculateVelocity(){
	
		// unter Voraussetung, dass zwischen den Bilder ein zeitlicher Aufnahmeabstand von 3 s ist
		return motionVec.computeVectorLength() / 3;
	}
	
	
	public double timeUntilSunIsReached(int picWidth, int picHeight){
		
		double velocity = this.calculateVelocity();
		double alpha;
		double xStart;
		double yStart;
		double deltaX;
		double deltaY;
		double distance;
		
		
		if(vecDirection.equals("North")){
		
			yStart = correspondence.getY() - correspondence.getHeight() / 2;
			
			distance = yStart;
			
			return distance / velocity;
		}
		
		if(vecDirection.equals("NorthEast")){
			
			alpha = Math.abs(motionVec.getY() / motionVec.getX());
			
			xStart = correspondence.getX();
			yStart = correspondence.getY();
			
			deltaX = picWidth - xStart;
			
			deltaY = alpha * deltaX;
			
			distance = Math.sqrt(deltaX*deltaX + deltaY*deltaY);
			
			return distance / velocity;
		}
		
		if(vecDirection.equals("East")){
			
			xStart = correspondence.getX() + correspondence.getWidth() / 2;
			distance = picWidth - xStart;
			
			return distance/velocity;
		}
		if(vecDirection.equals("SouthEast")){

			alpha = Math.abs(motionVec.getX() / motionVec.getY());
			
			xStart = correspondence.getX();
			yStart = correspondence.getY();
			
			deltaX = picWidth - xStart;
			
			deltaY = alpha * deltaX;
			
			distance = Math.sqrt(deltaX*deltaX + deltaY*deltaY);
			
			return distance / velocity;
		}
		if(vecDirection.equals("South")){
			
			yStart = correspondence.getY() + correspondence.getHeight() / 2;
			
			distance = picHeight - yStart;
			
			return distance / velocity;
		}
		if(vecDirection.equals("SouthWest")){
			
			alpha = Math.abs(motionVec.getY() / motionVec.getX());
			
			xStart = correspondence.getX();
			yStart = correspondence.getY();
			
			deltaX = xStart;
			
			
			deltaY = alpha * deltaX;
			
			distance = Math.sqrt(deltaX*deltaX + deltaY*deltaY);
			
			return distance / velocity;
		}
		if(vecDirection.equals("West")){
			
			xStart = correspondence.getX() - correspondence.getWidth() / 2;
			distance = xStart;
			
			return distance/velocity;
		}
		if(vecDirection.equals("NorthWest")){

			alpha = Math.abs(motionVec.getX() / motionVec.getY());
			
			xStart = correspondence.getX();
			yStart = correspondence.getY();
			
			deltaX = xStart;
			
			deltaY = alpha * deltaX;
			
			distance = Math.sqrt(deltaX*deltaX + deltaY*deltaY);
			
			return distance / velocity;

		}

		else 
			return 0;
	
	}
	
	public String determineVectorDirection(){
		
		if(motionVec.getX() == 0 && motionVec.getY() < 0)  // If else benutzen, da schneller  (es m�ssen nicht alle F�lle gepr�ft werden)
			vecDirection = "North";						   // Enumeration beutzen --> schneller

		if(motionVec.getX() > 0 && motionVec.getY() < 0)
			vecDirection = "NorthEast";		

		if(motionVec.getX() > 0 && motionVec.getY() == 0)
			vecDirection = "East";

		if(motionVec.getX() > 0 && motionVec.getY() > 0)
			vecDirection = "SouthEast";
		
		if(motionVec.getX() == 0 && motionVec.getY() > 0)
			vecDirection = "South";
		
		if(motionVec.getX() < 0 && motionVec.getY() > 0)
			vecDirection = "SouthWest";
		
		if(motionVec.getX() < 0 && motionVec.getY() == 0)
			vecDirection = "West";
		
		if(motionVec.getX() < 0 && motionVec.getY() < 0)
			vecDirection = "NorthWest";
		
		if(motionVec.getX() == 0 && motionVec.getY() == 0)
			vecDirection = "Central";
		
		return vecDirection == null ? "" : vecDirection;
	}

	public Cloud getReference() {
		return reference;
	}

	public void setReference(Cloud reference) {
		this.reference = reference;
	}

	public Cloud getCorrespondence() {
		return correspondence;
	}

	public void setCorrespondence(Cloud correspondence) {
		this.correspondence = correspondence;
	}

	public Vec2 getMotionVec() {
		return motionVec;
	}

	public void setMotionVec(Vec2 motionVec) {
		this.motionVec = motionVec;
	}

	public String getVecDirection() {
		return vecDirection;
	}

	public void setVecDirection(String vecDirection) {
		this.vecDirection = vecDirection;
	}
	
	public int getNumOfIteration() {
		return numOfIteration;
	}

	public void setNumOfIteration(int numOfIteration) {
		this.numOfIteration = numOfIteration;
	}

}
