package edu.stanford.math.plex_plus.homology.streams.storage_structures;

import java.util.Comparator;

public class StreamStorageStructureFactory {
	public static <T> StreamStorageStructure<T> getDefaultStorageStructure(Comparator<T> comparator) {
		return new LocalStorageStructure<T>(comparator);
	}
}
