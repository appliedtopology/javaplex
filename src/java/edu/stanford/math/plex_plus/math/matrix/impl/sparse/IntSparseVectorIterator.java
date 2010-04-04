/**
 * 
 */
package edu.stanford.math.plex_plus.math.matrix.impl.sparse;

import edu.stanford.math.plex_plus.math.matrix.interfaces.IntAbstractVectorIterator;
import edu.stanford.math.plex_plus.utility.ExceptionUtility;
import gnu.trove.iterator.TIntIntIterator;

/**
 * @author Andrew Tausz
 *
 */
public class IntSparseVectorIterator implements IntAbstractVectorIterator {
	private final TIntIntIterator iterator;
	
	public IntSparseVectorIterator(IntSparseVector vector) {
		ExceptionUtility.verifyNonNull(vector);
		this.iterator = vector.map.iterator();
	}
	
	/* (non-Javadoc)
	 * @see edu.stanford.math.plex_plus.math.matrix.primitive.AbstractIntVectorIterator#advance()
	 */
	@Override
	public void advance() {
		this.iterator.advance();
	}

	/* (non-Javadoc)
	 * @see edu.stanford.math.plex_plus.math.matrix.primitive.AbstractIntVectorIterator#hasNext()
	 */
	@Override
	public boolean hasNext() {
		return this.iterator.hasNext();
	}

	/* (non-Javadoc)
	 * @see edu.stanford.math.plex_plus.math.matrix.primitive.AbstractIntVectorIterator#index()
	 */
	@Override
	public int index() {
		return this.iterator.key();
	}

	/* (non-Javadoc)
	 * @see edu.stanford.math.plex_plus.math.matrix.primitive.AbstractIntVectorIterator#remove()
	 */
	@Override
	public void remove() {
		this.iterator.remove();
	}

	/* (non-Javadoc)
	 * @see edu.stanford.math.plex_plus.math.matrix.primitive.AbstractIntVectorIterator#value()
	 */
	@Override
	public int value() {
		return this.iterator.value();
	}

}
