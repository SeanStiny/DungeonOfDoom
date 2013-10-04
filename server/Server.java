import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.UnknownHostException;

import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

/**
 * The Server class sets up the sockets and listens for new connections from clients
 * 
 * @author Sean Stinson, ss938
 *
 */
public class Server extends Thread {
	private Game game;
	private ServerGUI view;
	
	private Listener listener;
	private boolean listening;
	
	/**
	 * Constructor initialises the GUI
	 * 
	 * @throws UnknownHostException Thrown if the server was unable to find a local host
	 */
	public Server() throws UnknownHostException {
		listening = false;
		view = new ServerGUI(InetAddress.getLocalHost().getHostAddress());
		
		game = new Game();
		game.addObserver(view);
		
		addEventListeners();
		view.draw();
	}
	
	/**
	 * Adds event listeners to all of the GUI components
	 */
	public void addEventListeners() {
		addLoadMapButtonListener();
		addListenButtonListener();
		addKickButtonListener();
		addMuteButtonListener();
	}
	
	/**
	 * Adds the load map button action listener
	 */
	public void addLoadMapButtonListener() {
		view.getBtnLoadMap().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (view.getLstPlayers().getModel().getSize() == 0) {
					game.startNewGame(view.getTxtLoadMap().getText());
				} else {
					JOptionPane.showMessageDialog(view, "Cannot load a new map while there are players connected.", "Load Map", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
	}
	
	/**
	 * Adds the listen button action listener
	 */
	public void addListenButtonListener() {
		view.getBtnListen().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (view.getTxtPort().getText().matches("[0-9]+")) {
					int port = Integer.parseInt(view.getTxtPort().getText());
					
					if (!listening) {
						try {
							ServerSocket serverSock = new ServerSocket(port);
							
							listening = true;
							listener = new Listener(serverSock, game);
							view.startListening();
						} catch (IOException e) {
							JOptionPane.showMessageDialog(view, e.getMessage(), "Server Socket Exception", JOptionPane.ERROR_MESSAGE);
						}
					} else {
						try {
							listener.close();
							listening = false;
							view.stopListening();
						} catch (IOException e) {
							// Just continue
						}
					}
				} else {
					JOptionPane.showMessageDialog(view, "Invalid port number.", "Invalid Port", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
	}
	
	/**
	 * Adds the kick out button action listener
	 */
	public void addKickButtonListener() {
		view.getBtnKickOut().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Player selectedPlayer = (Player) view.getLstPlayers().getSelectedValue();
				if (selectedPlayer != null) {
					if (JOptionPane.showConfirmDialog(view, "Are you sure you want to kick out this player?", "Kick Out Player", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
						selectedPlayer.kickOut();
					}
				} else {
					JOptionPane.showMessageDialog(view, "Please select a player from the list first.", "Kick Out Player", JOptionPane.INFORMATION_MESSAGE);
				}
			}
		});
	}
	
	/**
	 * Adds the mute button action listener
	 */
	public void addMuteButtonListener() {
		view.getBtnMute().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Player selectedPlayer = (Player) view.getLstPlayers().getSelectedValue();
				if (selectedPlayer != null) {
					if (!selectedPlayer.getClient().isMuted()) {
						if (JOptionPane.showConfirmDialog(view, "Are you sure you want to mute " + selectedPlayer.toString() + "?", "Mute Player", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
							selectedPlayer.getClient().setMuted(true);
						}
					} else {
						selectedPlayer.getClient().setMuted(false);
						JOptionPane.showMessageDialog(view, selectedPlayer.toString() + " was unmuted." , "Unmute Player", JOptionPane.INFORMATION_MESSAGE);
					}
				} else {
					JOptionPane.showMessageDialog(view, "Please select a player from the list first", "Mute Player", JOptionPane.INFORMATION_MESSAGE);
				}
			}
		});
	}
	
	/**
	 * Starts a new game with a given map file
	 * 
	 * @param mapFile The map file to load
	 */
	public void loadGame(String mapFile) {
		if (mapFile.length() > 0) {
			game = new Game(mapFile);
		} else {
			// If no map file is specified, use default map
			game = new Game();
		}
		game.addObserver(view);
	}
	
	/**
	 * The main method.
	 * 
	 * @param args The command line args
	 */
	public static void main(String[] args) {
		try {
		    for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
		        if ("Nimbus".equals(info.getName())) {
		            UIManager.setLookAndFeel(info.getClassName());
		            break;
		        }
		    }
		} catch (Exception e) {
		    // If Nimbus is not available, use the default look and feel.
		}
		
		try {
			new Server();
		} catch (UnknownHostException e) {
			System.out.println(e.toString());
		}
	}
}