import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JOptionPane;

/**
 * The Client class handles all data being sent and received from the server.
 * 
 * @author Sean Stinson, ss938
 *
 */
public class Client extends Thread {
	private String username;
	private String host;
	private int port;
	
	private PlayModel model;
	
	private Socket sock;
	private BufferedReader input;
	private PrintWriter output;
	
	/**
	 * Constructor passes parameter values to the fields
	 * 
	 * @param username The user name associated with the client
	 * @param host     The server host address
	 * @param port     The server port
	 */
	public Client(String username, String host, int port) {
		this.username = username;
		this.host = host;
		this.port = port;
	}
	
	/**
	 * Gets the input stream for the client socket
	 * 
	 * @return The input stream for the client socket
	 */
	public BufferedReader getInput() {
		return input;
	}

	/**
	 * Gets the output stream for the client socket
	 * 
	 * @return The output stream for the client socket
	 */
	public PrintWriter getOutput() {
		return output;
	}

	/**
	 * Gets the user name associated with the client
	 * 
	 * @return The user name associated with the client
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * Sets the user name associated with the client
	 * 
	 * @param username The new user name associated with the client
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * Gets the server host address
	 * 
	 * @return The server host address
	 */
	public String getHost() {
		return host;
	}

	/**
	 * Sets the server host address
	 * 
	 * @param host The new server host address
	 */
	public void setHost(String host) {
		this.host = host;
	}

	/**
	 * Gets the server port
	 * 
	 * @return The server port
	 */
	public int getPort() {
		return port;
	}

	/**
	 * Sets the server port
	 * 
	 * @param port The new server port
	 */
	public void setPort(int port) {
		this.port = port;
	}

	/**
	 * Gets the model of the game being played by the client
	 * 
	 * @return The model of the game being played by the client
	 */
	public PlayModel getModel() {
		return model;
	}
	
	/**
	 * Sets the model of the game being played by the client
	 * 
	 * @param model The new model of the game being played by the client
	 */
	public void setModel(PlayModel model) {
		this.model = model;
	}
	
	/**
	 * Connects to the server using the <code>host</code> and <code>port</code> field values
	 * 
	 * @return <code>null</code> if the connection was successful, otherwise an error message is returned
	 */
	public String connect() {
		try {
			sock = new Socket(host, port);
			
			input = new BufferedReader(new InputStreamReader(sock.getInputStream()));
			output = new PrintWriter(sock.getOutputStream(), false);
			
			sendMessage("HELLO " + username);
		} catch (UnknownHostException e) {
			return e.getMessage();
		} catch (IOException e) {
			return e.getMessage();
		}
		return null;
	}
	
	/**
	 * Listens for messages received from the server
	 */
	public void run() {
		try {
			while (true) {
				// Read messages from the server
				String message = input.readLine();
				
				// If the connection is broken, throw IOException
				if (message == null) {
					throw new IOException();
				}
				
				// Handle the message from the server
				ServerMessage serverMessage = new ServerMessage(message, this);
				serverMessage.handle();
			}
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Lost connection to the server.", "Lost Connection", JOptionPane.ERROR_MESSAGE);
			System.exit(1);
		}
	}
	
	/**
	 * Sends a message to the server
	 * 
	 * @param message The message to send to the server
	 */
	public void sendMessage(String message) {
		output.println(message);
		output.flush();
	}
}
