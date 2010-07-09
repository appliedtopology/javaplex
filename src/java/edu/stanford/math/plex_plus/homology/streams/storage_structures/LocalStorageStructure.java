/**
 * 
 */
package edu.stanford.math.plex_plus.homology.streams.storage_structures;

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
	private final List<DoubleGenericPair<T>> elementFiltrationPairs = new ArrayList<DoubleGenericPair<T>>();

	/**
	 * This hash map contains the filtration values of the basis elements in the complex.
	 */
	private final TObjectDoubleHashMap<T> filtrationValues = new TObjectDoubleHashMap<T>();

	/**
	 * Comparator which provides ordering of elements of the stream.
	 */
	private final Comparator<T> basisComparator;

	/**
	 * This comparator defines the filtration ordering on filtration-object pairs.
	 */
	private final DoubleGenericPairComparator<T> filteredComparator;
	
	/**
	 * Boolean which indicates whether stream has been finalized or not
	 */
	private boolean isFinalized = false;
	
	public LocalStorageStructure(Comparator<T> basisComparator) {
		this.basisComparator = basisComparator;
		this.filteredComparator = new DoubleGenericPairComparator<T>(this.basisComparator);
	}
	
	/* (non-Javadoc)
	 * @see edu.stanford.math.plex_plus.homology.stream_structure.StreamStorageStructure#addElement(java.lang.Object, double)
	 */
	public void addElement(T basisElement, double filtrationValue) {
		ExceptionUtility.verifyNonNull(basisElement);

		if (this.isFinalized) {
			throw new IllegalStateException("Cannot add objects to finalized storage structure.");
		}

		this.elementFiltrationPairs.add(new DoubleGenericPair<T>(filtrationValue, basisElement));
		this.filtrationValues.put(basisElement, filtrationValue);
	}
	
	public void updateOrAddElement(T basisElement, double newFiltrationValue) {
		ExceptionUtility.verifyNonNull(basisElement);
		
		if (this.isFinalized) {
			throw new IllegalStateException("Cannot update objects in finalized storage structure.");
		}
		
		if (this.filtrationValues.containsKey(basisElement)) {
			// remove the old (filtration value, basis element) pair
			DoubleGenericPair<T> pair = new DoubleGenericPair<T>(this.filtrationValues.get(basisElement), basisElement);
			this.elementFiltrationPairs.remove(pair);
			
			// add the new pair
			this.elementFiltrationPairs.add(new DoubleGenericPair<T>(newFiltrationValue, basisElement));
		} else {
			this.elementFiltrationPairs.add(new DoubleGenericPair<T>(newFiltrationValue, basisElement));
		}
		
		this.filtrationValues.adjustOrPutValue(basisElement, newFiltrationValue, newFiltrationValue);
	}

	public void removeElement(T basisElement) {
		if (!this.filtrationValues.containsKey(basisElement)) {
			throw new IllegalArgumentException("Element: " + basisElement + " is not present in the stream.");
		}
		
		// remove the old (filtration value, basis element) pair
		DoubleGenericPair<T> pair = new DoubleGenericPair<T>(this.filtrationValues.get(basisElement), basisElement);
		this.elementFiltrationPairs.remove(pair);
		
		// remove the element from the filtration values map
		this.filtrationValues.remove(basisElement);
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
		return new DoubleOrderedIterator<T>(this.elementFiltrationPairs);
	}

	public double getFiltrationValue(T basisElement) {
		return this.filtrationValues.get(basisElement);
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();

		for (DoubleGenericPair<T> pair : this.elementFiltrationPairs) {
			builder.append(pair.toString());
			builder.append('\n');
		}

		return builder.toString();
	}

	public Comparator<T> getBasisComparator() {
		return this.basisComparator;
	}

	public boolean containsElement(T basisElement) {
		return this.filtrationValues.containsKey(basisElement);
	}

}
