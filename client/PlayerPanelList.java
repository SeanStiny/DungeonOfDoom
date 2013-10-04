import java.awt.Color;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

/**
 * The PlayerPanel class represents a panel displaying all of the players in the game.
 * 
 * @author Sean Stinson, ss938
 *
 */
public class PlayerPanelList extends JPanel implements Observer {
	private static final long serialVersionUID = 6530162287721447393L;
	
	private Object[] players;
	private JTextArea txtaChat;

	/**
	 * Constructor sets the background colour and LayoutManager
	 */
	public PlayerPanelList(JTextArea txtaChat) {
		this.txtaChat = txtaChat;
		setBackground(Color.WHITE);
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
	}

	/**
	 * Updates the GUI to reflect changes in the players model
	 */
	@Override
	public void update(Observable observable, Object value) {
		players = (Object[]) value;
		
		// Update the GUI
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				// Remove the old list
				removeAll();
				
				// Add each player to the component list
				for (Object playerObject : players) {
					Player player = (Player) playerObject;
					String newUsername = player.getUsername();
					int newState = player.getStatus();
					boolean newTyping = player.isTyping();
					boolean newChat = player.isChat();
					
					PlayerPanel newPlayerComponent = new PlayerPanel(newUsername, newState, newTyping, newChat, txtaChat);
					player.addObserver(newPlayerComponent);
					add(newPlayerComponent);
				}
				
				// Refresh the display
				revalidate();
				repaint();
			}
		});
	}
}
