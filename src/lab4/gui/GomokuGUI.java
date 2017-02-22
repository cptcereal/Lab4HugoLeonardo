package lab4.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Observable;
import java.util.Observer;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import lab4.client.GomokuClient;
import lab4.data.GomokuGameState;

/**
 * The GUI class
 * 
 * @author hugwan-6, leopel-6 
 */
public class GomokuGUI implements Observer{

	private GomokuClient client;
	private GomokuGameState gamestate;
	private JFrame frame;
	private GamePanel gameGridPanel;
	private JLabel messageLabel;
//	private JPanel gridLayout,buttonLayout,messageLayout;
	private Box contentBox, buttonBox, messageBox;
	private JButton connectButton, newGameButton, disconnectButton;
	
	/**
	 * The constructor
	 * 
	 * @param g   The game state that the GUI will visualize
	 * @param c   The client that is responsible for the communication
	 */
	public GomokuGUI(GomokuGameState g, GomokuClient c){
		this.client = c;
		this.gamestate = g;
		client.addObserver(this);
		gamestate.addObserver(this);
		
		frame = new JFrame("Gomoku");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		gameGridPanel = new GamePanel(gamestate.getGameGrid());
		connectButton = new JButton("Connect");
		newGameButton = new JButton("New game");
		disconnectButton = new JButton("Disconnect");
		messageLabel = new JLabel("Welcome to Gomoku!");
		
		gameGridPanel.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				super.mouseClicked(e);
				// The coordinates where the player clicked.
				int xGrid = e.getX();
				int yGrid = e.getY();
				
				int[] xyArray = gameGridPanel.getGridPosition(xGrid, yGrid);
				gamestate.move(xyArray[0], xyArray[1]);
			}
		});
		
		connectButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ConnectionWindow connectionWindow = new ConnectionWindow(client);
			}
		});
		
		newGameButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				gamestate.newGame();
				
			}
		});
		
		disconnectButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				gamestate.disconnect();
			}
		});
		
		contentBox = Box.createVerticalBox();
		buttonBox = Box.createHorizontalBox();
		messageBox = Box.createHorizontalBox();

		buttonBox.add(connectButton);
		buttonBox.add(newGameButton);
		buttonBox.add(disconnectButton);
		messageBox.add(messageLabel);
		contentBox.add(gameGridPanel);
		contentBox.add(buttonBox);
		contentBox.add(messageBox);
		
		frame.add(contentBox);
		frame.pack();
		frame.setVisible(true);
	}
	
	
	public void update(Observable arg0, Object arg1) {
		
		// Update the buttons if the connection status has changed
		if(arg0 == client){
			if(client.getConnectionStatus() == GomokuClient.UNCONNECTED){
				connectButton.setEnabled(true);
				newGameButton.setEnabled(false);
				disconnectButton.setEnabled(false);
			}else{
				connectButton.setEnabled(false);
				newGameButton.setEnabled(true);
				disconnectButton.setEnabled(true);
			}
		}
		
		// Update the status text if the gamestate has changed
		if(arg0 == gamestate){
			messageLabel.setText(gamestate.getMessageString());
		}
		
	}
	
}