package edu.stanford.math.plex_plus.datastructures.pairs;

import edu.stanford.math.plex_plus.utility.ExceptionUtility;
import edu.stanford.math.plex_plus.utility.MathUtility;

/**
 * This class implements a pair (i, j), where i and j are ints. 
 * It implements the Comparable interface with dictionary
 * ordering. Note that any instance of this class is immutable, and
 * implements value semantics.
 * 
 * @author Andrew Tausz
 *
 */
public class IntIntPair implements Comparable<IntIntPair> {
	
	/*
	 * Make the fields first and second final to maintain immutability.
	 */
	private final int first;
	private final int second;
	
	/**
	 * Constructor which initializes the pair.
	 * 
	 * @param first the value of the first component
	 * @param second the value of the second component
	 */
	public IntIntPair(int first, int second) {
		this.first = first;
		this.second = second;
	}
	
	/**
	 * Constructor which initializes from another DoubleLongPair.
	 * 
	 * @param pair the DoubleLongPair to initialize from
	 */
	public IntIntPair(IntIntPair pair) {
		ExceptionUtility.verifyNonNull(pair);
		this.first = pair.first;
		this.second = pair.second;
	}
	
	/**
	 * Get the first component.
	 * 
	 * @return the first component
	 */
	public int getFirst() {
		return this.first;
	}
	
	/**
	 * Get the second component.
	 * 
	 * @return the second component
	 */
	public int getSecond() {
		return this.second;
	}
	
	@Override
	public String toString() {
		return ("(" + first + ", " + second + ")");
	}
	
	public int compareTo(IntIntPair o) {
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
		result = prime * result + first;
		result = prime * result + second;
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
		if (!(obj instanceof IntIntPair))
			return false;
		IntIntPair other = (IntIntPair) obj;
		if (first != other.first)
			return false;
		if (second != other.second)
			return false;
		return true;
	}
}
