import java.util.ArrayList;
import java.util.Collection;
import java.util.Observable;
import java.util.Observer;

/**
 * The ObservableArrayList class represents an ArrayList that can be watched by an Observer.
 * 
 * @author Sean Stinson, ss938
 *
 * @param <E> The type to be stored in the ArrayList
 */
public class ObservableArrayList<E> extends ArrayList<E> {
	private static final long serialVersionUID = 3300772099615948438L;
	
	private MyObservable observable;
	
	/**
	 * Constructor sets up the Observable field
	 */
	public ObservableArrayList() {
		observable = new MyObservable();
	}
	
	/**
	 * Adds an observer to the Observable object
	 * 
	 * @param o The Observer to add to the Observable object
	 */
	public void addObserver(Observer o) {
		observable.addObserver(o);
	}
	
	@Override
	public boolean add(E e) {
		boolean result = super.add(e);
		
		observable.setChanged();
		observable.notifyObservers(this.toArray());
		
		return result;
	}
	
	@Override
	public void add(int index, E element) {
		super.add(index, element);
		
		observable.setChanged();
		observable.notifyObservers(this.toArray());
	}
	
	@Override
	public boolean addAll(Collection<? extends E> c) {
		boolean result = super.addAll(c);
		
		observable.setChanged();
		observable.notifyObservers(this.toArray());
		
		return result;
	}
	
	@Override
	public boolean addAll(int index, Collection<? extends E> c) {
		boolean result = super.addAll(index, c);
		
		observable.setChanged();
		observable.notifyObservers(this.toArray());
		
		return result;
	}
	
	@Override
	public void clear() {
		super.clear();
		
		observable.setChanged();
		observable.notifyObservers(this.toArray());
	}
	
	@Override
	public E remove(int index) {
		E result = super.remove(index);
		
		observable.setChanged();
		observable.notifyObservers(this.toArray());
		
		return result;
	}
	
	@Override
	public boolean remove(Object o) {
		boolean result = super.remove(o);
		
		observable.setChanged();
		observable.notifyObservers(this.toArray());
		
		return result;
	}
	
	@Override
	public E set(int index, E element) {
		E result = super.set(index, element);
		
		observable.setChanged();
		observable.notifyObservers(this.toArray());
		
		return result;
	}
	
	/**
	 * MyObservable acts as a wrapper to allow the Observable object to be set to changed when
	 * the ArrayList is modified.
	 * 
	 * @author Sean Stinson, ss938
	 *
	 */
	private class MyObservable extends Observable {
		/**
		 * Allows <code>setChanged()</code> to be called publicly
		 */
		public void setChanged() {
			super.setChanged();
		}
	}
}
