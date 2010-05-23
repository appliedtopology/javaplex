package edu.stanford.math.plex_plus.deprecated;

import edu.stanford.math.plex_plus.utility.ExceptionUtility;

public class CoefficientObjectPair<K, T extends Comparable<T>> implements Comparable<CoefficientObjectPair<K, T>> {
	
	private final K coefficient;
	private final T object;
	
	CoefficientObjectPair(K coefficient, T object) {
		ExceptionUtility.verifyNonNull(coefficient);
		ExceptionUtility.verifyNonNull(object);
		this.object = object;
		this.coefficient = coefficient;
	}
	
	/**
	 * @return the coefficient
	 */
	public K getCoefficient() {
		return coefficient;
	}

	/**
	 * @return the object
	 */
	public T getObject() {
		return object;
	}
	
	@Override
	public int compareTo(CoefficientObjectPair<K, T> o) {
		ExceptionUtility.verifyNonNull(o);
		return this.object.compareTo(o.object);
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result	+ ((coefficient == null) ? 0 : coefficient.hashCode());
		result = prime * result + ((object == null) ? 0 : object.hashCode());
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
		if (!(obj instanceof CoefficientObjectPair<?, ?>))
			return false;
		CoefficientObjectPair<?, ?> other = (CoefficientObjectPair<?, ?>) obj;
		if (coefficient == null) {
			if (other.coefficient != null)
				return false;
		} else if (!coefficient.equals(other.coefficient))
			return false;
		if (object == null) {
			if (other.object != null)
				return false;
		} else if (!object.equals(other.object))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return (this.coefficient.toString() + " " + this.object.toString());
	}
}
