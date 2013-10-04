import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

/**
 * The Connect class handles the details of the server connection as well as setting up PlayGame and Client
 * ready to start playing a new game.
 * 
 * @author Sean Stinson, ss938
 *
 */
public class Connect {
	private ConnectGUI connectView;
	
	/**
	 * Initialises the fields and calls for the GUI to appear
	 */
	public Connect() {
		connectView = new ConnectGUI("Sean", "127.0.0.1", 51007);
		askForConnection();
	}
	
	/**
	 * Displays the GUI asking the user to specify a user name, server host and server port
	 */
	public void askForConnection() {
		connectView.getBtnConnect().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Get values from text fields
				String username = connectView.getTxtUsername().getText();
				String host = connectView.getTxtHost().getText();
				String portStr = connectView.getTxtPort().getText();
				
				// Check the user name is valid
				if (username.length() == 0) {
					JOptionPane.showMessageDialog(connectView, "Must enter a username.", "Incorrect Username", JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				// Check the host is valid
				if (host.length() == 0) {
					JOptionPane.showMessageDialog(connectView, "Must enter a host name.", "Incorrect Host", JOptionPane.ERROR_MESSAGE);
					return;
				}

				// Check the port is valid
				int port;
				if (portStr.matches("[0-9]+")) {
					port = Integer.parseInt(portStr);
				} else {
					JOptionPane.showMessageDialog(connectView, "Port must be numeric.", "Incorrect Port", JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				// Attempt to connect to the server
				Client client = new Client(username, host, port);
				String error = client.connect();
				
				// If successful, dispose of the GUI and start listening for server messages, otherwise show error message
				if (error == null) {
					connectView.dispose();
					
					PlayModel game = new PlayModel();
					client.setModel(game);
					new PlayGame(client);
				} else {
					JOptionPane.showMessageDialog(connectView, error, "Connection Failed", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		
		connectView.draw();
	}
	
	/**
	 * The main method
	 * 
	 * @param args The command line arguments
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
		
		new Connect();
	}
}
