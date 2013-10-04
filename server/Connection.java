import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Timer;
import java.util.Vector;

/**
 * The Connection class handles the data sent and received from a client
 * 
 * @author Sean Stinson, ss938
 *
 */
public class Connection extends Thread {
	public static Vector<Connection> connections = new Vector<Connection>();
	private static int nameCount = 0;
	
	private Game game;
	
	private Player player;
	private String username;
	
	private Socket sock;
	private BufferedReader input;
	private PrintWriter output;

	private Timer spamTimer;
	private int spamCount;
	private boolean muted;
	
	/**
	 * Constructor sets up the input and output streams
	 * 
	 * @param sock The socket connection to the client
	 */
	public Connection(Socket sock, Game game) {
		this.game = game;
		this.sock = sock;
		spamTimer = new Timer();
		
		try {
			input = new BufferedReader(new InputStreamReader(sock.getInputStream()));
			output = new PrintWriter(sock.getOutputStream(), false);
			
			start();
		} catch (IOException e) {
			// Allow the thread to die quietly
		}
	}
	
	/**
	 * Gets the game the client is playing
	 * 
	 * @return The game the client is playing
	 */
	public Game getGame() {
		return game;
	}
	
	/**
	 * Sets the game the client is playing
	 * 
	 * @param game The new game the client is playing
	 */
	public void setGame(Game game) {
		this.game = game;
	}

	/**
	 * Gets the name of the player
	 * 
	 * @return The name of the player
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * Sets the name of the player
	 * 
	 * @param name The new name for the player
	 */
	public void setUsername(String username) {
		// Check that the name is unique
		boolean unique = false;
		String temp = username;

		int counter = 2;
		while (!unique) {
			unique = true;
			
			for (Connection c : connections) {
				if (c.getUsername().equalsIgnoreCase(temp)) {
					temp = username + counter++;
					unique = false;
					break;
				}
			}
		}
		
		this.username = temp;
		connections.add(this);
		sendMessage("HELLO " + this.username);
	}
	
	/**
	 * Sets the player name to a unique default name
	 */
	public void setUsername() {
		setUsername("Player " + ++nameCount);
	}
	
	/**
	 * Gets the player for the connection
	 * 
	 * @return The player being used by the connection
	 */
	public Player getPlayer() {
		return player;
	}
	
	/**
	 * Sets the player for the connection
	 * 
	 * @param player The new player being used by the connection
	 */
	public void setPlayer(Player player) {
		this.player = player;
	}

	/**
	 * Gets the spam timer object for this client
	 * 
	 * @return The spam timer object for this client
	 */
	public Timer getSpamTimer() {
		return spamTimer;
	}

	/**
	 * Gets the spam count number for this client
	 * 
	 * @return The spam count number
	 */
	public int getSpamCount() {
		return spamCount;
	}

	/**
	 * Sets the spam count number for this client
	 * 
	 * @param spamCount The new spam count number
	 */
	public void setSpamCount(int spamCount) {
		this.spamCount = spamCount;
	}
	
	/**
	 * Increases the spam count by one
	 */
	public void incrementSpamCount() {
		spamCount++;
	}
	
	/**
	 * Decreases the spam count by one
	 */
	public void decrementSpamCount() {
		spamCount--;
	}

	/**
	 * Gets whether this client has their chat muted or not
	 * 
	 * @return Client <code>true</code> if the client's chat is muted, <code>false</code> otherwise
	 */
	public boolean isMuted() {
		return muted;
	}

	/**
	 * Sets whether this client has their chat muted or not
	 * 
	 * @param muted <code>true</code> if the client's chat is muted, <code>false</code> otherwise
	 */
	public void setMuted(boolean muted) {
		this.muted = muted;
		
		if (muted) {
			sendMessage("MUTE");
		} else {
			sendMessage("UNMUTE");
		}
	}

	/**
	 * Listens for incoming messages from the client
	 */
	@Override
	public void run() {
		try {
			while (true) {
				String message = input.readLine();
				
				if (message == null) {
					throw new IOException();
				}
				
				new ClientMessage(message, this);
			}
		} catch (IOException e) {
			if (player != null) {
				player.leaveGame();
			}
			
			connections.remove(this);
		}
	}
	
	/**
	 * Sends a message to the client
	 * 
	 * @param message The message to be sent
	 */
	public void sendMessage(String message) {
		output.println(message);
		output.flush();
	}
	
	/**
	 * Checks if the client is currently in a game
	 * 
	 * @return <code>true</code> if the client is in a game, <code>false</code> otherwise
	 */
	public boolean isInGame() {
		return (player != null);
	}
	
	/**
	 * Closes the connection to the client
	 * 
	 * @throws IOException Thrown if there is an error closing the connection
	 */
	public void close() throws IOException {
		sock.close();
	}
}