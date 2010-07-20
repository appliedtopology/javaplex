/**
 * 
 */
package edu.stanford.math.plex4.datastructures;

import java.util.Comparator;

import edu.stanford.math.plex4.utility.ExceptionUtility;

/**
 * @author Andris
 *
 */
public class ReversedComparator<T> implements Comparator<T> {
	private final Comparator<T> forwardComparator;
	
	public ReversedComparator(Comparator<T> forwardComparator) {
		ExceptionUtility.verifyNonNull(forwardComparator);
		this.forwardComparator = forwardComparator;
	}
	
	/* (non-Javadoc)
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	public int compare(T o1, T o2) {
		return -this.forwardComparator.compare(o1, o2);
	}

}
