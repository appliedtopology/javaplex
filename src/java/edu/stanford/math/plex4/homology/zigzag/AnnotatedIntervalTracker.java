package edu.stanford.math.plex4.homology.zigzag;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import edu.stanford.math.plex4.homology.barcodes.IntAnnotatedBarcode;
import edu.stanford.math.plex4.homology.barcodes.IntAnnotatedBarcodeCollection;
import edu.stanford.math.plex4.homology.barcodes.IntBarcode;
import edu.stanford.math.plex4.homology.barcodes.IntHalfOpenInterval;
import edu.stanford.math.primitivelib.autogen.pair.ObjectObjectPair;

public class AnnotatedIntervalTracker<K, V> extends IntervalTracker<K> {
	protected final IntAnnotatedBarcodeCollection<V> annotatedBarcodes = new IntAnnotatedBarcodeCollection<V>();

	public void startInterval(K key, int startIndex, int dimension, V generator) {
		Descriptor<V> descriptor = new Descriptor<V>(startIndex, dimension, generator);
		this.openIntervals.put(key, descriptor);
	}

	public void startInterval(K key, int startIndex, int dimension) {
		this.startInterval(key, startIndex, dimension, null);
	}

	@SuppressWarnings("unchecked")
	public void endInterval(K key, int endIndex) {
		edu.stanford.math.plex4.homology.zigzag.IntervalTracker.Descriptor<?> descriptor = this.openIntervals.get(key);

		if (endIndex > descriptor.start) {
			this.annotatedBarcodes.addInterval(descriptor.dimension, descriptor.start, endIndex, (V) descriptor.generator);
			this.barcodes.addInterval(descriptor.dimension, descriptor.start, endIndex);
		}

		this.openIntervals.remove(key);
	}
	
	@SuppressWarnings("unchecked")
	public void endAllIntervals(int endIndex) {
		for (K key: this.openIntervals.keySet()) {
			edu.stanford.math.plex4.homology.zigzag.IntervalTracker.Descriptor<?> descriptor = this.openIntervals.get(key);
			if (endIndex > descriptor.start) {
				this.annotatedBarcodes.addInterval(descriptor.dimension, descriptor.start, endIndex, (V) descriptor.generator);
				this.barcodes.addInterval(descriptor.dimension, descriptor.start, endIndex);
			}
		}
		
		this.openIntervals.clear();
	}

	public IntAnnotatedBarcodeCollection<V> getAnnotatedBarcodes() {
		return BasisTrackingUtility.union(this.getFiniteAnnotatedBarcodes(), this.getInfiniteAnnotatedBarcodes());
	}

	@SuppressWarnings("unchecked")
	protected IntAnnotatedBarcodeCollection<V> getInfiniteAnnotatedBarcodes() {
		IntAnnotatedBarcodeCollection<V> collection = new IntAnnotatedBarcodeCollection<V>();
		for (K key: this.openIntervals.keySet()) {
			Descriptor<?> descriptor = this.openIntervals.get(key);
			collection.addRightInfiniteInterval(descriptor.dimension, descriptor.start, (V) descriptor.generator);
		}

		return collection;
	}

	protected IntAnnotatedBarcodeCollection<V> getFiniteAnnotatedBarcodes() {
		return this.annotatedBarcodes;
	}

	@SuppressWarnings("unchecked")
	public static <L, U, V> AnnotatedIntervalTracker<L, V> join(AnnotatedIntervalTracker<L, U> X, AnnotatedIntervalTracker<L, V> Z, Map<L, L> map, int ZIndex) {
		AnnotatedIntervalTracker<L, V> result = new AnnotatedIntervalTracker<L, V>();

		Set<L> processedZIndices = new HashSet<L>();

		for (L X_key: X.openIntervals.keySet()) {
			Descriptor<U> X_descriptor = (Descriptor<U>) X.openIntervals.get(X_key);
			if (map.containsKey(X_key)) {
				L Z_key = map.get(X_key);
				Descriptor<V> Z_descriptor = (Descriptor<V>) Z.openIntervals.get(Z_key);

				result.startInterval(Z_key, X_descriptor.start, Z_descriptor.dimension, Z_descriptor.generator);

				processedZIndices.add(map.get(X_key));
			} else {
				result.startInterval(X_key, X_descriptor.start, X_descriptor.dimension, null);
				result.endInterval(X_key, ZIndex);
			}
		}

		for (L Z_key: Z.openIntervals.keySet()) {
			if (processedZIndices.contains(Z_key)) {
				continue;
			}

			Descriptor<V> Z_descriptor = (Descriptor<V>) Z.openIntervals.get(Z_key);

			result.startInterval(Z_key, Z_descriptor.start, Z_descriptor.dimension, Z_descriptor.generator);
		}
		
		for (Entry<Integer, IntAnnotatedBarcode<U>> barcode: X.annotatedBarcodes) {
			int dimension = barcode.getKey();
			for (ObjectObjectPair<IntHalfOpenInterval, U> intervalPair: barcode.getValue()) {
				result.annotatedBarcodes.addInterval(dimension, intervalPair.getFirst(), (V) intervalPair.getSecond());
			}
		}
		
		for (Entry<Integer, IntBarcode> barcode: X.barcodes) {
			int dimension = barcode.getKey();
			for (IntHalfOpenInterval interval: barcode.getValue()) {
				result.barcodes.addInterval(dimension, interval);
			}
		}

		return result;
	}
}
