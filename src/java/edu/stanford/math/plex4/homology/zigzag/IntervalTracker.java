package edu.stanford.math.plex4.homology.zigzag;

import java.util.HashMap;
import java.util.Map;

import edu.stanford.math.plex4.homology.barcodes.IntBarcodeCollection;

public class IntervalTracker<K> {

	public static class Descriptor<V> {
		int start;
		int dimension;
		V generator;

		public Descriptor(int start, int dimension, V generator) {
			super();
			this.start = start;
			this.dimension = dimension;
			this.generator = generator;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + dimension;
			result = prime * result + ((generator == null) ? 0 : generator.hashCode());
			result = prime * result + start;
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Descriptor<?> other = (Descriptor<?>) obj;
			if (dimension != other.dimension)
				return false;
			if (generator == null) {
				if (other.generator != null)
					return false;
			} else if (!generator.equals(other.generator))
				return false;
			if (start != other.start)
				return false;
			return true;
		}

		@Override
		public String toString() {
			return "Descriptor [dimension=" + dimension + ", " + (generator != null ? "generator=" + generator + ", " : "") + "start=" + start + "]";
		}
	}

	protected final Map<K, Descriptor<?>> openIntervals = new HashMap<K, Descriptor<?>>();
	protected final IntBarcodeCollection barcodes = new IntBarcodeCollection();

	public Descriptor<?> getDescriptor(K key) {
		return this.openIntervals.get(key);
	}

	public void startInterval(K key, int startIndex, int dimension) {
		Descriptor<Object> descriptor = new Descriptor<Object>(startIndex, dimension, null);
		this.openIntervals.put(key, descriptor);
	}

	public void endInterval(K key, int endIndex) {
		Descriptor<?> descriptor = this.openIntervals.get(key);
		if (endIndex > descriptor.start) {
			this.barcodes.addInterval(descriptor.dimension, descriptor.start, endIndex);
		}
		this.openIntervals.remove(key);
	}

	public IntBarcodeCollection getBarcodes() {
		return BasisTrackingUtility.union(this.getFiniteBarcodes(), this.getInfiniteBarcodes());
	}

	protected IntBarcodeCollection getInfiniteBarcodes() {
		IntBarcodeCollection collection = new IntBarcodeCollection();
		for (K key: this.openIntervals.keySet()) {
			Descriptor<?> descriptor = this.openIntervals.get(key);
			collection.addRightInfiniteInterval(descriptor.dimension, descriptor.start);
		}

		return collection;
	}

	protected IntBarcodeCollection getFiniteBarcodes() {
		return this.barcodes;
	}
}
