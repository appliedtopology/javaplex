package edu.stanford.math.plex4.math.matrix.impl.sparse;

import edu.stanford.math.plex4.math.matrix.interfaces.IntAbstractMatrix;
import edu.stanford.math.plex4.math.matrix.interfaces.IntAbstractMatrixIterator;
import edu.stanford.math.plex4.math.matrix.interfaces.IntAbstractVector;
import edu.stanford.math.plex4.utility.ExceptionUtility;
import gnu.trove.iterator.TIntObjectIterator;
import gnu.trove.map.hash.TIntObjectHashMap;

public class IntSparseMatrix implements IntAbstractMatrix {
	
	/**
	 * We use a row-wise storage scheme. The variable map stores
	 * the rows of the matrix on an as-needed basis. Each row is a 
	 * IntSparseVector. This choice was made so that matrix-vector
	 * products can be computed very quickly.
	 */
	protected final TIntObjectHashMap<IntSparseVector> map = new TIntObjectHashMap<IntSparseVector>();

	protected final int rows;
	protected final int columns;
	
	public IntSparseMatrix(int rows, int columns) {
		ExceptionUtility.verifyNonNegative(rows);
		ExceptionUtility.verifyNonNegative(columns);
		this.rows = rows;
		this.columns = columns;
	}
	
	public int getNumColumns() {
		return this.columns;
	}

	public int getNumRows() {
		return this.rows;
	}
	
	public void set(int row, int column, int value) {
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
				this.map.put(row, new IntSparseVector(this.columns));
			}
			this.map.get(row).set(column, value);
		}
	}
	
	public int get(int row, int column) {
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
	
	public int getDensity() {
		return ((int) this.getNumNonzeroElements()) / ((int) (rows * columns));
	}
	
	public IntAbstractMatrixIterator iterator() {
		return new IntSparseMatrixIterator(this);
	}
	
	public int[][] getDenseForm() {
		int[][] array = new int[this.rows][this.columns];
		for (IntAbstractMatrixIterator iterator = this.iterator(); iterator.hasNext(); ) {
			iterator.advance();
			array[iterator.row()][iterator.column()] = iterator.value();
		}
		return array;
	}
	
	public IntAbstractVector multiply(IntAbstractVector vector) {
		IntAbstractVector result = new IntSparseVector(this.rows);
		int innerProductValue = 0;
		for (TIntObjectIterator<IntSparseVector> iterator = this.map.iterator(); iterator.hasNext(); ) {
			iterator.advance();
			innerProductValue = iterator.value().innerProduct(vector);
			if (innerProductValue != 0) {
				result.set(iterator.key(), innerProductValue);
			}
		}
		
		return result;
	}

	public IntAbstractMatrix transpose() {
		IntSparseMatrix result = new IntSparseMatrix(this.columns, this.rows);
		for (IntAbstractMatrixIterator iterator = this.iterator(); iterator.hasNext(); ) {
			iterator.advance();
			result.set(iterator.column(), iterator.row(), iterator.value());
		}
		return result;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		
		for (IntAbstractMatrixIterator iterator = this.iterator(); iterator.hasNext(); ) {
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
