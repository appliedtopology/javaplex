/**
 * 
 */
package edu.stanford.math.plex_plus.math.matrix.impl.sparse;

import edu.stanford.math.plex_plus.math.matrix.interfaces.GenericAbstractVectorIterator;
import edu.stanford.math.plex_plus.utility.ExceptionUtility;
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
	@Override
	public void advance() {
		this.iterator.advance();
	}

	/* (non-Javadoc)
	 * @see edu.stanford.math.plex_plus.math.matrix.generic.AbstractGenericVectorIterator#hasNext()
	 */
	@Override
	public boolean hasNext() {
		return this.iterator.hasNext();
	}

	/* (non-Javadoc)
	 * @see edu.stanford.math.plex_plus.math.matrix.generic.AbstractGenericVectorIterator#index()
	 */
	@Override
	public int index() {
		return this.iterator.key();
	}

	/* (non-Javadoc)
	 * @see edu.stanford.math.plex_plus.math.matrix.generic.AbstractGenericVectorIterator#remove()
	 */
	@Override
	public void remove() {
		this.iterator.remove();
	}

	/* (non-Javadoc)
	 * @see edu.stanford.math.plex_plus.math.matrix.generic.AbstractGenericVectorIterator#value()
	 */
	@Override
	public T value() {
		return this.iterator.value();
	}

}
