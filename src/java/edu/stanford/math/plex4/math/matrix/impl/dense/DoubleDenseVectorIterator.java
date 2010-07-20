/**
 * 
 */
package edu.stanford.math.plex4.math.matrix.impl.dense;

import edu.stanford.math.plex4.math.matrix.interfaces.DoubleAbstractVectorIterator;

/**
 * @author Andrew Tausz
 *
 */
public class DoubleDenseVectorIterator implements DoubleAbstractVectorIterator {
	private int index = -1;
	private final int vectorLength;
	private final DoubleDenseVector vector;
	
	public DoubleDenseVectorIterator(DoubleDenseVector vector) {
		this.vectorLength = vector.getLength();
		this.vector = vector;
	}
	
	/* (non-Javadoc)
	 * @see edu.stanford.math.plex_plus.math.matrix.interfaces.DoubleAbstractVectorIterator#advance()
	 */
	public void advance() {
		this.index++;
	}

	/* (non-Javadoc)
	 * @see edu.stanford.math.plex_plus.math.matrix.interfaces.DoubleAbstractVectorIterator#hasNext()
	 */
	public boolean hasNext() {
		return (this.index < this.vectorLength - 1);
	}

	/* (non-Javadoc)
	 * @see edu.stanford.math.plex_plus.math.matrix.interfaces.DoubleAbstractVectorIterator#index()
	 */
	public int index() {
		return this.index;
	}

	/* (non-Javadoc)
	 * @see edu.stanford.math.plex_plus.math.matrix.interfaces.DoubleAbstractVectorIterator#remove()
	 */
	public void remove() {
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see edu.stanford.math.plex_plus.math.matrix.interfaces.DoubleAbstractVectorIterator#value()
	 */
	public double value() {
		return this.vector.get(this.index);
	}

}
