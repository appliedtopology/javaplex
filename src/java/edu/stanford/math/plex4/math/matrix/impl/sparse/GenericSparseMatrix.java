/**
 * 
 */
package edu.stanford.math.plex4.math.matrix.impl.sparse;

import edu.stanford.math.plex4.math.matrix.interfaces.GenericAbstractMatrix;
import edu.stanford.math.plex4.math.matrix.interfaces.GenericAbstractMatrixIterator;
import edu.stanford.math.plex4.utility.ExceptionUtility;
import gnu.trove.TIntObjectHashMap;

/**
 * @author Andrew Tausz
 *
 */
public class GenericSparseMatrix<T> implements GenericAbstractMatrix<T> {
	/**
	 * We use a row-wise storage scheme. The variable map stores
	 * the rows of the matrix on an as-needed basis. Each row is a 
	 * DoubleSparseVector. This choice was made so that matrix-vector
	 * products can be computed very quickly.
	 */
	protected final TIntObjectHashMap<GenericSparseVector<T>> map = new TIntObjectHashMap<GenericSparseVector<T>>();

	protected final int rows;
	protected final int columns;
	
	public GenericSparseMatrix(int rows, int columns) {
		ExceptionUtility.verifyNonNegative(rows);
		ExceptionUtility.verifyNonNegative(columns);
		this.rows = rows;
		this.columns = columns;
	}
	
	/* (non-Javadoc)
	 * @see edu.stanford.math.plex_plus.math.matrix.generic.GenericAbstractMatrix#get(int, int)
	 */
	public T get(int row, int column) {
		ExceptionUtility.verifyIndex(this.rows, row);
		ExceptionUtility.verifyIndex(this.columns, column);
		return this.map.get(row).get(column);
	}

	/* (non-Javadoc)
	 * @see edu.stanford.math.plex_plus.math.matrix.generic.GenericAbstractMatrix#getNumColumns()
	 */
	public int getNumColumns() {
		return this.columns;
	}

	/* (non-Javadoc)
	 * @see edu.stanford.math.plex_plus.math.matrix.generic.GenericAbstractMatrix#getNumRows()
	 */
	public int getNumRows() {
		return this.rows;
	}

	/* (non-Javadoc)
	 * @see edu.stanford.math.plex_plus.math.matrix.generic.GenericAbstractMatrix#iterator()
	 */
	public GenericAbstractMatrixIterator<T> iterator() {
		return new GenericSparseMatrixIterator<T>(this);
	}

	/* (non-Javadoc)
	 * @see edu.stanford.math.plex_plus.math.matrix.generic.GenericAbstractMatrix#set(int, int, java.lang.Object)
	 */
	public void set(int row, int column, T value) {
		ExceptionUtility.verifyNonNegative(rows);
		ExceptionUtility.verifyNonNegative(columns);
		ExceptionUtility.verifyNonNull(value);
		if (!this.map.contains(row)) {
			this.map.put(row, new GenericSparseVector<T>(this.columns));
		}
		this.map.get(row).set(column, value);
	}

}
