package edu.stanford.math.plex4.metric.impl;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import edu.stanford.math.primitivelib.autogen.pair.ObjectDoublePair;

/**
 * <p>This class is used for holding the set of k-indices with the lowest ranks or values.
 * It is used by adding elements along with their ranks. It stores a maximum of k values, 
 * and discards all values that are not in the lowest k.</p>
 * 
 * <p>An example of a use of this class is for maintaining a list of k-nearest neighbors.</p>
 * 
 * @author Andrew Tausz
 *
 * @param <T> the index type
 */
public class TruncatedPriorityQueue<T> {
	private final LinkedList<ObjectDoublePair<T>> list = new LinkedList<ObjectDoublePair<T>>();
	private final int k;
	
	public TruncatedPriorityQueue(int k) {
		this.k = k;
	}
	
	/**
	 * This function returns the list of object-value pairs currently in the queue.
	 * 
	 * @return a list of object-value pairs
	 */
	public List<ObjectDoublePair<T>> getListOfPairs() {
		return this.list;
	}
	
	/**
	 * This function returns the list of indices currently in the queue.
	 * 
	 * @return a list of indices
	 */
	public List<T> getIndices() {
		List<T> indices = new ArrayList<T>();
		
		for (ObjectDoublePair<T> pair: this.list) {
			indices.add(pair.getFirst());
		}
		
		return indices;
	}
	
	/**
	 * <p>This function inserts the given object (or index), with a specified value into the queue.
	 * It holds the elements in sorted order. Note, however, if the queue is already full and the
	 * value of the inserted object is higher than all of the elements in the queue, it is not
	 * actually inserted.</p>
	 * 
	 * <p>Examples:</p>
	 * <p>list = {(a, 1), (b, 4)}. Inserting (c, 2) produces {(a, 1), (c, 2), (b, 4)}, when k >= 3.
	 * If k == 2, then the result is {(a, 1), (c, 2)}. Inserting (d, 8), with k == 2 does not
	 * modify the list.</p>
	 * 
	 * @param index the object to insert
	 * @param value the value of the object
	 */
	public void insert(T index, double value) {
		if (this.list.isEmpty()) {
			this.list.add(new ObjectDoublePair<T>(index, value));
			return;
		}
		
		if (value < this.list.getFirst().getSecond()) {
			this.list.addFirst(new ObjectDoublePair<T>(index, value));
			this.truncate();
			return;
		}
		
		if (this.list.getLast().getSecond() <= value) {
			this.list.addLast(new ObjectDoublePair<T>(index, value));
			this.truncate();
			return;
		}
		
		// at this point we know list[0] <= value < list[k - 1]
		
		int i = 0;
		int insertionPoint = 0;
		for (ObjectDoublePair<T> pair: this.list) {
			if (value < pair.getSecond()) {
				insertionPoint = i;
				break;
			}
			i++;
		}
		
		this.list.add(insertionPoint, new ObjectDoublePair<T>(index, value));
		this.truncate();
	}
	
	/**
	 * This function truncates the queue so that it contains a maximum of k elements.
	 */
	private void truncate() {
		while (this.list.size() > k) {
			this.list.removeLast();
		}
	}
}
