/**
 * 
 */
package edu.stanford.math.plex_plus.homology.simplex_streams;

import java.lang.reflect.Array;
import java.util.Collections;
import java.util.Comparator;
import java.util.Set;

import edu.stanford.math.plex_plus.datastructures.ReversedComparator;
import edu.stanford.math.plex_plus.datastructures.pairs.DoubleGenericPair;
import edu.stanford.math.plex_plus.datastructures.pairs.DoubleGenericPairComparator;
import edu.stanford.math.plex_plus.datastructures.pairs.IntGenericPair;
import edu.stanford.math.plex_plus.homology.simplex.ChainBasisElement;
import gnu.trove.map.hash.THashMap;
import gnu.trove.set.hash.THashSet;

/**
 * @author Andrew Tausz
 *
 */
public class DualStream<T extends ChainBasisElement> extends BasicStream<T> {
	private final SimplexStream<T> forwardStream;
	private final THashMap<T, T[]> coboundaryMap = new THashMap<T, T[]>();
	private final THashMap<T, int[]> coboundaryCoefficientMap = new THashMap<T, int[]>();
	
	public DualStream(SimplexStream<T> forwardStream, Comparator<T> comparator) {
		super(new ReversedComparator<T>(comparator));
		this.forwardStream = forwardStream;
	}
	
	@Override
	public T[] getBoundary(T simplex) {
		return this.coboundaryMap.get(simplex);
	}
	
	@Override
	public int[] getBoundaryCoefficients(T simplex) {
		return this.coboundaryCoefficientMap.get(simplex);
	}
	
	@Override
	public Comparator<T> getBasisComparator() {
		return this.basisComparator;
	}

	/* (non-Javadoc)
	 * @see edu.stanford.math.plex_plus.homology.simplex_streams.BasicStream#finalizeStream()
	 */
	@Override
	public void finalizeStream() {
		if (!this.forwardStream.isFinalized()) {
			forwardStream.finalizeStream();
		}
		
		this.constructComplex();
		
		this.isFinalized = true;
	}

	private void constructComplex() {
		THashMap<T, THashSet<IntGenericPair<T>>> coboundarySetMap = new THashMap<T, THashSet<IntGenericPair<T>>>();
		for (T a: this.forwardStream) {
			this.addSimplexInternal(a, this.forwardStream.getFiltrationValue(a));
			T[] boundary = this.forwardStream.getBoundary(a);
			int[] boundaryCoefficients = this.forwardStream.getBoundaryCoefficients(a);
			for (int i = 0; i < boundary.length; i++) {
				T boundaryElement = boundary[i];
				if (!coboundarySetMap.containsKey(boundaryElement)) {
					coboundarySetMap.put(boundaryElement, new THashSet<IntGenericPair<T>>());
				}
				coboundarySetMap.get(boundaryElement).add(new IntGenericPair<T>(boundaryCoefficients[i], a));
			}
		}
		
		Set<T> coboundaryKeys = coboundarySetMap.keySet();
		for (T element: coboundaryKeys) {
			THashSet<IntGenericPair<T>> coboundarySet = coboundarySetMap.get(element);
			T[] coboundaryArray = (T[]) Array.newInstance(element.getClass(), coboundarySet.size());
			int[] coboundaryCoefficients = new int[coboundarySet.size()];
			int i = 0;
			for (IntGenericPair<T> pair: coboundarySet) {
				coboundaryArray[i] = pair.getSecond();
				coboundaryCoefficients[i] = pair.getFirst();
				i++;
			}
			this.coboundaryMap.put(element, coboundaryArray);
			this.coboundaryCoefficientMap.put(element, coboundaryCoefficients);
		}
		
		Comparator<DoubleGenericPair<T>> filteredComparator = new DoubleGenericPairComparator<T>(this.getBasisComparator());
		Collections.sort(this.simplices, filteredComparator);
	}
}
