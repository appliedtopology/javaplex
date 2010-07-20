/**
 * 
 */
package edu.stanford.math.plex4.datastructures.pairs;


/**
 * @author atausz
 *
 */
public class DoubleGenericPair<T> {
	private final double first;
	private final T second;
	
	public DoubleGenericPair(double first, T second) {
		this.first = first;
		this.second = second;
	}
	
	/**
	 * Get the first component.
	 * 
	 * @return the first component
	 */
	public double getFirst() {
		return this.first;
	}
	
	/**
	 * Get the second component.
	 * 
	 * @return the second component
	 */
	public T getSecond() {
		return this.second;
	}
	
	@Override
	public String toString() {
		return ("(" + first + ", " + second.toString() + ")");
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	/*
	@Override
	public int compareTo(DoubleGenericPair<T> o) {
		ExceptionUtility.verifyNonNull(o);
		int comparison = MathUtility.signum(this.first - o.first);
		if (comparison != 0) {
			return comparison;
		} else {
			return this.second.compareTo(o.second);
		}
	}
	*/

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(first);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((second == null) ? 0 : second.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof DoubleGenericPair<?>)) {
			return false;
		}
		DoubleGenericPair<?> other = (DoubleGenericPair<?>) obj;
		if (Double.doubleToLongBits(first) != Double
				.doubleToLongBits(other.first)) {
			return false;
		}
		if (second == null) {
			if (other.second != null) {
				return false;
			}
		} else if (!second.equals(other.second)) {
			return false;
		}
		return true;
	}
}
