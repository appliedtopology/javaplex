/**
 * 
 */
package edu.stanford.math.plex4.homology.streams.storage_structures;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import edu.stanford.math.plex4.utility.ExceptionUtility;
import edu.stanford.math.primitivelib.autogen.pair.IntObjectPair;
import edu.stanford.math.primitivelib.autogen.pair.IntObjectPairComparator;
import gnu.trove.TObjectIntHashMap;

/**
 * This class implements an in-memory stream storage mechanism where all of the
 * elements are held in an in-memory collection. It implements the StreamStorageStructure
 * interface.
 * 
 * @author Andrew Tausz
 *
 * @param <T> the underlying basis type
 */
public class LocalStorageStructure<T> implements StreamStorageStructure<T> {
	/**
	 * This contains the basis elements of the complex.
	 */
	private final List<IntObjectPair<T>> elementFiltrationPairs = new ArrayList<IntObjectPair<T>>();

	/**
	 * This hash map contains the filtration values of the basis elements in the complex.
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
	
	public LocalStorageStructure(Comparator<T> basisComparator) {
		this.basisComparator = basisComparator;
		this.filteredComparator = new IntObjectPairComparator<T>(this.basisComparator);
	}
	
	/* (non-Javadoc)
	 * @see edu.stanford.math.plex_plus.homology.stream_structure.StreamStorageStructure#addElement(java.lang.Object, int)
	 */
	public void addElement(T basisElement, int filtrationValue) {
		ExceptionUtility.verifyNonNull(basisElement);

		if (this.isFinalized) {
			throw new IllegalStateException("Cannot add objects to finalized storage structure.");
		}

		this.elementFiltrationPairs.add(new IntObjectPair<T>(filtrationValue, basisElement));
		this.filtrationIndices.put(basisElement, filtrationValue);
	}
	
	public void updateOrAddElement(T basisElement, int newFiltrationValue) {
		ExceptionUtility.verifyNonNull(basisElement);
		
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
	 * @see edu.stanford.math.plex_plus.homology.stream_structure.StreamStorageStructure#setAsFinalized()
	 */
	public void setAsFinalized() {
		this.isFinalized = true;
	}

	/* (non-Javadoc)
	 * @see edu.stanford.math.plex_plus.homology.stream_structure.StreamStorageStructure#sortByFiltration()
	 */
	public void sortByFiltration() {
		Collections.sort(this.elementFiltrationPairs, this.filteredComparator);
	}

	public Iterator<T> iterator() {
		return new IntOrderedIterator<T>(this.elementFiltrationPairs);
	}

	public int getFiltrationIndex(T basisElement) {
		return this.filtrationIndices.get(basisElement);
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();

		for (IntObjectPair<T> pair : this.elementFiltrationPairs) {
			builder.append(pair.toString());
			builder.append('\n');
		}

		return builder.toString();
	}

	public Comparator<T> getBasisComparator() {
		return this.basisComparator;
	}

	public boolean containsElement(T basisElement) {
		return this.filtrationIndices.containsKey(basisElement);
	}

	public int getSize() {
		return this.filtrationIndices.size();
	}

}
