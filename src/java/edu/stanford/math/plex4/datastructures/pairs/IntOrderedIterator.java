package edu.stanford.math.plex4.datastructures.pairs;

import java.util.Iterator;

public class IntOrderedIterator<E> implements Iterator<E> {

	private final Iterator<IntGenericPair<E>> internalIterator;
	
	public IntOrderedIterator(Iterable<IntGenericPair<E>> collection) {
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

