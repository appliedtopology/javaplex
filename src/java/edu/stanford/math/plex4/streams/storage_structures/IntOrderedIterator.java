package edu.stanford.math.plex4.streams.storage_structures;

import java.util.Iterator;

import edu.stanford.math.primitivelib.autogen.pair.IntObjectPair;

public class IntOrderedIterator<E> implements Iterator<E> {

	private final Iterator<IntObjectPair<E>> internalIterator;

	public IntOrderedIterator(Iterable<IntObjectPair<E>> collection) {
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

