/**
 * 
 */
package edu.stanford.math.plex4.streams.derived;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import edu.stanford.math.plex4.homology.barcodes.Interval;
import edu.stanford.math.plex4.homology.barcodes.PersistenceInvariantDescriptor;
import edu.stanford.math.plex4.streams.interfaces.AbstractFilteredStream;
import edu.stanford.math.primitivelib.array.ObjectArrayUtility;
import edu.stanford.math.primitivelib.autogen.pair.IntObjectPair;
import edu.stanford.math.primitivelib.collections.utility.ReversedComparator;
import gnu.trove.THashMap;

/**
 * This class implements the dual cochain complex from a given chain complex. It 
 * transforms the boundary operators into coboundary operators.
 * 
 * @author Andrew Tausz
 *
 * @param <T>
 */
public class DualStream<T> implements AbstractFilteredStream<T> {
	
	/**
	 * This is the chain complex.
	 */
	private final AbstractFilteredStream<T> forwardStream;
	
	/**
	 * This map contains the coboundaries of the elements in the stream. Unfortunately,
	 * there is no general way of getting around this.
	 */
	private final THashMap<T, T[]> coboundaryMap = new THashMap<T, T[]>();
	
	/**
	 * This map contains the coefficients of the coboundaries of the elements in the stream.
	 */
	private final THashMap<T, int[]> coboundaryCoefficientMap = new THashMap<T, int[]>();
	
	/**
	 * This stores the elements in the stream in reversed order.
	 */
	private final List<T> reversedElements = new ArrayList<T>();
	
	/**
	 * This constructor initializes the dualized stream from the regular (forward stream).
	 * 
	 * @param forwardStream the forward stream
	 */
	public DualStream(AbstractFilteredStream<T> forwardStream) {
		this.forwardStream = forwardStream;
		this.constructCoboundaries();
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Iterable#iterator()
	 */
	public Iterator<T> iterator() {
		return this.reversedElements.iterator();
	}

	/* (non-Javadoc)
	 * @see edu.stanford.math.plex4.homology.streams.interfaces.AbstractFilteredStream#getFiltrationIndex(java.lang.Object)
	 */
	public int getFiltrationIndex(T basisElement) {
		return this.forwardStream.getFiltrationIndex(basisElement);
	}

	/* (non-Javadoc)
	 * @see edu.stanford.math.plex4.homology.streams.interfaces.AbstractFilteredStream#getBoundary(java.lang.Object)
	 */
	public T[] getBoundary(T basisElement) {
		if (this.coboundaryMap.containsKey(basisElement)) {
			return this.coboundaryMap.get(basisElement);
		} else {
			return ObjectArrayUtility.createArray(0, basisElement);
		}
	}

	/* (non-Javadoc)
	 * @see edu.stanford.math.plex4.homology.streams.interfaces.AbstractFilteredStream#getBoundaryCoefficients(java.lang.Object)
	 */
	public int[] getBoundaryCoefficients(T basisElement) {
		if (this.coboundaryCoefficientMap.containsKey(basisElement)) {
			return this.coboundaryCoefficientMap.get(basisElement);
		} else {
			return new int[0];
		}
	}

	/* (non-Javadoc)
	 * @see edu.stanford.math.plex4.homology.streams.interfaces.AbstractFilteredStream#getDimension(java.lang.Object)
	 */
	public int getDimension(T basisElement) {
		return this.forwardStream.getDimension(basisElement);
	}

	/* (non-Javadoc)
	 * @see edu.stanford.math.plex4.homology.streams.interfaces.AbstractFilteredStream#finalizeStream()
	 */
	public void finalizeStream() {
		if (!this.forwardStream.isFinalized()) {
			this.forwardStream.finalizeStream();
		}
	}

	/* (non-Javadoc)
	 * @see edu.stanford.math.plex4.homology.streams.interfaces.AbstractFilteredStream#isFinalized()
	 */
	public boolean isFinalized() {
		return this.forwardStream.isFinalized();
	}

	/* (non-Javadoc)
	 * @see edu.stanford.math.plex4.homology.streams.interfaces.AbstractFilteredStream#getSize()
	 */
	public int getSize() {
		return this.forwardStream.getSize();
	}
	
	/**
	 * This function constructs the coboundary maps.
	 */
	private void constructCoboundaries() {
		THashMap<T, ArrayList<IntObjectPair<T>>> coboundaryListMap = new THashMap<T, ArrayList<IntObjectPair<T>>>();
		for (T element: this.forwardStream) {
			this.reversedElements.add(element);
			T[] boundary = this.forwardStream.getBoundary(element);
			int[] boundaryCoefficients = this.forwardStream.getBoundaryCoefficients(element);
			for (int i = 0; i < boundary.length; i++) {
				T boundaryElement = boundary[i];
				if (!coboundaryListMap.containsKey(boundaryElement)) {
					coboundaryListMap.put(boundaryElement, new ArrayList<IntObjectPair<T>>());
				}
				coboundaryListMap.get(boundaryElement).add(new IntObjectPair<T>(boundaryCoefficients[i], element));
			}
		}
		
		Set<T> coboundaryKeys = coboundaryListMap.keySet();
		for (T element: coboundaryKeys) {
			ArrayList<IntObjectPair<T>> coboundary = coboundaryListMap.get(element);
			T[] coboundaryArray = ObjectArrayUtility.createArray(coboundary.size(), element);
			int[] coboundaryCoefficients = new int[coboundary.size()];
			int i = 0;
			for (IntObjectPair<T> pair: coboundary) {
				coboundaryArray[i] = pair.getSecond();
				coboundaryCoefficients[i] = pair.getFirst();
				i++;
			}
			this.coboundaryMap.put(element, coboundaryArray);
			this.coboundaryCoefficientMap.put(element, coboundaryCoefficients);
		}
		
		Collections.reverse(this.reversedElements);
	}

	/* (non-Javadoc)
	 * @see edu.stanford.math.plex4.homology.streams.interfaces.AbstractFilteredStream#getMaximumFiltrationIndex()
	 */
	public int getMaximumFiltrationIndex() {
		return this.forwardStream.getMaximumFiltrationIndex();
	}

	/* (non-Javadoc)
	 * @see edu.stanford.math.plex4.streams.interfaces.AbstractFilteredStream#getBasisComparator()
	 */
	public Comparator<T> getBasisComparator() {
		return new ReversedComparator<T>(this.forwardStream.getBasisComparator());
	}

	public <G> PersistenceInvariantDescriptor<Interval<Double>, G> transform(PersistenceInvariantDescriptor<Interval<Integer>, G> barcodeCollection) {
		return this.forwardStream.transform(barcodeCollection);
	}
}
