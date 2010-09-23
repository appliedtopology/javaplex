package edu.stanford.math.plex4.math.matrix.impl.sparse;

import edu.stanford.math.plex4.math.matrix.interfaces.DoubleAbstractMatrix;
import edu.stanford.math.plex4.math.matrix.interfaces.DoubleAbstractMatrixIterator;
import edu.stanford.math.plex4.math.matrix.interfaces.DoubleAbstractVector;
import edu.stanford.math.plex4.math.matrix.interfaces.GenericAbstractMatrix;
import edu.stanford.math.plex4.math.matrix.interfaces.GenericAbstractMatrixIterator;
import edu.stanford.math.plex4.utility.ExceptionUtility;
import gnu.trove.TIntObjectHashMap;
import gnu.trove.TIntObjectIterator;

public class DoubleSparseMatrix extends DoubleAbstractMatrix {
	
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
	
	/**
	 * This constructor initializes the matrix from a block matrix.
	 * 
	 * @param blockMatrix
	 */
	public DoubleSparseMatrix(GenericAbstractMatrix<DoubleAbstractMatrix> blockMatrix) {
		// verify consistency of blocks
		int blockWidth = -1;
		int blockHeight = -1;
		for (GenericAbstractMatrixIterator<DoubleAbstractMatrix> blockIterator = blockMatrix.iterator(); blockIterator.hasNext(); ) {
			blockIterator.advance();
			if (blockWidth == -1 || blockHeight == -1) {
				blockWidth = blockIterator.value().getNumColumns();
				blockHeight = blockIterator.value().getNumRows();
			} else if (blockWidth != blockIterator.value().getNumColumns() || blockHeight != blockIterator.value().getNumRows()) {
				throw new IllegalArgumentException("Block matrix must have same-sized blocks");
			}
		}
		
		this.rows = blockHeight * blockMatrix.getNumRows();
		this.columns = blockWidth * blockMatrix.getNumColumns();
		
		for (GenericAbstractMatrixIterator<DoubleAbstractMatrix> blockIterator = blockMatrix.iterator(); blockIterator.hasNext(); ) {
			blockIterator.advance();
			int startingRow = blockHeight * blockIterator.row();
			int startingColumn = blockWidth * blockIterator.column();
			
			for (DoubleAbstractMatrixIterator indexIterator = blockIterator.value().iterator(); indexIterator.hasNext(); ) {
				indexIterator.advance();
				this.set(startingRow + indexIterator.row(), startingColumn + indexIterator.column(), indexIterator.value());
			}
		}
	}
	
	public DoubleAbstractMatrix createIdentityMatrix(int size) {
		ExceptionUtility.verifyNonNegative(size);
		DoubleAbstractMatrix result = new DoubleSparseMatrix(size, size);
		for (int i = 0; i < size; i++) {
			result.set(i, i, 1);
		}
		return result;
	}
	
	@Override
	public DoubleAbstractMatrix like(int rows, int columns) {
		return new DoubleSparseMatrix(rows, columns);
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
	
	/**
	 * This function computes the product A * B^T, ie. the product of this with the
	 * transpose of the argument. 
	 * 
	 * @param other
	 * @return
	 */
	public DoubleAbstractMatrix multiplyTranspose(DoubleSparseMatrix other) {
		ExceptionUtility.verifyEqual(this.columns, other.columns);
		DoubleAbstractMatrix result = this.like(this.getNumRows(), other.getNumRows());
		
		for (TIntObjectIterator<DoubleSparseVector> thisIterator = this.map.iterator(); thisIterator.hasNext(); ) {
			thisIterator.advance();
			
			for (TIntObjectIterator<DoubleSparseVector> otherIterator = other.map.iterator(); otherIterator.hasNext(); ) {
				otherIterator.advance();
				
				result.set(thisIterator.key(), otherIterator.key(), thisIterator.value().innerProduct(otherIterator.value()));
			}
		}
		
		return result;
	}
}
