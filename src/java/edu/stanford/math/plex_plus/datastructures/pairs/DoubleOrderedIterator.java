/**
 * 
 */
package edu.stanford.math.plex_plus.datastructures.pairs;

import java.util.Iterator;


/**
 * @author atausz
 *
 */
public class DoubleOrderedIterator<E> implements Iterator<E> {

	private final Iterator<DoubleGenericPair<E>> internalIterator;
	
	public DoubleOrderedIterator(Iterable<DoubleGenericPair<E>> collection) {
		this.internalIterator = collection.iterator();
	}
	
	/* (non-Javadoc)
	 * @see java.util.Iterator#hasNext()
	 */
	public boolean hasNext() {
		return this.internalIterator.hasNext();
	}

	/* (non-Javadoc)
	 * @see java.util.Iterator#next()
	 */
	public E next() {
		return this.internalIterator.next().getSecond();
	}

	/* (non-Javadoc)
	 * @see java.util.Iterator#remove()
	 */
	public void remove() {
		throw new UnsupportedOperationException();
	}

}
