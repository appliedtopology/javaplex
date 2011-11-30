package edu.stanford.math.plex4.streams.storage_structures;

import java.util.Comparator;

/**
 * This interface defines the functionality of a stream storage structure. Such
 * a structure is designed to be the backing of a streamed filtered chain complex.
 * 
 * Note that this interface extends the Iterable<T> interface. An implementing
 * class must ensure that the elements are provided in increasing order of
 * filtration index.
 * 
 * @author Andrew Tausz
 *
 * @param <T>
 */
public interface StreamStorageStructure<T> extends Iterable<T> {
	
	/**
	 * This function adds the given basis element to the storage structure, with
	 * the supplied filtration index.
	 * 
	 * @param basisElement the basis element to add
	 * @param filtrationIndex the filtration index of the basis element
	 */
	void addElement(T basisElement, int filtrationIndex);
	
	/**
	 * This function removes the specified element form the storage structure.
	 * In the case that the element is not present in the stream, this function 
	 * throws an IllegalArgumentException.
	 * 
	 * @param basisElement the basisElement to remove
	 */
	void removeElement(T basisElement);
	
	/**
	 * This function returns true if the stream contains the given basis element,
	 * and false otherwise.
	 * 
	 * @param basisElement the basis element to query
	 * @return true if the stream contains the basis element and false otherwise
	 */
	boolean containsElement(T basisElement);
	
	/**
	 * This function returns the filtration index of the given basis element.
	 * 
	 * @param basisElement the element to query
	 * @return the filtration index of the given basis element
	 */
	int getFiltrationIndex(T basisElement);
	
	/**
	 * This function sets the stream as being finalized. A finalized storage structure
	 * cannot accept any more elements, and should be ready for consumption.
	 */
	void finalizeStructure();
	
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
	
	/**
	 * This function returns the number of elements in the storage structure.
	 * 
	 * @return the number of elements in the storage structure
	 */
	int getSize();
	
	/**
	 * This function gets the maximum filtration index in the complex.
	 * 
	 * @return the maximum filtration index
	 */
	public int getMaximumFiltrationIndex();
	
	
	/**
	 * This function gets the minimum filtration index in the complex.
	 * 
	 * @return the minimum filtration index
	 */
	public int getMinimumFiltrationIndex();
}
