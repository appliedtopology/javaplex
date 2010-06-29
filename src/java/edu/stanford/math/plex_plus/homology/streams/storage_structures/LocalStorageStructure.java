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
	@Override
	public void addElement(T basisElement, double filtrationValue) {
		ExceptionUtility.verifyNonNull(basisElement);

		if (this.isFinalized) {
			throw new IllegalStateException("Cannot add objects to finalized storage structure.");
		}

		this.elementFiltrationPairs.add(new DoubleGenericPair<T>(filtrationValue, basisElement));
		this.filtrationValues.put(basisElement, filtrationValue);
	}

	/* (non-Javadoc)
	 * @see edu.stanford.math.plex_plus.homology.stream_structure.StreamStorageStructure#isFinalized()
	 */
	@Override
	public boolean isFinalized() {
		return this.isFinalized;
	}

	/* (non-Javadoc)
	 * @see edu.stanford.math.plex_plus.homology.stream_structure.StreamStorageStructure#setAsFinalized()
	 */
	@Override
	public void setAsFinalized() {
		this.isFinalized = true;
	}

	/* (non-Javadoc)
	 * @see edu.stanford.math.plex_plus.homology.stream_structure.StreamStorageStructure#sortByFiltration()
	 */
	@Override
	public void sortByFiltration() {
		Collections.sort(this.elementFiltrationPairs, this.filteredComparator);
	}

	@Override
	public Iterator<T> iterator() {
		return new DoubleOrderedIterator<T>(this.elementFiltrationPairs);
	}

	@Override
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

	@Override
	public Comparator<T> getBasisComparator() {
		return this.basisComparator;
	}

}
