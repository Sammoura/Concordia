package game;


/* for MinMax Algorithm */

public class GameNode {
	public enum NodeType {MAX, MIN};	
	Game game;
	Player player, opponent;
	String pcMove, bestMove;		// parent-child(pc)move
	NodeType type;					// Min or Max
	int nodeScore;
	
	protected GameNode(Game otherGame) {
		this.game = new Game(otherGame);
		setPlayers(otherGame);
		nodeScore = 0;
	}
	
	protected GameNode(GameNode other){
		this.game = new Game(other.game);
		this.type = other.type;
		this.player = new Player(other.player);
		this.opponent = new Player(other.opponent);
	}

	private void setPlayers(Game otherGame){
		if(this.game.getTurn()%2 == 1){
			this.player = new Player(otherGame.getOffender());
			this.opponent = new Player(otherGame.getDefender());
		} else {
			this.player = new Player(otherGame.getDefender());
			this.opponent = new Player(otherGame.getOffender());
		}
	}
	
	// copy the parent, apply a move from legal moves, hence it is a child
	protected GameNode getNextChild(String newMove, int depth){
		GameNode childNode = new GameNode(game);
		childNode.type = this.type.equals(NodeType.MAX) ? NodeType.MIN:NodeType.MAX;
		childNode.setPcMove(newMove);
		// players' scores before the move 
		// notice that player becomes opponent and opponent becomes player after applying the move
		int scoreBefore = childNode.player.getScore() - childNode.opponent.getScore();
		childNode.getGame().updateState(newMove);
		childNode.setNodeScore(depth, scoreBefore);
		return childNode;
	}
	
	public void setNodeScore(int depth, int scoreBefore){
		int tempScore = 0 ;
		if(game.evaluateState() == true){
			tempScore = (depth+10)*1000;
		} else if(game.getStrength() == 1){ // for basic level only
			tempScore = calcNodeScore();
		} else {			// for higher levels
			//players' scores after the move
			int scoreAfter = player.getScore() - opponent.getScore();
			tempScore = scoreBefore + scoreAfter; 
		}
		if(type.equals(NodeType.MAX)){
			nodeScore = -tempScore;
		} else {
			nodeScore = tempScore;	
		}
	}
	
	public void setType(NodeType type) {
		this.type = type;
	}

	public int calcNodeScore(){
		int nScore = 0;
		long moveBit = player.stringToBit(this.pcMove);
		for(Ladder myLad: player.getAvailableLadders()){
			if( (moveBit & myLad.getWinPattern()) != 0 ){
				nScore += myLad.getLadWeight()/2;
			}	
		} 
		for(Ladder oppLad: opponent.getAvailableLadders()) {
			if( (moveBit & oppLad.getWinPattern()) != 0  ){
				nScore += oppLad.getLadWeight();
			} 
			else if( (moveBit & oppLad.getNeutralPattern()) != 0  ){
				nScore += oppLad.getLadWeight();
			}
		}
		return nScore;
	}
	
	public Game getGame() {
		return game;
	}

	public String getBestMove() {
		return bestMove;
	}

	public int getNodeScore() {
		return nodeScore;
	}

	public String getPcMove() {
		return pcMove;
	}

	public void setPcMove(String pcMove) {
		this.pcMove = pcMove;
	}

	public NodeType getType() {
		return type;
	}

	public void setBestMove(String bestMove) {
		this.bestMove = bestMove;
	}
	
	
}
