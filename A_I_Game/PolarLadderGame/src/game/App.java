package game;

import java.util.Scanner;

public class App {

	public static String[] Message = 
		{	"Choose a play mode ",
		
			"1	--> human VS A.I.  \n2	--> A.I. VS human \nelse	--> human VS human",
			
			"Enter human player's name :	",

			"Level	|	average thinking time in sec\\move \n"+
			"  1	|		0.032 (Default) \n" 	+
			"  2	|		0.080 \n" +
			"  3	|		2\n" +
			"  4+	|		more than 15 \n"	+
			"___________________________________\n"+
			"Enter A.I. playing strength level: "
		};
	
	public static int mode = 0;	 		//	0 --> human VS human,  1	--> human VS A.I.  ,  2	--> A.I. VS human 
	public static String off = "", def = "";
	public static int gameStrength = 1;
	
	public static void choosePlayMode(){
		setPlayMode();
		if( mode == 1){
			off = getHumanName() + " (Human)";
			def = " (A.I.)";
			setAIStrength();
		} else if (mode == 2) {
			off = " (A.I.)";
			def = getHumanName() + " (Human)";
			setAIStrength();
		} else {
			mode = 0;
			off = getHumanName() + " (Human)";
			def = getHumanName() + " (Human)";
		}
	}
		
	public static void setPlayMode(){
		@SuppressWarnings("resource")
		Scanner userInput = new Scanner(System.in);
		System.out.println(Message[0] +"\n" + Message[1]);
		while( !userInput.hasNextInt() ){
			System.out.print(Message[0] +"\n" + Message[1]);
			userInput.next();
		}
		mode = userInput.nextInt();
	}
	
	private static String getHumanName(){
		String hName = "George";
		Scanner userInput = new Scanner(System.in);
		System.out.println(Message[2]);
		while( !userInput.hasNext() ){
			System.out.print(Message[2]);
			userInput.next();
		}
		hName = userInput.next();
		return hName;
	}
	
	public static void setAIStrength(){

		Scanner userInput = new Scanner(System.in);
		System.out.print(Message[3]);
		while( !userInput.hasNextInt() ) {
			System.out.println(Message[3]);
			userInput.next();
		}
		gameStrength = userInput.nextInt();
		if(gameStrength <= 0 || gameStrength > 49){
			System.out.println("Invalid difficulty level, Playing default = 1");
			gameStrength = 1;
		}
	}
	
	public static String getNewMove(int turn, MinMax robot){
		String nextMove = null;
		@SuppressWarnings("resource")
		Scanner userInput = new Scanner(System.in);
		if(turn%2 == 1){  	// offender's turn
			switch (mode){
				case 1:
					System.out.print("\n" + off + "	Enter a valid move:	");
					nextMove = (userInput.next()).toUpperCase();
					break;
				case 2:
					nextMove = robot.suggestMove();
					break;
				default:
					System.out.print("\n" + off + "	Enter a valid move:	");
					nextMove = (userInput.next()).toUpperCase();
			}
		} else {			// defender's turn
				switch (mode){
				case 1:
					nextMove = robot.suggestMove();
					break;
				case 2:
					System.out.print("\n" + def + "	Enter a valid move:	");
					nextMove = (userInput.next()).toUpperCase();
					break;
				default:
					System.out.print("\n" + def + "	Enter a valid move:	");
					nextMove = (userInput.next()).toUpperCase();
			}
		}
		return nextMove;	
	}
	
	public static void startNewGame(Game game){

		System.out.println("------------------------ <<<< Welcome to Polarized Ladder >>>> ------------------------");
		choosePlayMode();
		game.setStrength(gameStrength);
		System.out.println(off +" starts the game with symbol X Against " + def + " with symbol O");
		System.out.println("\n---------------------------------------------------------------------");
		System.out.println("Enter a letter [a-m] (lower or upper case) + number [1-7] like g5 or B2");
		game.displayBoard();
		System.out.println("\n_______________________________________________________________");
		String nextMove = null;
		int turn = 1;
		MinMax robot = null;
		while (! game.gameEnd()) {		
			robot = new MinMax(game);
			turn = game.getTurn();	
			nextMove = getNewMove(turn, robot);
			if(nextMove.equals("ZZ") ){
				System.out.println("\n**************************  GoodBye  ***************************");
				System.exit(0);
				}
			
			while (!game.getGameBoard().validateMove(nextMove)) {
				nextMove = getNewMove(turn, robot);
				if(nextMove.equals("ZZ") ){
					System.out.println("\n**************************  GoodBye  ***************************");
					System.exit(0);
					}
			}
			game.updateState(nextMove);
			game.displayBoard();
			System.out.println("\n_______________________________________________________________");
			if (game.evaluateState() == true) {
				if (turn%2 == 1) {
					if(mode != 2){
						System.out.println("\nCongratulations " + off + " using (X) Wins");
					} else {
						System.out.println("\nOOPS " + off + " using (X) Wins");
					}

				} else {
					if(mode != 1){
						System.out.println("\nCongratulations " + def + " using (O) Wins");
					} else {
						System.out.println("\nOOPS " + def + " using (O) Wins");
					}
				}
				return;
			}
		}
		System.out.println("\nIt's a Tie");
	}
	
	public static void main(String[] args) {
		
		Scanner uInput = new Scanner(System.in);
		String userChoice = null;
		System.out.println("Enter zz to quit the game at any time, else to start a new game");
		
		while(uInput.hasNext()){
			userChoice = uInput.next();
			while (!userChoice.equals("zz")) {
				Game newGame = new Game();
				startNewGame(newGame);

				System.out.println("Enter zz to quit the game, else to start a new game");
				userChoice = uInput.next();
			}
			System.out.println("\n**************************  GoodBye  ***************************");
			System.exit(0);
		}
	}
}
