package edu.stanford.math.plex_plus.math.matrix.impl.sparse;

import edu.stanford.math.plex_plus.math.matrix.interfaces.IntAbstractVector;
import edu.stanford.math.plex_plus.math.matrix.interfaces.IntAbstractVectorIterator;
import edu.stanford.math.plex_plus.utility.ExceptionUtility;
import gnu.trove.iterator.TIntIntIterator;
import gnu.trove.map.hash.TIntIntHashMap;

public class IntSparseVector implements IntAbstractVector {
	protected final TIntIntHashMap map = new TIntIntHashMap();
	protected final int size;
	
	public IntSparseVector(int size) {
		ExceptionUtility.verifyNonNegative(size);
		this.size = size;
	}
	
	public int getNumNonzeroElements() {
		return this.map.size();
	}
	
	public int getDensity() {
		return ((int) this.getNumNonzeroElements()) / ((int) (size));
	}
	
	public void set(int index, int value) {
		ExceptionUtility.verifyIndex(this.size, index);
		if (value == 0) {
			this.map.remove(index);
		}
		this.map.put(index, value);
	}
	
	public int get(int index) {
		ExceptionUtility.verifyIndex(this.size, index);
		return this.map.get(index);
	}
	
	public int innerProduct(IntSparseVector other) {
		ExceptionUtility.verifyEqual(this.size, other.size);
		int sum = 0;
		IntSparseVector smaller = (this.map.size() < other.map.size() ? this : other);
		IntSparseVector larger = (this.map.size() < other.map.size() ? other : this);
		
		for (TIntIntIterator iterator = smaller.map.iterator(); iterator.hasNext(); ) {
			iterator.advance();
			sum += iterator.value() * larger.get(iterator.key());
		}
		
		return sum;
	}
	
	public int innerProduct(int[] other) {
		ExceptionUtility.verifyEqual(this.size, other.length);
		int sum = 0;
		for (TIntIntIterator iterator = this.map.iterator(); iterator.hasNext(); ) {
			iterator.advance();
			sum += iterator.value() * other[iterator.key()];
		}
		return sum;
	}
	
	@Override
	public int innerProduct(IntAbstractVector other) {
		if (other instanceof IntSparseVector) {
			return this.innerProduct((IntSparseVector) other);
		}
		ExceptionUtility.verifyEqual(this.size, other.getLength());
		int sum = 0;
		for (TIntIntIterator iterator = this.map.iterator(); iterator.hasNext(); ) {
			iterator.advance();
			sum += iterator.value() * other.get(iterator.key());
		}
		return sum;
	}
	
	@Override
	public IntAbstractVectorIterator iterator() {
		return new IntSparseVectorIterator(this);
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		int index = 0;
		builder.append("[");
		for (IntAbstractVectorIterator iterator = this.iterator(); iterator.hasNext(); ) {
			iterator.advance();
			if (index > 0) {
				builder.append(", ");
			}
			builder.append(iterator.index());
			builder.append(": ");
			builder.append(iterator.value());
			index++;
		}
		builder.append("]");
		
		return builder.toString();
	}

	@Override
	public int getLength() {
		return this.size;
	}
}
