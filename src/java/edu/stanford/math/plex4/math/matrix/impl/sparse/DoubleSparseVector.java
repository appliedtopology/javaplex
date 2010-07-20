package edu.stanford.math.plex4.math.matrix.impl.sparse;

import edu.stanford.math.plex4.math.matrix.interfaces.DoubleAbstractVector;
import edu.stanford.math.plex4.math.matrix.interfaces.DoubleAbstractVectorIterator;
import edu.stanford.math.plex4.utility.ExceptionUtility;
import gnu.trove.iterator.TIntDoubleIterator;
import gnu.trove.map.hash.TIntDoubleHashMap;

public class DoubleSparseVector extends DoubleAbstractVector {
	protected final TIntDoubleHashMap map = new TIntDoubleHashMap();
	protected final int size;
	
	public DoubleSparseVector(int size) {
		ExceptionUtility.verifyNonNegative(size);
		this.size = size;
	}
	
	@Override
	public DoubleAbstractVector like(int size) {
		return new DoubleSparseVector(size);
	}
	
	public int getNumNonzeroElements() {
		return this.map.size();
	}
	
	public double getDensity() {
		return ((double) this.getNumNonzeroElements()) / ((double) (size));
	}
	
	public void set(int index, double value) {
		ExceptionUtility.verifyIndex(this.size, index);
		if (value == 0) {
			this.map.remove(index);
		}
		this.map.put(index, value);
	}
	
	public double get(int index) {
		ExceptionUtility.verifyIndex(this.size, index);
		return this.map.get(index);
	}
	
	public double innerProduct(DoubleSparseVector other) {
		ExceptionUtility.verifyEqual(this.size, other.size);
		double sum = 0;
		DoubleSparseVector smaller = (this.map.size() < other.map.size() ? this : other);
		DoubleSparseVector larger = (this.map.size() < other.map.size() ? other : this);
		
		for (TIntDoubleIterator iterator = smaller.map.iterator(); iterator.hasNext(); ) {
			iterator.advance();
			sum += iterator.value() * larger.get(iterator.key());
		}
		
		return sum;
	}
	
	public double innerProduct(double[] other) {
		ExceptionUtility.verifyEqual(this.size, other.length);
		double sum = 0;
		for (TIntDoubleIterator iterator = this.map.iterator(); iterator.hasNext(); ) {
			iterator.advance();
			sum += iterator.value() * other[iterator.key()];
		}
		return sum;
	}
	
	@Override
	public double innerProduct(DoubleAbstractVector other) {
		if (other instanceof DoubleSparseVector) {
			return this.innerProduct((DoubleSparseVector) other);
		}
		ExceptionUtility.verifyEqual(this.size, other.getLength());
		double sum = 0;
		for (TIntDoubleIterator iterator = this.map.iterator(); iterator.hasNext(); ) {
			iterator.advance();
			sum += iterator.value() * other.get(iterator.key());
		}
		return sum;
	}
	
	@Override
	public DoubleAbstractVectorIterator iterator() {
		return new DoubleSparseVectorIterator(this);
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		int index = 0;
		builder.append("[");
		for (DoubleAbstractVectorIterator iterator = this.iterator(); iterator.hasNext(); ) {
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
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((map == null) ? 0 : map.hashCode());
		result = prime * result + size;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DoubleSparseVector other = (DoubleSparseVector) obj;
		if (map == null) {
			if (other.map != null)
				return false;
		} else if (!map.equals(other.map))
			return false;
		if (size != other.size)
			return false;
		return true;
	}
}
