package game;

import java.util.ArrayList;
import java.util.Iterator;

public class Player {
	Long bitBoard;			// player occupied positions represented as a Long of binary bits
	ArrayList<Ladder> availableLadders;
	int score;
	
	protected Player(){
		this.bitBoard = 0L;
		availableLadders = new ArrayList<Ladder>(50);
		generateLadders();
	}
		
	public Player(Player pl) {
		this.bitBoard = new Long(pl.getBitBoard());
		this.availableLadders = new ArrayList<Ladder>();
		for(Ladder lad: pl.getAvailableLadders()) {
			this.availableLadders.add(new Ladder(lad));
		}
		this.score = pl.score;
	}
	
	public Long stringToBit(String moveStr){
		int col = (77-moveStr.charAt(0));
		int row = Integer.parseInt(moveStr.charAt(1)+"");
		int pos = (row-1)*(15 - row) + col - row + 1 ;
		long moveBit = 0B1L << (pos);
		return moveBit;
	}
	
	public void updateState(String moveStr){
		long moveBit = stringToBit(moveStr);
		bitBoard = (bitBoard | moveBit);
		incLadWeight(moveBit);
//		System.out.println("Player new Status after move	" + this.toString());
	}
	
	public void incLadWeight(long aMoveBit){
		for(Ladder aLad: this.availableLadders){
			long winBit = aLad.getWinPattern();
			
			if( (winBit & aMoveBit) != 0){
				aLad.addWeight();
			} 
		} 
	}
	
	public void decLadWeight(String moveStr){
		long moveBit = stringToBit(moveStr);
		for(Ladder aLad: this.getAvailableLadders()) {
			
			if( (moveBit & aLad.getWinPattern()) != 0  ){
				aLad.setLadWeight(0);
			} 
			else if( (moveBit & aLad.getNeutralPattern()) != 0  ){
				aLad.subWeight();
			}
		}
	}
	
	public boolean evaluateState(Player opponent){
		
		this.score = 0;
		Long opponentBoard = opponent.getBitBoard();
		Long winPattern, neuPattern;
		Iterator<Ladder> ladIterator = availableLadders.iterator();
		
		while(ladIterator.hasNext()){
			Ladder aLad = ladIterator.next();
			winPattern = aLad.getWinPattern();
			neuPattern = aLad.getNeutralPattern();
			Long bitWin = this.bitBoard & winPattern; // did i win ?
			Long oppWin = opponentBoard & winPattern; // did opponent destroy my ladder ?
			Long oppNeu = opponentBoard & neuPattern; // did opponent neutralise my ladder?


			// Ladder was destroyed		or		neutralised by opponent ? 
			if( !(oppWin).equals(0L)    ||    ( !neuPattern.equals(0L) && (neuPattern).equals(oppNeu) ) ){
				ladIterator.remove();
			}		
			else if((winPattern).equals( bitWin)){
				return true;
			}
			if(aLad != null){
				this.score += aLad.getLadWeight();
			}
		}
		//System.out.println("Player Score = " + this.score);
		return false;
	}
		
	public int getAvLadCount(){
		return availableLadders.size();
	}
	
	public void removeWinPattern(Ladder aLad){
		availableLadders.remove(aLad);
	}
	
	public String toString(){
		return String.format("%49s", Long.toBinaryString(this.bitBoard)).replace(' ', '0');
	}
	
	public ArrayList<Ladder> getAvailableLadders() {
		return availableLadders;
	}
	
	public Long getBitBoard() {
		return bitBoard;
	}

	public void setBitBoard(Long bitBoard) {
		this.bitBoard = bitBoard;
	}
		
	public void setAvailableLadders(ArrayList<Ladder> availableLadders) {
		this.availableLadders = availableLadders;
	}

	public void generateLadders(){
		Long winLeftPattern, winRightPattern;
		Long neutralLeftPattern, neutralRightPattern; 
		int k = 8;
		for(int i = 0; i < 5; i++){
			winLeftPattern  = ladderStates[i];
			winRightPattern = ladderStates[i+5];

			availableLadders.add(new Ladder(winLeftPattern,0L));
			availableLadders.add(new Ladder(winRightPattern,0L));
			
			for(int j = 1; j <= k; j++){
				winLeftPattern = winLeftPattern << (1);
				winRightPattern = winRightPattern >> (1);
				
				if(j> 1 && j <= k){
					neutralLeftPattern = neutralStates[i] << (j-2);
					neutralRightPattern = neutralStates[i+4] >> (j-2);
					availableLadders.add(new Ladder(winLeftPattern, neutralLeftPattern));
					availableLadders.add(new Ladder(winRightPattern, neutralRightPattern));
				} else {
					availableLadders.add(new Ladder(winLeftPattern,0L));
					availableLadders.add(new Ladder(winRightPattern,0L));
				}
			}
			k-=2;
		}
	}
		
	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	// Numeric Literals representation in Java SE 7
	// https://docs.oracle.com/javase/8/docs/technotes/guides/language/underscores-literals.html
	public static Long[] ladderStates = {
		// ladder direction is left
		0B0_000_00000_0000000_000000001_00000000011_0000000000011L,   // Level 1 (base)  	<< 1-8
		0B0_000_00000_0000001_000000011_00000000011_0000000000000L,   // Level 2			<< 1-6
		0B0_000_00001_0000011_000000011_00000000000_0000000000000L,   // Level 3			<< 1-4
		0B0_001_00011_0000011_000000000_00000000000_0000000000000L,   // Level 4			<< 1-2
		0B1_011_00011_0000000_000000000_00000000000_0000000000000L,   // Level 5			<< 0

		// ladder direction is right
		0B0_000_00000_0000000_100000000_11000000000_1100000000000L,   // Level 1 (base)		>> 1-8
		0B0_000_00000_1000000_110000000_11000000000_0000000000000L,   // Level 2			>> 1-6
		0B0_000_10000_1100000_110000000_00000000000_0000000000000L,   // Level 3			>> 1-4
		0B0_100_11000_1100000_000000000_00000000000_0000000000000L,   // Level 4			>> 1-2
		0B1_110_11000_0000000_000000000_00000000000_0000000000000L,   // Level 5			>> 0
	};
	
	public static Long[] neutralStates = {
		// ladder direction is left
		0B0_000_00000_0000000_000000001_00000000000_0000000010000L,   // Level 1 (base)  	<< 1-6
		0B0_000_00000_0000001_000000000_00000010000_0000000000000L,   // Level 2			<< 1-4
		0B0_000_00001_0000000_000010000_00000000000_0000000000000L,   // Level 3			<< 1-2
		0B0_001_00000_0010000_000000000_00000000000_0000000000000L,   // Level 4			<< 0
		
		// ladder direction is right
		0B0_000_00000_0000000_100000000_00000000000_0000100000000L,   // Level 1 (base)  	<< 1-6
		0B0_000_00000_1000000_000000000_00001000000_0000000000000L,   // Level 2			<< 1-4
		0B0_000_10000_0000000_000010000_00000000000_0000000000000L,   // Level 3			<< 1-2
		0B0_100_00000_0000100_000000000_00000000000_0000000000000L,   // Level 4			<< 0
	};
	
}
