package game;

import java.util.ArrayList;

public class Board {
		
	public static final int GRID_COUNT = 49;
	char board [] = new char[GRID_COUNT];

	char [] legalLetters = {'A', 'M', 'B' , 'L' , 'C' , 'K' , 'D' , 'J' , 'E', 'I' , 'F', 'H', 'G'};
	String [] legPos = {
			"G3", 
			"F3", "H3", 
			"G2", "G4", "F2", "F4", "E3", "H2", "H4", "I3",
			"E2", "I2", "E4", "I4", "D3", "J3",
			"G5", "D2", "J2", 
			"F5", "H5",
			"G6", "D4", "E5", "J4", "I5", "C2", "K2", "E1", "F1", "G1", "H1", "I1",
			"F6", "H6", "C3", "K3", "D1", "J1",
			"G7", "B2", "L2", "B1", "C1", "K1", "L1",
			"A1", "M1" };
	
	ArrayList<String> legalPositions;
	
	public Board(){
		this.legalPositions = new ArrayList<String>();
		initializeLegalPositions();
	}
		
	public Board(Board other) {
		this.legalPositions = new ArrayList<String>();
		for(String pos: other.getLegalPositions()) {
			this.legalPositions.add(pos);
		}
	}
	
	private void renderBoard(Player offender, Player defender){
		String offenderBinStr = offender.toString();
		String defenderBinStr = defender.toString();
		
		int gridCtr = GRID_COUNT-1;
		for(int i = 0; i < GRID_COUNT; i++){
			if (offenderBinStr.charAt(i) == '1'){
				board[gridCtr] = 'X';
			} else if(defenderBinStr.charAt(i) == '1'){
				board[gridCtr] = 'O';
			} else {
				board[gridCtr] = '.';
			}
			gridCtr--;
		}
	}
	
	public void displayBoard(Player offender, Player defender){
//		System.out.println(legalPositions.toString());
		renderBoard(offender, defender);
		int k = 6, mid = 1; 
		int gridCtr = GRID_COUNT-1;
		
		for(int i = 7; i > 0; i--){
			System.out.print("\n\n"+ i + "	");
			int j = 0;
			
			for(j = 0; j < k; j++){
				System.out.print("    "); }
			
			for(j = 0; j < mid; j++){
				System.out.print(board[gridCtr] + "   ");
				gridCtr--;}
			
			for(j = 0; j < k; j++){
				System.out.print("    ");}
			k--; mid +=2;
		}
			System.out.print("\n\n	");
			System.out.print("A   B   C   D   E   F   G   H   I   J   K   L   M");
	}

	private void initializeLegalPositions() {
		for(int i=0; i < legPos.length ;i++){
			legalPositions.add(legPos[i]);
		}
//		int k = 0;
//		for(int i = 1; i <= 13; i++){
//			char col = legalLetters[i-1];
//			k = (int) Math.floor((i+1)/2);
//			for(int j = 1; j <= k; j++){
//				legalPositions.add(col+""+j);
//			}
//		}
	}
		
	public boolean validateMove(String aMove){
		if(legalPositions.contains(aMove)){
			legalPositions.remove(aMove);
			return true;
		}
		return false;
	}

	public ArrayList<String> getLegalPositions() {
		return legalPositions;
	}
	
	public void removeMove(String aMove){
		this.legalPositions.remove(aMove);
	}
	
}
