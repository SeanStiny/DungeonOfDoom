import java.util.Observable;

/**
 * The ObservableValue class represents a value that can be watched by an Observer.
 * 
 * @author Sean Stinson, ss938
 *
 * @param <E> The value type to be observed
 */
public class ObservableValue<E> extends Observable {
	private E value;
	
	/**
	 * Constructor passes the value as a parameter
	 * 
	 * @param value The value of the ObservableValue
	 */
	public ObservableValue(E value) {
		this.value = value;
	}
	
	/**
	 * Gets the value of the ObservableValue
	 * 
	 * @return The value of the ObservableValue
	 */
	public E getValue() {
		return value;
	}
	
	/**
	 * Sets the value of the ObservableValue and notifies any Observers of the change
	 * 
	 * @param value The new value of the ObservableValue
	 */
	public void setValue(E value) {
		this.value = value;
		
		setChanged();
		notifyObservers(this.value);
	}
}
