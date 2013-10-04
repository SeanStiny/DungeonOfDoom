/**
 * The PlayModel class models the current game values, allowing an Observer to watch
 * them for changes.
 * 
 * @author Sean Stinson, ss938
 *
 */
public class PlayModel {
	private ObservableValue<String> dungeonName;
	private ObservableValue<Integer> goal;
	private ObservableValue<Boolean> turn;
	private ObservableValue<Boolean> inGame;
	private ObservableValue<String> username;
	
	private ObservableValue<char[][]> map;
	
	private ObservableValue<Integer> gold;
	private ObservableValue<Integer> hp;
	
	private ObservableValue<Boolean> sword;
	private ObservableValue<Boolean> armour;
	private ObservableValue<Boolean> lantern;
	
	private ObservableValue<GameMessage> gameMessage;
	private ObservableArrayList<Player> players;
	
	/**
	 * Constructor initialises the fields
	 */
	public PlayModel() {
		dungeonName = new ObservableValue<String>("");
		goal = new ObservableValue<Integer>(0);
		turn = new ObservableValue<Boolean>(false);
		inGame = new ObservableValue<Boolean>(true);
		username = new ObservableValue<String>("");
		
		map = new ObservableValue<char[][]>(new char[7][7]);
		
		gold = new ObservableValue<Integer>(0);
		hp = new ObservableValue<Integer>(3);
		
		sword = new ObservableValue<Boolean>(false);
		armour = new ObservableValue<Boolean>(false);
		lantern = new ObservableValue<Boolean>(false);
		
		gameMessage = new ObservableValue<GameMessage>(null);
		players = new ObservableArrayList<Player>();
	}
	
	/**
	 * Gets the name of the dungeon
	 * 
	 * @return The name of the dungeon
	 */
	public ObservableValue<String> getDungeonName() {
		return dungeonName;
	}

	/**
	 * Gets the amount of gold needed to win
	 * 
	 * @return The amount of gold needed to win
	 */
	public ObservableValue<Integer> getGoal() {
		return goal;
	}
	
	/**
	 * Gets whether it's this client player's turn
	 * 
	 * @return Whether it's this client player's turn
	 */
	public ObservableValue<Boolean> getTurn() {
		return turn;
	}
	
	/**
	 * Gets whether the player is still in the game or not
	 * 
	 * @return Whether the player is still in the game or not
	 */
	public ObservableValue<Boolean> getInGame() {
		return inGame;
	}

	/**
	 * Gets this client player's user name
	 * 
	 * @return This client player's user name
	 */
	public ObservableValue<String> getUsername() {
		return username;
	}

	/**
	 * Gets the visible map tiles
	 * 
	 * @return The visible map tiles
	 */
	public ObservableValue<char[][]> getMap() {
		return map;
	}

	/**
	 * Gets the amount of gold the player has
	 * 
	 * @return The amount of gold the player has
	 */
	public ObservableValue<Integer> getGold() {
		return gold;
	}

	/**
	 * Gets the amount of hit points the player has
	 * 
	 * @return The amount of hit points the player has
	 */
	public ObservableValue<Integer> getHp() {
		return hp;
	}

	/**
	 * Gets whether the player has a sword or not
	 * 
	 * @return Whether the player has a sword or not
	 */
	public ObservableValue<Boolean> getSword() {
		return sword;
	}

	/**
	 * Gets whether the player has armour or not
	 * 
	 * @return Whether the player has armour or not
	 */
	public ObservableValue<Boolean> getArmour() {
		return armour;
	}

	/**
	 * Gets whether the player has a lantern or not
	 * 
	 * @return Whether the player has a lantern or not
	 */
	public ObservableValue<Boolean> getLantern() {
		return lantern;
	}

	/**
	 * Gets all of the game messages received from the server
	 * 
	 * @return All of the game messages received from the server
	 */
	public ObservableValue<GameMessage> getGameMessage() {
		return gameMessage;
	}

	/**
	 * Gets all of the players in the game
	 * 
	 * @return All of the players in the game
	 */
	public ObservableArrayList<Player> getPlayers() {
		return players;
	}
	
	/**
	 * Gets a specific player in the game from their user name
	 * 
	 * @param username The target player's user name
	 * @return         The player matching the user name specified. <code>null</code> if no player has the specified user name
	 */
	public Player getPlayer(String username) {
		for (Player player : players) {
			if (player.getUsername().equals(username)) {
				return player;
			}
		}
		return null;
	}
	
	/**
	 * Adds a new game message to the model
	 * 
	 * @param message The message to display
	 * @param type    The type of message
	 */
	public void addGameMessage(String message, int type) {
		gameMessage.setValue(new GameMessage(message, type));
	}
}
