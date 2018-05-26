package game;

public class Ladder {
	Long winPattern, neutralPattern;
	int ladWeight;
	
	protected Ladder(Long winPattern, Long neutralPattern) {
		super();
		this.winPattern = winPattern;
		this.neutralPattern = neutralPattern;
		ladWeight = 2;
	}
	
	protected Ladder(Ladder otherLad){
		this.winPattern = new Long(otherLad.winPattern);
		this.neutralPattern = new Long(otherLad.neutralPattern);
		this.ladWeight = otherLad.getLadWeight();
	}

	public Long getWinPattern() {
		return winPattern;
	}

	public void setWinPattern(Long winPattern) {
		this.winPattern = winPattern;
	}

	public Long getNeutralPattern() {
		return neutralPattern;
	}

	public void setNeutralPattern(Long neutralPattern) {
		this.neutralPattern = neutralPattern;
	}
	
	public int getLadWeight() {
		return ladWeight;
	}

	public void setLadWeight(int ladWeight) {
		this.ladWeight = ladWeight;
	}

	public void addWeight(){
//		if(ladWeight == 0) {
//			ladWeight = 1;
//		} 
		ladWeight *= 2;
	}
	
	public void subWeight(){
//		if(ladWeight == 2) {
//			ladWeight = 0;
//		} 
		ladWeight /= 2;
	}
	
	@Override
	public boolean equals(Object other){
	    if (other == null) return false;
	    if (other == this) return true;
	    if (!(other instanceof Ladder))return false;
	    Ladder otherLadder = (Ladder)other;
	    return(otherLadder.winPattern.equals(this.winPattern));
	    
	}
	
	public String toString(){
		return "( " + Long.toBinaryString(winPattern) + " , " + Long.toBinaryString(neutralPattern) + " )";
	}
	
}
