package edu.stanford.math.plex_plus.math.matrix.interfaces;

import edu.stanford.math.plex_plus.utility.ExceptionUtility;

public abstract class BinaryAbstractMatrix {
	public abstract BinaryAbstractMatrix like(int rows, int columns);
	
	public abstract int getNumRows();
	public abstract int getNumColumns();
	
	public abstract boolean get(int row, int column);
	public abstract void set(int row, int column, boolean value);
	public abstract void toggle(int row, int column);
	
	public abstract BinaryAbstractMatrixIterator iterator();
	
	public abstract BinaryAbstractVector multiply(BinaryAbstractVector vector);
	
	public abstract String toString();
	
	public BinaryAbstractMatrix add(BinaryAbstractMatrix other) {
		ExceptionUtility.verifyNonNull(other);
		ExceptionUtility.verifyEqual(this.getNumRows(), other.getNumRows());
		ExceptionUtility.verifyEqual(this.getNumColumns(), other.getNumColumns());
		
		BinaryAbstractMatrix result = this.like(this.getNumColumns(), this.getNumRows());
		
		for (BinaryAbstractMatrixIterator iterator = this.iterator(); iterator.hasNext(); ) {
			iterator.advance();
			result.set(iterator.row(), iterator.column(), iterator.value());
		}
		
		for (BinaryAbstractMatrixIterator iterator = other.iterator(); iterator.hasNext(); ) {
			iterator.advance();
			result.set(iterator.row(), iterator.column(), result.get(iterator.row(), iterator.column()) ^ iterator.value());
		}
		
		return result;
	}
	
	public BinaryAbstractMatrix subtract(BinaryAbstractMatrix other) {
		return this.add(other);
	}
	
	public BinaryAbstractMatrix transpose() {
		BinaryAbstractMatrix result = this.like(this.getNumColumns(), this.getNumRows());
		for (BinaryAbstractMatrixIterator iterator = this.iterator(); iterator.hasNext(); ) {
			iterator.advance();
			result.set(iterator.column(), iterator.row(), iterator.value());
		}
		return result;
	}
	
	public BinaryAbstractMatrix tensorProduct(BinaryAbstractMatrix other) {
		ExceptionUtility.verifyNonNull(other);
		BinaryAbstractMatrix result = this.like(this.getNumRows() * other.getNumRows(), this.getNumColumns() * other.getNumColumns());
		
		int blockWidth = other.getNumColumns();
		int blockHeight = other.getNumRows();
		
		for (BinaryAbstractMatrixIterator blockIterator = this.iterator(); blockIterator.hasNext(); ) {
			blockIterator.advance();
			
			boolean blockMultiplier = blockIterator.value();
			int blockRowStart = blockIterator.row() * blockHeight;
			int blockColStart = blockIterator.column() * blockWidth;
			
			if (blockMultiplier == true) {
				for (BinaryAbstractMatrixIterator indexIterator = other.iterator(); indexIterator.hasNext(); ) {
					indexIterator.advance();
					result.set(blockRowStart + indexIterator.row(), blockColStart + indexIterator.column(), indexIterator.value());
				}
			}
		}
		
		return result;
	}
}
