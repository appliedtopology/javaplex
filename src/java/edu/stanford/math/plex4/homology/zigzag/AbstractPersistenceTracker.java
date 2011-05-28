package edu.stanford.math.plex4.homology.zigzag;

import java.util.Map;

import edu.stanford.math.plex4.homology.barcodes.AnnotatedBarcodeCollection;

public interface AbstractPersistenceTracker<K, I extends Comparable<I>, G> {
	public AnnotatedBarcodeCollection<I, G> getInactiveGenerators();
	public Map<K, IntervalDescriptor<I, G>> getActiveGenerators();
}
