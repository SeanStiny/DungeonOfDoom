/**
 * GameMessage models the messages received from the server that are to be displayed
 * to the player.
 * 
 * @author Sean Stinson, ss938
 *
 */
public class GameMessage {
	// Message types
	public static final int NORMAL = 0;
	public static final int WHISPER = 1;
	public static final int WARNING = 2;
	public static final int ERROR = 3;
	public static final int DEATH = 4;
	
	public static final int WIN = 5;
	public static final int HP = 6;
	public static final int GOLD = 7;
	public static final int CHAT = 8;
	public static final int SWORD = 9;
	
	public static final int LANTERN = 10;
	public static final int ARMOUR = 11;
	public static final int SHOUT = 12;
	public static final int HUG = 13;
	public static final int AGREE = 14;
	
	public static final int DISAGREE = 15;
	public static final int LOL = 16;
	public static final int COFFEE = 17;
	public static final int BEER = 18;
	public static final int GIFT = 19;
	
	public static final int ANGRY = 20;
	public static final int CONFUSED = 21;
	public static final int TIRED = 22;
	public static final int HPLOSS = 23;
	public static final int PIZZA = 24;
	
	public static final int LOVE = 25;
	public static final int BRB = 26;
	public static final int KISS = 27;
	public static final int WAITING = 28;
	public static final int SUCCEED = 29;
	
	private int type;
	private String message;
	
	/**
	 * Constructor passes parameters to the fields
	 * 
	 * @param message The message to display to the user
	 * @param type    The message type
	 */
	public GameMessage(String message, int type) {
		this.message = message;
		this.type = type;
	}
	
	/**
	 * Constructor passes in message parameter and uses default message type
	 * 
	 * @param message The message to display to the user
	 */
	public GameMessage(String message) {
		this(message, NORMAL);
	}

	/**
	 * Gets the type of the message
	 * 
	 * @return The type of the message
	 */
	public int getType() {
		return type;
	}

	/**
	 * Sets the type of the message
	 * 
	 * @param type The new type of the message
	 */
	public void setType(int type) {
		this.type = type;
	}

	/**
	 * Gets the message content
	 * 
	 * @return The message content
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * Sets the message content
	 * 
	 * @param message The new message content
	 */
	public void setMessage(String message) {
		this.message = message;
	}
}