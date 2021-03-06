package lab4.data;

import java.util.Observable;

/**
 *
 * Represents the 2-d game grid
 * 
 * @author hugwan-6, leopel-6
 */
public class GameGrid extends Observable {
	public static final int EMPTY = 0;
	public static final int ME = 1;
	public static final int OTHER = 2;
	public static final int INROW = 5;	// Number of consecutive pieces in a row required to win.
	private int[][] board;
	private int sideLength;

	/**
	 * Constructor
	 * 
	 * @param size The width/height of the game grid
	 */
	public GameGrid(int size) {
		
		board = new int[size][size];
		this.sideLength = size;
		
		// Make all the squares on the board empty.
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				board[i][j] = EMPTY;
			}
		}
	}
	
	/**
	 * Reads a location of the grid 
	 * 
	 * @param x The index x in the 2-d array
	 * @param y The index y in the 2-d array
	 * @return the value of the specified location
	 */
	public int getLocation(int x, int y) {
		return board[x][y];
	}
	
	/**
	 * Returns the size of the grid
	 * 
	 * @return the grid size
	 */
	public int getSize() {
		return sideLength;
	}
	
	/**
	 * Enters a move in the game grid
	 * 
	 * @param x the x position
	 * @param y the y position
	 * @param player
	 * @return true if the insertion worked, false otherwise
	 */
	public boolean move(int x, int y, int player) {
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
	public void clearGrid() {
		for (int i = 0; i < sideLength; i++) {
			for (int j = 0; j < sideLength; j++) {
				board[i][j] = EMPTY;
			}
		}
		
		setChanged();
		notifyObservers();
	}
	
	/**
	 * Check if a player has the required number of consecutive pieces in a row to win
	 * 
	 * @param player the player to check for
	 * @return true if player has won , false otherwise
	 */
	public boolean isWinner(int player) {
		int playerPieces = 0;
		int playerInRow = 0;
		
		// Count the player pieces
		for (int i = 0; i < sideLength; i++) {
			for (int j = 0; j < sideLength; j++) {
				if (board[i][j] == player) {
					playerPieces++;
				}
			} 
		}
		
		
		// Only check if if it's possible to win.
		if (playerPieces >= INROW) {
			
			//Checks if player has INROW Horizontally 
			for (int i = 0; i < sideLength; i++ ) {
				for (int j = 0; j < sideLength; j++) {
					if (board[i][j] == player) {
						playerInRow++;
						if (playerInRow == INROW) {
							return true;
						}
					} else {
						playerInRow = 0;
					}
				}
				playerInRow = 0;
			}
			
			//Checks if player has INROW Vertically
			for (int i = 0; i < sideLength; i++ ) {
				for (int j = 0; j < sideLength; j++) {
					if (board[j][i] == player) {
						playerInRow++;
						if (playerInRow == INROW) {
							return true;
						}
					} else {
						playerInRow = 0;
					}
				}
				playerInRow = 0;
			}
			
			//Checks if player has INROW Diagonally to the right
			for (int i = 0; i < sideLength; i++ ) {
				for (int j = 0; j < sideLength; j++) {
					if (board[i][j] == player) {
						playerInRow++;
						if (i + INROW - 1 < sideLength && j + INROW - 1  < sideLength) {
							for(int k = 1; k < INROW; k++){								
								if(board[i+k][j+k] == player) {
									playerInRow++;
									if (playerInRow == INROW) {
										return true;
									}	
								}else{
									playerInRow=0;
								}
							}
						} else {
							playerInRow = 0;
						}
					} else {
						playerInRow = 0;
					}
				}
			}
			
			//Checks if player has INROW Diagonally to the left
			for (int i = 0; i < sideLength; i++ ) {
				for (int j = 0; j < sideLength; j++) {
					if (board[i][j] == player) {
						playerInRow++;
						if (i + INROW - 1 < sideLength && j - INROW + 1 >= 0) {
							for(int k = 1; k < INROW; k++){								
								if(board[i+k][j-k] == player) {
									playerInRow++;
									if (playerInRow == INROW) {
										return true;
									}	
								}else{
									playerInRow=0;
								}
							}
						} else {
							playerInRow = 0;
						}
					} else {
						playerInRow = 0;
					}
				}
			}
		}
		return false;
	}
}