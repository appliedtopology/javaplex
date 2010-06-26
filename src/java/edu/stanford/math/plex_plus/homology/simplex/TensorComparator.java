/**
 * 
 */
package edu.stanford.math.plex_plus.homology.simplex;

import java.util.Comparator;

import edu.stanford.math.plex_plus.utility.ExceptionUtility;

/**
 * @author Andris
 *
 */
public class TensorComparator<T extends ChainBasisElement> implements Comparator<ChainElementTensor<T>> {
	private final Comparator<T> baseComparator;
	
	public TensorComparator(Comparator<T> baseComparator) {
		this.baseComparator = baseComparator;
	}
	
	/* (non-Javadoc)
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	@Override
	public int compare(ChainElementTensor<T> o1, ChainElementTensor<T> o2) {
		ExceptionUtility.verifyNonNull(o1);
		ExceptionUtility.verifyNonNull(o2);
		int comparison = this.baseComparator.compare(o1.getFirst(), o2.getFirst());
		if (comparison != 0) {
			return comparison;
		} else {
			return this.baseComparator.compare(o1.getSecond(), o2.getSecond());
		}
	}

}
