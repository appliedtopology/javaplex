/**
 * 
 */
package edu.stanford.math.plex_plus.homology.simplex_streams;

import java.util.Collections;
import java.util.Comparator;

import edu.stanford.math.plex_plus.datastructures.pairs.DoubleGenericPairComparator;
import edu.stanford.math.plex_plus.homology.simplex.ChainBasisElement;

/**
 * This class provides functionality for creating a user-defined filtration of
 * simplicial complexes. It implements the SimplexStream interface.
 * 
 * @author Andrew Tausz
 *
 */
public class ExplicitStream<T extends ChainBasisElement> extends BasicStream<T> {

	/**
	 * Constructor which accepts a comparator for comparing the type T.
	 * This comparator defines the ordering on the type T. Thus the overall
	 * filtered objects are sorted first in order of filtration, and then by
	 * the ordering provided by the comparator. 
	 * 
	 * @param comparator a Comparator which provides an ordering of the objects
	 */
	public ExplicitStream(Comparator<T> comparator) {
		super(comparator);
	}
	
	@Override
	public void finalizeStream() {
		Collections.sort(this.simplices, new DoubleGenericPairComparator<T>(this.basisComparator));
		this.isFinalized = true;
	}
	
	public void addSimplex(T simplex, double filtrationIndex) {
		this.addSimplexInternal(simplex, filtrationIndex);
	}
}
