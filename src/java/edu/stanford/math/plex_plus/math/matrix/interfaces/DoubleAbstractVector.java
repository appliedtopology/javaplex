package edu.stanford.math.plex_plus.math.matrix.interfaces;

import edu.stanford.math.plex_plus.utility.ExceptionUtility;


public abstract class DoubleAbstractVector {
	public abstract DoubleAbstractVector like(int size);
	
	public abstract double get(int index);
	public abstract void set(int index, double value);
	public abstract int getLength();
	public abstract double innerProduct(DoubleAbstractVector other);
	
	public abstract DoubleAbstractVectorIterator iterator();
	public abstract String toString();
	
	public DoubleAbstractVector add(DoubleAbstractVector other) {
		ExceptionUtility.verifyNonNull(other);
		ExceptionUtility.verifyEqual(this.getLength(), other.getLength());
		
		DoubleAbstractVector result = this.like(this.getLength());
		
		for (DoubleAbstractVectorIterator iterator = this.iterator(); iterator.hasNext(); ) {
			iterator.advance();
			result.set(iterator.index(), iterator.value());
		}
		
		for (DoubleAbstractVectorIterator iterator = other.iterator(); iterator.hasNext(); ) {
			iterator.advance();
			result.set(iterator.index(), result.get(iterator.index()) + iterator.value());
		}
		
		return result;
	}
	
	public DoubleAbstractVector subtract(DoubleAbstractVector other) {
		ExceptionUtility.verifyNonNull(other);
		ExceptionUtility.verifyEqual(this.getLength(), other.getLength());
		
		DoubleAbstractVector result = this.like(this.getLength());
		
		for (DoubleAbstractVectorIterator iterator = this.iterator(); iterator.hasNext(); ) {
			iterator.advance();
			result.set(iterator.index(), iterator.value());
		}
		
		for (DoubleAbstractVectorIterator iterator = other.iterator(); iterator.hasNext(); ) {
			iterator.advance();
			result.set(iterator.index(), result.get(iterator.index()) - iterator.value());
		}
		
		return result;
	}
	
	public DoubleAbstractVector multiply(double scalar) {
		DoubleAbstractVector result = this.like(this.getLength());
		if (scalar == 0) {
			return result;
		}
		
		for (DoubleAbstractVectorIterator iterator = this.iterator(); iterator.hasNext(); ) {
			iterator.advance();
			result.set(iterator.index(), scalar * iterator.value());
		}
		
		return result;
	}
	
	public DoubleAbstractVector tensorProduct(DoubleAbstractVector other) {
		DoubleAbstractVector result = this.like(this.getLength() * other.getLength());
		
		int blockSize = other.getLength();
		
		for (DoubleAbstractVectorIterator blockIterator = this.iterator(); blockIterator.hasNext(); ) {
			blockIterator.advance();
			
			double blockMultiplier = blockIterator.value();
			int blockIndex = blockIterator.index() * blockSize;
			
			if (blockMultiplier != 0) {
				for (DoubleAbstractVectorIterator indexIterator = other.iterator(); indexIterator.hasNext(); ) {
					indexIterator.advance();
					result.set(blockIndex + indexIterator.index(), blockMultiplier * indexIterator.value());
				}
			}
				
		}
		
		return result;
	}
}
