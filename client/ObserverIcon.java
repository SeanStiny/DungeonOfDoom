import java.util.Observable;
import java.util.Observer;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

/**
 * The ObserverIcon class represents an icon label component that changes image when an Observable
 * Boolean value is changed.
 * 
 * @author Sean Stinson, ss938
 *
 */
public class ObserverIcon extends JLabel implements Observer {
	private static final long serialVersionUID = -1623662899025177548L;

	private String imageOn;
	private String imageOff;
	private boolean active;
	
	/**
	 * Constructor passes parameters to the fields and sets the label icon and text
	 * 
	 * @param imageOn  The image to display when the boolean value is <code>true</code>
	 * @param imageOff The image to display when the boolean value is <code>false</code>
	 * @param text     The text to display underneath the image
	 * @param active   The initial value of the boolean
	 */
	public ObserverIcon(String imageOn, String imageOff, String text, boolean active) {
		this.imageOn = imageOn;
		this.imageOff = imageOff;
		this.active = active;
		
		setText(text);
		setHorizontalTextPosition(JLabel.CENTER);
		setVerticalTextPosition(JLabel.BOTTOM);
		
		if (active) {
			setIcon(new ImageIcon(imageOn));
		} else {
			setIcon(new ImageIcon(imageOff));
		}
	}
	
	/**
	 * Gets the image displayed when the boolean is <code>true</code>
	 * 
	 * @return The image displayed when the boolean is <code>true</code>
	 */
	public String getImageOn() {
		return imageOn;
	}

	/**
	 * Sets the image displayed when the boolean is <code>true</code>
	 * 
	 * @param imageOn The image displayed when the boolean is <code>true</code>
	 */
	public void setImageOn(String imageOn) {
		this.imageOn = imageOn;
	}

	/**
	 * Gets the image displayed when the boolean is <code>false</code>
	 * 
	 * @return The image displayed when the boolean is <code>false</code>
	 */
	public String getImageOff() {
		return imageOff;
	}

	/**
	 * Sets the image displayed when the boolean is <code>false</code>
	 * 
	 * @param imageOff The image displayed when the boolean is <code>false</code>
	 */
	public void setImageOff(String imageOff) {
		this.imageOff = imageOff;
	}

	/**
	 * Gets the value of the boolean
	 * 
	 * @return The value of the boolean
	 */
	public boolean isActive() {
		return active;
	}

	/**
	 * Sets the value of the boolean and updates the image
	 * 
	 * @param active The new value of the boolean
	 */
	public void setActive(boolean active) {
		this.active = active;
		
		if (active) {
			setIcon(new ImageIcon(imageOn));
		} else {
			setIcon(new ImageIcon(imageOff));
		}
	}

	/**
	 * Updates the image displayed when the Observable Boolean value is changed
	 */
	@Override
	public void update(Observable observable, Object value) {
		boolean active = (Boolean) value;
		
		if (active) {
			setIcon(new ImageIcon(imageOn));
		} else {
			setIcon(new ImageIcon(imageOff));
		}
	}
}
