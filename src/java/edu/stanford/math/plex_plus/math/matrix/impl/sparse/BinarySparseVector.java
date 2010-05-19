package edu.stanford.math.plex_plus.math.matrix.impl.sparse;

import edu.stanford.math.plex_plus.math.matrix.interfaces.BinaryAbstractVector;
import edu.stanford.math.plex_plus.math.matrix.interfaces.BinaryAbstractVectorIterator;
import edu.stanford.math.plex_plus.utility.ExceptionUtility;
import gnu.trove.set.hash.TIntHashSet;

public class BinarySparseVector extends BinaryAbstractVector {
	protected final TIntHashSet indices = new TIntHashSet();
	protected final int size;
	
	public BinarySparseVector(int size) {
		ExceptionUtility.verifyNonNegative(size);
		this.size = size;
	}
	
	public BinaryAbstractVector like(int size) {
		return new BinarySparseVector(size);
	}
	
	@Override
	public boolean get(int index) {
		ExceptionUtility.verifyIndex(this.size, index);
		return this.indices.contains(index);
	}

	@Override
	public int getLength() {
		return this.size;
	}

	@Override
	public boolean innerProduct(BinaryAbstractVector other) {
		if (other instanceof BinarySparseVector) {
			return this.innerProduct((BinarySparseVector) other);
		}
		ExceptionUtility.verifyEqual(this.size, other.getLength());
		boolean sum = false;

		for (BinaryAbstractVectorIterator iterator = this.iterator(); iterator.hasNext(); ) {
			iterator.advance();
			sum = sum ^ other.get(iterator.index());
		}
		
		return sum;
	}
	
	public boolean innerProduct(BinarySparseVector other) {
		ExceptionUtility.verifyEqual(this.size, other.size);
		boolean sum = false;
		BinarySparseVector smaller = (this.indices.size() < other.indices.size() ? this : other);
		BinarySparseVector larger = (this.indices.size() < other.indices.size() ? other : this);
		
		for (BinaryAbstractVectorIterator iterator = smaller.iterator(); iterator.hasNext(); ) {
			iterator.advance();
			sum = sum ^ larger.indices.contains(iterator.index());
		}
		
		return sum;
	}

	@Override
	public BinaryAbstractVectorIterator iterator() {
		return new BinarySparseVectorIterator(this);
	}

	@Override
	public void set(int index, boolean value) {
		if (value) {
			this.indices.add(index);
		} else if (this.indices.contains(index)){
			this.indices.remove(index);
		}
	}

	@Override
	public void toggle(int index) {
		if (this.indices.contains(index)) {
			this.indices.remove(index);
		} else {
			this.indices.add(index);
		}
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		int index = 0;
		builder.append("[");
		for (BinaryAbstractVectorIterator iterator = this.iterator(); iterator.hasNext(); ) {
			iterator.advance();
			if (index > 0) {
				builder.append(", ");
			}
			builder.append(iterator.index());
			builder.append(": 1");
			index++;
		}
		builder.append("]");
		
		return builder.toString();
	}

}
