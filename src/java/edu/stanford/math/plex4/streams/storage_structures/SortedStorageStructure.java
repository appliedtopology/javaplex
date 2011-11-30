/**
 * 
 */
package edu.stanford.math.plex4.streams.storage_structures;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import edu.stanford.math.primitivelib.autogen.pair.IntObjectPair;
import edu.stanford.math.primitivelib.autogen.pair.IntObjectPairComparator;
import gnu.trove.TObjectIntHashMap;

/**
 * This class implements an in-memory stream storage mechanism where all of the
 * elements are held in an in-memory collection in fully sorted order. It implements 
 * the StreamStorageStructure interface.
 * 
 * @author Andrew Tausz
 *
 * @param <T> the underlying basis type
 */
public class SortedStorageStructure<T> implements StreamStorageStructure<T> {
	/**
	 * This contains the pairs of filtration index and basis elements.
	 */
	private final List<IntObjectPair<T>> elementFiltrationPairs = new ArrayList<IntObjectPair<T>>();

	/**
	 * This hash map contains the filtration indices of the basis elements in the complex.
	 */
	private final TObjectIntHashMap<T> filtrationIndices = new TObjectIntHashMap<T>();

	/**
	 * Comparator which provides ordering of elements of the stream.
	 */
	private final Comparator<T> basisComparator;

	/**
	 * This comparator defines the filtration ordering on filtration-object pairs.
	 */
	private final IntObjectPairComparator<T> filteredComparator;
	
	/**
	 * Boolean which indicates whether stream has been finalized or not
	 */
	private boolean isFinalized = false;
	
	/**
	 * This construction initializes the class with a comparator for comparing the basis elements.
	 * 
	 * @param basisComparator the comparator
	 */
	public SortedStorageStructure(Comparator<T> basisComparator) {
		this.basisComparator = basisComparator;
		this.filteredComparator = new IntObjectPairComparator<T>(this.basisComparator);
	}
	
	/* (non-Javadoc)
	 * @see edu.stanford.math.plex4.homology.streams.storage_structures.StreamStorageStructure#addElement(java.lang.Object, int)
	 */
	public void addElement(T basisElement, int filtrationIndex) {
		this.elementFiltrationPairs.add(new IntObjectPair<T>(filtrationIndex, basisElement));
		this.filtrationIndices.put(basisElement, filtrationIndex);
	}
	
	/* (non-Javadoc)
	 * @see edu.stanford.math.plex4.homology.streams.storage_structures.StreamStorageStructure#updateOrAddElement(java.lang.Object, int)
	 */
	public void updateOrAddElement(T basisElement, int newFiltrationValue) {
		if (this.isFinalized) {
			throw new IllegalStateException("Cannot update objects in finalized storage structure.");
		}
		
		if (this.filtrationIndices.containsKey(basisElement)) {
			// remove the old (filtration value, basis element) pair
			IntObjectPair<T> pair = new IntObjectPair<T>(this.filtrationIndices.get(basisElement), basisElement);
			this.elementFiltrationPairs.remove(pair);
			
			// add the new pair
			this.elementFiltrationPairs.add(new IntObjectPair<T>(newFiltrationValue, basisElement));
		} else {
			this.elementFiltrationPairs.add(new IntObjectPair<T>(newFiltrationValue, basisElement));
		}
		
		this.filtrationIndices.adjustOrPutValue(basisElement, newFiltrationValue, newFiltrationValue);
	}

	/* (non-Javadoc)
	 * @see edu.stanford.math.plex4.homology.streams.storage_structures.StreamStorageStructure#removeElement(java.lang.Object)
	 */
	public void removeElement(T basisElement) {
		if (!this.filtrationIndices.containsKey(basisElement)) {
			throw new IllegalArgumentException("Element: " + basisElement + " is not present in the stream.");
		}
		
		// remove the old (filtration value, basis element) pair
		IntObjectPair<T> pair = new IntObjectPair<T>(this.filtrationIndices.get(basisElement), basisElement);
		this.elementFiltrationPairs.remove(pair);
		
		// remove the element from the filtration values map
		this.filtrationIndices.remove(basisElement);
	}
	
	/* (non-Javadoc)
	 * @see edu.stanford.math.plex_plus.homology.stream_structure.StreamStorageStructure#isFinalized()
	 */
	public boolean isFinalized() {
		return this.isFinalized;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#finalize()
	 */
	public void finalizeStructure() {
		Collections.sort(this.elementFiltrationPairs, this.filteredComparator);
		this.isFinalized = true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Iterable#iterator()
	 */
	public Iterator<T> iterator() {
		return new IntOrderedIterator<T>(this.elementFiltrationPairs);
	}

	/* (non-Javadoc)
	 * @see edu.stanford.math.plex4.homology.streams.storage_structures.StreamStorageStructure#getFiltrationIndex(java.lang.Object)
	 */
	public int getFiltrationIndex(T basisElement) {
		return this.filtrationIndices.get(basisElement);
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();

		for (IntObjectPair<T> pair : this.elementFiltrationPairs) {
			builder.append(pair.toString());
			builder.append('\n');
		}

		return builder.toString();
	}

	/* (non-Javadoc)
	 * @see edu.stanford.math.plex4.homology.streams.storage_structures.StreamStorageStructure#getBasisComparator()
	 */
	public Comparator<T> getBasisComparator() {
		return this.basisComparator;
	}

	/* (non-Javadoc)
	 * @see edu.stanford.math.plex4.homology.streams.storage_structures.StreamStorageStructure#containsElement(java.lang.Object)
	 */
	public boolean containsElement(T basisElement) {
		return this.filtrationIndices.containsKey(basisElement);
	}

	/* (non-Javadoc)
	 * @see edu.stanford.math.plex4.homology.streams.storage_structures.StreamStorageStructure#getSize()
	 */
	public int getSize() {
		return this.filtrationIndices.size();
	}

	/* (non-Javadoc)
	 * @see edu.stanford.math.plex4.homology.streams.storage_structures.StreamStorageStructure#getMaximumFiltrationIndex()
	 */
	public int getMaximumFiltrationIndex() {
		int maxIndex = Integer.MIN_VALUE;
		for (IntObjectPair<T> pair : this.elementFiltrationPairs) {
			if (pair.getFirst() > maxIndex) {
				maxIndex = pair.getFirst();
			}
		}
		
		return maxIndex;
	}
	
	public int getMinimumFiltrationIndex() {
		int minIndex = Integer.MAX_VALUE;
		for (IntObjectPair<T> pair : this.elementFiltrationPairs) {
			if (pair.getFirst() < minIndex) {
				minIndex = pair.getFirst();
			}
		}
		
		return minIndex;
	}
}
