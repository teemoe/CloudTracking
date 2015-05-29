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
	}
	
	public void determineMotionVec(){
		
	}
	
	public void calculateVelocity(){
		
	}
	
	public void determineVectorDirection(){
		
		if(motionVec.getX() == 0 && motionVec.getY() < 0)
			vecDirection = "North";

		if(motionVec.getX() > 0 && motionVec.getY() < 0)
			vecDirection = "NorthEast";		

		if(motionVec.getX() > 0 && motionVec.getY() == 0)
			vecDirection = "East";

		if(motionVec.getX() > 0 && motionVec.getY() > 0)
			vecDirection = "SouthEast";
		
		if(motionVec.getX() > 0 && motionVec.getY() == 0)
			vecDirection = "South";
		
		if(motionVec.getX() < 0 && motionVec.getY() > 0)
			vecDirection = "SouthWest";
		
		if(motionVec.getX() < 0 && motionVec.getY() == 0)
			vecDirection = "West";
		
		if(motionVec.getX() < 0 && motionVec.getY() < 0)
			vecDirection = "NorthWest";
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
