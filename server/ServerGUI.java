import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.Observable;
import java.util.Observer;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

/**
 * The ServerGUI class sets up all of the swing components for displaying the server user interface.
 * 
 * @author Sean Stinson, ss938
 *
 */
public class ServerGUI extends JFrame implements Observer {
	private static final long serialVersionUID = -4722662871859003395L;
	
	private JTextArea txtaMap;
	private JTextField txtLoadMap;
	private JButton btnLoadMap;
	
	private JLabel lblHost;
	private JTextField txtPort;
	private JButton btnListen;
	private JLabel lblListening;
	
	private JList lstPlayers;
	private JButton btnKickOut;
	private JButton btnMute;
	
	/**
	 * Constructor initialises all of the component fields
	 * 
	 * @param host The host address of the server
	 */
	public ServerGUI(String host) {
		txtaMap = new JTextArea();
		txtLoadMap = new JTextField();
		btnLoadMap = new JButton("Load Map");
		
		lblHost = new JLabel(host);
		txtPort = new JTextField("51007");
		btnListen = new JButton("Start Listening");
		lblListening = new JLabel("Client listening disabled.");
		
		lstPlayers = new JList();
		btnKickOut = new JButton("Kick Out");
		btnMute = new JButton("Mute/Unmute");
	}
	
	/**
	 * Gets the map text area component
	 * 
	 * @return The map text area component
	 */
	public JTextArea getTxtaMap() {
		return txtaMap;
	}

	/**
	 * Gets the load map text field component
	 * 
	 * @return The load map text field component
	 */
	public JTextField getTxtLoadMap() {
		return txtLoadMap;
	}

	/**
	 * Gets the load map button component
	 * 
	 * @return The load map button component
	 */
	public JButton getBtnLoadMap() {
		return btnLoadMap;
	}

	/**
	 * Gets the host label component
	 * 
	 * @return The host label component
	 */
	public JLabel getLblHost() {
		return lblHost;
	}

	/**
	 * Gets the port text field component
	 * 
	 * @return The port text field component
	 */
	public JTextField getTxtPort() {
		return txtPort;
	}

	/**
	 * Gets the listen button component
	 * 
	 * @return The listen button component
	 */
	public JButton getBtnListen() {
		return btnListen;
	}

	/**
	 * Gets the client listening label
	 * 
	 * @return The client listening label
	 */
	public JLabel getLblListening() {
		return lblListening;
	}

	/**
	 * Gets the player list component
	 * 
	 * @return The player list component
	 */
	public JList getLstPlayers() {
		return lstPlayers;
	}

	/**
	 * Gets the kick out player button
	 * 
	 * @return The kick out player button
	 */
	public JButton getBtnKickOut() {
		return btnKickOut;
	}

	/**
	 * Gets the mute player button
	 * 
	 * @return The mute player button
	 */
	public JButton getBtnMute() {
		return btnMute;
	}

	/**
	 * Sets up all of the GUI components and sets the JFrame to visible
	 */
	public void draw() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Dungeon of Dooom - Server");
		setLayout(new BorderLayout());
		
		// Border panel
		JPanel borderPanel = new JPanel(new BorderLayout());
		borderPanel.setBorder(new EmptyBorder(5, 3, 2, 3));
		
		// Map panel
		JPanel mapPanel = new JPanel(new BorderLayout());
		mapPanel.setBorder(BorderFactory.createTitledBorder(""));
		JScrollPane mapScroll = new JScrollPane(txtaMap);
		mapScroll.setPreferredSize(new Dimension(400, 0));
		txtaMap.setEditable(false);
		txtaMap.setFont(new Font("Monospaced", Font.PLAIN, 20));
		txtaMap.setLineWrap(true);
		txtaMap.setFocusable(false);
		mapPanel.add(mapScroll, BorderLayout.CENTER);
		
		JPanel loadMapPanel = new JPanel(new BorderLayout());
		JLabel mapLabel = new JLabel("Map: ");
		mapLabel.setFont(mapLabel.getFont().deriveFont(Font.BOLD));
		loadMapPanel.add(mapLabel, BorderLayout.LINE_START);
		loadMapPanel.add(txtLoadMap, BorderLayout.CENTER);
		loadMapPanel.add(btnLoadMap, BorderLayout.LINE_END);
		mapPanel.add(loadMapPanel, BorderLayout.PAGE_START);
		
		borderPanel.add(mapPanel, BorderLayout.CENTER);
		
		// Right panel
		JPanel rightPanel = new JPanel(new BorderLayout());
		
		// Server details
		JPanel detailsPanel = new JPanel(new BorderLayout());
		detailsPanel.setBorder(BorderFactory.createTitledBorder(""));
		lblListening.setForeground(Color.RED);
		detailsPanel.add(lblListening, BorderLayout.PAGE_START);
		
		JPanel labelPanel = new JPanel(new GridLayout(3, 1));
		JLabel hostLabel = new JLabel("Host:   ");
		hostLabel.setFont(hostLabel.getFont().deriveFont(Font.BOLD));
		labelPanel.add(hostLabel);
		JLabel portLabel = new JLabel("Port:   ");
		portLabel.setFont(portLabel.getFont().deriveFont(Font.BOLD));
		labelPanel.add(portLabel);
		detailsPanel.add(labelPanel, BorderLayout.LINE_START);
		
		JPanel editablePanel = new JPanel(new GridLayout(3, 1));
		editablePanel.add(lblHost);
		editablePanel.add(txtPort);
		editablePanel.add(btnListen);
		detailsPanel.add(editablePanel, BorderLayout.CENTER);
		
		rightPanel.add(detailsPanel, BorderLayout.PAGE_START);
		
		// Players
		JPanel playerPanel = new JPanel();
		playerPanel.setLayout(new BorderLayout());
		
		JScrollPane playerScroll = new JScrollPane(lstPlayers);
		playerScroll.setPreferredSize(new Dimension(0, 200));
		playerPanel.add(playerScroll, BorderLayout.CENTER);
		
		JPanel playerButtonPanel = new JPanel(new GridLayout(1,2));
		playerButtonPanel.add(btnKickOut);
		playerButtonPanel.add(btnMute);
		playerPanel.add(playerButtonPanel, BorderLayout.PAGE_END);
		
		rightPanel.add(playerPanel, BorderLayout.CENTER);
		
		borderPanel.add(rightPanel, BorderLayout.LINE_END);
		
		// Display the GUI
		add(borderPanel);
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	/**
	 * Updates the listening label to show that the server is listening for clients
	 */
	public void startListening() {
		lblListening.setText("Client listening enabled.");
		lblListening.setForeground(new Color(0, 150, 0));
		btnListen.setText("Stop Listening");
		txtPort.setEnabled(false);
	}
	
	/**
	 * Updates the listening label to show that the server has stopped listening for clients
	 */
	public void stopListening() {
		lblListening.setText("Client listening disabled.");
		lblListening.setForeground(Color.RED);
		btnListen.setText("Start Listening");
		txtPort.setEnabled(true);
	}

	/**
	 * Updates the GUI when the game model has changed
	 */
	@Override
	public void update(Observable observable, Object value) {
		Game game = (Game) observable;
		
		// Update the map
		txtaMap.setText(game.getMap().toString());
		
		// Update the players
		Vector<Player> players = game.getPlayers();
		lstPlayers.setListData(players);
		
		if (players.size() > 0) {
			txtLoadMap.setEnabled(false);
			btnLoadMap.setEnabled(false);
		} else {
			txtLoadMap.setEnabled(true);
			btnLoadMap.setEnabled(true);
		}
	}
}