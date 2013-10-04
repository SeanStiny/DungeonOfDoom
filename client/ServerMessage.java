import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;

import javax.swing.JOptionPane;

/**
 * The ServerMessage class represents a message received from the server.
 * 
 * @author Sean Stinson, ss938
 *
 */
public class ServerMessage {
	private Client client;
	private PlayModel model;
	
	private String command;
	private String parameter;
	
	/**
	 * Constructor initialises the fields and passes the message and client as parameters
	 * 
	 * @param message The message sent by the server
	 * @param client  The connection to the server
	 */
	public ServerMessage(String message, Client client) {
		this.client = client;
		model = client.getModel();
		
		String[] splitMessage = message.split(" ", 2);
		command = splitMessage[0];
		if (splitMessage.length == 2) {
			parameter = splitMessage[1];
		} else {
			parameter = null;
		}
	}
	
	/**
	 * Gets the server message command
	 * 
	 * @return The server message command
	 */
	public String getCommand() {
		return command;
	}

	/**
	 * Sets the server message command
	 * 
	 * @param command The new server message command
	 */
	public void setCommand(String command) {
		this.command = command;
	}

	/**
	 * Gets the server message parameter
	 * 
	 * @return The server message parameter
	 */
	public String getParameter() {
		return parameter;
	}

	/**
	 * Sets the server message parameter
	 * 
	 * @param parameter The new server message parameter
	 */
	public void setParameter(String parameter) {
		this.parameter = parameter;
	}

	/**
	 * Handles the message with the appropriate action
	 */
	public void handle() {
		if (command.equals("HELLO") && parameter != null) {
			serverHello();
		} else if (command.equals("GOAL") && parameter != null) {
			serverGoal();
		} else if (command.equals("WIN")) {
			serverWin();
		} else if (command.equals("LOSE")) {
			serverLose();
		} else if (command.equals("CHANGE")) {
			serverChange();
		} else if (command.equals("STARTTURN")) {
			serverStartTurn();
		} else if (command.equals("ENDTURN")) {
			serverEndTurn();
		} else if (command.equals("HITMOD") && parameter != null) {
			serverHitMod();
		} else if (command.equals("TREASUREMOD") && parameter != null) {
			serverTreasureMod();
		} else if (command.equals("MESSAGE") && parameter != null) {
			serverMessage();
		} else if (command.equals("FAIL") && parameter != null) {
			serverFail();
		} else if (command.equals("LOOKREPLY")) {
			serverLookReply();
		} else if (command.equals("DUNGEON") && parameter != null) {
			serverDungeon();
		} else if (command.equals("SWORDMOD")) {
			serverSwordMod();
		} else if (command.equals("ARMOURMOD")) {
			serverArmourMod();
		} else if (command.equals("LANTERNMOD")) {
			serverLanternMod();
		} else if (command.equals("SHOUT") && parameter != null) {
			serverShout();
		} else if (command.equals("PLAYERS") && parameter != null) {
			serverPlayers();
		} else if (command.equals("STARTTYPING") && parameter != null) {
			serverStartTyping();
		} else if (command.equals("ENDTYPING") && parameter != null) {
			serverEndTyping();
		} else if (command.equals("CHAT") && parameter != null) {
			serverChat();
		} else if (command.equals("WHISPERTO") && parameter != null) {
			serverWhisperTo();
		} else if (command.equals("WHISPERFROM") && parameter != null) {
			serverWhisperFrom();
		} else if (command.equals("HUG") && parameter != null) {
			serverHug();
		} else if (command.equals("AGREE") && parameter != null) {
			serverAgree();
		} else if (command.equals("DISAGREE") && parameter != null) {
			serverDisagree();
		} else if (command.equals("LOL") && parameter != null) {
			serverLol();
		} else if (command.equals("COFFEE") && parameter != null) {
			serverCoffee();
		} else if (command.equals("BEER") && parameter != null) {
			serverBeer();
		} else if (command.equals("GIFT") && parameter != null) {
			serverGift();
		} else if (command.equals("ANGRY") && parameter != null) {
			serverAngry();
		} else if (command.equals("CONFUSED") && parameter != null) {
			serverConfused();
		} else if (command.equals("TIRED") && parameter != null) {
			serverTired();
		} else if (command.equals("PIZZA") && parameter != null) {
			serverPizza();
		} else if (command.equals("LOVE") && parameter != null) {
			serverLove();
		} else if (command.equals("BRB") && parameter != null) {
			serverBrb();
		} else if (command.equals("KISS") && parameter != null) {
			serverKiss();
		} else if (command.equals("WAITING") && parameter != null) {
			serverWaiting();
		} else if (command.equals("MUTE")) {
			serverMute();
		} else if (command.equals("UNMUTE")) {
			serverUnmute();
		} else if (command.equals("ATTACKWARNING") && parameter != null) {
			serverAttackWarning();
		} else if (command.equals("ATTACKSUCCEED") && parameter != null) {
			serverAttackSucceed();
		} else if (command.equals("DEATH") && parameter != null) {
			serverDeath();
		}
	}
	
	/**
	 * Replaces characters with HTML special characters
	 * 
	 * @param source The source text to escape
	 * @return       The escape HTML text
	 */
	public String escapeHTML(String source) {
		source = source.replaceAll("&", "&amp;");
		source = source.replaceAll("\"", "&quot;");
		source = source.replaceAll("<", "&lt;");
		source = source.replaceAll(">", "&gt;");
		return source;
	}
	
	/**
	 * Replaces chat tags and emoticons with HTML tags and images
	 * 
	 * @param source The source text to format
	 * @return       The replaced HTML text
	 */
	public String chatFormatting(String source) {
		source = escapeHTML(source);
		
		// Text styling
		if (source.matches(".*\\[b\\].*\\[/b\\].*")) {
			source = source.replaceAll("\\[b\\]", "<b>");
			source = source.replaceAll("\\[/b\\]", "</b>");
		}
		if (source.matches(".*\\[i\\].*\\[/i\\].*")) {
			source = source.replaceAll("\\[i\\]", "<i>");
			source = source.replaceAll("\\[/i\\]", "</i>");
		}
		if (source.matches(".*\\[u\\].*\\[/u\\].*")) {
			source = source.replaceAll("\\[u\\]", "<u>");
			source = source.replaceAll("\\[/u\\]", "</u>");
		}
		if (source.matches(".*\\[c=#[A-Za-z0-9]+\\].*\\[/c\\].*")) {
			source = source.replaceAll("\\[c=(#[A-Za-z0-9]+)\\]", "<font color='$1'>");
			source = source.replaceAll("\\[/c\\]", "</font>");
		}
		
		// Multiple lines
		if (source.contains("[br]")) {
			source = source.replaceAll("\\[br\\]", "<br>");
		}
		
		// Emoticons
		try {
			source = source.replaceAll(":\\)", "<img src='" + new File("resources/images/smile_emoticon.png").toURI().toURL().toString() + "'></img>");
			source = source.replaceAll(":\\(", "<img src='" + new File("resources/images/frown_emoticon.png").toURI().toURL().toString() + "'></img>");
			source = source.replaceAll(":O", "<img src='" + new File("resources/images/shocked_emoticon.png").toURI().toURL().toString() + "'></img>");
			source = source.replaceAll(":D", "<img src='" + new File("resources/images/happy_emoticon.png").toURI().toURL().toString() + "'></img>");
			source = source.replaceAll(":S", "<img src='" + new File("resources/images/confused_emoticon.png").toURI().toURL().toString() + "'></img>");
			source = source.replaceAll(":P", "<img src='" + new File("resources/images/tongue_emoticon.png").toURI().toURL().toString() + "'></img>");
			source = source.replaceAll("\\(Y\\)|\\(y\\)", "<img src='" + new File("resources/images/thumb_emoticon.png").toURI().toURL().toString() + "'></img>");
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		
		return source;
	}
	
	/** SERVER COMMANDS */
	
	/**
	 * Handles the server HELLO command
	 */
	private void serverHello() {
		model.getUsername().setValue(parameter);
		client.sendMessage("LOOK");
	}
	
	/**
	 * Handles the server GOAL command
	 */
	private void serverGoal() {
		model.getGoal().setValue(Integer.parseInt(parameter));
	}
	
	/**
	 * Handles the server WIN command
	 */
	private void serverWin() {
		model.addGameMessage("You won the game!", GameMessage.WIN);
		model.getInGame().setValue(false);
	}
	
	/**
	 * Handles the server LOSE command
	 */
	private void serverLose() {
		model.addGameMessage("You lost the game.", GameMessage.WARNING);
		JOptionPane.showMessageDialog(null, "You lost the game!", "Lose", JOptionPane.WARNING_MESSAGE);
		model.getInGame().setValue(false);
	}
	
	/**
	 * Handles the server CHANGE command
	 */
	private void serverChange() {
		client.sendMessage("LOOK");
	}
	
	/**
	 * Handles the server STARTTURN command
	 */
	private void serverStartTurn() {
		model.getTurn().setValue(true);
	}
	
	/**
	 * Handles the server ENDTURN command
	 */
	private void serverEndTurn() {
		model.addGameMessage("Your turn has ended.", GameMessage.WARNING);
		model.getTurn().setValue(false);
	}
	
	/**
	 * Handles the server HITMOD command
	 */
	private void serverHitMod() {
		int amount = Integer.parseInt(parameter);
		model.getHp().setValue(model.getHp().getValue() + amount);
		
		if (amount < 0) {
			model.addGameMessage("Your HP has decreased by " + Math.abs(amount) + "!", GameMessage.HPLOSS);
		} else {
			model.addGameMessage("Your HP has increased by " + amount + ".", GameMessage.HP);
		}
	}
	
	/**
	 * Handles the server TREASUREMOD command
	 */
	private void serverTreasureMod() {
		int amount = Integer.parseInt(parameter);
		model.getGold().setValue(model.getGold().getValue() + amount);
		
		if (amount < 0) {
			model.addGameMessage("Your Gold has decreased by " + Math.abs(amount) + "!", GameMessage.GOLD);
		} else {
			model.addGameMessage("Your Gold has increased by " + amount + "!", GameMessage.GOLD);
		}
	}
	
	/**
	 * Handles the server MESSAGE command
	 */
	private void serverMessage() {
		model.addGameMessage(parameter, GameMessage.NORMAL);
	}
	
	/**
	 * Handles the server FAIL command
	 */
	private void serverFail() {
		model.addGameMessage(parameter, GameMessage.ERROR);
	}
	
	/**
	 * Handles the server LOOKREPLY command
	 */
	private void serverLookReply() {
		// Set up new vision to default 'X' values
		char[][] lookReply = new char[7][7];
		for (int row = 0; row < 7; row++) {
			for (int col = 0; col < 7; col++) {
				lookReply[row][col] = 'X';
			}
		}
		
		try {
			String rowString = client.getInput().readLine();
			
			// If player has lantern, offSet the look vision to leave borders as unknown 'X'
			int offSet = 1;
			if (rowString.length() == 7) {
				offSet = 0;
			}
			
			ArrayList<String> rows = new ArrayList<String>();
			rows.add(rowString);
			for (int i = 1; i < rowString.length(); i++) {
				rowString = client.getInput().readLine();
				rows.add(rowString);
			}
			
			for (int row = 0; row < rows.size(); row++) {
				String currentRow = rows.get(row);
				
				for (int col = 0; col < currentRow.length(); col++) {
					lookReply[row + offSet][col + offSet] = currentRow.charAt(col);
				}
			}
			
			model.getMap().setValue(lookReply);
		} catch (IOException e) {
			return;
		}
	}
	
	/**
	 * Handles the server DUNGEON command
	 */
	private void serverDungeon() {
		model.getDungeonName().setValue(parameter);
	}
	
	/**
	 * Handles the server SWORDMOD command
	 */
	private void serverSwordMod() {
		model.getSword().setValue(true);
		model.addGameMessage("Picked up a sword.", GameMessage.SWORD);
	}
	
	/**
	 * Handles the server ARMOURMOD command
	 */
	private void serverArmourMod() {
		model.getArmour().setValue(true);
		model.addGameMessage("Picked up armour.", GameMessage.ARMOUR);
	}
	
	/**
	 * Handles the server LANTERNMOD command
	 */
	private void serverLanternMod() {
		model.getLantern().setValue(true);
		model.addGameMessage("Picked up a lantern.", GameMessage.LANTERN);
	}
	
	/**
	 * Handles the server SHOUT command
	 */
	private void serverShout() {
		String[] shoutParams = parameter.split(" ", 2);
		model.addGameMessage(shoutParams[0] + " shouted: " + shoutParams[1], GameMessage.SHOUT);
	}
	
	/**
	 * Handles the server PLAYERS command
	 */
	private void serverPlayers() {
		int count = Integer.parseInt(parameter);
		
		try {
			ArrayList<Player> players = new ArrayList<Player>();
			
			for (int i = 0; i < count; i++) {
				String playerLine = client.getInput().readLine();
				String[] playerParameters = playerLine.split(" ", 3);
				
				String username = playerParameters[0];
				int status = Integer.parseInt(playerParameters[1]);
				boolean chat = false;
				if (playerParameters[2].equals("1")) {
					chat = true;
				}
				
				players.add(new Player(status, username, chat));
			}
			
			model.getPlayers().clear();
			model.getPlayers().addAll(players);
		} catch(IOException e) {
			return;
		}
	}
	
	/**
	 * Handles the server STARTTYPING command
	 */
	private void serverStartTyping() {
		model.getPlayer(parameter).setTyping(true);
	}
	
	/**
	 * Handles the server ENDTYPING command
	 */
	private void serverEndTyping() {
		model.getPlayer(parameter).setTyping(false);
	}
	
	/**
	 * Handles the server CHAT command
	 */
	private void serverChat() {
		String[] chatParameters = parameter.split(" ", 2);
		String username = chatParameters[0];
		String chatMessage = chatParameters[1];
		chatMessage = chatFormatting(chatMessage);
		
		model.addGameMessage("<b>" + username + "</b>: " + chatMessage, GameMessage.CHAT);
	}
	
	/**
	 * Handles the server WHISPERTO command
	 */
	private void serverWhisperTo() {
		String[] whisperParameters = parameter.split(" ", 2);
		String username = whisperParameters[0];
		String whisperMessage = whisperParameters[1];
		whisperMessage = chatFormatting(whisperMessage);
		
		model.addGameMessage("(To <b>" + username + "</b>): " + whisperMessage, GameMessage.WHISPER);
	}
	
	/**
	 * Handles the server WHISPERFROM command
	 */
	private void serverWhisperFrom() {
		String[] whisperParameters = parameter.split(" ", 2);
		String username = whisperParameters[0];
		String whisperMessage = whisperParameters[1];
		whisperMessage = chatFormatting(whisperMessage);
		
		model.addGameMessage("(From <b>" + username + "</b>): " + whisperMessage, GameMessage.WHISPER);
	}
	
	/**
	 * Handles the server HUG command
	 */
	private void serverHug() {
		String[] hugParameters = parameter.split(" ", 2);
		String sender = escapeHTML(hugParameters[0]);
		String receiver = escapeHTML(hugParameters[1]);
		if (sender.equals(receiver)) {
			model.addGameMessage("<b>" + sender + "</b> gave themself a big hug.", GameMessage.HUG);
		} else {
			model.addGameMessage("<b>" + sender + "</b> gave <b>" + receiver + "</b> a big hug.", GameMessage.HUG);
		}
	}
	
	/**
	 * Handles the server AGREE command
	 */
	private void serverAgree() {
		String[] agreeParameters = parameter.split(" ", 2);
		String sender = escapeHTML(agreeParameters[0]);
		String receiver = escapeHTML(agreeParameters[1]);
		if (sender.equals(receiver)) {
			model.addGameMessage("<b>" + sender + "</b> agrees with themself.", GameMessage.AGREE);
		} else {
			model.addGameMessage("<b>" + sender + "</b> agrees with <b>" + receiver + "</b>.", GameMessage.AGREE);
		}
	}
	
	/**
	 * Handles the server DISAGREE command
	 */
	private void serverDisagree() {
		String[] disagreeParameters = parameter.split(" ", 2);
		String sender = escapeHTML(disagreeParameters[0]);
		String receiver = escapeHTML(disagreeParameters[1]);
		if (sender.equals(receiver)) {
			model.addGameMessage("<b>" + sender + "</b> disagrees with themself.", GameMessage.DISAGREE);
		} else {
			model.addGameMessage("<b>" + sender + "</b> disagrees with <b>" + receiver + "</b>.", GameMessage.DISAGREE);
		}
	}
	
	/**
	 * Handles the server LOL command
	 */
	private void serverLol() {
		model.addGameMessage("<b>" + escapeHTML(parameter) + "</b> laughed out loud.", GameMessage.LOL);
	}
	
	/**
	 * Handles the server COFFEE command
	 */
	private void serverCoffee() {
		model.addGameMessage("<b>" + escapeHTML(parameter) + "</b> drank some coffee.", GameMessage.COFFEE);
	}
	
	/**
	 * Handles the server BEER command
	 */
	private void serverBeer() {
		model.addGameMessage("<b>" + escapeHTML(parameter) + "</b> drank some beer.", GameMessage.BEER);
	}
	
	/**
	 * Handles the server GIFT command
	 */
	private void serverGift() {
		String[] giftParameters = parameter.split(" ", 2);
		String sender = escapeHTML(giftParameters[0]);
		String receiver = escapeHTML(giftParameters[1]);
		if (sender.equals(receiver)) {
			model.addGameMessage("<b>" + sender + "</b> sent themself a gift.", GameMessage.GIFT);
		} else {
			model.addGameMessage("<b>" + sender + "</b> sent <b>" + receiver + "</b> a gift.", GameMessage.GIFT);
		}
	}
	
	/**
	 * Handles the server ANGRY command
	 */
	private void serverAngry() {
		model.addGameMessage("<b>" + escapeHTML(parameter) + "</b> is getting angry.", GameMessage.ANGRY);
	}
	
	/**
	 * Handles the server CONFUSED command
	 */
	private void serverConfused() {
		model.addGameMessage("<b>" + escapeHTML(parameter) + "</b> is confused.", GameMessage.CONFUSED);
	}
	
	/**
	 * Handles the server TIRED command
	 */
	private void serverTired() {
		model.addGameMessage("<b>" + escapeHTML(parameter) + "</b> is getting tired.", GameMessage.TIRED);
	}
	
	/**
	 * Handles the server PIZZA command
	 */
	private void serverPizza() {
		model.addGameMessage("<b>" + escapeHTML(parameter) + "</b> ate a slice of pizza.", GameMessage.PIZZA);
	}
	
	/**
	 * Handles the server LOVE command
	 */
	private void serverLove() {
		String[] loveParameters = parameter.split(" ", 2);
		String sender = escapeHTML(loveParameters[0]);
		String receiver = escapeHTML(loveParameters[1]);
		if (sender.equals(receiver)) {
			model.addGameMessage("<b>" + sender + "</b> loves themself very much.", GameMessage.LOVE);
		} else {
			model.addGameMessage("<b>" + sender + "</b> loves <b>" + receiver + "</b> very much.", GameMessage.LOVE);
		}
	}
	
	/**
	 * Handles the server BRB command
	 */
	private void serverBrb() {
		model.addGameMessage("<b>" + escapeHTML(parameter) + "</b> will be right back.", GameMessage.BRB);
	}
	
	/**
	 * Handles the server KISS command
	 */
	private void serverKiss() {
		String[] kissParameters = parameter.split(" ", 2);
		String sender = escapeHTML(kissParameters[0]);
		String receiver = escapeHTML(kissParameters[1]);
		if (sender.equals(receiver)) {
			model.addGameMessage("<b>" + sender + "</b> kissed themself passionately.", GameMessage.KISS);
		} else {
			model.addGameMessage("<b>" + sender + "</b> kissed <b>" + receiver + "</b> passionately.", GameMessage.KISS);
		}
	}
	
	/**
	 * Handles the server WAITING command
	 */
	private void serverWaiting() {
		model.addGameMessage("<b>" + escapeHTML(parameter) + "</b> is waiting for the next turn.", GameMessage.WAITING);
	}
	
	/**
	 * Handles the server MUTE command
	 */
	private void serverMute() {
		model.addGameMessage("You have been muted and cannot chat.", GameMessage.WARNING);
	}
	
	/**
	 * Handles the server UNMUTE command
	 */
	private void serverUnmute() {
		model.addGameMessage("You are now unmuted.", GameMessage.NORMAL);
	}
	
	/**
	 * Handles the server ATTACKWARNING command
	 */
	private void serverAttackWarning() {
		model.addGameMessage(escapeHTML(parameter), GameMessage.WARNING);
	}
	
	/**
	 * Handles the server ATTACKSUCCEED command
	 */
	private void serverAttackSucceed() {
		model.addGameMessage("Attack against " + "<b>" + escapeHTML(parameter) + "</b> was successful.", GameMessage.SUCCEED);
	}
	
	/**
	 * Handles the server DEATH command
	 */
	private void serverDeath() {
		model.addGameMessage("<b>" + escapeHTML(parameter) + "</b> has died.", GameMessage.DEATH);
		model.getPlayer(parameter).setStatus(Player.DEAD);
	}
}
