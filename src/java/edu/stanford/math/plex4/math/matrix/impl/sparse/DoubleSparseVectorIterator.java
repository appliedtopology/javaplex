/**
 * 
 */
package edu.stanford.math.plex4.math.matrix.impl.sparse;

import edu.stanford.math.plex4.math.matrix.interfaces.DoubleAbstractVectorIterator;
import edu.stanford.math.plex4.utility.ExceptionUtility;
import gnu.trove.TIntDoubleIterator;

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
	public void advance() {
		this.iterator.advance();
	}

	/* (non-Javadoc)
	 * @see edu.stanford.math.plex_plus.math.matrix.primitive.AbstractDoubleVectorIterator#hasNext()
	 */
	public boolean hasNext() {
		return this.iterator.hasNext();
	}

	/* (non-Javadoc)
	 * @see edu.stanford.math.plex_plus.math.matrix.primitive.AbstractDoubleVectorIterator#index()
	 */
	public int index() {
		return this.iterator.key();
	}

	/* (non-Javadoc)
	 * @see edu.stanford.math.plex_plus.math.matrix.primitive.AbstractDoubleVectorIterator#remove()
	 */
	public void remove() {
		this.iterator.remove();
	}

	/* (non-Javadoc)
	 * @see edu.stanford.math.plex_plus.math.matrix.primitive.AbstractDoubleVectorIterator#value()
	 */
	public double value() {
		return this.iterator.value();
	}

}
