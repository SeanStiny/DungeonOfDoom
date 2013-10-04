import java.util.TimerTask;

/**
 * The ClientMessage class handles a message received from the client
 * 
 * @author Sean Stinson, ss938
 *
 */
public class ClientMessage {
	private Connection client;
	private Game game;
	private Player player;
	
	private String command;
	private String parameter;
	
	/**
	 * Constructor splits the command from the argument
	 * 
	 * @param message    The message received from the client
	 * @param client     The connection to the client
	 */
	public ClientMessage(String message, Connection client) {
		this.client = client;
		game = client.getGame();
		
		if (client.isInGame()) {
			player = client.getPlayer();
		} else {
			player = null;
		}
		
		command = message.split(" ", 2)[0];
		
		if (message.contains(" ")) {
			parameter = message.split(" ", 2)[1];
		} else {
			parameter = null;
		}
		
		handle();
	}
	
	/**
	 * Checks which command has been sent
	 */
	public void handle() {
		if (command.equals("HELLO")) {
			if (client.getUsername() == null) {
				clientHello();
			} else {
				client.sendMessage("FAIL Must be sent before any other command");
			}
		} else if (client.isInGame()) {
			if (command.equals("LOOK")) {
				clientLook();
			} else if (command.equals("MOVE")) {
				clientMove();
			} else if (command.equals("ATTACK")) {
				clientAttack();
			} else if (command.equals("PICKUP")) {
				clientPickUp();
			} else if (command.equals("SHOUT")) {
				clientShout();
			} else if (command.equals("ENDTURN")) {
				clientEndTurn();
			} else if (command.equals("STARTTYPING")) {
				clientStartTyping();
			} else if (command.equals("ENDTYPING")) {
				clientEndTyping();
			} else if (command.equals("CHAT")) {
				clientChat();
			} else {
				client.sendMessage("FAIL Invalid command.");
			}
		} else {
			client.setUsername();
			game.join(client);
			handle();
		}
	}
	
	/**
	 * Handles the HELLO command
	 */
	public void clientHello() {
		if (parameter != null) {
			client.setUsername(parameter);
			game.join(client);
		} else {
			client.sendMessage("FAIL Must include name.");
		}
	}
	
	/**
	 * Handles the LOOK command
	 */
	public void clientLook() {
		game.playerLook(player);
	}
	
	/**
	 * Handles the MOVE command
	 */
	public void clientMove() {
		if (!game.isFinished()) {
			if (!player.isDead()) {
				if (player.isTurn()) {
					if (parameter != null) {
						if (parameter.matches("[NSEW]")) {
							game.playerMove(parameter.charAt(0));
						} else {
							client.sendMessage("FAIL Direction must be N, S, E or W.");
						}
					} else {
						client.sendMessage("FAIL Move must include direction.");
					}
				} else {
					client.sendMessage("FAIL You can only move when it is your turn.");
				}
			} else {
				client.sendMessage("FAIL You cannot move when you are dead.");
			}
		} else {
			client.sendMessage("FAIL You cannot move after the game has finished.");
		}
	}
	
	/**
	 * Handles the ATTACK command
	 */
	public void clientAttack() {
		if (!game.isFinished()) {
			if (!player.isDead()) {
				if (player.isTurn()) {
					if (parameter != null) {
						if (parameter.matches("[NSEW]")) {
							game.playerAttack(parameter.charAt(0));
						}
					} else {
						client.sendMessage("FAIL Attack must include direction.");
					}
				} else {
					client.sendMessage("FAIL You can only attack when it is your turn.");
				}
			} else {
				client.sendMessage("FAIL You cannot attack when you are dead.");
			}
		} else {
			client.sendMessage("FAIL You cannot attack after the game has finished.");
		}
	}
	
	/**
	 * Handles the PICKUP command
	 */
	public void clientPickUp() {
		if (!game.isFinished()) {
			if (!player.isDead()) {
				if (player.isTurn()) {
					game.playerPickUp();
				} else {
					client.sendMessage("FAIL You can only pick up when it is your turn.");
				}
			} else {
				client.sendMessage("FAIL You cannot pick up when you are dead.");
			}
		} else {
			client.sendMessage("FAIL You cannot pick up after the game has finished.");
		}
	}
	
	/**
	 * Handles the SHOUT command
	 */
	public void clientShout() {
		if (parameter != null) {
			game.playerShout(client.getPlayer(), parameter);
		} else {
			client.sendMessage("FAIL Must include message.");
		}
	}
	
	/**
	 * Handles the ENDTURN command
	 */
	public void clientEndTurn() {
		if (!game.isFinished()) {
			if (player.isTurn()) {
				game.playerEndTurn();
			} else {
				client.sendMessage("FAIL It is not your turn to end.");
			}
		} else {
			client.sendMessage("FAIL You cannot end the turn after the game has finished.");
		}
	}
	
	/**
	 * Handles the STARTTYPING command
	 */
	public void clientStartTyping() {
		game.sendToAll("STARTTYPING " + client.getUsername());
	}
	
	/**
	 * Handles the ENDTYPING command
	 */
	public void clientEndTyping() {
		game.sendToAll("ENDTYPING " + client.getUsername());
	}
	
	/**
	 * Handles the CHAT command
	 */
	public void clientChat() {
		if (parameter.length() > 0) {
			if (!client.isMuted()) {
				// If the user's message starts with a "/" then perform a chat command, else just send the chat message
				if (parameter.charAt(0) == '/') {
					String[] chatParameters = parameter.split(" ", 3);
					String chatCommand = chatParameters[0];
					String chatParameter = null;
					String chatMessage = null;
					
					if (chatParameters.length > 1) {
						chatParameter = chatParameters[1];
						if (chatParameters.length > 2) {
							chatMessage = chatParameters[2];
						}
					}
						
					// Whisper chat command
					if (chatCommand.equalsIgnoreCase("/whisper")) {
						if (chatParameter != null) {
							Player receivingPlayer = game.getPlayer(chatParameter);
							if (receivingPlayer != null) {
								if (chatMessage != null) {
									receivingPlayer.getClient().sendMessage("WHISPERFROM " + client.getUsername() + " " + chatMessage);
									client.sendMessage("WHISPERTO " + receivingPlayer.getClient().getUsername() + " " + chatMessage);
								} else {
									client.sendMessage("FAIL Must include a message to whisper.");
								}
							} else {
								client.sendMessage("FAIL Player '" + chatParameter + "' does not exist.");
							}
						} else {
							client.sendMessage("FAIL Must include a player to whisper to.");
						}
					}
					
					// Hug chat command
					else if (chatCommand.equalsIgnoreCase("/hug")) {
						if (chatParameter != null) {
							Player receivingPlayer = game.getPlayer(chatParameter);
							if (receivingPlayer != null) {
								game.sendToAll("HUG " + client.getUsername() + " " + receivingPlayer.getClient().getUsername());
							} else {
								client.sendMessage("FAIL Player '" + chatParameter + "' does not exist.");
							}
						} else {
							client.sendMessage("FAIL Must include a player to hug.");
						}
					}
					
					// Agree chat command
					else if (chatCommand.equalsIgnoreCase("/agree")) {
						if (chatParameter != null) {
							Player receivingPlayer = game.getPlayer(chatParameter);
							if (receivingPlayer != null) {
								game.sendToAll("AGREE " + client.getUsername() + " " + receivingPlayer.getClient().getUsername());
							} else {
								client.sendMessage("FAIL Player '" + chatParameter + "' does not exist.");
							}
						} else {
							client.sendMessage("FAIL Must include a player to agree with.");
						}
					}
					
					// Disagree chat command
					else if (chatCommand.equalsIgnoreCase("/disagree")) {
						if (chatParameter != null) {
							Player receivingPlayer = game.getPlayer(chatParameter);
							if (receivingPlayer != null) {
								game.sendToAll("DISAGREE " + client.getUsername() + " " + receivingPlayer.getClient().getUsername());
							} else {
								client.sendMessage("FAIL Player '" + chatParameter + "' does not exist.");
							}
						} else {
							client.sendMessage("FAIL Must include a player to disagree with.");
						}
					}
					
					// Lol chat command
					else if (chatCommand.equalsIgnoreCase("/lol")) {
						game.sendToAll("LOL " + client.getUsername());
					}
					
					// Coffee chat command
					else if (chatCommand.equalsIgnoreCase("/coffee")) {
						game.sendToAll("COFFEE " + client.getUsername());
					}
					
					// Beer chat command
					else if (chatCommand.equalsIgnoreCase("/beer")) {
						game.sendToAll("BEER " + client.getUsername());
					}
					
					// Gift chat command
					else if (chatCommand.equalsIgnoreCase("/gift")) {
						if (chatParameter != null) {
							Player receivingPlayer = game.getPlayer(chatParameter);
							if (receivingPlayer != null) {
								game.sendToAll("GIFT " + client.getUsername() + " " + receivingPlayer.getClient().getUsername());
							} else {
								client.sendMessage("FAIL Player '" + chatParameter + "' does not exist.");
							}
						} else {
							client.sendMessage("FAIL Must include a player to give a gift.");
						}
					}
					
					// Angry chat command
					else if (chatCommand.equalsIgnoreCase("/angry")) {
						game.sendToAll("ANGRY " + client.getUsername());
					}
					
					// Confused chat command
					else if (chatCommand.equalsIgnoreCase("/confused")) {
						game.sendToAll("CONFUSED " + client.getUsername());
					}
					
					// Tired chat command
					else if (chatCommand.equalsIgnoreCase("/tired")) {
						game.sendToAll("TIRED " + client.getUsername());
					}
					
					// Pizza chat command
					else if (chatCommand.equalsIgnoreCase("/pizza")) {
						game.sendToAll("PIZZA " + client.getUsername());
					}
					
					// Love chat command
					else if (chatCommand.equalsIgnoreCase("/love")) {
						if (chatParameter != null) {
							Player receivingPlayer = game.getPlayer(chatParameter);
							if (receivingPlayer != null) {
								game.sendToAll("LOVE " + client.getUsername() + " " + receivingPlayer.getClient().getUsername());
							} else {
								client.sendMessage("FAIL Player '" + chatParameter + "' does not exist.");
							}
						} else {
							client.sendMessage("FAIL Must include a player to love.");
						}
					}
					
					// Brb chat command
					else if (chatCommand.equalsIgnoreCase("/brb")) {
						game.sendToAll("BRB " + client.getUsername());
					}
					
					// Kiss chat command
					else if (chatCommand.equalsIgnoreCase("/kiss")) {
						if (chatParameter != null) {
							Player receivingPlayer = game.getPlayer(chatParameter);
							if (receivingPlayer != null) {
								game.sendToAll("KISS " + client.getUsername() + " " + receivingPlayer.getClient().getUsername());
							} else {
								client.sendMessage("FAIL Player '" + chatParameter + "' does not exist.");
							}
						} else {
							client.sendMessage("FAIL Must include a player to kiss.");
						}
					}
					
					// Waiting chat command
					else if (chatCommand.equalsIgnoreCase("/waiting")) {
						game.sendToAll("WAITING " + client.getUsername());
					}
					
					// If no matching command is found, send fail message
					else {
						client.sendMessage("FAIL Unknown chat command '" + chatCommand + "'.");
					}
				} else {
					game.sendToAll("CHAT " + client.getUsername() + " " + parameter);
				}
				
				// Anti-spam
				client.incrementSpamCount();
				client.getSpamTimer().schedule(new TimerTask() {
					@Override
					public void run() {
						client.decrementSpamCount();
					}
				}, 3000); // decrement the spam count 3 seconds after receiving a chat message
				
				if (client.getSpamCount() >= 5) {
					client.setMuted(true);
					client.getSpamTimer().schedule(new TimerTask() {
						@Override
						public void run() {
							client.setMuted(false);
						}
					}, 10000);
				}
			} else {
				client.sendMessage("FAIL You are muted and cannot chat.");
			}
		} else {
			client.sendMessage("FAIL You must include a chat message.");
		}
	}
}
