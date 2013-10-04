import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

/**
 * PlayerComponent represents a player displayed in the player list panel
 * 
 * @author Sean Stinson, ss938
 *
 */
public class PlayerPanel extends JPanel implements Observer {
	private static final long serialVersionUID = 3956414370239972062L;
	
	private int state;
	private boolean typing;
	private String playerName;
	
	private JLabel lblDeadIcon;
	private JLabel lblTypingIcon;
	private JButton btnPrivateChat;
	private JTextArea chatTextField;
	
	/**
	 * Constructor passes the parameters to the fields and initialises the components used to
	 * display the player's details.
	 * 
	 * @param username The player user name
	 * @param state    The player's current state
	 * @param typing   <code>true</code> if the player is typing, <code>false</code> otherwise
	 * @param chat     <code>true</code> if a private chat button should be displayed, <code>false</code> otherwise
	 * @param txtaChat The text field to update when clicking the whisper button
	 */
	public PlayerPanel(String username, int state, boolean typing, boolean chat, JTextArea txtaChat) {
		this.state = state;
		this.typing = typing;
		this.chatTextField = txtaChat;
		this.playerName = username;
		
		setLayout(new BorderLayout());
		setOpaque(false);
		setMaximumSize(new Dimension(600, 24));
		
		// Icon labels
		JPanel paneIcons = new JPanel(new FlowLayout());
		paneIcons.setOpaque(false);
		
		// Dead icon
		lblDeadIcon = new JLabel();
		lblDeadIcon.setPreferredSize(new Dimension(16, 16));
		if (state == Player.DEAD) {
			lblDeadIcon.setIcon(new ImageIcon("resources/images/death.png"));
		} else {
			lblDeadIcon.setIcon(new ImageIcon("resources/images/alive.png"));
		}
		paneIcons.add(lblDeadIcon);
		
		// Typing icon
		lblTypingIcon = new JLabel();
		lblTypingIcon.setPreferredSize(new Dimension(16, 16));
		if (typing) {
			lblTypingIcon.setIcon(new ImageIcon("resources/images/typing.png"));
		}
		paneIcons.add(lblTypingIcon);
		
		add(paneIcons, BorderLayout.LINE_START);
		
		// User name and private chat button
		JPanel paneRight = new JPanel(new BorderLayout());
		paneRight.setOpaque(false);
		JLabel lblUsername = new JLabel(username);
		paneRight.add(lblUsername, BorderLayout.LINE_START);
		if (chat) {
			btnPrivateChat = new JButton("Whisper");
			btnPrivateChat.setIcon(new ImageIcon("resources/images/private_chat.png"));
			btnPrivateChat.setFocusable(false);
			btnPrivateChat.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					chatTextField.setText("/whisper " + playerName + " ");
				}
			});
			paneRight.add(btnPrivateChat, BorderLayout.LINE_END);
		}
		
		add(paneRight, BorderLayout.CENTER);
	}
	
	/**
	 * Gets the dead icon label
	 * 
	 * @return The dead icon label
	 */
	public JLabel getLblDeadIcon() {
		return lblDeadIcon;
	}

	/**
	 * Gets the typing icon label
	 * 
	 * @return The typing icon label
	 */
	public JLabel getLblTypingIcon() {
		return lblTypingIcon;
	}

	/**
	 * Gets the private chat button
	 * 
	 * @return The private chat button
	 */
	public JButton getBtnPrivateChat() {
		return btnPrivateChat;
	}

	/**
	 * Updates the player details when the player model has been changed
	 */
	@Override
	public void update(Observable observable, Object value) {
		Player player = (Player) value;
		state = player.getStatus();
		typing = player.isTyping();
		
		// Update the GUI
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				// Update the dead icon
				if (state == Player.DEAD) {
					lblDeadIcon.setIcon(new ImageIcon("resources/images/death.png"));
				} else {
					lblDeadIcon.setIcon(new ImageIcon("resources/images/alive.png"));
				}
				
				// Update the typing icon
				if (typing) {
					lblTypingIcon.setIcon(new ImageIcon("resources/images/typing.png"));
				} else {
					lblTypingIcon.setIcon(null);
				}
			}
		});
	}
}