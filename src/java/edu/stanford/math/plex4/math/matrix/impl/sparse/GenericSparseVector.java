package edu.stanford.math.plex4.math.matrix.impl.sparse;

import edu.stanford.math.plex4.math.matrix.interfaces.GenericAbstractVector;
import edu.stanford.math.plex4.math.matrix.interfaces.GenericAbstractVectorIterator;
import edu.stanford.math.plex4.utility.ExceptionUtility;
import gnu.trove.TIntObjectHashMap;

/**
 * @author Andrew Tausz
 *
 * @param <T>
 */
public class GenericSparseVector<T> implements GenericAbstractVector<T> {
	protected final TIntObjectHashMap<T> map = new TIntObjectHashMap<T>();
	protected final int length;
	
	public GenericSparseVector(int length) {
		ExceptionUtility.verifyNonNegative(length);
		this.length = length;
	}
	
	/* (non-Javadoc)
	 * @see edu.stanford.math.plex_plus.math.matrix.generic.AbstractGenericVector#get(int)
	 */
	public T get(int index) {
		ExceptionUtility.verifyIndex(this.length, index);
		return this.map.get(index);
	}
	
	/* (non-Javadoc)
	 * @see edu.stanford.math.plex_plus.math.matrix.generic.AbstractGenericVector#set(int, java.lang.Object)
	 */
	public void set(int index, T value) {
		ExceptionUtility.verifyIndex(this.length, index);
		this.map.put(index, value);
	}

	/* (non-Javadoc)
	 * @see edu.stanford.math.plex_plus.math.matrix.generic.AbstractGenericVector#getLength()
	 */
	public int getLength() {
		return this.length;
	}

	/* (non-Javadoc)
	 * @see edu.stanford.math.plex_plus.math.matrix.generic.AbstractGenericVector#iterator()
	 */
	public GenericAbstractVectorIterator<T> iterator() {
		return new GenericSparseVectorIterator<T>(this);
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		int index = 0;
		builder.append("[");
		for (GenericAbstractVectorIterator<T> iterator = this.iterator(); iterator.hasNext(); ) {
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
}
