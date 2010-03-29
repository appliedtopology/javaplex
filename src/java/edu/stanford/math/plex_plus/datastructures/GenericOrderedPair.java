package edu.stanford.math.plex_plus.datastructures;

import edu.stanford.math.plex_plus.utility.ExceptionUtility;

/**
 * This class implements an ordered pair with the standard 
 * dictionary ordering.
 * 
 * @author Andrew Tausz
 *
 * @param <T> the type of the first component
 * @param <U> the type of the second component
 */
public class GenericOrderedPair<T extends Comparable<T>, U extends Comparable<U>> extends GenericPair<T, U> implements Comparable<GenericOrderedPair<T,U>> {

	public GenericOrderedPair(T first, U second) {
		super(first, second);
	}

	@Override
	public int compareTo(GenericOrderedPair<T, U> o) {
		ExceptionUtility.verifyNonNull(o);
		int comparison = this.first.compareTo(o.first);
		if (comparison != 0) {
			return comparison;
		} else {
			return this.second.compareTo(o.second);
		}
	}

}
