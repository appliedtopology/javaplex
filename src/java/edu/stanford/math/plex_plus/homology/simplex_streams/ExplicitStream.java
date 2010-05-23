/**
 * 
 */
package edu.stanford.math.plex_plus.homology.simplex_streams;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import edu.stanford.math.plex_plus.datastructures.pairs.DoubleGenericPair;
import edu.stanford.math.plex_plus.datastructures.pairs.DoubleGenericPairComparator;
import edu.stanford.math.plex_plus.datastructures.pairs.DoubleOrderedIterator;
import edu.stanford.math.plex_plus.utility.ExceptionUtility;
import gnu.trove.map.hash.TObjectDoubleHashMap;

/**
 * This class provides functionality for creating a user-defined filtration of
 * simplicial complexes. It implements the SimplexStream interface.
 * 
 * @author Andrew Tausz
 *
 */
public class ExplicitStream<T> implements SimplexStream<T> {
	
	/**
	 * This contains the simplicies of the complex ordered in order of filtration value.
	 */
	private final List<DoubleGenericPair<T>> simplices = new ArrayList<DoubleGenericPair<T>>();
	
	/**
	 * This hash map contains the filtration values of the simplicies in the complex.
	 */
	private final TObjectDoubleHashMap<T> filtrationValues = new TObjectDoubleHashMap<T>();
	
	/**
	 * Comparator which provides ordering of elements of the stream.
	 */
	private final Comparator<T> comparator;
	
	/**
	 * Boolean which indicates whether stream has been finalized or not
	 */
	private boolean isFinalized = false;
	
	/**
	 * Constructor which accepts a comparator for comparing the type T.
	 * This comparator defines the ordering on the type T. Thus the overall
	 * filtered objects are sorted first in order of filtration, and then by
	 * the ordering provided by the comparator. 
	 * 
	 * @param comparator a Comparator which provides an ordering of the objects
	 */
	public ExplicitStream(Comparator<T> comparator) {
		this.comparator = comparator;
	}
	
	/**
	 * This function adds a simplex to the complex with given filtration value.
	 * 
	 * @param vertices the vertices of the simplex to add
	 * @param filtrationIndex the filtration value of the simplex
	 */
	public void addSimplex(T simplex, double filtrationIndex) {
		ExceptionUtility.verifyNonNull(simplex);
		
		if (this.isFinalized) {
			throw new IllegalStateException("Cannot add objects to finalized stream.");
		}
		
		this.simplices.add(new DoubleGenericPair<T>(filtrationIndex, simplex));
		this.filtrationValues.put(simplex, filtrationIndex);
	}
	
	/**
	 * This function verifies the consistency of the filtration. 
	 * 
	 * @return true if the filtration is consistent, and false otherwise
	 */
	public boolean verifyConsistency() {
		// TODO: Complete
		return true;
	}	
	
	@Override
	public Iterator<T> iterator() {
		return new DoubleOrderedIterator<T>(this.simplices);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		
		for (DoubleGenericPair<T> simplex : this.simplices) {
			builder.append(simplex.toString());
			builder.append('\n');
		}
		
		return builder.toString();
	}

	@Override
	public double getFiltrationValue(T simplex) {
		return this.filtrationValues.get(simplex);
	}
	
	@Override
	public void finalizeStream() {
		Collections.sort(this.simplices, new DoubleGenericPairComparator<T>(this.comparator));
		this.isFinalized = true;
	}

	@Override
	public boolean isFinalized() {
		return this.isFinalized;
	}	
}
