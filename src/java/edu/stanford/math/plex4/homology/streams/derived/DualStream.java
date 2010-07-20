/**
 * 
 */
package edu.stanford.math.plex4.homology.streams.derived;

import java.util.Comparator;
import java.util.Set;

import edu.stanford.math.plex4.datastructures.ReversedComparator;
import edu.stanford.math.plex4.datastructures.pairs.IntGenericPair;
import edu.stanford.math.plex4.homology.streams.interfaces.AbstractFilteredStream;
import edu.stanford.math.plex4.homology.streams.interfaces.DerivedStream;
import edu.stanford.math.plex4.utility.ArrayUtility2;
import gnu.trove.map.hash.THashMap;
import gnu.trove.set.hash.THashSet;

/**
 * @author Andrew Tausz
 *
 */
public class DualStream<T> extends DerivedStream<T> {
	private final AbstractFilteredStream<T> forwardStream;
	private final THashMap<T, T[]> coboundaryMap = new THashMap<T, T[]>();
	private final THashMap<T, int[]> coboundaryCoefficientMap = new THashMap<T, int[]>();
	
	public DualStream(AbstractFilteredStream<T> forwardStream, Comparator<T> comparator) {
		super(new ReversedComparator<T>(comparator));
		this.forwardStream = forwardStream;
	}

	public T[] getBoundary(T basisElement) {
		if (this.coboundaryMap.containsKey(basisElement)) {
			return this.coboundaryMap.get(basisElement);
		} else {
			return ArrayUtility2.newGenericArray(0, basisElement);
		}
	}

	public int[] getBoundaryCoefficients(T basisElement) {
		if (this.coboundaryCoefficientMap.containsKey(basisElement)) {
			return this.coboundaryCoefficientMap.get(basisElement);
		} else {
			return new int[0];
		}
	}

	public int getDimension(T basisElement) {
		return forwardStream.getDimension(basisElement);
	}
	
	@Override
	protected void constructDerivedStream() {
		THashMap<T, THashSet<IntGenericPair<T>>> coboundarySetMap = new THashMap<T, THashSet<IntGenericPair<T>>>();
		for (T a: this.forwardStream) {
			this.storageStructure.addElement(a, this.forwardStream.getFiltrationValue(a));
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
			T[] coboundaryArray = ArrayUtility2.newGenericArray(coboundarySet.size(), element);
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
	}

	@Override
	protected void finalizeUnderlyingStreams() {
		if (!forwardStream.isFinalized()) {
			forwardStream.finalizeStream();
		}
	}
}
