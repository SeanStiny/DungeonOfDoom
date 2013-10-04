import java.io.IOException;

/**
 * The Player class represents a user currently playing the game
 * 
 * @author Sean Stinson, ss938
 *
 */
public class Player {
	private Connection client;
	private Game game;
	
	private boolean dead;
	private boolean online;
	
	// Position
	private int x;
	private int y;
	
	// Items
	private int lantern;
	private int sword;
	private int armour;
	
	// Attribute values
	private int gold;
	private int hp;
	private int ap;
	
	/**
	 * Constructor sets up fields
	 * 
	 * @param client The client associated with this player
	 */
	public Player(Connection client, Game game) {
		this.client = client;
		client.setPlayer(this);
		
		this.game = game;
		
		x = -1;
		y = -1;
		
		lantern = 0;
		sword = 0;
		armour = 0;
		
		gold = 0;
		hp = 3;
		ap = 0;
	}
	
	public void reset() {
		setLantern(0);
		setSword(0);
		setArmour(0);
		
		setGold(0);
		setHp(3);
		setAp(0);
	}
	
	/**
	 * Gets the client connection for this player
	 * 
	 * @return The client connection
	 */
	public Connection getClient() {
		return client;
	}
	
	/**
	 * Gets the game the player belongs to
	 * 
	 * @return The game the player belongs to
	 */
	public Game getGame() {
		return game;
	}

	/**
	 * Checks whether the player is dead or not
	 * 
	 * @return <code>true</code> if the player is dead, <code>false</code> otherwise
	 */
	public boolean isDead() {
		return dead;
	}

	/**
	 * Sets whether the player is dead or not
	 * 
	 * @param dead Whether the player is now dead or not
	 */
	public void setDead(boolean dead) {
		this.dead = dead;
	}

	/**
	 * Gets the X position of the player on the map
	 * 
	 * @return The X position of the player
	 */
	public int getX() {
		return x;
	}

	/**
	 * Sets the X position of the player on the map
	 * 
	 * @param x The new X position of the player
	 */
	public void setX(int x) {
		this.x = x;
	}

	/**
	 * Gets the Y position of the player on the map
	 * 
	 * @return The Y position of the player
	 */
	public int getY() {
		return y;
	}

	/**
	 * Sets the Y position of the player on the map
	 * 
	 * @param y The new Y position of the player
	 */
	public void setY(int y) {
		this.y = y;
	}

	/**
	 * Gets the player lantern value
	 * 
	 * @return <code>1</code> if the player has a lantern, <code>0</code> otherwise
	 */
	public int getLantern() {
		return lantern;
	}

	/**
	 * Sets the player lantern value
	 * 
	 * @param lantern The new lantern value
	 */
	public void setLantern(int lantern) {
		this.lantern = lantern;
		
		if (lantern == 1) {
			client.sendMessage("LANTERNMOD");
		}
	}

	/**
	 * Gets the player sword value
	 * 
	 * @return <code>1</code> if the player has a sword, <code>0</code> otherwise
	 */
	public int getSword() {
		return sword;
	}

	/**
	 * Sets the player sword value
	 * 
	 * @param sword The new sword value
	 */
	public void setSword(int sword) {
		this.sword = sword;
		
		if (sword == 1) {
			client.sendMessage("SWORDMOD");
		}
	}

	/**
	 * Gets the player armour value
	 * 
	 * @return <code>1</code> if the player has armour, <code>0</code> otherwise
	 */
	public int getArmour() {
		return armour;
	}

	/**
	 * Sets the player armour value
	 * 
	 * @param armour The new armour value
	 */
	public void setArmour(int armour) {
		this.armour = armour;
		
		if (armour == 1) {
			client.sendMessage("ARMOURMOD");
		}
	}

	/**
	 * Gets the amount of gold the player has
	 * 
	 * @return The amount of gold the player has
	 */
	public int getGold() {
		return gold;
	}

	/**
	 * Sets the amount of gold the player has
	 * 
	 * @param gold The new amount of gold
	 */
	public void setGold(int gold) {
		int difference = gold - this.gold;
		this.gold = gold;
		client.sendMessage("TREASUREMOD " + difference);
	}
	
	/**
	 * Adds gold to the player's total
	 * 
	 * @param amount The amount of gold to add. This can be negative
	 */
	public void addGold(int amount) {
		setGold(gold + amount);
	}

	/**
	 * Gets the amount of hit points the player has
	 * 
	 * @return The amount of hit points the player has
	 */
	public int getHp() {
		return hp;
	}

	/**
	 * Sets the amount of hit points the player has
	 * 
	 * @param hp The new amount of hit points
	 */
	public void setHp(int hp) {
		// Prevent negative hp
		if (hp < 0) {
			hp = 0;
		}
		
		// Calculate difference in hp
		int difference = hp - this.hp;
		this.hp = hp;
		client.sendMessage("HITMOD " + difference);
		
		// Check if the player has died
		if (hp == 0) {
			die();
		}
	}

	/**
	 * Gets the amount of action points the player has
	 * 
	 * @return The amount of action points the player has
	 */
	public int getAp() {
		return ap;
	}

	/**
	 * Sets the amount of action points the player has
	 * 
	 * @param ap The amount of action points the player has
	 */
	public void setAp(int ap) {
		this.ap = ap;
	}
	
	/**
	 * Checks if the player is currently online
	 * 
	 * @return <code>true</code> if the player is online, <code>false</code> otherwise
	 */
	public boolean isOnline() {
		return online;
	}

	/**
	 * Sets whether the player is currently online or not
	 * 
	 * @param online Whether the player is currently online or not
	 */
	public void setOnline(boolean online) {
		this.online = online;
	}

	/**
	 * Checks if it is the player's turn
	 * 
	 * @return <code>true</code> if it is the player's turn, <code>false</code> otherwise
	 */
	public boolean isTurn() {
		return (game.getPlayer() == this);
	}
	
	/**
	 * Removes the player from the game it's currently part of
	 */
	public void leaveGame() {
		game.leave(this);
	}
	
	/**
	 * Kills the player
	 */
	public void die() {
		dead = true;
		if (isTurn()) {
			game.newTurn();
		}
		game.sendToAll("DEATH " + client.getUsername());
		client.sendMessage("LOSE");
		
		// Update the map to remove the dead player and drop their gold
		Tile deathTile = game.getTile(x, y);
		deathTile.unOccupy();
		
		if (gold > 0) {
			deathTile.setBase('G');
			deathTile.addGold(gold);
		}
		
		game.updateChange(x, y);
	}
	
	/**
	 * Gets the farthest distance the player can see
	 * 
	 * @return The farthest distance the player can see
	 */
	public int getVisionDistance() {
		return 2 + lantern;
	}
	
	/**
	 * Checks if the player can see a location on the map
	 * 
	 * @param xLocation The X position of the location
	 * @param yLocation The Y position of the location
	 * @return          <code>true</code> if the player can see the location, <code>false</code> otherwise
	 */
	public boolean canSeeLocation(int xLocation, int yLocation) {
		int xDistance = Math.abs(x - xLocation);
		int yDistance = Math.abs(y - yLocation);
		int range = getVisionDistance();
		int maxDistance = range + 1;
		
		if (xDistance <= range && yDistance <= range && xDistance + yDistance <= maxDistance) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Gets the Tile the player is stood on
	 * 
	 * @return The Tile the player is stood on
	 */
	public Tile getCurrentTile() {
		return game.getTile(x, y);
	}
	
	public void kickOut() {
		try {
			client.close();
		} catch (IOException e) {
			// Just let the thread die quietly
		}
	}
	
	@Override
	public String toString() {
		return client.getUsername();
	}
}
