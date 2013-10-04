import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Utilities;

/**
 * The PlayGame class acts as a controller between the PlayGUI and PlayModel.
 * 
 * @author Sean Stinson, ss938
 *
 */
public class PlayGame {
	public static final ArrayList<String> chatCommands = new ArrayList<String>();
	{
		chatCommands.add("/whisper <playername> <message>");
		chatCommands.add("/hug <playername>");
		chatCommands.add("/agree <playername>");
		chatCommands.add("/disagree <playername>");
		chatCommands.add("/love <playername>");
		chatCommands.add("/kiss <playername>");
		chatCommands.add("/gift <playername>");
		chatCommands.add("/lol");
		chatCommands.add("/coffee");
		chatCommands.add("/beer");
		chatCommands.add("/angry");
		chatCommands.add("/confused");
		chatCommands.add("/tired");
		chatCommands.add("/pizza");
		chatCommands.add("/brb");
		chatCommands.add("/waiting");
	}
	
	private PlayModel model;
	private PlayGUI view;
	private Client client;
	
	private boolean typing;
	
	/**
	 * Constructor sets up the fields and starts the client game
	 * 
	 * @param client The client connection
	 */
	public PlayGame(Client client) {
		this.client = client;
		model = client.getModel();
		view = new PlayGUI();
		typing = false;
		
		startGame();
	}
	
	/**
	 * Sets up the game environment for the player
	 */
	public void startGame() {
		client.start();
		
		addObservers();
		addEventListeners();

		view.draw();
	}
	
	/**
	 * Links the observer objects to the observable model objects
	 */
	public void addObservers() {
		model.getMap().addObserver(view.getPaneMap());
		model.getDungeonName().addObserver(view.getLblDungeonName());
		model.getGoal().addObserver(view.getLblGoal());
		model.getGold().addObserver(view.getLblGold());
		model.getHp().addObserver(view.getLblHp());
		model.getSword().addObserver(view.getLblSword());
		model.getArmour().addObserver(view.getLblArmour());
		model.getLantern().addObserver(view.getLblLantern());
		model.getGameMessage().addObserver(view.getPaneGameMessages());
		model.getTurn().addObserver(view.getPaneTurn());
		model.getPlayers().addObserver(view.getPanePlayers());
		model.getInGame().addObserver(view);
	}
	
	/**
	 * Adds event listeners to the GUI components
	 */
	public void addEventListeners() {
		leaveButtonEvents();
		mapButtonEvents();
		chatBoxEvents();
		endTurnButtonEvents();
		chatStylingEvents();
		chatComboBoxEvents();
	}
	
	/**
	 * Adds the chat combo box action listener
	 */
	public void chatComboBoxEvents() {
		view.getCmbChat().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (((String)view.getCmbChat().getSelectedItem()).equals("Shout: ")) {
					view.getTxtaChat().setText(view.getTxtaChat().getText().replaceAll("\n", ""));
				}
			}
		});
	}
	
	/**
	 * Adds the chat styling button action listener
	 */
	public void chatStylingEvents() {
		// Chat styling buttons
		addStylingEvent(view.getBtnBold());
		addStylingEvent(view.getBtnItalic());
		addStylingEvent(view.getBtnUnderline());
		
		// Colour button
		addColorStylingEvent(view.getBtnColor());
		
		// Emoticon buttons
		addEmoticonEvent(view.getBtnSmile());
		addEmoticonEvent(view.getBtnSad());
		addEmoticonEvent(view.getBtnShocked());
		addEmoticonEvent(view.getBtnTongue());
		addEmoticonEvent(view.getBtnHappy());
		addEmoticonEvent(view.getBtnConfused());
		addEmoticonEvent(view.getBtnThumb());
	}
	
	/**
	 * Adds an action listener to an emoticon button
	 * 
	 * @param emoticonButton The button to add the action listener to
	 */
	public void addEmoticonEvent(final JButton emoticonButton) {
		emoticonButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				view.insertChatTags(emoticonButton.getName(), "");
			}
		});
	}
	
	/**
	 * Adds an action listener to a chat styling button
	 * 
	 * @param tagButton The button to add the action listener to
	 */
	public void addStylingEvent(final JButton tagButton) {
		tagButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				view.insertChatTags("[" + tagButton.getName() + "]", "[/" + tagButton.getName() + "]");
			}
		});
	}
	
	/**
	 * Adds an action listener to a colour styling button
	 * 
	 * @param colorButton The button to add the action listener to
	 */
	public void addColorStylingEvent(final JButton colorButton) {
		colorButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Color pickedColor = JColorChooser.showDialog(view, "Choose Text Color", colorButton.getBackground());
				if (pickedColor != null) {
					colorButton.setBackground(pickedColor);
					view.insertChatTags("[c=#" +  Integer.toHexString(pickedColor.getRGB() & 0xFFFFFF).toUpperCase() + "]", "[/c]");
				}
			}
		});
	}
	
	/**
	 * Adds the leave button event listener
	 */
	public void leaveButtonEvents() {
		view.getBtnLeave().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
	}
	
	/**
	 * Adds the map button event listeners
	 */
	public void mapButtonEvents() {
		// Sends the PICKUP command
		view.getPaneMap().getCentreButton().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				client.sendMessage("PICKUP");
			}
		});
		
		// Sends the MOVE N or ATTACK N commands
		view.getPaneMap().getButtonN().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (view.getPaneMap().getButtonN().getText().equals("P")) {
					client.sendMessage("ATTACK N");
				} else {
					client.sendMessage("MOVE N");
				}
			}
		});
		
		// Sends the MOVE S or ATTACK S commands
		view.getPaneMap().getButtonS().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (view.getPaneMap().getButtonS().getText().equals("P")) {
					client.sendMessage("ATTACK S");
				} else {
					client.sendMessage("MOVE S");
				}
			}
		});
		
		// Sends the MOVE E or ATTACK E commands
		view.getPaneMap().getButtonE().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (view.getPaneMap().getButtonE().getText().equals("P")) {
					client.sendMessage("ATTACK E");
				} else {
					client.sendMessage("MOVE E");
				}
			}
		});
		
		// Sends the MOVE W or ATTACK W commands
		view.getPaneMap().getButtonW().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (view.getPaneMap().getButtonW().getText().equals("P")) {
					client.sendMessage("ATTACK W");
				} else {
					client.sendMessage("MOVE W");
				}
			}
		});
		
		// Mouse hover listener
		MouseListener hover = new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) { /* None */ }

			@Override
			public void mouseEntered(MouseEvent e) {
				JButton btnHovered = (JButton) e.getSource();
				btnHovered.setContentAreaFilled(true);
			}

			@Override
			public void mouseExited(MouseEvent e) {
				JButton btnHovered = (JButton) e.getSource();
				btnHovered.setContentAreaFilled(false);
			}

			@Override
			public void mousePressed(MouseEvent e) { /* None */ }

			@Override
			public void mouseReleased(MouseEvent e) { /* None */ }
		};
		
		// Adds the hover MouseListener to the control buttons
		view.getPaneMap().getCentreButton().addMouseListener(hover);
		view.getPaneMap().getButtonN().addMouseListener(hover);
		view.getPaneMap().getButtonE().addMouseListener(hover);
		view.getPaneMap().getButtonS().addMouseListener(hover);
		view.getPaneMap().getButtonW().addMouseListener(hover);
	}
	
	/**
	 * Adds the chat text box event listeners
	 */
	public void chatBoxEvents() {
		// Remove the default ENTER newline (because we want ENTER to send the message)
		view.getTxtaChat().getInputMap().put(KeyStroke.getKeyStroke("ENTER"), "doNothing");
		
		// Keyboard event
		view.getTxtaChat().addKeyListener(new KeyListener() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					String message = view.getTxtaChat().getText();
					
					if (e.getModifiers() != KeyEvent.SHIFT_MASK) {
						if (message.length() > 0) {
							if (((String) view.getCmbChat().getSelectedItem()).equals("Shout: ")) {
								client.sendMessage("SHOUT " + message);
								view.getTxtaChat().setText("");
							} else {
								if (message.equals("/commands")) {
									view.getTxtaChat().setText("");
									
									String commandList = "";
									for (String command : chatCommands) {
										commandList += command + "\n";
									}
									
									// Chat command list text area
									JTextArea txtaCommands = new JTextArea(commandList);
									txtaCommands.setRows(chatCommands.size());
									txtaCommands.setLineWrap(true);
									txtaCommands.setEditable(false);
									
									JOptionPane.showMessageDialog(view, txtaCommands, "Chat Commands", JOptionPane.PLAIN_MESSAGE);
								} else {
									// Convert line wrapping to new lines
									ArrayList<String> lines = new ArrayList<String>();
									try {
										int beginIndex = 0;
										while (beginIndex < message.length()) {
											int endIndex = Utilities.getRowEnd(view.getTxtaChat(), beginIndex);
											lines.add(message.substring(beginIndex, endIndex));
											beginIndex = endIndex + 1;
										}
									} catch (BadLocationException ble) {
										ble.printStackTrace();
									}
									
									message = "";
									for (int i = 0; i < lines.size(); i++) {
										String line = lines.get(i).replaceAll("\n", ""); // Remove new line characters
										message += line;
										if (i != lines.size() - 1) {
											message += "[br]";
										}
									}
									
									client.sendMessage("CHAT " + message);
									view.getTxtaChat().setText("");
								}
							}
						}
					} else {
						if (!view.getCmbChat().getSelectedItem().equals("Shout: ")) {
							view.getTxtaChat().setText(message + "\n");
						}
					}
				}
			}
			
			@Override
			public void keyReleased(KeyEvent e) { /* None */ }

			@Override
			public void keyTyped(KeyEvent e) {
				if (view.getTxtaChat().getForeground() == Color.GRAY) {
					view.getTxtaChat().setText("");
					view.getTxtaChat().setForeground(Color.BLACK);
				}
			}
		});
		
		// Typing events
		view.getTxtaChat().getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void changedUpdate(DocumentEvent e) {
				sendTypingMessage();
			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				sendTypingMessage();
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				sendTypingMessage();
			}
			
			public void sendTypingMessage() {
				if (!view.getTxtaChat().getText().equals("<Type your chat message here and press ENTER>")) {
					if (view.getTxtaChat().getText().length() > 0 && !typing) {
						client.sendMessage("STARTTYPING");
						typing = true;
					} else if (view.getTxtaChat().getText().length() == 0 && typing) {
						client.sendMessage("ENDTYPING");
						typing = false;
					}
					
					view.getTxtaChat().setForeground(Color.BLACK);
				}
			}
		});
	}
	
	/**
	 * Adds the end turn button event listener
	 */
	public void endTurnButtonEvents() {
		view.getPaneTurn().getBtnEndTurn().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				client.sendMessage("ENDTURN");
			}
		});
	}
}
