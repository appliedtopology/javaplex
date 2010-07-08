package edu.stanford.math.plex_plus.datastructures.pairs;

import edu.stanford.math.plex_plus.utility.ExceptionUtility;
import edu.stanford.math.plex_plus.utility.MathUtility;

/**
 * This class implements a pair (d, l), where d is a double and l 
 * is a long. It implements the Comparable interface with dictionary
 * ordering. Note that any instance of this class is immutable, and
 * implements value semantics.
 * 
 * @author Andrew Tausz
 *
 */
public class DoubleLongPair implements Comparable<DoubleLongPair> {
	/*
	 * Make the fields first and second final to maintain immutability.
	 */
	private final double first;
	private final long second;
	
	/**
	 * Constructor which initializes the pair.
	 * 
	 * @param first the value of the first component
	 * @param second the value of the second component
	 */
	public DoubleLongPair(double first, long second) {
		this.first = first;
		this.second = second;
	}
	
	/**
	 * Constructor which initializes from another DoubleLongPair.
	 * 
	 * @param pair the DoubleLongPair to initialize from
	 */
	public DoubleLongPair(DoubleLongPair pair) {
		ExceptionUtility.verifyNonNull(pair);
		this.first = pair.first;
		this.second = pair.second;
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
	public long getSecond() {
		return this.second;
	}
	
	@Override
	public String toString() {
		return ("(" + first + ", " + second + ")");
	}
	
	public int compareTo(DoubleLongPair o) {
		ExceptionUtility.verifyNonNull(o);
		int comparison = MathUtility.signum(this.first - o.first);
		if (comparison != 0) {
			return comparison;
		} else {
			return MathUtility.signum(this.second - o.second);
		}
	}
	
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
		result = prime * result + (int) (second ^ (second >>> 32));
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
		if (getClass() != obj.getClass())
			return false;
		DoubleLongPair other = (DoubleLongPair) obj;
		if (Double.doubleToLongBits(first) != Double
				.doubleToLongBits(other.first))
			return false;
		if (second != other.second)
			return false;
		return true;
	}
}
