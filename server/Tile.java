/**
 * The Tile class represents a square tile in the dungeon
 * 
 * @author Sean Stinson, ss938
 *
 */
public class Tile {
	// Tile characters
	public static final char FLOOR   = '.';
	public static final char WALL    = '#';
	public static final char EXIT    = 'E';
	
	// Item characters
	public static final char GOLD    = 'G';
	public static final char SWORD   = 'S';
	public static final char ARMOUR  = 'A';
	public static final char LANTERN = 'L';
	public static final char HEALTH  = 'H';
	
	private char base;
	private int gold;
	
	private Player player;
	
	/**
	 * Constructor initialises the base tile and item fields
	 * 
	 * @param base The base tile content
	 */
	public Tile(char base) {
		this.base = base;
		
		if (base == 'G') {
			gold = 1;
		} else {
			gold = 0;
		}
		
		player = null;
	}

	/**
	 * Gets the base tile content
	 * 
	 * @return The base tile content
	 */
	public char getBase() {
		return base;
	}

	/**
	 * Sets the base tile content
	 * 
	 * @param base The new base tile content
	 */
	public void setBase(char base) {
		this.base = base;
	}
	
	/**
	 * Gets the amount of gold on the tile
	 * 
	 * @return The amount of gold on the tile
	 */
	public int getGold() {
		return gold;
	}
	
	/**
	 * Sets the amount of gold on the tile
	 * 
	 * @param gold The new amount of gold on the tile
	 */
	public void setGold(int gold) {
		if (gold > 0) {
			base = 'G';
		}
		this.gold = gold;
	}
	
	/**
	 * Adds gold to the tile
	 * 
	 * @param amount The amount of gold to add to the tile
	 */
	public void addGold(int amount) {
		gold += amount;
		if (gold > 0) {
			base = 'G';
		}
	}
	
	/**
	 * Gets the amount of gold on the tile then returns and removes all of the gold
	 * 
	 * @return The amount of gold on the tile
	 */
	public int popGold() {
		int amount = gold;
		gold = 0;
		base = FLOOR;
		return amount;
	}
	
	/**
	 * Occupies the tile with a player
	 * 
	 * @param player The player to occupy the tile
	 */
	public void occupy(Player player) {
		this.player = player;
	}
	
	/**
	 * Removes the player from the tile
	 */
	public void unOccupy() {
		player = null;
	}
	
	/**
	 * Gets the player currently occupying the tile
	 * 
	 * @return The player currently occupying the tile, <code>null</code> if tile has no player
	 */
	public Player getPlayer() {
		return player;
	}
	
	/**
	 * Checks if the tile is occupied by a player
	 * 
	 * @return <code>true</code> if the tile is occupied by a player, <code>false</code> otherwise
	 */
	public boolean isOccupied() {
		if (player == null) {
			return false;
		}
		return true;
	}
	
	/**
	 * Checks if the tile can be walked on by a player
	 * 
	 * @return <code>true</code> if the tile is walkable, <code>false</code> otherwise
	 */
	public boolean isWalkable() {
		if (base == '#' || isOccupied()) {
			return false;
		}
		return true;
	}
}