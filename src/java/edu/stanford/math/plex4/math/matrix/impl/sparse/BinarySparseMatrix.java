/**
 * 
 */
package edu.stanford.math.plex4.math.matrix.impl.sparse;

import edu.stanford.math.plex4.math.matrix.interfaces.BinaryAbstractMatrix;
import edu.stanford.math.plex4.math.matrix.interfaces.BinaryAbstractMatrixIterator;
import edu.stanford.math.plex4.math.matrix.interfaces.BinaryAbstractVector;
import edu.stanford.math.plex4.math.matrix.interfaces.GenericAbstractMatrix;
import edu.stanford.math.plex4.math.matrix.interfaces.GenericAbstractMatrixIterator;
import edu.stanford.math.plex4.utility.ExceptionUtility;
import gnu.trove.iterator.TIntObjectIterator;
import gnu.trove.map.hash.TIntObjectHashMap;

/**
 * @author atausz
 *
 */
public class BinarySparseMatrix extends BinaryAbstractMatrix {

	/**
	 * We use a row-wise storage scheme. The variable map stores
	 * the rows of the matrix on an as-needed basis. Each row is a 
	 * BinarySparseVector. This choice was made so that matrix-vector
	 * products can be computed very quickly.
	 */
	protected final TIntObjectHashMap<BinarySparseVector> map = new TIntObjectHashMap<BinarySparseVector>();
	protected final int rows;
	protected final int columns;
	
	public BinarySparseMatrix(int rows, int columns) {
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
	public BinarySparseMatrix(GenericAbstractMatrix<BinaryAbstractMatrix> blockMatrix) {
		// verify consistency of blocks
		int blockWidth = -1;
		int blockHeight = -1;
		for (GenericAbstractMatrixIterator<BinaryAbstractMatrix> blockIterator = blockMatrix.iterator(); blockIterator.hasNext(); ) {
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
		
		for (GenericAbstractMatrixIterator<BinaryAbstractMatrix> blockIterator = blockMatrix.iterator(); blockIterator.hasNext(); ) {
			blockIterator.advance();
			int startingRow = blockHeight * blockIterator.row();
			int startingColumn = blockWidth * blockIterator.column();
			
			for (BinaryAbstractMatrixIterator indexIterator = blockIterator.value().iterator(); indexIterator.hasNext(); ) {
				indexIterator.advance();
				this.set(startingRow + indexIterator.row(), startingColumn + indexIterator.column(), indexIterator.value());
			}
		}
	}
	
	public BinaryAbstractMatrix createIdentityMatrix(int size) {
		ExceptionUtility.verifyNonNegative(size);
		BinaryAbstractMatrix result = new BinarySparseMatrix(size, size);
		for (int i = 0; i < size; i++) {
			result.set(i, i, true);
		}
		return result;
	}
	
	/* (non-Javadoc)
	 * @see edu.stanford.math.plex_plus.math.matrix.interfaces.BinaryAbstractMatrix#get(int, int)
	 */
	@Override
	public boolean get(int row, int column) {
		ExceptionUtility.verifyIndex(this.rows, row);
		ExceptionUtility.verifyIndex(this.columns, column);
		if (!this.map.contains(row)) {
			return false;
		}
		return this.map.get(row).get(column);
	}

	/* (non-Javadoc)
	 * @see edu.stanford.math.plex_plus.math.matrix.interfaces.BinaryAbstractMatrix#getNumColumns()
	 */
	@Override
	public int getNumColumns() {
		return this.columns;
	}

	/* (non-Javadoc)
	 * @see edu.stanford.math.plex_plus.math.matrix.interfaces.BinaryAbstractMatrix#getNumRows()
	 */
	@Override
	public int getNumRows() {
		return this.rows;
	}

	/* (non-Javadoc)
	 * @see edu.stanford.math.plex_plus.math.matrix.interfaces.BinaryAbstractMatrix#iterator()
	 */
	@Override
	public BinaryAbstractMatrixIterator iterator() {
		return new BinarySparseMatrixIterator(this);
	}

	/* (non-Javadoc)
	 * @see edu.stanford.math.plex_plus.math.matrix.interfaces.BinaryAbstractMatrix#like(int, int)
	 */
	@Override
	public BinaryAbstractMatrix like(int rows, int columns) {
		return new BinarySparseMatrix(rows, columns);
	}

	/* (non-Javadoc)
	 * @see edu.stanford.math.plex_plus.math.matrix.interfaces.BinaryAbstractMatrix#multiply(edu.stanford.math.plex_plus.math.matrix.interfaces.BinaryAbstractVector)
	 */
	@Override
	public BinaryAbstractVector multiply(BinaryAbstractVector vector) {
		BinaryAbstractVector result = new BinarySparseVector(this.rows);
		boolean innerProductValue = false;
		for (TIntObjectIterator<BinarySparseVector> iterator = this.map.iterator(); iterator.hasNext(); ) {
			iterator.advance();
			innerProductValue = iterator.value().innerProduct(vector);
			if (innerProductValue) {
				result.set(iterator.key(), innerProductValue);
			}
		}
		
		return result;
	}

	/* (non-Javadoc)
	 * @see edu.stanford.math.plex_plus.math.matrix.interfaces.BinaryAbstractMatrix#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		
		for (BinaryAbstractMatrixIterator iterator = this.iterator(); iterator.hasNext(); ) {
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
	public void set(int row, int column, boolean value) {
		ExceptionUtility.verifyNonNegative(rows);
		ExceptionUtility.verifyNonNegative(columns);
		if (!value) {
			if (!this.map.contains(row)) {
				return;
			} else {
				this.map.remove(column);
			}
		} else {
			if (!this.map.contains(row)) {
				this.map.put(row, new BinarySparseVector(this.columns));
			}
			this.map.get(row).set(column, value);
		}
	}

	@Override
	public void toggle(int row, int column) {
		ExceptionUtility.verifyNonNegative(rows);
		ExceptionUtility.verifyNonNegative(columns);
		if (!this.map.contains(row)) {
			this.map.put(row, new BinarySparseVector(this.columns));
			this.map.get(row).set(column, true);
		} else {
			this.map.get(row).toggle(column);
		}
	}
	
	/**
	 * This function computes the product A * B^T, ie. the product of this with the
	 * transpose of the argument. 
	 * 
	 * @param other
	 * @return
	 */
	public BinaryAbstractMatrix multiplyTranspose(BinarySparseMatrix other) {
		ExceptionUtility.verifyEqual(this.columns, other.columns);
		BinaryAbstractMatrix result = this.like(this.getNumRows(), other.getNumRows());
		
		for (TIntObjectIterator<BinarySparseVector> thisIterator = this.map.iterator(); thisIterator.hasNext(); ) {
			thisIterator.advance();
			
			for (TIntObjectIterator<BinarySparseVector> otherIterator = other.map.iterator(); otherIterator.hasNext(); ) {
				otherIterator.advance();
				
				result.set(thisIterator.key(), otherIterator.key(), thisIterator.value().innerProduct(otherIterator.value()));
			}
		}
		
		return result;
	}	

}
