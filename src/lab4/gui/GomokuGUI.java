package lab4.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SpringLayout;

import lab4.client.GomokuClient;
import lab4.data.GameGrid;
import lab4.data.GomokuGameState;

/*
 * The GUI class
 */

public class GomokuGUI implements Observer{

	private GomokuClient client;
	private GomokuGameState gamestate;
	private JFrame frame;
	private GamePanel gameGridPanel;
	private JLabel messageLabel;
	private JButton connectButton, newGameButton, disconnectButton;
	
	/**
	 * The constructor test
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
		
		JPanel panel = new JPanel();	// The background that i can use spring layout on.
		gameGridPanel = new GamePanel(gamestate.getGameGrid());
		connectButton = new JButton("Connect");
		newGameButton = new JButton("New game");
		disconnectButton = new JButton("Disconnect");
		messageLabel = new JLabel();
		
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
				g.newGame();
				
			}
		});
		
		disconnectButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				g.disconnect();
			}
		});
		
		panel.add(gameGridPanel);
		panel.add(connectButton);
		panel.add(newGameButton);
		panel.add(disconnectButton);
		panel.add(messageLabel);
		frame.setContentPane(panel);	// Connects the panel to the frame.
		
		SpringLayout layout = new SpringLayout();
		panel.setLayout(layout);
		layout.putConstraint(SpringLayout.WEST, gameGridPanel, 5, SpringLayout.EAST, panel);
		
		layout.putConstraint(SpringLayout.NORTH, connectButton, 10, SpringLayout.SOUTH, gameGridPanel);
		layout.putConstraint(SpringLayout.NORTH, newGameButton, 10, SpringLayout.SOUTH, gameGridPanel);
		layout.putConstraint(SpringLayout.NORTH, disconnectButton, 10, SpringLayout.SOUTH, gameGridPanel);
		
		layout.putConstraint(SpringLayout.WEST, newGameButton, 10, SpringLayout.EAST, gameGridPanel);
		layout.putConstraint(SpringLayout.WEST, disconnectButton, 10, SpringLayout.EAST, newGameButton);
		
		layout.putConstraint(SpringLayout.NORTH, messageLabel, 10, SpringLayout.SOUTH, connectButton);

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