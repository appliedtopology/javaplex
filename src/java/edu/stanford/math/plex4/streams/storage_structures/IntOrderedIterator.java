package edu.stanford.math.plex4.streams.storage_structures;

import java.util.Iterator;

import edu.stanford.math.primitivelib.autogen.pair.IntObjectPair;

/**
 * This class provides iteration over the second component of each element
 * in an Iterable collection of type IntObjectPair<E>. It essentially 
 * ignores the first component, and only returns the second. 
 * 
 * @author Andrew Tausz
 *
 * @param <E>
 */
public class IntOrderedIterator<E> implements Iterator<E> {

	/**
	 * The iterator over the pair collection.
	 */
	private final Iterator<IntObjectPair<E>> internalIterator;

	/**
	 * This constructor initializes the iterator with an iterable collection
	 * over the type IntObjectPair<E>.
	 * 
	 * @param collection the Iterable collection to initialize with
	 */
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

