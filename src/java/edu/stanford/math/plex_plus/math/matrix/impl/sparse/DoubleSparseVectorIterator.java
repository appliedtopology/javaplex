/**
 * 
 */
package edu.stanford.math.plex_plus.math.matrix.impl.sparse;

import edu.stanford.math.plex_plus.math.matrix.interfaces.DoubleAbstractVectorIterator;
import edu.stanford.math.plex_plus.utility.ExceptionUtility;
import gnu.trove.iterator.TIntDoubleIterator;

/**
 * @author Andrew Tausz
 *
 */
public class DoubleSparseVectorIterator implements DoubleAbstractVectorIterator {
	private final TIntDoubleIterator iterator;
	
	public DoubleSparseVectorIterator(DoubleSparseVector vector) {
		ExceptionUtility.verifyNonNull(vector);
		this.iterator = vector.map.iterator();
	}
	
	/* (non-Javadoc)
	 * @see edu.stanford.math.plex_plus.math.matrix.primitive.AbstractDoubleVectorIterator#advance()
	 */
	@Override
	public void advance() {
		this.iterator.advance();
	}

	/* (non-Javadoc)
	 * @see edu.stanford.math.plex_plus.math.matrix.primitive.AbstractDoubleVectorIterator#hasNext()
	 */
	@Override
	public boolean hasNext() {
		return this.iterator.hasNext();
	}

	/* (non-Javadoc)
	 * @see edu.stanford.math.plex_plus.math.matrix.primitive.AbstractDoubleVectorIterator#index()
	 */
	@Override
	public int index() {
		return this.iterator.key();
	}

	/* (non-Javadoc)
	 * @see edu.stanford.math.plex_plus.math.matrix.primitive.AbstractDoubleVectorIterator#remove()
	 */
	@Override
	public void remove() {
		this.iterator.remove();
	}

	/* (non-Javadoc)
	 * @see edu.stanford.math.plex_plus.math.matrix.primitive.AbstractDoubleVectorIterator#value()
	 */
	@Override
	public double value() {
		return this.iterator.value();
	}

}
