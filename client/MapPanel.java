import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 * The MapView component displays dungeon map tiles to the user in a grid.
 * 
 * @author Sean Stinson, ss938
 *
 */
public class MapPanel extends JPanel implements Observer {
	private static final long serialVersionUID = 7603139619585824498L;
	
	public static final int LOOK_WIDTH = 7;
	public static final int LOOK_HEIGHT = 7;
	
	private JComponent[][] tiles;
	private char[][] newMap;
	
	/**
	 * Constructor initialises and adds the inner components of the MapPanel
	 */
	public MapPanel() {
		// Container panel
		setLayout(new FlowLayout());
		setBackground(Color.BLACK);
		
		// Initialise grid panel
		JPanel paneGrid = new JPanel(new GridLayout(LOOK_HEIGHT, LOOK_WIDTH));
		paneGrid.setPreferredSize(new Dimension(380, 380));
		paneGrid.setBackground(Color.BLACK);
		
		// Initialise grid of tile labels and buttons
		Font tileFont = new Font("Monospaced", Font.BOLD, 35);
		tiles = new JComponent[LOOK_HEIGHT][LOOK_WIDTH];
		for (int row = 0; row < LOOK_HEIGHT; row++) {
			for (int col = 0; col < LOOK_WIDTH; col++) {
				JComponent currentTile;
				
				if (row == LOOK_HEIGHT / 2 && col == LOOK_WIDTH / 2) {
					// The centre button
					JButton btnCenter = new JButton("X");
					btnCenter.setForeground(Color.RED);
					btnCenter.setContentAreaFilled(false);
					btnCenter.setBorderPainted(false);
					btnCenter.setFocusable(false);
					btnCenter.setOpaque(false);
					btnCenter.setBackground(Color.BLACK);
					currentTile = btnCenter;
				} else if ((row == LOOK_HEIGHT / 2 - 1 && col == LOOK_WIDTH / 2) || (row == LOOK_HEIGHT / 2 + 1 && col == LOOK_WIDTH / 2) ||
						(row == LOOK_HEIGHT / 2 && col == LOOK_WIDTH / 2 - 1) || (row == LOOK_HEIGHT / 2 && col == LOOK_WIDTH / 2 + 1)) {
					// Buttons adjacent to the centre of the grid (N, S, E and W)
					JButton btnAdjacent = new JButton("X");
					btnAdjacent.setForeground(Color.YELLOW);
					btnAdjacent.setContentAreaFilled(false);
					btnAdjacent.setBorderPainted(false);
					btnAdjacent.setFocusable(false);
					btnAdjacent.setOpaque(false);
					btnAdjacent.setBackground(Color.BLACK);
					currentTile = btnAdjacent;
				} else {
					currentTile = new JLabel("X", JLabel.CENTER);
					currentTile.setForeground(Color.WHITE);
				}
				
				currentTile.setFont(tileFont);
				
				tiles[row][col] = currentTile;
				paneGrid.add(tiles[row][col]);
			}
		}
		
		add(paneGrid);
	}
	
	/**
	 * Gets the button in the centre of the grid (the tile the player is stood on)
	 * 
	 * @return The button in the centre of the grid
	 */
	public JButton getCentreButton() {
		return (JButton) tiles[LOOK_HEIGHT / 2][LOOK_WIDTH / 2];
	}
	
	/**
	 * Gets the button directly to the north of the player's position
	 * 
	 * @return The button directly north of the player's position
	 */
	public JButton getButtonN() {
		return (JButton) tiles[LOOK_HEIGHT / 2 - 1][LOOK_WIDTH / 2];
	}
	
	/**
	 * Gets the button directly south of the player's position
	 * 
	 * @return The button directly south of the player's position
	 */
	public JButton getButtonS() {
		return (JButton) tiles[LOOK_HEIGHT / 2 + 1][LOOK_WIDTH / 2];
	}
	
	/**
	 * Gets the button directly east of the player's position
	 * 
	 * @return The button directly east of the player's position
	 */
	public JButton getButtonE() {
		return (JButton) tiles[LOOK_HEIGHT / 2][LOOK_WIDTH / 2 + 1];
	}
	
	/**
	 * Gets the button west of the player's position
	 * 
	 * @return The button west of the player's position
	 */
	public JButton getButtonW() {
		return (JButton) tiles[LOOK_HEIGHT / 2][LOOK_WIDTH / 2 - 1];
	}
	
	/**
	 * Gets the width of the grid
	 * 
	 * @return The width of the grid
	 */
	public int getGridWidth() {
		return LOOK_WIDTH;
	}
	
	/**
	 * Gets the height of the grid
	 * 
	 * @return The height of the grid
	 */
	public int getGridHeight() {
		return LOOK_HEIGHT;
	}

	/**
	 * Updates the tile display when the tile model is changed
	 */
	@Override
	public void update(Observable observable, Object value) {
		newMap = (char[][]) value;
		
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				// Update each tile text
				for (int row = 0; row < LOOK_HEIGHT; row++) {
					for (int col = 0; col < LOOK_WIDTH; col++) {
						if ((row == LOOK_HEIGHT / 2 - 1 && col == LOOK_WIDTH / 2) || (row == LOOK_HEIGHT / 2 + 1 && col == LOOK_WIDTH / 2)
								|| (row == LOOK_HEIGHT / 2 && col == LOOK_WIDTH / 2 - 1) || (row == LOOK_HEIGHT / 2 && col == LOOK_WIDTH / 2 + 1)
								|| (row == LOOK_HEIGHT / 2 && col == LOOK_WIDTH / 2)) {
							JButton currentTile = (JButton) tiles[row][col];
							currentTile.setText(Character.toString(newMap[row][col]));
							
							// If the current tile is adjacent to the player, colour the tile accordingly
							if (!(row == LOOK_HEIGHT / 2 && col == LOOK_WIDTH / 2)) {
								if (!currentTile.getText().equals("#")) {
									// If an action (such as move or attack) is available on the tile, colour it in yellow
									currentTile.setForeground(Color.ORANGE);
								} else {
									// If the player has no actions available on a tile, colour it in white
									currentTile.setForeground(Color.WHITE);
								}
							} else if (!currentTile.getText().equals(".") && !currentTile.getText().equals("E")) {
								// If the player is stood on an item, colour the tile red
								currentTile.setForeground(Color.RED);
							} else {
								currentTile.setForeground(Color.WHITE);
							}
						} else {
							JLabel currentTile = (JLabel) tiles[row][col];
							
							currentTile.setText(Character.toString(newMap[row][col]));
							
							// Colour players in green on the map
							if (currentTile.getText().equals("P")) {
								currentTile.setForeground(Color.GREEN);
							} else {
								currentTile.setForeground(Color.WHITE);
							}
						}
					}
				}
			}
		});
	}
}