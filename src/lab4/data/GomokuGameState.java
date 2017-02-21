package lab4.data;

import java.util.Observable;
import java.util.Observer;
import lab4.client.GomokuClient;

/**
 * Represents the state of a game.
 * 
 * @author hugwan-6, leopel-6
 */
public class GomokuGameState extends Observable implements Observer{
	

   // Game variables
	private final int DEFAULT_SIZE = 20;
	private GameGrid gameGrid;
	
    //Possible game states
	private final int NOT_STARTED = 0;
	private int currentState;
	
	private GomokuClient client;
	
	private String message;
	
	private final int MY_TURN = 1;
	private final int OTHERS_TURN = 2;
	private final int FINISHED = 3; 
	
	/**
	 * The constructor
	 * 
	 * @param gc The client used to communicate with the other player
	 */
	public GomokuGameState(GomokuClient gc) {
		client = gc;
		client.addObserver(this);
		gc.setGameState(this);
		currentState = NOT_STARTED;
		gameGrid = new GameGrid(DEFAULT_SIZE);
	}
	

	/**
	 * Returns the message string
	 * 
	 * @return the message string
	 */
	public String getMessageString() {
		return message;
		}
	
	/**
	 * Returns the game grid
	 * 
	 * @return the game grid
	 */
	public GameGrid getGameGrid() {
		return gameGrid;
		}
		
	/**
	 * This player makes a move at a specified location
	 * 
	 * @param x the x coordinate
	 * @param y the y coordinate
	 */
	public void move(int x, int y) {
		if(currentState == MY_TURN){
			if(gameGrid.move(x,y,MY_TURN)) {
				client.sendMoveMessage(x, y);
				currentState = OTHERS_TURN;
				if(gameGrid.isWinner(MY_TURN)) {
					currentState = FINISHED;
					message = "Congratulation you have won";
					setChanged();
					notifyObservers();
				}else{
					currentState = OTHERS_TURN;
					message = "Other player's turn";
					setChanged();
					notifyObservers();
				}
			} else {	// If the move can't be made because of not empty.
				message = "The move can't me made.";
				setChanged();
				notifyObservers();
			}
		}
		
	}
	
	/**
	 * Starts a new game with the current client
	 */
	public void newGame(){
		if (currentState != NOT_STARTED) {
			gameGrid.clearGrid();
			currentState = OTHERS_TURN;
			message = "Other player's turn";
			client.sendNewGameMessage();
			setChanged();
			notifyObservers();
		}
	}
	
	/**
	 * Other player has requested a new game, so the 
	 * game state is changed accordingly
	 */
	public void receivedNewGame() {
		gameGrid.clearGrid();
		currentState = MY_TURN;
		message = "Your turn";
		setChanged();
		notifyObservers();
	}
	
	/**
	 * The connection to the other player is lost, 
	 * so the game is interrupted
	 */
	public void otherGuyLeft() {
		gameGrid.clearGrid();
		currentState= NOT_STARTED;
		message = "Other player has left";
		setChanged();
		notifyObservers();
	}
	
	/**
	 * The player disconnects from the client
	 */
	public void disconnect(){
		if (currentState != NOT_STARTED) {
			gameGrid.clearGrid();
			currentState = NOT_STARTED;
			message = "You have been disconnected";
			setChanged();
			notifyObservers();
			client.disconnect();
		}
	}
	
	/**
	 * The player receives a move from the other player
	 * 
	 * @param x The x coordinate of the move
	 * @param y The y coordinate of the move
	 */
	public void receivedMove(int x, int y) {
		gameGrid.move(x, y, OTHERS_TURN);
		if(gameGrid.isWinner(OTHERS_TURN)){
			currentState= FINISHED;
			message = "Other player has won";
			setChanged();
			notifyObservers();
		}else{
			message = " Your turn";
			currentState = MY_TURN;
			setChanged();
			notifyObservers();
		}
	}
	/**
	 * Changes the game state and message when a connection is established between two players.
	 */
	public void update(Observable o, Object arg) {
		
		switch(client.getConnectionStatus()){
		case GomokuClient.CLIENT:
			message = "Game started, it is your turn!";
			currentState = MY_TURN;
			break;
		case GomokuClient.SERVER:
			message = "Game started, waiting for other player...";
			currentState = OTHERS_TURN;
			break;
		}
		setChanged();
		notifyObservers();
	}
}