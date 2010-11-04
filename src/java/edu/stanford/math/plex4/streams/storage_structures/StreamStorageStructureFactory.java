package edu.stanford.math.plex4.streams.storage_structures;

import java.util.Comparator;

import edu.stanford.math.plex4.homology.chain_basis.PrimitiveBasisElement;

public class StreamStorageStructureFactory {
	public static <T extends PrimitiveBasisElement> StreamStorageStructure<T> getDefaultStorageStructure(Comparator<T> comparator) {
		return new SortedStorageStructure<T>(comparator);
	}
	
	public static <T> StreamStorageStructure<T> getSortedStorageStructure(Comparator<T> comparator) {
		return new SortedStorageStructure<T>(comparator);
	}
	
	public static <T extends PrimitiveBasisElement> StreamStorageStructure<T> getHashedStorageStructure(Comparator<T> comparator) {
		return new HashedStorageStructure<T>(comparator);
	}
}
