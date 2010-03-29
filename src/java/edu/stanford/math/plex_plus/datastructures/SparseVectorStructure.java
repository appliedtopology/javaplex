/**
 * 
 */
package edu.stanford.math.plex_plus.datastructures;

import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import edu.stanford.math.plex_plus.utility.ExceptionUtility;

/**
 * This class implements a sparse vector data structure. It is used
 * for storing  
 * 
 * @author Andrew Tausz
 *
 * @param <T>
 */
public class SparseVectorStructure<T> {
	protected SortedMap<Integer, T> map = new TreeMap<Integer, T>();
	protected final int size;
	
	public SparseVectorStructure(int size) {
		ExceptionUtility.verifyNonNegative(size);
		this.size = size;
	}
	
	protected SparseVectorStructure(int size, Map<Integer, T> map) {
		ExceptionUtility.verifyNonNegative(size);
		this.size = size;
		this.map.putAll(map);
	}
	
	public int getNumNonzeroElements() {
		return this.map.size();
	}
	
	public double getDensity() {
		return ((double) this.getNumNonzeroElements()) / ((double) (size));
	}
	
	public void set(int index, T value) {
		ExceptionUtility.verifyIndex(this.size, index);
		this.map.put(index, value);
	}
	
	public void get(int index) {
		ExceptionUtility.verifyIndex(this.size, index);
		this.map.get(index);
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((map == null) ? 0 : map.hashCode());
		result = prime * result + size;
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof SparseVectorStructure))
			return false;
		SparseVectorStructure<?> other = (SparseVectorStructure<?>) obj;
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
