package game;

public class Game {
	Board gameBoard;
	Player offender, defender;
	int turn;
	int strength;
	
	protected Game() {
		this.offender = new Player();
		this.defender = new Player();
		this.gameBoard = new Board();
		turn = 1;				// odd for offender, even for defender
		strength = 1;
	}
	
	protected Game(Game other){
		this.gameBoard = new Board(other.getGameBoard());
		this.offender = new Player(other.getOffender());
		this.defender = new Player(other.getDefender());
		this.turn = other.getTurn();
		this.strength = other.strength;
	}
	
	public Board getGameBoard() {
		return gameBoard;
	}

	public void setGameBoard(Board gameBoard) {
		this.gameBoard = gameBoard;
	}

	public Player getOffender() {
		return offender;
	}

	public void setOffender(Player offender) {
		this.offender = offender;
	}

	public Player getDefender() {
		return defender;
	}

	public void setDefender(Player defender) {
		this.defender = defender;
	}

	public int getTurn() {
		return turn;
	}

//	public void incTurn() {
//		this.turn += 1;
//	}

	public void setTurn(int turn) {
		this.turn = turn;
	}
	
	public boolean gameEnd(){
//		return(	turn == 50 || (offender.getAvLadCount() == 0 && defender.getAvLadCount() == 0 ) );
		return(	turn >= 50 || evaluateState()==true );
	}
		
	public void displayBoard(){
		gameBoard.displayBoard(offender, defender);
	}
	
	public void updateState(String move) {
		if(turn%2 == 1) {
			offender.updateState(move);
			defender.decLadWeight(move);
		} else {
			defender.updateState(move);
			offender.decLadWeight(move);
		}
		this.gameBoard.removeMove(move);
		turn++;
	}
	
	public boolean evaluateState(){
		if(turn%2 == 0) {
			defender.evaluateState(offender);
			return offender.evaluateState(defender);
		} else {
			offender.evaluateState(defender);
			return defender.evaluateState(offender);
		} 
	}

	public int getStrength() {
		return strength;
	}

	public void setStrength(int strength) {
		this.strength = strength;
	}
	
}
