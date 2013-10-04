import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JOptionPane;

/**
 * The Map class handles loading a dungeon map from a file and arranging the dungeon tiles
 * 
 * @author Sean Stinson, ss938
 *
 */
public class Map {
	private String name = "Default Dungeon";
	private int goal = 2;
	private int height = 5;
	private int width = 5;
	
	// Default map
	private Tile[][] tiles = {
			{new Tile(Tile.WALL),    new Tile(Tile.WALL),  new Tile(Tile.ARMOUR), new Tile(Tile.WALL),   new Tile(Tile.WALL)   },
			{new Tile(Tile.WALL),    new Tile(Tile.EXIT),  new Tile(Tile.GOLD),   new Tile(Tile.FLOOR),  new Tile(Tile.GOLD)   },
			{new Tile(Tile.LANTERN), new Tile(Tile.GOLD),  new Tile(Tile.FLOOR),  new Tile(Tile.HEALTH), new Tile(Tile.LANTERN)},
			{new Tile(Tile.WALL),    new Tile(Tile.SWORD), new Tile(Tile.FLOOR),  new Tile(Tile.ARMOUR), new Tile(Tile.GOLD)   },
			{new Tile(Tile.WALL),    new Tile(Tile.WALL),  new Tile(Tile.SWORD),  new Tile(Tile.WALL),   new Tile(Tile.WALL)   }
	};
	
	/**
	 * Constructor attempts to read a map from a file and uses the default map if this fails
	 * 
	 * @param fileName The name of the file to read
	 */
	public Map(String fileName) {
		try {
			ArrayList<String> lines = loadFile(fileName);
			if (lines.size() >= 3) {
				parseMap(lines);
			} else {
				JOptionPane.showMessageDialog(null, "Missing map file elements, using default map", "Using Default Map", JOptionPane.WARNING_MESSAGE);
			}
		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(null, "File not found, using default map", "Using Default Map", JOptionPane.WARNING_MESSAGE);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Unable to load map, using default map", "Using Default Map", JOptionPane.WARNING_MESSAGE);
		}
	}
	
	/**
	 * Constructor loads the default map
	 */
	public Map() {
		// Keep default map values
	}
	
	/**
	 * Gets the name of the dungeon
	 * 
	 * @return The name of the dungeon
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name of the dungeon
	 * 
	 * @param name The new name of the dungeon
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Gets the amount of gold needed to win
	 * 
	 * @return The amount of gold needed to win
	 */
	public int getGoal() {
		return goal;
	}

	/**
	 * Sets the amount of gold needed to win
	 * 
	 * @param goal The new amount of gold needed to win
	 */
	public void setGoal(int goal) {
		this.goal = goal;
	}

	/**
	 * Gets the height of the map
	 * 
	 * @return The height of the map
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * Sets the height of the map
	 * 
	 * @param height The new height of the map
	 */
	public void setHeight(int height) {
		this.height = height;
	}

	/**
	 * Gets the width of the map
	 * 
	 * @return The width of the map
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * Sets the width of the map
	 * 
	 * @param width The new width of the map
	 */
	public void setWidth(int width) {
		this.width = width;
	}

	/**
	 * Gets the map
	 * 
	 * @return The map
	 */
	public Tile[][] getTiles() {
		return tiles;
	}

	/**
	 * Loads the map from a file
	 * 
	 * @param fileName     The name of the file to load
	 * @return             An ArrayList of lines read from the file
	 * @throws IOException Thrown if there was a problem reading from the file
	 */
	private ArrayList<String> loadFile(String fileName) throws IOException {
		ArrayList<String> lines = new ArrayList<String>();
		
		BufferedReader reader = new BufferedReader(new FileReader(fileName));
		
		String line;
		while ((line = reader.readLine()) != null) {
			lines.add(line);
		}
		
		reader.close();
		return lines;
	}
	
	/**
	 * Converts the map from String to Tile format
	 * 
	 * @param lines The lines read from the map file
	 */
	private void parseMap(ArrayList<String> lines) {
		// Parse map name
		String first = lines.get(0);
		String tempName = "";
		if (first.matches("name .*")) {
			tempName = lines.get(0).split(" ", 2)[1];
		} else {
			JOptionPane.showMessageDialog(null, "No map name found, using default map", "Using Default Map", JOptionPane.WARNING_MESSAGE);
			return;
		}
		
		// Parse map goal
		String second = lines.get(1);
		int tempGoal;
		String goalString;
		if (second.matches("win [0-9]+")) {
			goalString = second.split(" ", 2)[1];
			tempGoal = Integer.parseInt(goalString);
		} else {
			JOptionPane.showMessageDialog(null, "No goal found, using default map", "Using Default Map", JOptionPane.WARNING_MESSAGE);
			return;
		}
		
		// Get map dimensions
		int tempHeight = lines.size() - 2;
		int tempWidth = lines.get(3).length();
		
		// Parse map tiles
		Tile[][] tempMap = new Tile[tempHeight][tempWidth];
		int goldCount = 0;
		
		for (int row = 0; row < tempHeight; row++) {
			String rowString = lines.get(row + 2);
			
			if (!rowString.matches("[ASLGHE.#]*")) {
				JOptionPane.showMessageDialog(null, "Invalid map characters, using default map", "Using Default Map", JOptionPane.WARNING_MESSAGE);
				return;
			}
			
			for (int col = 0; col < tempWidth; col++) {
				char currentChar = rowString.charAt(col);
				
				tempMap[row][col] = new Tile(currentChar);
				
				if (currentChar == 'G') {
					goldCount++;
				}
			}
		}
		
		// If there's enough gold, initialise map
		if (goldCount >= tempGoal) {
			name = tempName;
			goal = tempGoal;
			width = tempWidth;
			height = tempHeight;
			tiles = tempMap;
		} else {
			JOptionPane.showMessageDialog(null, "Not enough gold on the map, using default map", "Using Default Map", JOptionPane.WARNING_MESSAGE);
			return;
		}
	}
	
	/**
	 * Converts the entire map into String format
	 * 
	 * @return The entire map as a String
	 */
	public String toString() {
		String mapString = "";
		
		for (int row = 0; row < height; row++) {
			for (int col = 0; col < width; col++) {
				if (tiles[row][col].isOccupied()) {
					mapString += "P";
				} else {
					mapString += tiles[row][col].getBase();
				}
			}
			
			if (row != height - 1) {
				mapString += "\n";
			}
		}
		
		return mapString;
	}
}
