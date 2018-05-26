package game;

import java.util.ArrayList;

import game.GameNode.NodeType;

public class MinMax {
	public static long TIME_LIMIT = 20000 ; // milliseconds
	Game currentGame;
	
	public MinMax(Game game){
		currentGame = new Game(game);
	}
		
	// iterative deepening MinMax
	public String suggestMove() {
		long elapsedTime = 0;
		int startItr = currentGame.getStrength();
		String bestMoveSoFar = null;
		long startTime = System.currentTimeMillis();
		bestMoveSoFar = getBestMove(startItr);
		elapsedTime = System.currentTimeMillis() - startTime;
		System.out.println("Move time " + elapsedTime);
//		// for iterative deepening 
//		do{
//			bestMoveSoFar = getBestMove(startItr);
//			startItr += 1;
//			elapsedTime = System.currentTimeMillis() - startTime;
//		} while (elapsedTime < TIME_LIMIT);
		
		return bestMoveSoFar;
	}

	public String getBestMove(int depth){
		GameNode rootNode = new GameNode(currentGame);
		rootNode.setType(NodeType.MAX);
		System.out.println("root type is " + rootNode.type);

		int finalResult = minMaxAlphaBeta(depth, rootNode, -100000, 100000);
		System.out.println("finalResult = " + finalResult);
		System.out.println("best move " + rootNode.bestMove);
		return rootNode.bestMove;
	}

	// outputs the node value
	public int minMaxAlphaBeta(int depth, GameNode gNode, int alpha, int beta){
//		System.out.println("Depth= " + depth + ", alpha = " + alpha + ", beta= "+ beta);
		GameNode nextChild = null;
		int val;
		// if gNode is a leaf, return value of gNode
		if(gNode.game.gameEnd() || depth == 0){
			val = gNode.getNodeScore();
			System.out.println("Leaf Node value = " + val);
			return val;
		} 
		else if(gNode.type == NodeType.MAX) {
			ArrayList<String> avMoves = gNode.game.getGameBoard().getLegalPositions();
			for(String move: avMoves){
				
				System.out.println("Try Move 	" + move + "	Turn = " + gNode.game.getTurn() + " Type: MAX");
				nextChild = gNode.getNextChild(move, depth);
				int newAlpha = minMaxAlphaBeta(depth-1, nextChild, alpha, beta);
				if(newAlpha > alpha){
					gNode.setBestMove( move );
					System.out.println("Max Best: " + move + " with Score " + nextChild.getNodeScore());
					alpha = newAlpha;
				} else {
//					System.out.println("Max Best still: " + gNode.getBestMove() + " with Score " + gNode.getNodeScore());
				}
				if( alpha >= beta){		// pruning
					return beta;
				}
			}
			return alpha;
		} 
		else {
			ArrayList<String> avMoves = gNode.game.getGameBoard().getLegalPositions();
			for(String move: avMoves){
				System.out.println("Try Move	" + move + "	Turn = " + gNode.game.getTurn()+ " Type: MIN");
				nextChild = gNode.getNextChild(move, depth);
				int newBeta = minMaxAlphaBeta(depth-1, nextChild, alpha, beta);
				if(newBeta < beta){
					gNode.setBestMove( move );
					System.out.println("Min Best:	" + move + " with Score " + nextChild.getNodeScore());
					beta = newBeta;
				} else {
//					System.out.println("Min Best still: " + gNode.getBestMove() + " with Score " + gNode.getNodeScore());
				}
				if( alpha >= beta){		// pruning
					return alpha;
				}
			}
			return beta;
		}
	}
		
}
