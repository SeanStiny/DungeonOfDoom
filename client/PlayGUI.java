import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;
import javax.swing.text.BadLocationException;

/**
 * The PlayGUI class creates all of the game GUI components and displays them to the user.
 * 
 * @author Sean Stinson, ss938
 *
 */
public class PlayGUI extends JFrame implements Observer {
	private static final long serialVersionUID = 604467670312810787L;
	
	private MapPanel paneMap;
	
	// Top panel components
	private ObserverLabel lblDungeonName;
	private ObserverLabel lblGoal;
	private ObserverLabel lblGold;
	private ObserverLabel lblHp;
	private JButton btnLeave;
	
	// Bottom panel components
	private JComboBox cmbChat;
	private JTextArea txtaChat;
	private JButton btnBold;
	private JButton btnItalic;
	private JButton btnUnderline;
	private JButton btnColor;
	private JButton btnShocked;
	private JButton btnSmile;
	private JButton btnSad;
	private JButton btnHappy;
	private JButton btnConfused;
	private JButton btnTongue;
	private JButton btnThumb;
	private ObserverIcon lblSword;
	private ObserverIcon lblArmour;
	private ObserverIcon lblLantern;
	private MessagePanel paneGameMessages;
	private TurnPanel paneTurn;
	private PlayerPanelList panePlayers;
	
	/**
	 * Constructor initialises the GUI components
	 */
	public PlayGUI() {
		getContentPane().setBackground(Color.BLACK);
		paneMap = new MapPanel();
		lblDungeonName = new ObserverLabel("Map: ", "Default Map");
		lblGoal = new ObserverLabel("Goal: ", "0");
		lblGold = new ObserverLabel("Gold: ", "0");
		lblHp = new ObserverLabel("HP: ", "3");
		btnLeave = new JButton("Leave Game");
		
		cmbChat = new JComboBox();
		txtaChat = new JTextArea();
		btnBold = new JButton("B");
		btnItalic = new JButton("I");
		btnUnderline = new JButton("<html><u>U</u></html>");
		btnColor = new JButton();
		btnShocked = new JButton(new ImageIcon("resources/images/shocked_emoticon.png"));
		btnSmile = new JButton(new ImageIcon("resources/images/smile_emoticon.png"));
		btnSad = new JButton(new ImageIcon("resources/images/frown_emoticon.png"));
		btnHappy = new JButton(new ImageIcon("resources/images/happy_emoticon.png"));
		btnConfused = new JButton(new ImageIcon("resources/images/confused_emoticon.png"));
		btnTongue = new JButton(new ImageIcon("resources/images/tongue_emoticon.png"));
		btnThumb = new JButton(new ImageIcon("resources/images/thumb_emoticon.png"));
		lblSword = new ObserverIcon("resources/images/item_sword.png", "resources/images/item_sword_shadow.png", "Sword", false);
		lblArmour = new ObserverIcon("resources/images/item_armour.png", "resources/images/item_armour_shadow.png", "Armour", false);
		lblLantern = new ObserverIcon("resources/images/item_lantern.png", "resources/images/item_lantern_shadow.png", "Lantern", false);
		paneGameMessages = new MessagePanel();
		paneTurn = new TurnPanel();
		panePlayers = new PlayerPanelList(txtaChat);
	}
	
	/**
	 * Gets the leave button component
	 * 
	 * @return The leave button component
	 */
	public JButton getBtnLeave() {
		return btnLeave;
	}
	
	/**
	 * Gets the chat combo box component
	 * 
	 * @return The chat combo box component
	 */
	public JComboBox getCmbChat() {
		return cmbChat;
	}
	
	/**
	 * Gets the chat text field component
	 * 
	 * @return The chat text field component
	 */
	public JTextArea getTxtaChat() {
		return txtaChat;
	}
	
	/**
	 * Gets the bold chat tag button component
	 * 
	 * @return The bold chat tag button component
	 */
	public JButton getBtnBold() {
		return btnBold;
	}

	/**
	 * Gets the italic chat tag button component
	 * 
	 * @return The italic chat tag button component
	 */
	public JButton getBtnItalic() {
		return btnItalic;
	}

	/**
	 * Gets the underline chat tag button component
	 * 
	 * @return The underline chat tag button component
	 */
	public JButton getBtnUnderline() {
		return btnUnderline;
	}

	/**
	 * Gets the colour chat tag button component
	 * 
	 * @return The colour chat tag button component
	 */
	public JButton getBtnColor() {
		return btnColor;
	}

	/**
	 * Gets the shocked emoticon button component
	 * 
	 * @return The shocked emoticon button component
	 */
	public JButton getBtnShocked() {
		return btnShocked;
	}

	/**
	 * Gets the smile emoticon button component
	 * 
	 * @return The smile emoticon button component
	 */
	public JButton getBtnSmile() {
		return btnSmile;
	}

	/**
	 * Gets the sad emoticon button component
	 * 
	 * @return The sad emoticon button component
	 */
	public JButton getBtnSad() {
		return btnSad;
	}

	/**
	 * Gets the happy emoticon button component
	 * 
	 * @return The happy emoticon button component
	 */
	public JButton getBtnHappy() {
		return btnHappy;
	}

	/**
	 * Gets the confused emoticon button component
	 * 
	 * @return The confused emoticon button component
	 */
	public JButton getBtnConfused() {
		return btnConfused;
	}

	/**
	 * Gets the tongue emoticon button component
	 * 
	 * @return The tongue emoticon button component
	 */
	public JButton getBtnTongue() {
		return btnTongue;
	}

	/**
	 * Gets the thumb emoticon button component
	 * 
	 * @return The thumb emoticon button component
	 */
	public JButton getBtnThumb() {
		return btnThumb;
	}

	/**
	 * Gets the map panel component
	 * 
	 * @return The map panel component
	 */
	public MapPanel getPaneMap() {
		return paneMap;
	}
	
	/**
	 * Gets the dungeon name label component
	 * 
	 * @return The dungeon name label component
	 */
	public ObserverLabel getLblDungeonName() {
		return lblDungeonName;
	}
	
	/**
	 * Gets the goal label component
	 * 
	 * @return The goal label component
	 */
	public ObserverLabel getLblGoal() {
		return lblGoal;
	}
	
	/**
	 * Gets the gold label component
	 * 
	 * @return The gold label component
	 */
	public ObserverLabel getLblGold() {
		return lblGold;
	}
	
	/**
	 * Gets the hit points label component
	 * 
	 * @return The hit points label component
	 */
	public ObserverLabel getLblHp() {
		return lblHp;
	}
	
	/**
	 * Gets the sword label component
	 * 
	 * @return The sword label component
	 */
	public ObserverIcon getLblSword() {
		return lblSword;
	}
	
	/**
	 * Gets the armour label component
	 * 
	 * @return The armour label component
	 */
	public ObserverIcon getLblArmour() {
		return lblArmour;
	}
	
	/**
	 * Gets the lantern label component
	 * 
	 * @return The lantern label component
	 */
	public ObserverIcon getLblLantern() {
		return lblLantern;
	}
	
	/**
	 * Gets the game message panel component
	 * 
	 * @return The game message panel component
	 */
	public MessagePanel getPaneGameMessages() {
		return paneGameMessages;
	}
	
	/**
	 * Gets the turn panel component
	 * 
	 * @return The turn panel component
	 */
	public TurnPanel getPaneTurn() {
		return paneTurn;
	}
	
	/**
	 * Gets the player panel component
	 * 
	 * @return The player panel component
	 */
	public PlayerPanelList getPanePlayers() {
		return panePlayers;
	}
	
	/**
	 * Draws the GUI on the screen
	 */
	public void draw() {
		setTitle("Dungeon of Dooom - Game");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBackground(Color.BLACK);
		setLayout(new BorderLayout());
		
		add(paneMap, BorderLayout.CENTER); // Map panel component
		addTopPanel(); // Top panel
		addBottomPanel();
		
		pack();
		if (getSize().width < 950) {
			setSize(new Dimension(950, getSize().height));
		}
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	/**
	 * Adds the bottom panel to the frame
	 */
	private void addBottomPanel() {
		JPanel paneBottom = new JPanel(new BorderLayout());
		paneBottom.setOpaque(false);
		
		// Bottom left panel
		JPanel paneBottomLeftPadding = new JPanel();
		paneBottomLeftPadding.setBorder(BorderFactory.createRaisedBevelBorder());
		JPanel paneBottomLeft = new JPanel(new BorderLayout());
		
		// Chat input panel
		JPanel paneChatInput = new JPanel(new BorderLayout());
		
		// Chat styling panel
		Dimension styleButtonSize = new Dimension(40, 30);
		JPanel paneChatStyling = new JPanel(new FlowLayout());
		
		btnBold.setFont(btnBold.getFont().deriveFont(Font.BOLD));
		btnBold.setName("b");
		btnBold.setPreferredSize(styleButtonSize);
		btnBold.setFocusable(false);
		paneChatStyling.add(btnBold);
		
		btnItalic.setFont(btnItalic.getFont().deriveFont(Font.ITALIC));
		btnItalic.setName("i");
		btnItalic.setPreferredSize(styleButtonSize);
		btnItalic.setFocusable(false);
		paneChatStyling.add(btnItalic);
		
		btnUnderline.setName("u");
		btnUnderline.setPreferredSize(styleButtonSize);
		btnUnderline.setFocusable(false);
		paneChatStyling.add(btnUnderline);
		
		btnColor.setPreferredSize(styleButtonSize);
		btnColor.setBackground(Color.BLUE);
		btnColor.setFocusable(false);
		paneChatStyling.add(btnColor);
		
		btnSmile.setName(":)");
		btnSmile.setPreferredSize(styleButtonSize);
		btnSmile.setIcon(new ImageIcon("resources/images/smile_emoticon.png"));
		btnSmile.setFocusable(false);
		paneChatStyling.add(btnSmile);
		
		btnSad.setName(":(");
		btnSad.setPreferredSize(styleButtonSize);
		btnSad.setIcon(new ImageIcon("resources/images/frown_emoticon.png"));
		btnSad.setFocusable(false);
		paneChatStyling.add(btnSad);
		
		btnShocked.setName(":O");
		btnShocked.setPreferredSize(styleButtonSize);
		btnShocked.setIcon(new ImageIcon("resources/images/shocked_emoticon.png"));
		btnShocked.setFocusable(false);
		paneChatStyling.add(btnShocked);
		
		btnTongue.setName(":P");
		btnTongue.setPreferredSize(styleButtonSize);
		btnTongue.setIcon(new ImageIcon("resources/images/tongue_emoticon.png"));
		btnTongue.setFocusable(false);
		paneChatStyling.add(btnTongue);
		
		btnHappy.setName(":D");
		btnHappy.setPreferredSize(styleButtonSize);
		btnHappy.setIcon(new ImageIcon("resources/images/happy_emoticon.png"));
		btnHappy.setFocusable(false);
		paneChatStyling.add(btnHappy);
		
		btnConfused.setName(":S");
		btnConfused.setPreferredSize(styleButtonSize);
		btnConfused.setIcon(new ImageIcon("resources/images/confused_emoticon.png"));
		btnConfused.setFocusable(false);
		paneChatStyling.add(btnConfused);
		
		btnThumb.setName("(Y)");
		btnThumb.setPreferredSize(styleButtonSize);
		btnThumb.setIcon(new ImageIcon("resources/images/thumb_emoticon.png"));
		btnThumb.setFocusable(false);
		paneChatStyling.add(btnThumb);
		
		paneChatInput.add(paneChatStyling, BorderLayout.PAGE_START);
		
		// Chat typing panel
		JPanel paneChat = new JPanel(new BorderLayout());
		cmbChat.addItem("Chat: ");
		cmbChat.addItem("Shout: ");
		cmbChat.setFocusable(false);
		cmbChat.setPreferredSize(new Dimension(130, 0));
		paneChat.add(cmbChat, BorderLayout.LINE_START);
		txtaChat.setForeground(Color.GRAY);
		txtaChat.setText("<Type your chat message here and press ENTER>");
		txtaChat.setCaretPosition(0);
		txtaChat.setLineWrap(true);
		txtaChat.setWrapStyleWord(true);
		txtaChat.setRows(3);
		JScrollPane scrlChatBox = new JScrollPane(txtaChat);
		paneChat.add(scrlChatBox, BorderLayout.CENTER);
		paneChatInput.add(paneChat, BorderLayout.PAGE_END);
		
		paneBottomLeft.add(paneChatInput, BorderLayout.PAGE_END);
		
		// Game message panel
		JScrollPane scrlMessages = new JScrollPane(paneGameMessages);
		scrlMessages.setPreferredSize(new Dimension(550, 120));
		scrlMessages.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		paneBottomLeft.add(scrlMessages, BorderLayout.CENTER);
		
		paneBottomLeftPadding.add(paneBottomLeft);
		paneBottom.add(paneBottomLeftPadding, BorderLayout.LINE_START);
		
		// Bottom right panel
		JPanel paneBottomRightPadding = new JPanel();
		paneBottomRightPadding.setBorder(BorderFactory.createRaisedBevelBorder());
		JPanel paneBottomRight = new JPanel(new BorderLayout());
		
		// Player list
		JScrollPane scrlPlayers = new JScrollPane(panePlayers);
		scrlPlayers.setPreferredSize(new Dimension(300, 130));
		scrlPlayers.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrlPlayers.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		paneBottomRight.add(scrlPlayers, BorderLayout.CENTER);
		
		// Items and turn panel
		JPanel paneItems = new JPanel(new BorderLayout());
		paneItems.setBorder(new EmptyBorder(10, 0, 0, 0));
		JPanel paneItemImages = new JPanel(new FlowLayout());
		paneItemImages.setBorder(BorderFactory.createTitledBorder(""));
		paneItemImages.add(lblSword);
		lblArmour.setBorder(new EmptyBorder(0, 10, 0, 10));
		paneItemImages.add(lblArmour);
		paneItemImages.add(lblLantern);
		paneItems.add(paneItemImages, BorderLayout.LINE_START);
		paneItems.add(paneTurn, BorderLayout.CENTER);
		paneBottomRight.add(paneItems, BorderLayout.PAGE_END);
		
		paneBottomRightPadding.add(paneBottomRight);
		paneBottom.add(paneBottomRightPadding, BorderLayout.LINE_END);
		
		add(paneBottom, BorderLayout.PAGE_END);
	}
	
	/**
	 * Adds the top panel to the frame
	 */
	private void addTopPanel() {
		Font topFont = new Font("SansSerif", Font.BOLD, 16);
		
		JPanel paneTop = new JPanel(new BorderLayout());
		paneTop.setBackground(Color.BLACK);
		
		// Top left panel
		JPanel paneTopLeft = new JPanel(new BorderLayout());
		paneTopLeft.setOpaque(false);
		
		// Dungeon name
		lblDungeonName.setForeground(Color.WHITE);
		lblDungeonName.setIcon(new ImageIcon("resources/images/map.png"));
		lblDungeonName.setFont(topFont);
		paneTopLeft.add(lblDungeonName, BorderLayout.LINE_START);
		
		// Goal
		lblGoal.setForeground(Color.WHITE);
		lblGoal.setIcon(new ImageIcon("resources/images/goal.png"));
		lblGoal.setFont(topFont);
		paneTopLeft.add(lblGoal, BorderLayout.LINE_END);
		
		paneTop.add(paneTopLeft, BorderLayout.LINE_START);
		
		// Top right panel
		JPanel paneTopRight = new JPanel(new FlowLayout());
		paneTopRight.setOpaque(false);
		
		// Gold
		lblGold.setForeground(Color.WHITE);
		lblGold.setIcon(new ImageIcon("resources/images/gold.png"));
		lblGold.setFont(topFont);
		lblGold.setPreferredSize(new Dimension(130, 48));
		paneTopRight.add(lblGold);
		
		// HP
		lblHp.setForeground(Color.WHITE);
		lblHp.setIcon(new ImageIcon("resources/images/hp.png"));
		lblHp.setFont(topFont);
		lblHp.setPreferredSize(new Dimension(110, 48));
		paneTopRight.add(lblHp);
		
		// Leave button
		btnLeave.setPreferredSize(new Dimension(btnLeave.getPreferredSize().width, 48));
		btnLeave.setFocusable(false);
		paneTopRight.add(btnLeave);
		
		paneTop.add(paneTopRight, BorderLayout.LINE_END);
		
		add(paneTop, BorderLayout.PAGE_START);
	}
	
	/**
	 * Inserts tags into the chat text box
	 * 
	 * @param tagStart The tag to signify to the start of the selection
	 * @param tagEnd   The tag to signify the end of the selection
	 */
	public void insertChatTags(String tagStart, String tagEnd) {
		// Remove water mark text
		if (txtaChat.getForeground() != Color.BLACK) {
			txtaChat.setForeground(Color.BLACK);
			txtaChat.setText("");
		}
		
		// Insert the tags around the selected text
		int startIndex = txtaChat.getSelectionStart();
		int endIndex = txtaChat.getSelectionEnd() + tagStart.length();
		try {
			txtaChat.getDocument().insertString(startIndex, tagStart, null);
			txtaChat.getDocument().insertString(endIndex, tagEnd, null);
			txtaChat.setCaretPosition(endIndex);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Updates the GUI when the player is no longer in the game
	 */
	@Override
	public void update(Observable observable, Object value) {
		paneTurn.chatOnly();
	}
}
