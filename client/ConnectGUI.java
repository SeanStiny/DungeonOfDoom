import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

/**
 * ConnectGUI represents the interface displayed to the user allowing them to enter a custom
 * host and port to connect to.
 * 
 * @author Sean Stinson, ss938
 *
 */
public class ConnectGUI extends JFrame {
	private static final long serialVersionUID = -2051753925842606753L;
	
	private JTextField txtUsername;
	private JTextField txtHost;
	private JTextField txtPort;
	private JButton btnConnect;
	
	/**
	 * Constructor initialises the swing components and sets the frame layout
	 */
	public ConnectGUI(String username, String host, int port) {
		txtUsername = new JTextField(username);
		txtHost = new JTextField(host);
		txtPort = new JTextField(Integer.toString(port));
		btnConnect = new JButton("Connect");
		
		setTitle("Dungeon of Dooom - Connect");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new BorderLayout());
		setResizable(false);
	}
	
	/**
	 * Gets the user name text box component
	 * 
	 * @return The user name text box component
	 */
	public JTextField getTxtUsername() {
		return txtUsername;
	}
	
	/**
	 * Gets the host text box component
	 * 
	 * @return The host text box component
	 */
	public JTextField getTxtHost() {
		return txtHost;
	}

	/**
	 * Gets the port text box component
	 * 
	 * @return The port text box component
	 */
	public JTextField getTxtPort() {
		return txtPort;
	}

	/**
	 * Gets the connect button component
	 * 
	 * @return The connect button component
	 */
	public JButton getBtnConnect() {
		return btnConnect;
	}

	/**
	 * Draws the GUI components and displays the interface to the user
	 */
	public void draw() {
		// Container panel
		JPanel paneContainer = new JPanel(new BorderLayout());
		paneContainer.setBorder(new EmptyBorder(10, 10, 10, 10));
		paneContainer.setOpaque(false);
		
		// Label panel
		JPanel paneLabels = new JPanel();
		paneLabels.setLayout(new GridLayout(4, 1));
		paneLabels.setOpaque(false);
		paneLabels.setBorder(new EmptyBorder(0, 0, 0, 10));
		
		// Field panel
		JPanel paneFields = new JPanel();
		paneFields.setLayout(new GridLayout(4, 1));
		paneFields.setOpaque(false);
		paneFields.setPreferredSize(new Dimension(300, 150));
		
		// User name text box
		JLabel lblUsername = new JLabel("Username: ");
		lblUsername.setFont(lblUsername.getFont().deriveFont(Font.BOLD));
		paneLabels.add(lblUsername);
		paneFields.add(txtUsername);
		
		// Host text box
		JLabel lblHost = new JLabel("Host: ");
		lblHost.setFont(lblHost.getFont().deriveFont(Font.BOLD));
		paneLabels.add(lblHost);
		paneFields.add(txtHost);
		
		// Port text box
		JLabel lblPort = new JLabel("Port: ");
		lblPort.setFont(lblPort.getFont().deriveFont(Font.BOLD));
		paneLabels.add(lblPort);
		paneFields.add(txtPort);
		
		// Connect button
		paneFields.add(btnConnect);
		
		// Add panels
		paneContainer.add(paneLabels, BorderLayout.LINE_START);
		paneContainer.add(paneFields, BorderLayout.CENTER);
		add(paneContainer, BorderLayout.CENTER);
		
		// Show GUI to the user
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}
}
