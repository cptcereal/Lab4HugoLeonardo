package lab4;

import lab4.client.*;
import lab4.data.GomokuGameState;
import lab4.gui.*;

/**
 * Creates an instance of the game Gomoku that can be played over the internet.
 * 
 * @author hugwan-6, leopel-6
 *
 */
public class GomokuMain2 {
	private static int port = 4500;
	
	/**
	 * Creates all the dynamic objects that makes up the game.
	 * 
	 * @param args	The port number
	 */
	public static void main(String[] args) {
		if (args.length == 1) {
			int tempPort = Integer.parseInt(args[0]);	// Problem if the argument is not a number.
			
			if (tempPort > 0) {	// A port number needs to be a positive integer
				port = tempPort;
			}
		}
		
		GomokuClient client = new GomokuClient(port);
		GomokuGameState gameState = new GomokuGameState(client);
		GomokuGUI GUI = new GomokuGUI(gameState, client);
		
		GomokuClient client2 = new GomokuClient(4502);
		GomokuGameState gameState2 = new GomokuGameState(client2);
		GomokuGUI GUI2 = new GomokuGUI(gameState2, client2);
	}
}
