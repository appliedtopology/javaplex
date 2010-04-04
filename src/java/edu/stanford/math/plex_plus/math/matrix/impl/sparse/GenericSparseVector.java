package edu.stanford.math.plex_plus.math.matrix.impl.sparse;

import edu.stanford.math.plex_plus.math.matrix.interfaces.GenericAbstractVector;
import edu.stanford.math.plex_plus.math.matrix.interfaces.GenericAbstractVectorIterator;
import edu.stanford.math.plex_plus.utility.ExceptionUtility;
import gnu.trove.map.hash.TIntObjectHashMap;

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
	@Override
	public T get(int index) {
		ExceptionUtility.verifyIndex(this.length, index);
		return this.map.get(index);
	}
	
	/* (non-Javadoc)
	 * @see edu.stanford.math.plex_plus.math.matrix.generic.AbstractGenericVector#set(int, java.lang.Object)
	 */
	@Override
	public void set(int index, T value) {
		ExceptionUtility.verifyIndex(this.length, index);
		this.map.put(index, value);
	}

	/* (non-Javadoc)
	 * @see edu.stanford.math.plex_plus.math.matrix.generic.AbstractGenericVector#getLength()
	 */
	@Override
	public int getLength() {
		return this.length;
	}

	/* (non-Javadoc)
	 * @see edu.stanford.math.plex_plus.math.matrix.generic.AbstractGenericVector#iterator()
	 */
	@Override
	public GenericAbstractVectorIterator<T> iterator() {
		// TODO Auto-generated method stub
		return null;
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
