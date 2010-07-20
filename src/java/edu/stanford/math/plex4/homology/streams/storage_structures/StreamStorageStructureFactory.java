package edu.stanford.math.plex4.homology.streams.storage_structures;

import java.util.Comparator;

public class StreamStorageStructureFactory {
	public static <T> StreamStorageStructure<T> getDefaultStorageStructure(Comparator<T> comparator) {
		return new LocalStorageStructure<T>(comparator);
	}
}
