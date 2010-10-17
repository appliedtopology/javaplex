package edu.stanford.math.plex4.datastructures.pairs;

import java.util.Comparator;

import edu.stanford.math.plex4.utility.MathUtility;

public class IntGenericPairComparator<T> implements Comparator<IntGenericPair<T>> {

	private final Comparator<T> genericComparator;
	
	public IntGenericPairComparator(Comparator<T> genericComparator) {
		this.genericComparator = genericComparator;
	}
	
	/* (non-Javadoc)
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	public int compare(IntGenericPair<T> o1, IntGenericPair<T> o2) {
		int comparison = MathUtility.signum(o1.getFirst() - o2.getFirst());
		if (comparison != 0) {
			return comparison;
		} else {
			return this.genericComparator.compare(o1.getSecond(), o2.getSecond());
		}
	}
}
