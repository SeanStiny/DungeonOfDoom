import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Rectangle;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 * The MessagePanel class represents the component that displays all of the Game and Chat messages
 * to the user.
 * 
 * @author Sean Stinson, ss938
 *
 */
public class MessagePanel extends JPanel implements Observer {
	public static final Color COMMAND_GREEN = new Color(0, 150, 0);
	public static final Color COMMAND_PINK = new Color(231, 84, 128);
	public static final Color COMMAND_ORANGE = new Color(255, 100, 0);
	
	private static final long serialVersionUID = -5910691876482450031L;
	
	private GameMessage newMessage;
	
	/**
	 * Constructor sets the component layout and background colour
	 */
	public MessagePanel() {
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		setBackground(Color.WHITE);
	}
	
	/**
	 * Updates the display when there is a change in the GameMessage model
	 */
	@Override
	public void update(Observable observable, Object value) {
		newMessage = (GameMessage) value;
		
		// Update the GUI
		SwingUtilities.invokeLater(new Runnable() {
			private GameMessage message = newMessage;
			
			@Override
			public void run() {
				JPanel paneMessage = new JPanel(new BorderLayout());
				paneMessage.setOpaque(false);
				
				// Time stamp label
				JLabel lblTime = new JLabel(new SimpleDateFormat(" [HH:mm] ").format(Calendar.getInstance().getTime()));
				lblTime.setForeground(Color.GRAY);
				paneMessage.add(lblTime, BorderLayout.LINE_START);
				
				// Surround the message with HTML tags and add an icon
				JLabel lblNewMessage = new JLabel("<html>" + message.getMessage() + "<html>");
				lblNewMessage.setIcon(new ImageIcon("resources/images/message_" + message.getType() + ".png"));
				
				// Give the messages colour depending on their type
				switch (message.getType()) {
					// Green messages
					case GameMessage.WIN:
					case GameMessage.HP:
					case GameMessage.AGREE:
					case GameMessage.LOL:
					case GameMessage.COFFEE:
					case GameMessage.BEER:
					case GameMessage.GIFT:
					case GameMessage.PIZZA:
					case GameMessage.SUCCEED:
						lblNewMessage.setForeground(COMMAND_GREEN);
						break;
						
					// Red messages
					case GameMessage.DISAGREE:
					case GameMessage.ERROR:
					case GameMessage.ANGRY:
					case GameMessage.HPLOSS:
					case GameMessage.DEATH:
						lblNewMessage.setForeground(Color.RED);
						break;
						
					// Orange messages
					case GameMessage.WARNING:
					case GameMessage.CONFUSED:
					case GameMessage.TIRED:
					case GameMessage.BRB:
					case GameMessage.WAITING:
						lblNewMessage.setForeground(COMMAND_ORANGE);
						break;
						
					// Blue messages
					case GameMessage.CHAT:
						lblNewMessage.setForeground(Color.BLUE);
						break;
						
					// Whisper messages
					case GameMessage.WHISPER:
						lblNewMessage.setForeground(Color.BLUE);
						lblNewMessage.setFont(lblNewMessage.getFont().deriveFont(Font.ITALIC));
						lblNewMessage.setBackground(new Color(230, 230, 230));
						lblNewMessage.setOpaque(true);
						break;
					
					// Pink messages
					case GameMessage.HUG:
					case GameMessage.LOVE:
					case GameMessage.KISS:
						lblNewMessage.setForeground(COMMAND_PINK);
						break;
						
					// Bold messages
					case GameMessage.SHOUT:
						lblNewMessage.setFont(lblNewMessage.getFont().deriveFont(Font.BOLD));
						break;
				}
				
				paneMessage.add(lblNewMessage, BorderLayout.CENTER);
				add(paneMessage);

				// Re-draw the GUI component
				revalidate();
				repaint();
				
				// Scroll down to the bottom of the message list
		        int height = (int) getPreferredSize().getHeight();
		        Rectangle rect = new Rectangle(0,height,10,10);
		        scrollRectToVisible(rect);
			}
		});
	}
}
