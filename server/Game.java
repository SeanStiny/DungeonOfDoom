import java.util.Observable;
import java.util.Observer;
import java.util.Random;
import java.util.Vector;

/**
 * The Game class represents a game of Dungeon of Dooom.
 * 
 * @author Sean Stinson, ss938
 *
 */
public class Game extends Observable {
	private Vector<Player> players;
	private int currentPlayer;
	private Player player;
	
	private boolean finished;
	
	private Map map;
	
	/**
	 * Constructor initialises fields and uses default map
	 */
	public Game() {
		players = new Vector<Player>();
		currentPlayer = -1;
		finished = false;
		map = new Map();
	}
	
	/**
	 * Constructor initialises fields and uses a given map file
	 * 
	 * @param mapFile The map file to load
	 */
	public Game(String mapFile) {
		players = new Vector<Player>();
		currentPlayer = -1;
		finished = false;
		map = new Map(mapFile);
	}
	
	/**
	 * Gets the players that are in the game
	 * 
	 * @return The players that are in the game
	 */
	public Vector<Player> getPlayers() {
		return players;
	}
	
	/**
	 * Gets whether the game has finished or not
	 * 
	 * @return <code>true</code> if the game has finished, <code>false</code> otherwise
	 */
	public boolean isFinished() {
		return finished;
	}

	/**
	 * Sets whether the game has finished or not
	 * 
	 * @param finished <code>true</code> if the game has finished, <code>false</code> otherwise
	 */
	public void setFinished(boolean finished) {
		this.finished = finished;
	}

	/**
	 * Gets the player whose turn it is
	 * 
	 * @return The player whose turn it is
	 */
	public Player getPlayer() {
		return player;
	}
	
	/**
	 * Gets a player from a given user name
	 * 
	 * @param username The user name to search for
	 * @return         The player that is using the given user name
	 */
	public Player getPlayer(String username) {
		for (Player p : players) {
			if (p.getClient().getUsername().equalsIgnoreCase(username)) {
				return p;
			}
		}
		return null;
	}

	/**
	 * Sets the player whose turn it is
	 * 
	 * @param player The new player whose turn it is
	 */
	public void setPlayer(Player player) {
		this.player = player;
	}
	
	/**
	 * Gets the game map object
	 * 
	 * @return The game map object
	 */
	public Map getMap() {
		return map;
	}
	
	/**
	 * Gets the tile at a given position
	 * 
	 * @param tileX The X position of the tile
	 * @param tileY The Y position of the tile
	 * @return      The Tile at the position
	 */
	public Tile getTile(int tileX, int tileY) {
		return map.getTiles()[tileY][tileX];
	}
	
	/**
	 * Starts a new game with the specified map
	 * 
	 * @param mapFile
	 */
	public void startNewGame(String mapFile) {
		map = new Map(mapFile);
		
		// Relocate players
		for (Player p : players) {
			p.reset();
			
			randomLocation(p);
		}
		
		finished = false;
		updateView();
	}
	
	/**
	 * Joins a new client to the game, creating a Player for them to control
	 * 
	 * @param client The client that joined the game
	 */
	public void join(Connection client) {
		Player newPlayer = new Player(client, this);
		randomLocation(newPlayer);
		
		newPlayer.getClient().sendMessage("GOAL " + map.getGoal());
		newPlayer.getClient().sendMessage("DUNGEON " + map.getName());
		
		synchronized (players) {
			players.add(newPlayer);
			sendToAll("MESSAGE " + newPlayer.getClient().getUsername() + " joined the game.");
			
			if (players.size() == 1 && !finished) {
				newTurn();
			}
		}
		updateClientPlayers();
	}
	
	/**
	 * Removes a player from the game
	 * 
	 * @param removedPlayer The player to remove
	 */
	public void leave(Player removedPlayer) {
		players.remove(removedPlayer);
		removedPlayer.getCurrentTile().unOccupy();
		updateChange(removedPlayer.getX(), removedPlayer.getY());
		
		if (removedPlayer.isTurn()) {
			player = null;
			currentPlayer--;
			newTurn();
		}
		
		// Update the map to remove the dead player and drop their gold
		Tile leftTile = getTile(removedPlayer.getX(), removedPlayer.getY());
		leftTile.unOccupy();
		
		if (removedPlayer.getGold() > 0) {
			leftTile.setBase('G');
			leftTile.addGold(removedPlayer.getGold());
		}
		
		sendToAll("MESSAGE " + removedPlayer.getClient().getUsername() + " left the game.");
		updateClientPlayers();
	}
	
	/**
	 * Notifies all clients in the game if there is a change in the player list
	 */
	public void updateClientPlayers() {
		synchronized (players) {
			for (Player p : players) {
				String playerMessage = "PLAYERS " + players.size();
				
				for (Player p2 : players) {
					int chat = 1;
					int dead = 0;
					
					if (p2 == p) {
						chat = 0;
					}
					if (p2.isDead()) {
						dead = 1;
					}
					
					playerMessage += "\n" + p2.getClient().getUsername() + " " + dead + " " + chat;
				}
				
				p.getClient().sendMessage(playerMessage);
			}
		}
		
		updateView();
	}
	
	/**
	 * Generates a random position for a player. A new position is generated until a
	 * free tile is found.
	 * 
	 * @param locatedPlayer The player to locate to a random position
	 */
	public void randomLocation(Player locatedPlayer) {
		Random rand = new Random();
		int newX = locatedPlayer.getX();
		int newY = locatedPlayer.getY();
		
		synchronized (map) {
			// Continue to generate a random location until a free tile is found
			while (true) {
				if (newX < 0 || newY < 0 || !getTile(newX, newY).isWalkable()) {
					newX = rand.nextInt(map.getWidth());
					newY = rand.nextInt(map.getHeight());
				} else {
					break;
				}
			}
			
			getTile(newX, newY).occupy(locatedPlayer);
		}
		
		// Update the player's position
		locatedPlayer.setX(newX);
		locatedPlayer.setY(newY);
		
		// Notify clients of the newly positioned player on the map
		updateChange(newX, newY);
	}
	
	/**
	 * Starts a new turn
	 */
	public void newTurn() {
		if (player != null) {
			player.getClient().sendMessage("ENDTURN");
		}
		
		changePlayer();
		
		if (player != null) {
			sendToAll("MESSAGE It is now " + player.getClient().getUsername() + "'s turn.");
			player.setAp(6 - player.getSword() - player.getArmour() - player.getLantern());
			player.getClient().sendMessage("STARTTURN");
		}
	}
	
	/**
	 * Advances the current turn forward
	 */
	public void advanceTurn() {
		// Check if player has won
		if (player.getCurrentTile().getBase() == 'E' && player.getGold() >= map.getGoal()) {
			winGame();
		} else {
			// Check if turn is ended
			if (player.getAp() == 0) {
				newTurn();
			}
		}
	}
	
	/**
	 * Sends the appropriate win or lose message to the players and ends the game
	 */
	public void winGame() {
		sendToAll("MESSAGE " + player.getClient().getUsername() + " won the game.");
		player.getClient().sendMessage("WIN");
		
		for (Player p : players) {
			if (p != player && !p.isDead()) {
				p.getClient().sendMessage("LOSE");
			}
		}

		endGame();
	}
	
	/**
	 * Ends the game
	 */
	public void endGame() {
		finished = true;
	}
	
	/**
	 * Changes which player is having their turn
	 */
	public void changePlayer() {
		// Check if at least one player is alive
		boolean playerAlive = false;
		
		for (Player p : players) {
			if (!p.isDead()) {
				playerAlive = true;
				break;
			}
		}
		if (!playerAlive) {
			player = null;
			return;
		}
		
		while (true) {
			currentPlayer++;
			
			if (currentPlayer >= players.size()) {
				currentPlayer = 0;
			}
			
			// Skip over dead players
			if (!players.get(currentPlayer).isDead()) {
				player = players.get(currentPlayer);
				break;
			}
		}
	}
	
	/**
	 * Sends a message to all clients that belong to the game, giving the option
	 * to skip the player that is currently having their turn
	 * 
	 * @param message     The message to send to the clients
	 * @param skipCurrent <code>true</code> to skip the player whose turn it is, <code>false</code> to include them
	 */
	public void sendToAll(String message, boolean skipCurrent) {
		for (Player p : players) {
			if (skipCurrent && p == player) {
				continue;
			}
			p.getClient().sendMessage(message);
		}
	}
	
	/**
	 * Sends a message to all clients that belong to the game
	 * 
	 * @param message
	 */
	public void sendToAll(String message) {
		sendToAll(message, false);
	}
	
	/**
	 * Notifies all of the clients that can see one of two changes on the map
	 * 
	 * @param changeX1 The first change X position
	 * @param changeY1 The first change Y position
	 * @param changeX2 The second change X position. <code>-1</code> if there is only one change.
	 * @param changeY2 The second change Y position. <code>-1</code> if there is only one change.
	 */
	public void updateChange(int changeX1, int changeY1, int changeX2, int changeY2) {
		for (Player p : players) {
			if (p.canSeeLocation(changeX1, changeY1) || (changeX2 >= 0 && changeY2 >= 0 && p.canSeeLocation(changeX2, changeY2))) {
				p.getClient().sendMessage("CHANGE");
			}
		}
		
		updateView();
	}
	
	/**
	 * Notifies all of the clients that can see a change on the map
	 * 
	 * @param changeX The X position of the change
	 * @param changeY The Y position of the change
	 */
	public void updateChange(int changeX, int changeY) {
		updateChange(changeX, changeY, -1, -1);
	}
	
	/**
	 * Notifies the GUI that the game model has changed
	 */
	public void updateView() {
		setChanged();
		notifyObservers();
	}
	
	/**
	 * Adds an observer to watch the game and updates the observer
	 */
	@Override
	public void addObserver(Observer observer) {
		super.addObserver(observer);
		updateView();
	}
	
	
	/** PLAYER ACTIONS **/
	
	
	/**
	 * Moves a player in a given direction on the map
	 * 
	 * @param direction The direction to move. Must be <code>N</code>, <code>S</code>, <code>E</code> or <code>W</code>
	 */
	public void playerMove(char direction) {
		int oldX = player.getX();
		int oldY = player.getY();
		int newX = oldX;
		int newY = oldY;
		
		// Calculate new position
		switch (direction) {
			case 'N': newY--; break;
			case 'S': newY++; break;
			case 'W': newX--; break;
			case 'E': newX++; break;
			default: System.out.println("Invalid direction given. Please validate the direction before calling playerMove()."); return;
		}
		
		synchronized (map) {
			// Check if the player can even walk onto the new tile
			if (newX >= 0 && newY >= 0 && newX < map.getWidth() && newY < map.getHeight() && getTile(newX, newY).isWalkable()) {
				// Update tile occupation
				getTile(newX, newY).occupy(player);
				getTile(oldX, oldY).unOccupy();
				
				// Update player position
				player.setX(newX);
				player.setY(newY);
				
				// Update action points
				player.setAp(player.getAp() - 1);
				
				// Notify clients of change
				updateChange(newX, newY, oldX, oldY);
				
				advanceTurn();
			} else {
				if (newX >= 0 && newY >= 0 && newX < map.getWidth() && newY < map.getHeight() && getTile(newX, newY).isOccupied()) {
					player.getClient().sendMessage("FAIL Player in the way");
				} else {
					player.getClient().sendMessage("FAIL Wall in the way");
				}
			}
		}
	}
	
	/**
	 * Sends the area of the map that the player can see to the client
	 * 
	 * @param lookPlayer The player to send the map to
	 */
	public void playerLook(Player lookPlayer) {
		String lookReply = "LOOKREPLY";
		
		int maxDistance = lookPlayer.getVisionDistance();
		int centerX = lookPlayer.getX();
		int centerY = lookPlayer.getY();
		
		// Iterate through rows
		for (int row = -maxDistance; row <= maxDistance; row++) {
			lookReply += "\n";
			
			// Iterate through columns
			for (int col = -maxDistance; col <= maxDistance; col++) {
				int xPos = centerX + col;
				int yPos = centerY + row;
				int distance = Math.abs(row) + Math.abs(col);
				
				// Assume tiles outside of the map to be walls
				if (distance > maxDistance + 1) {
					lookReply += "X";
					continue;
				} else if (xPos < 0 || yPos < 0 || xPos >= map.getWidth() || yPos >= map.getHeight()) {
					lookReply += "#";
					continue;
				}
				
				Tile currentTile = getTile(xPos, yPos);
				
				if (currentTile != lookPlayer.getCurrentTile() && currentTile.isOccupied()) {
					lookReply += "P";
				} else {
					lookReply += currentTile.getBase();
				}
			}
		}
		
		lookPlayer.getClient().sendMessage(lookReply);
	}
	
	/**
	 * Picks up an item from the tile below the current player and gives it to them
	 */
	public void playerPickUp() {
		Tile tile = player.getCurrentTile();
		char contents = tile.getBase();
		
		switch (contents) {
			case Tile.EXIT:
			case Tile.FLOOR:
				player.getClient().sendMessage("FAIL Nothing to pick up.");
				break;
				
			case Tile.GOLD:
				// Remove the gold from the tile and give to the player
				int amount = tile.popGold();
				player.addGold(amount);
				
				// Notify clients of change
				updateChange(player.getX(), player.getY());
				player.getClient().sendMessage("SUCCEED Successfully picked up " + amount + " gold.");
				
				// Move turn along
				player.setAp(player.getAp() - 1);
				advanceTurn();
				break;
				
			case Tile.ARMOUR:
				if (player.getArmour() == 0) {
					// Give the armour to the player
					player.setArmour(1);
					
					// Remove the armour from the tile
					tile.setBase(Tile.FLOOR);
					
					// Notify clients of the change
					updateChange(player.getX(), player.getY());
					player.getClient().sendMessage("SUCCEED Successfully picked up armour.");
					
					// Move turn along
					player.setAp(player.getAp() - 1);
					advanceTurn();
				} else {
					player.getClient().sendMessage("FAIL You already have armour.");
				}
				break;
				
			case Tile.HEALTH:
				// Give the health to the player
				player.setHp(player.getHp() + 1);
				
				// Remove the health from the tile
				tile.setBase(Tile.FLOOR);
				
				// Notify clients of change
				updateChange(player.getX(), player.getY());
				player.getClient().sendMessage("SUCCEED Successfully picked up and drank health potion.");
				
				// Move turn along
				player.setAp(0);
				advanceTurn();
				break;
				
			case Tile.LANTERN:
				if (player.getLantern() == 0) {
					// Give the lantern to the player
					player.setLantern(1);
					
					// Remove the lantern from the tile
					tile.setBase(Tile.FLOOR);
					
					// Notify clients of change
					updateChange(player.getX(), player.getY());
					player.getClient().sendMessage("SUCCEED Successfully picked up lantern");
					
					// Move turn along
					player.setAp(player.getAp() - 1);
					advanceTurn();
				} else {
					player.getClient().sendMessage("FAIL You already have a lantern.");
				}
				break;
				
			case Tile.SWORD:
				if (player.getSword() == 0) {
					// Give the sword to the player
					player.setSword(1);
					
					// Remove the sword from the tile
					tile.setBase(Tile.FLOOR);
					
					// Notify clients of the change
					updateChange(player.getX(), player.getY());
					player.getClient().sendMessage("SUCCEED Successfully picked up sword");
					
					// Move turn along
					player.setAp(player.getAp() - 1);
					advanceTurn();
				} else {
					player.getClient().sendMessage("FAIL you already have a sword.");
				}
				break;
		}
	}
	
	/**
	 * Sends a shouted message to all players within range of the player that sent the shouted
	 * 
	 * @param shoutPlayer The player that sent the shout
	 * @param message     The message sent in the shout
	 */
	public void playerShout(Player shoutPlayer, String message) {
		for (Player p : players) {
			if (p.canSeeLocation(shoutPlayer.getX(), shoutPlayer.getY())) {
				p.getClient().sendMessage("SHOUT " + shoutPlayer.getClient().getUsername() + " " + message);
			}
		}
	}
	
	/**
	 * Ends the current players turn
	 */
	public void playerEndTurn() {
		player.setAp(0);
		advanceTurn();
	}
	
	/**
	 * Attacks a player with a 75% chance of succeeding
	 * 
	 * @param direction The direction to attack in
	 */
	public void playerAttack(char direction) {
		int targetX = player.getX();
		int targetY = player.getY();
		
		// Calculate new position
		switch (direction) {
			case 'N': targetY--; break;
			case 'S': targetY++; break;
			case 'W': targetX--; break;
			case 'E': targetX++; break;
			default: System.out.println("Invalid direction given. Please validate the direction before calling playerMove()."); return;
		}
		
		// Check if there is a player stood in the target direction
		Tile targetTile = getTile(targetX, targetY);
		if (targetTile.isOccupied()) {
			Player targetPlayer = targetTile.getPlayer();
			
			// 75% chance of harming the other player
			Random rand = new Random();
			int randomInt = rand.nextInt(4);
			
			// If the player hits the target
			if (randomInt < 3) {
				int damage = 1 + player.getSword() - targetPlayer.getArmour();
				
				if (damage == 0) {
					player.getClient().sendMessage("ATTACKWARNING " + targetPlayer.getClient().getUsername() + "'s armour prevented any damage.");
					targetPlayer.getClient().sendMessage("ATTACKWARNING Your armour protected you from " + player.getClient().getUsername() + "'s attack.");
				} else {
					player.getClient().sendMessage("ATTACKSUCCEED " + targetPlayer.getClient().getUsername());
					targetPlayer.getClient().sendMessage("ATTACKWARNING You have been attacked by " + player.getClient().getUsername() + "!");
					targetPlayer.setHp(targetPlayer.getHp() - damage);
				}
			} else {
				targetPlayer.getClient().sendMessage("ATTACKWARNING " + player.getClient().getUsername() + " attempted to attack but failed.");
				player.getClient().sendMessage("ATTACKWARNING Your attack missed the target.");
			}
			
			player.setAp(0);
			advanceTurn();
		} else {
			player.getClient().sendMessage("FAIL No player to attack.");
		}
	}
}
