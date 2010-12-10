package edu.stanford.math.plex4.streams.storage_structures;

import java.util.Comparator;

import edu.stanford.math.plex4.homology.chain_basis.PrimitiveBasisElement;

/**
 * This class contains various convenience functions for constructing stream storage structures. It also
 * defines the default stream storage structure used by most filtered chain complexes.
 * 
 * @author Andrew Tausz
 *
 */
public class StreamStorageStructureFactory {
	
	/**
	 * This function returns the default stream storage structure for the type T.
	 * 
	 * @param <T> the underlying basis type
	 * @param comparator a comparator for comparing basis elements
	 * @return the default stream storage structure
	 */
	public static <T extends PrimitiveBasisElement> StreamStorageStructure<T> getDefaultStorageStructure(Comparator<T> comparator) {
		return new SortedStorageStructure<T>(comparator);
	}
	
	/**
	 * This function creates a new sorted storage structure.
	 * 
	 * @param <T> the underlying basis type
	 * @param comparator a comparator for comparing basis elements
	 * @return a new stream storage structure
	 */
	public static <T> StreamStorageStructure<T> getSortedStorageStructure(Comparator<T> comparator) {
		return new SortedStorageStructure<T>(comparator);
	}
	
	/**
	 * This function creates a new hashed storage structure.
	 * 
	 * @param <T> the underlying basis type
	 * @param comparator a comparator for comparing basis elements
	 * @return a new hashed stream storage structure
	 */
	public static <T extends PrimitiveBasisElement> StreamStorageStructure<T> getHashedStorageStructure(Comparator<T> comparator) {
		return new HashedStorageStructure<T>(comparator);
	}
}
