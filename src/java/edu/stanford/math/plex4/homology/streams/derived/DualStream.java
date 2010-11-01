/**
 * 
 */
package edu.stanford.math.plex4.homology.streams.derived;

import java.util.Comparator;
import java.util.Set;

import edu.stanford.math.plex4.homology.streams.interfaces.AbstractFilteredStream;
import edu.stanford.math.plex4.homology.streams.interfaces.DerivedStream;
import edu.stanford.math.primitivelib.array.ObjectArrayUtility;
import edu.stanford.math.primitivelib.autogen.pair.IntObjectPair;
import edu.stanford.math.primitivelib.utility.ReversedComparator;
import gnu.trove.THashMap;
import gnu.trove.THashSet;

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
			return ObjectArrayUtility.createArray(0, basisElement);
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
		THashMap<T, THashSet<IntObjectPair<T>>> coboundarySetMap = new THashMap<T, THashSet<IntObjectPair<T>>>();
		for (T a: this.forwardStream) {
			this.storageStructure.addElement(a, this.forwardStream.getFiltrationIndex(a));
			T[] boundary = this.forwardStream.getBoundary(a);
			int[] boundaryCoefficients = this.forwardStream.getBoundaryCoefficients(a);
			for (int i = 0; i < boundary.length; i++) {
				T boundaryElement = boundary[i];
				if (!coboundarySetMap.containsKey(boundaryElement)) {
					coboundarySetMap.put(boundaryElement, new THashSet<IntObjectPair<T>>());
				}
				coboundarySetMap.get(boundaryElement).add(new IntObjectPair<T>(boundaryCoefficients[i], a));
			}
		}
		
		Set<T> coboundaryKeys = coboundarySetMap.keySet();
		for (T element: coboundaryKeys) {
			THashSet<IntObjectPair<T>> coboundarySet = coboundarySetMap.get(element);
			T[] coboundaryArray = ObjectArrayUtility.createArray(coboundarySet.size(), element);
			int[] coboundaryCoefficients = new int[coboundarySet.size()];
			int i = 0;
			for (IntObjectPair<T> pair: coboundarySet) {
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
	
	public double getFiltrationValue(T basisElement) {
		return forwardStream.getFiltrationValue(basisElement);
	}
}
