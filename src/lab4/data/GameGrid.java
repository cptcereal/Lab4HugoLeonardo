package lab4.data;

import java.util.Observable;

/**
 *
 * Represents the 2-d game grid
 */

public class GameGrid extends Observable{
	public static final int EMPTY = 0;
	public static final int ME = 1;
	public static final int OTHER = 2;
	public static final int INROW = 5;	// Number of consecutive pieces in a row required to win.
	private int[][] board;
	private int sideLength;	// Allowed?

	/**
	 * Constructor
	 * 
	 * @param size The width/height of the game grid
	 */
	public GameGrid(int size){
		
		board = new int[size][size];
		this.sideLength = size;
		
		// Make all the squares on the board empty.
		for (int i = 0; i < size; i++) {
			for (int j = 0; i < size; j++) {
				board[i][j] = EMPTY;
			}
		}
		
	}
	
	/**
	 * Reads a location of the grid
	 * 
	 * @param x The x coordinate
	 * @param y The y coordinate
	 * @return the value of the specified location
	 */
	public int getLocation(int x, int y){
		return board[x][y];
	}
	
	/**
	 * Returns the size of the grid
	 * 
	 * @return the grid size
	 */
	public int getSize(){
		return sideLength * sideLength;
	}
	
	/**
	 * Enters a move in the game grid
	 * 
	 * @param x the x position
	 * @param y the y position
	 * @param player
	 * @return true if the insertion worked, false otherwise
	 */
	public boolean move(int x, int y, int player){
		if (board[x][y] == EMPTY) {
			board[x][y] = player;
			setChanged();
			notifyObservers();
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Clears the grid of pieces
	 */
	public void clearGrid(){
		for (int i = 0; i < sideLength; i++) {
			for (int j = 0; i < sideLength; j++) {
				board[i][j] = EMPTY;
			}
		}
		
		setChanged();
		notifyObservers();
	}
	
	/**
	 * Check if a player has 5 in row
	 * 
	 * @param player the player to check for
	 * @return true if player has 5 in row, false otherwise
	 */
	public boolean isWinner(int player){
		int playerPieces = 0;
		int playerInRow = 0;
		
		for (int i = 0; i < sideLength; i++) {
			for (int j = 0; i < sideLength; j++) {
				if (board[i][j] == player) {
					playerPieces++;
				}
			}
		}
		
		if (playerPieces >= INROW) {
			// Check if player has INROW horizontally.
			for (int i = 0; i < sideLength; i++ ) {
				for (int j = 0; j < sideLength; j++) {
					
					if (board[i][j] == player) {
						if (INROW <= sideLength - (j + 1)) {
							playerInRow++;
							for (int k = j + 1; k < j + INROW; k++) {
								if (board[i][k] == player) {
									playerInRow++;
								}
							}
							if (playerInRow == 5) {
								return true;
							} else {
								playerInRow = 0;
							}
						}
					}
				}
			}
			
			// Check if player has INROW vertically.
			for (int i = 0; i < sideLength; i++ ) {
				for (int j = 0; j < sideLength; j++) {
					
					if (board[j][i] == player) {
						if (INROW <= sideLength - (j + 1)) {
							playerInRow++;
							for (int k = j + 1; k < j + INROW; k++) {
								if (board[k][i] == player) {
									playerInRow++;
								}
							}
							if (playerInRow == 5) {
								return true;
							} else {
								playerInRow = 0;
							}
						}
					}
				}
			}

		}
	}
}