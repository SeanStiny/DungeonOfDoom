import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * The Listener class listens for clients connecting to the server. The class listens in a new thread.
 * 
 * @author Sean Stinson, ss938
 *
 */
public class Listener extends Thread {
	private ServerSocket serverSock;
	
	private Game game;
	
	public Listener(ServerSocket serverSock, Game game) {
		this.game = game;
		this.serverSock = serverSock;
		start();
	}
	
	/**
	 * Starts listening in a new thread
	 */
	@Override
	public void run() {
		listen();
	}
	
	/**
	 * Listens for new clients to connect and spawns connection threads
	 */
	public void listen() {
		try {
			while (true) {
				Socket newSock = serverSock.accept();
				
				new Connection(newSock, game);
			}
		} catch (IOException e) {
			// Just let the thread die quietly
		}
	}
	
	/**
	 * Closes the server socket listener
	 * 
	 * @throws IOException Thrown if there is a problem closing the server socket
	 */
	public void close() throws IOException {
		serverSock.close();
	}
}
