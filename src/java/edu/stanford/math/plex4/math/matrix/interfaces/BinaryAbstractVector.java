package edu.stanford.math.plex4.math.matrix.interfaces;

import edu.stanford.math.plex4.utility.ExceptionUtility;

public abstract class BinaryAbstractVector {
	public abstract BinaryAbstractVector like(int size);
	
	public abstract boolean get(int index);
	public abstract void set(int index, boolean value);
	public abstract void toggle(int index);
	public abstract int getLength();
	public abstract boolean innerProduct(BinaryAbstractVector other);
	
	public abstract BinaryAbstractVectorIterator iterator();
	public abstract String toString();
	
	public BinaryAbstractVector add(BinaryAbstractVector other) {
		ExceptionUtility.verifyEqual(this.getLength(), other.getLength());
		BinaryAbstractVector result = this.like(this.getLength());
		
		for (BinaryAbstractVectorIterator iterator = this.iterator(); iterator.hasNext(); ) {
			iterator.advance();
			result.set(iterator.index(), true);
		}
		
		for (BinaryAbstractVectorIterator iterator = other.iterator(); iterator.hasNext(); ) {
			iterator.advance();
			if (iterator.value()) {
				result.toggle(iterator.index());
			}
		}
		
		return result;
	}
	
	public BinaryAbstractVector subtract(BinaryAbstractVector other) {
		return this.add(other);
	}
	
	public BinaryAbstractVector multiply(boolean scalar) {
		BinaryAbstractVector result = this.like(this.getLength());
		if (scalar == false) {
			return result;
		}
		
		for (BinaryAbstractVectorIterator iterator = this.iterator(); iterator.hasNext(); ) {
			iterator.advance();
			result.set(iterator.index(), true);
		}
		
		return result;
	}
	
	public BinaryAbstractVector tensorProduct(BinaryAbstractVector other) {
		BinaryAbstractVector result = this.like(this.getLength() * other.getLength());
		
		int blockSize = other.getLength();
		
		for (BinaryAbstractVectorIterator blockIterator = this.iterator(); blockIterator.hasNext(); ) {
			blockIterator.advance();
			
			boolean blockMultiplier = blockIterator.value();
			int blockIndex = blockIterator.index() * blockSize;
			
			if (blockMultiplier == true) {
				for (BinaryAbstractVectorIterator indexIterator = other.iterator(); indexIterator.hasNext(); ) {
					indexIterator.advance();
					result.set(blockIndex + indexIterator.index(), indexIterator.value());
				}
			}
				
		}
		
		return result;
	}
}
