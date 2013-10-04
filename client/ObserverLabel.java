import java.util.Observable;
import java.util.Observer;

import javax.swing.JLabel;

/**
 * The ObserverLabel class represents a label that can update to reflect changes in an Observable object.
 * 
 * @author Sean Stinson, ss938
 *
 */
public class ObserverLabel extends JLabel implements Observer {
	private static final long serialVersionUID = -8354078644594669935L;
	
	private String preText;
	private String postText;
	private String value;
	
	/**
	 * Constructor passes the parameters to the fields
	 * 
	 * @param preText  The text to display before the value
	 * @param value    The initial value of the label
	 * @param postText The text to display after the value
	 */
	public ObserverLabel(String preText, String value, String postText) {
		this.value = value;
		this.preText = preText;
		this.postText = postText;
		setText();
	}
	
	/**
	 * Constructor passes parameters to the fields. No text is displayed after the value
	 * 
	 * @param preText The text to display before the value
	 * @param value   The initial value of the label
	 */
	public ObserverLabel(String preText, String value) {
		this(preText, value, "");
	}
	
	/**
	 * Constructor passes the value to the field. No text is displayed before or after the value
	 * 
	 * @param value The initial value of the label
	 */
	public ObserverLabel(String value) {
		this("", value, "");
	}
	
	/**
	 * Updates the text displayed on the label
	 */
	private void setText() {
		super.setText(preText + value + postText);
	}
	
	/**
	 * Updates the value of the label when an Observable is changed, displaying the new value on the label
	 */
	@Override
	public void update(Observable observable, Object value) {
		this.value = value.toString();
		setText();
	}
}