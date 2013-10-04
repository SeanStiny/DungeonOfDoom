import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

/**
 * The TurnPanel component displays the end turn button and tells the player when it is their turn.
 * 
 * @author Sean Stinson, ss938
 *
 */
public class TurnPanel extends JPanel implements Observer {
	private static final long serialVersionUID = 2125627303372256150L;
	
	private JPanel paneContent;
	private JLabel lblTurn;
	private JButton btnEndTurn;
	
	private boolean turn;

	/**
	 * Constructor initialises the components
	 */
	public TurnPanel() {
		// Gaps surrounding component
		setBorder(new EmptyBorder(0, 0, 4, 2));
		
		// Content panel
		paneContent = new JPanel(new BorderLayout());
		paneContent.setBorder(BorderFactory.createLoweredBevelBorder());
		lblTurn = new JLabel();
		lblTurn.setHorizontalAlignment(JLabel.CENTER);
		lblTurn.setVerticalAlignment(JLabel.CENTER);
		paneContent.add(lblTurn, BorderLayout.CENTER);
		
		// End turn button
		btnEndTurn = new JButton("End Turn");
		btnEndTurn.setFocusable(false);
		btnEndTurn.setPreferredSize(new Dimension(100, 50));
		paneContent.add(btnEndTurn, BorderLayout.PAGE_END);
		
		setLayout(new BorderLayout());
		add(paneContent, BorderLayout.CENTER);
		
		update(null, false);
	}
	
	/**
	 * Gets the content panel component
	 * 
	 * @return The content panel component
	 */
	public JPanel getPaneContent() {
		return paneContent;
	}

	/**
	 * Gets the turn label component
	 * 
	 * @return The turn label component
	 */
	public JLabel getLblTurn() {
		return lblTurn;
	}

	/**
	 * Gets the end turn button component
	 * 
	 * @return The end turn button component
	 */
	public JButton getBtnEndTurn() {
		return btnEndTurn;
	}
	
	/**
	 * Displays a message telling the user that they can only use chat and shout
	 */
	public void chatOnly() {
		paneContent.setBackground(Color.GRAY);
		lblTurn.setText("Game Over.");
		btnEndTurn.setEnabled(false);
	}

	/**
	 * Updates the display when the play's turn starts or ends
	 */
	@Override
	public void update(Observable observable, Object value) {
		turn = (Boolean) value;
		
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				if (turn) {
					paneContent.setBackground(new Color(102, 255, 102));
					lblTurn.setText("It is your turn!");
					btnEndTurn.setEnabled(true);
				} else {
					paneContent.setBackground(new Color(255, 178, 102));
					lblTurn.setText("It is not your turn.");
					btnEndTurn.setEnabled(false);
				}
			}
		});
		
	}
}
