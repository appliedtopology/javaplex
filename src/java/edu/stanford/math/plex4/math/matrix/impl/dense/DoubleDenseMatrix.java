/**
 * 
 */
package edu.stanford.math.plex4.math.matrix.impl.dense;

import edu.stanford.math.plex4.array_utility.ArrayPrinting;
import edu.stanford.math.plex4.math.matrix.interfaces.DoubleAbstractMatrix;
import edu.stanford.math.plex4.math.matrix.interfaces.DoubleAbstractMatrixIterator;
import edu.stanford.math.plex4.math.matrix.interfaces.DoubleAbstractVector;
import edu.stanford.math.plex4.math.matrix.interfaces.DoubleAbstractVectorIterator;
import edu.stanford.math.plex4.utility.ExceptionUtility;

/**
 * @author Andrew Tausz
 *
 */
public class DoubleDenseMatrix extends DoubleAbstractMatrix {
	private final double[][] values;
	private final int rows;
	private final int columns;
	/**
	 * 
	 */
	public DoubleDenseMatrix(int rows, int columns) {
		ExceptionUtility.verifyNonNegative(rows);
		ExceptionUtility.verifyNonNegative(columns);
		
		this.rows = rows;
		this.columns = columns;
		
		this.values = new double[rows][];
		for (int i = 0; i < rows; i++) {
			this.values[i] = new double[columns];
		}
	}

	/* (non-Javadoc)
	 * @see edu.stanford.math.plex_plus.math.matrix.interfaces.DoubleAbstractMatrix#get(int, int)
	 */
	@Override
	public double get(int row, int column) {
		return this.values[row][column];
	}

	/* (non-Javadoc)
	 * @see edu.stanford.math.plex_plus.math.matrix.interfaces.DoubleAbstractMatrix#getNumColumns()
	 */
	@Override
	public int getNumColumns() {
		return this.columns;
	}

	/* (non-Javadoc)
	 * @see edu.stanford.math.plex_plus.math.matrix.interfaces.DoubleAbstractMatrix#getNumRows()
	 */
	@Override
	public int getNumRows() {
		return this.rows;
	}

	/* (non-Javadoc)
	 * @see edu.stanford.math.plex_plus.math.matrix.interfaces.DoubleAbstractMatrix#iterator()
	 */
	@Override
	public DoubleAbstractMatrixIterator iterator() {
		return new DoubleDenseMatrixIterator(this);
	}

	/* (non-Javadoc)
	 * @see edu.stanford.math.plex_plus.math.matrix.interfaces.DoubleAbstractMatrix#like(int, int)
	 */
	@Override
	public DoubleAbstractMatrix like(int rows, int columns) {
		return new DoubleDenseMatrix(rows, columns);
	}

	/* (non-Javadoc)
	 * @see edu.stanford.math.plex_plus.math.matrix.interfaces.DoubleAbstractMatrix#multiply(edu.stanford.math.plex_plus.math.matrix.interfaces.DoubleAbstractVector)
	 */
	@Override
	public DoubleAbstractVector multiply(DoubleAbstractVector vector) {
		ExceptionUtility.verifyNonNull(vector);
		ExceptionUtility.verifyEqual(this.columns, vector.getLength());
		DoubleDenseVector result = new DoubleDenseVector(this.rows);
		
		for (int i = 0; i < this.rows; i++) {
			int sum = 0;
			for(DoubleAbstractVectorIterator iterator = vector.iterator(); iterator.hasNext(); ) {
				iterator.advance();
				sum += iterator.value() * this.values[i][iterator.index()];
			}
			result.set(i, sum);
		}
		
		return result;
	}

	/* (non-Javadoc)
	 * @see edu.stanford.math.plex_plus.math.matrix.interfaces.DoubleAbstractMatrix#set(int, int, double)
	 */
	@Override
	public void set(int row, int column, double value) {
		this.values[row][column] = value;
	}

	/* (non-Javadoc)
	 * @see edu.stanford.math.plex_plus.math.matrix.interfaces.DoubleAbstractMatrix#toString()
	 */
	@Override
	public String toString() {
		return ArrayPrinting.toString(this.values);
	}

}
