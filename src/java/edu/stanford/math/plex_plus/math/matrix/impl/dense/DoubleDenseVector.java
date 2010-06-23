/**
 * 
 */
package edu.stanford.math.plex_plus.math.matrix.impl.dense;

import edu.stanford.math.plex_plus.math.matrix.impl.sparse.DoubleSparseVector;
import edu.stanford.math.plex_plus.math.matrix.interfaces.DoubleAbstractVector;
import edu.stanford.math.plex_plus.math.matrix.interfaces.DoubleAbstractVectorIterator;
import edu.stanford.math.plex_plus.utility.ArrayUtility;
import edu.stanford.math.plex_plus.utility.ExceptionUtility;

/**
 * @author Andris
 *
 */
public class DoubleDenseVector extends DoubleAbstractVector {
	private final double[] values;
	
	public DoubleDenseVector(int length) {
		ExceptionUtility.verifyNonNegative(length);
		this.values = new double[length];
	}
	
	/* (non-Javadoc)
	 * @see edu.stanford.math.plex_plus.math.matrix.interfaces.DoubleAbstractVector#get(int)
	 */
	@Override
	public double get(int index) {
		return this.values[index];
	}

	/* (non-Javadoc)
	 * @see edu.stanford.math.plex_plus.math.matrix.interfaces.DoubleAbstractVector#getLength()
	 */
	@Override
	public int getLength() {
		return this.values.length;
	}

	public double innerProduct(DoubleSparseVector other) {
		ExceptionUtility.verifyEqual(this.getLength(), other.getLength());
		double sum = 0;
		for (DoubleAbstractVectorIterator iterator = other.iterator(); iterator.hasNext(); ) {
			iterator.advance();
			sum += iterator.value() * this.get(iterator.index());
		}
		
		return sum;
	}
	
	/* (non-Javadoc)
	 * @see edu.stanford.math.plex_plus.math.matrix.interfaces.DoubleAbstractVector#innerProduct(edu.stanford.math.plex_plus.math.matrix.interfaces.DoubleAbstractVector)
	 */
	@Override
	public double innerProduct(DoubleAbstractVector other) {
		if (other instanceof DoubleSparseVector) {
			return this.innerProduct((DoubleSparseVector) other);
		}
		ExceptionUtility.verifyEqual(this.getLength(), other.getLength());
		double sum = 0;
		
		for (int i = 0; i < this.values.length; i++) {
			sum += this.values[i] * other.get(i);
		}
		
		return sum;
	}

	/* (non-Javadoc)
	 * @see edu.stanford.math.plex_plus.math.matrix.interfaces.DoubleAbstractVector#iterator()
	 */
	@Override
	public DoubleAbstractVectorIterator iterator() {
		return new DoubleDenseVectorIterator(this);
	}

	/* (non-Javadoc)
	 * @see edu.stanford.math.plex_plus.math.matrix.interfaces.DoubleAbstractVector#like(int)
	 */
	@Override
	public DoubleAbstractVector like(int size) {
		return new DoubleDenseVector(this.values.length);
	}

	/* (non-Javadoc)
	 * @see edu.stanford.math.plex_plus.math.matrix.interfaces.DoubleAbstractVector#set(int, double)
	 */
	@Override
	public void set(int index, double value) {
		this.values[index] = value;
	}

	/* (non-Javadoc)
	 * @see edu.stanford.math.plex_plus.math.matrix.interfaces.DoubleAbstractVector#toString()
	 */
	@Override
	public String toString() {
		return ArrayUtility.toMatlabString(this.values);
	}

}