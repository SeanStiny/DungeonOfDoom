import java.util.Observable;

/**
 * The Player class models a single player currently in the game.
 * 
 * @author Sean Stinson, ss938
 *
 */
public class Player extends Observable {
	public static final int ALIVE = 0;
	public static final int DEAD = 1;
	
	private int status;
	private String username;
	private boolean typing;
	private boolean chat;
	
	/**
	 * Constructor passes parameters to the fields
	 * 
	 * @param status   The status of the player
	 * @param username The player user name
	 */
	public Player(int status, String username, boolean chat) {
		this.status = status;
		this.username = username;
		this.chat = chat;
	}

	/**
	 * Gets the status of the player
	 * 
	 * @return The status of the player
	 */
	public int getStatus() {
		return status;
	}

	/**
	 * Sets the status of the player
	 * 
	 * @param status The new status of the player
	 */
	public void setStatus(int status) {
		this.status = status;
		
		setChanged();
		notifyObservers(this);
	}

	/**
	 * Gets the user name of the player
	 * 
	 * @return The user name of the player
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * Sets the user name of the player
	 * 
	 * @param username The new user name of the player
	 */
	public void setUsername(String username) {
		this.username = username;
		
		setChanged();
		notifyObservers(this);
	}

	/**
	 * Gets whether the player is typing or not
	 * 
	 * @return <code>true</code> if the player is typing, <code>false</code> otherwise
	 */
	public boolean isTyping() {
		return typing;
	}

	/**
	 * Sets whether the player is typing or not
	 * 
	 * @param typing <code>true</code> if the player is typing, <code>false</code> otherwise
	 */
	public void setTyping(boolean typing) {
		this.typing = typing;
		
		setChanged();
		notifyObservers(this);
	}

	/**
	 * Gets whether the player has private chat enabled or not
	 * 
	 * @return <code>true</code> if private chat is enabled, <code>false</code> otherwise
	 */
	public boolean isChat() {
		return chat;
	}

	/**
	 * Sets whether the player has private chat enabled or not
	 * 
	 * @param chat <code>true</code> if private chat is enabled, <code>false</code> otherwise
	 */
	public void setChat(boolean chat) {
		this.chat = chat;
		
		setChanged();
		notifyObservers(this);
	}
}