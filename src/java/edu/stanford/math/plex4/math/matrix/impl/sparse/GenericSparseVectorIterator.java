/**
 * 
 */
package edu.stanford.math.plex4.math.matrix.impl.sparse;

import edu.stanford.math.plex4.math.matrix.interfaces.GenericAbstractVectorIterator;
import edu.stanford.math.plex4.utility.ExceptionUtility;
import gnu.trove.iterator.TIntObjectIterator;

/**
 * @author atausz
 *
 */
public class GenericSparseVectorIterator<T> implements GenericAbstractVectorIterator<T> {
	private final TIntObjectIterator<T> iterator;
	
	public GenericSparseVectorIterator(GenericSparseVector<T> vector) {
		ExceptionUtility.verifyNonNull(vector);
		this.iterator = vector.map.iterator();
	}
	
	/* (non-Javadoc)
	 * @see edu.stanford.math.plex_plus.math.matrix.generic.AbstractGenericVectorIterator#advance()
	 */
	public void advance() {
		this.iterator.advance();
	}

	/* (non-Javadoc)
	 * @see edu.stanford.math.plex_plus.math.matrix.generic.AbstractGenericVectorIterator#hasNext()
	 */
	public boolean hasNext() {
		return this.iterator.hasNext();
	}

	/* (non-Javadoc)
	 * @see edu.stanford.math.plex_plus.math.matrix.generic.AbstractGenericVectorIterator#index()
	 */
	public int index() {
		return this.iterator.key();
	}

	/* (non-Javadoc)
	 * @see edu.stanford.math.plex_plus.math.matrix.generic.AbstractGenericVectorIterator#remove()
	 */
	public void remove() {
		this.iterator.remove();
	}

	/* (non-Javadoc)
	 * @see edu.stanford.math.plex_plus.math.matrix.generic.AbstractGenericVectorIterator#value()
	 */
	public T value() {
		return this.iterator.value();
	}

}
