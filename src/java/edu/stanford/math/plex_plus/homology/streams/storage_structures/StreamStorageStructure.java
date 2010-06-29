package edu.stanford.math.plex_plus.homology.streams.storage_structures;

import java.util.Comparator;

/**
 * This interface defines the functionality of a stream storage structure. Such
 * a structure is designed to be the backing of a streamed filtered chain complex.
 * 
 * Note that this interface extends the Iterable<T> interface. An implementing
 * class must ensure that the elements are provided in increasing order of
 * filtration value.
 * 
 * @author Andrew Tausz
 *
 * @param <T>
 */
public interface StreamStorageStructure<T> extends Iterable<T> {
	
	/**
	 * This function adds the given basis element to the storage structure, with
	 * the supplied filtration value.
	 * 
	 * @param basisElement the basis element to add
	 * @param filtrationValue the filtration value of the basis element
	 */
	void addElement(T basisElement, double filtrationValue);
	
	/**
	 * This function returns the filtration value of the given basis element.
	 * 
	 * @param basisElement the element to query
	 * @return the filtration value of the given basis element
	 */
	double getFiltrationValue(T basisElement);
	
	/**
	 * This function ensures that the stream is in sorted order. 
	 */
	void sortByFiltration();
	
	/**
	 * This function sets the stream as being finalized.
	 */
	void setAsFinalized();
	
	/**
	 * This function returns true if the stream has been finalized.
	 * 
	 * @return true if the stream is finalized and false otherwise
	 */
	boolean isFinalized();
	
	/**
	 * This function returns a Comparator<T> object used to define the ordering
	 * on the basis type.
	 * 
	 * @return a Comparator on the basis type
	 */
	Comparator<T> getBasisComparator();
}
