package edu.stanford.math.plex_plus.math.matrix.interfaces;

import edu.stanford.math.plex_plus.utility.ExceptionUtility;


public abstract class DoubleAbstractMatrix {
	public abstract DoubleAbstractMatrix like(int rows, int columns);
	
	public abstract int getNumRows();
	public abstract int getNumColumns();
	
	public abstract double get(int row, int column);
	public abstract void set(int row, int column, double value);
	
	public abstract DoubleAbstractMatrixIterator iterator();
	
	public abstract DoubleAbstractVector multiply(DoubleAbstractVector vector);
	
	public abstract String toString();
	
	public DoubleAbstractMatrix add(DoubleAbstractMatrix other) {
		ExceptionUtility.verifyNonNull(other);
		ExceptionUtility.verifyEqual(this.getNumRows(), other.getNumRows());
		ExceptionUtility.verifyEqual(this.getNumColumns(), other.getNumColumns());
		
		DoubleAbstractMatrix result = this.like(this.getNumColumns(), this.getNumRows());
		
		for (DoubleAbstractMatrixIterator iterator = this.iterator(); iterator.hasNext(); ) {
			iterator.advance();
			result.set(iterator.row(), iterator.column(), iterator.value());
		}
		
		for (DoubleAbstractMatrixIterator iterator = other.iterator(); iterator.hasNext(); ) {
			iterator.advance();
			result.set(iterator.row(), iterator.column(), result.get(iterator.row(), iterator.column()) + iterator.value());
		}
		
		return result;
	}
	
	public DoubleAbstractMatrix subtract(DoubleAbstractMatrix other) {
		ExceptionUtility.verifyNonNull(other);
		ExceptionUtility.verifyEqual(this.getNumRows(), other.getNumRows());
		ExceptionUtility.verifyEqual(this.getNumColumns(), other.getNumColumns());
		
		DoubleAbstractMatrix result = this.like(this.getNumColumns(), this.getNumRows());
		
		for (DoubleAbstractMatrixIterator iterator = this.iterator(); iterator.hasNext(); ) {
			iterator.advance();
			result.set(iterator.row(), iterator.column(), iterator.value());
		}
		
		for (DoubleAbstractMatrixIterator iterator = other.iterator(); iterator.hasNext(); ) {
			iterator.advance();
			result.set(iterator.row(), iterator.column(), result.get(iterator.row(), iterator.column()) - iterator.value());
		}
		
		return result;
	}
	
	public DoubleAbstractMatrix multiply(double scalar) {
		DoubleAbstractMatrix result = this.like(this.getNumColumns(), this.getNumRows());
		
		if (scalar == 0) {
			return result;
		}
		
		for (DoubleAbstractMatrixIterator iterator = this.iterator(); iterator.hasNext(); ) {
			iterator.advance();
			result.set(iterator.row(), iterator.column(), scalar * iterator.value());
		}
		
		return result;
	}
	
	public DoubleAbstractMatrix transpose() {
		DoubleAbstractMatrix result = this.like(this.getNumColumns(), this.getNumRows());
		for (DoubleAbstractMatrixIterator iterator = this.iterator(); iterator.hasNext(); ) {
			iterator.advance();
			result.set(iterator.column(), iterator.row(), iterator.value());
		}
		return result;
	}
	
	public DoubleAbstractMatrix tensorProduct(DoubleAbstractMatrix other) {
		ExceptionUtility.verifyNonNull(other);
		DoubleAbstractMatrix result = this.like(this.getNumRows() * other.getNumRows(), this.getNumColumns() * other.getNumColumns());
		
		int blockWidth = other.getNumColumns();
		int blockHeight = other.getNumRows();
		
		for (DoubleAbstractMatrixIterator blockIterator = this.iterator(); blockIterator.hasNext(); ) {
			blockIterator.advance();
			
			double blockMultiplier = blockIterator.value();
			int blockRowStart = blockIterator.row() * blockHeight;
			int blockColStart = blockIterator.column() * blockWidth;
			
			if (blockMultiplier != 0) {
				for (DoubleAbstractMatrixIterator indexIterator = other.iterator(); indexIterator.hasNext(); ) {
					indexIterator.advance();
					result.set(blockRowStart + indexIterator.row(), blockColStart + indexIterator.column(), blockMultiplier * indexIterator.value());
				}
			}
		}
		
		return result;
	}
}
