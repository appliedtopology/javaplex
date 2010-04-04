package edu.stanford.math.plex_plus.math.matrix.impl.sparse;

import edu.stanford.math.plex_plus.math.matrix.interfaces.DoubleAbstractMatrix;
import edu.stanford.math.plex_plus.math.matrix.interfaces.DoubleAbstractMatrixIterator;
import edu.stanford.math.plex_plus.math.matrix.interfaces.DoubleAbstractVector;
import edu.stanford.math.plex_plus.utility.ExceptionUtility;
import gnu.trove.iterator.TIntObjectIterator;
import gnu.trove.map.hash.TIntObjectHashMap;

public class DoubleSparseMatrix implements DoubleAbstractMatrix {
	
	/**
	 * We use a row-wise storage scheme. The variable map stores
	 * the rows of the matrix on an as-needed basis. Each row is a 
	 * DoubleSparseVector. This choice was made so that matrix-vector
	 * products can be computed very quickly.
	 */
	protected final TIntObjectHashMap<DoubleSparseVector> map = new TIntObjectHashMap<DoubleSparseVector>();

	protected final int rows;
	protected final int columns;
	
	public DoubleSparseMatrix(int rows, int columns) {
		ExceptionUtility.verifyNonNegative(rows);
		ExceptionUtility.verifyNonNegative(columns);
		this.rows = rows;
		this.columns = columns;
	}
	
	@Override
	public int getNumColumns() {
		return this.columns;
	}

	@Override
	public int getNumRows() {
		return this.rows;
	}
	
	public void set(int row, int column, double value) {
		ExceptionUtility.verifyNonNegative(rows);
		ExceptionUtility.verifyNonNegative(columns);
		if (value == 0) {
			if (!this.map.contains(row)) {
				return;
			} else {
				this.map.remove(column);
			}
		} else {
			if (!this.map.contains(row)) {
				this.map.put(row, new DoubleSparseVector(this.columns));
			}
			this.map.get(row).set(column, value);
		}
	}
	
	public double get(int row, int column) {
		ExceptionUtility.verifyIndex(this.rows, row);
		ExceptionUtility.verifyIndex(this.columns, column);
		if (!this.map.contains(row)) {
			return 0;
		}
		return this.map.get(row).get(column);
	}
	
	public int getNumNonzeroElements() {
		return 0;
	}
	
	public double getDensity() {
		return ((double) this.getNumNonzeroElements()) / ((double) (rows * columns));
	}
	
	@Override
	public DoubleAbstractMatrixIterator iterator() {
		return new DoubleSparseMatrixIterator(this);
	}
	
	@Override
	public DoubleAbstractVector multiply(DoubleAbstractVector vector) {
		DoubleAbstractVector result = new DoubleSparseVector(this.rows);
		double innerProductValue = 0;
		for (TIntObjectIterator<DoubleSparseVector> iterator = this.map.iterator(); iterator.hasNext(); ) {
			iterator.advance();
			innerProductValue = iterator.value().innerProduct(vector);
			if (innerProductValue != 0) {
				result.set(iterator.key(), innerProductValue);
			}
		}
		
		return result;
	}

	@Override
	public DoubleAbstractMatrix transpose() {
		DoubleSparseMatrix result = new DoubleSparseMatrix(this.columns, this.rows);
		for (DoubleAbstractMatrixIterator iterator = this.iterator(); iterator.hasNext(); ) {
			iterator.advance();
			result.set(iterator.column(), iterator.row(), iterator.value());
		}
		return result;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		
		for (DoubleAbstractMatrixIterator iterator = this.iterator(); iterator.hasNext(); ) {
			iterator.advance();
			builder.append(iterator.row());
			builder.append(", ");
			builder.append(iterator.column());
			builder.append(": ");
			builder.append(iterator.value());
			builder.append("\n");
		}
		
		return builder.toString();
	}
}
