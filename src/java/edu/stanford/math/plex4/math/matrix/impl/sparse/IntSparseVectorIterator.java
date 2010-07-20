/**
 * 
 */
package edu.stanford.math.plex4.math.matrix.impl.sparse;

import edu.stanford.math.plex4.math.matrix.interfaces.IntAbstractVectorIterator;
import edu.stanford.math.plex4.utility.ExceptionUtility;
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
	public void advance() {
		this.iterator.advance();
	}

	/* (non-Javadoc)
	 * @see edu.stanford.math.plex_plus.math.matrix.primitive.AbstractIntVectorIterator#hasNext()
	 */
	public boolean hasNext() {
		return this.iterator.hasNext();
	}

	/* (non-Javadoc)
	 * @see edu.stanford.math.plex_plus.math.matrix.primitive.AbstractIntVectorIterator#index()
	 */
	public int index() {
		return this.iterator.key();
	}

	/* (non-Javadoc)
	 * @see edu.stanford.math.plex_plus.math.matrix.primitive.AbstractIntVectorIterator#remove()
	 */
	public void remove() {
		this.iterator.remove();
	}

	/* (non-Javadoc)
	 * @see edu.stanford.math.plex_plus.math.matrix.primitive.AbstractIntVectorIterator#value()
	 */
	public int value() {
		return this.iterator.value();
	}

}
