package edu.stanford.math.plex4.homology.zigzag;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import edu.stanford.math.plex4.homology.barcodes.AnnotatedBarcodeCollection;
import edu.stanford.math.plex4.homology.barcodes.BarcodeCollection;
import edu.stanford.math.plex4.homology.barcodes.Interval;
import edu.stanford.math.primitivelib.autogen.pair.ObjectObjectPair;


public class AnnotatedIntervalTracker<K, V> {
	
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
	
	protected final Map<K, Descriptor<V>> openIntervals = new HashMap<K, Descriptor<V>>();
	protected final AnnotatedBarcodeCollection<Integer, V> annotatedBarcodes = new AnnotatedBarcodeCollection<Integer, V>();

	protected boolean useLeftClosedIntervals = true;
	protected boolean useRightClosedIntervals = true;
	
	public void setUseLeftClosedIntervals(boolean value) {
		this.useLeftClosedIntervals = value;
	}
	
	public void setUseRightClosedIntervals(boolean value) {
		this.useRightClosedIntervals = value;
	}
	
	public void startInterval(K key, int startIndex, int dimension, V generator) {
		Descriptor<V> descriptor = new Descriptor<V>(startIndex, dimension, generator);
		this.openIntervals.put(key, descriptor);
	}
	
	public void endInterval(K key, int endIndex) {
		Descriptor<V> descriptor = this.openIntervals.get(key);

		if (this.useLeftClosedIntervals && this.useRightClosedIntervals) {
			if (endIndex >= descriptor.start) {
				this.annotatedBarcodes.addInterval(descriptor.dimension, Interval.makeInterval(descriptor.start, endIndex, this.useLeftClosedIntervals, this.useRightClosedIntervals, false, false), descriptor.generator);
			}
		} else {
			if (endIndex > descriptor.start) {
				this.annotatedBarcodes.addInterval(descriptor.dimension, Interval.makeInterval(descriptor.start, endIndex, this.useLeftClosedIntervals, this.useRightClosedIntervals, false, false), descriptor.generator);
			}
		}
		
		this.openIntervals.remove(key);
	}
	
	public void endAllIntervals(int endIndex) {
		for (K key: this.openIntervals.keySet()) {
			Descriptor<V> descriptor = this.openIntervals.get(key);
			if (this.useLeftClosedIntervals && this.useRightClosedIntervals) {
				if (endIndex >= descriptor.start) {
					this.annotatedBarcodes.addInterval(descriptor.dimension, Interval.makeInterval(descriptor.start, endIndex, this.useLeftClosedIntervals, this.useRightClosedIntervals, false, false), descriptor.generator);
				}
			} else {
				if (endIndex > descriptor.start) {
					this.annotatedBarcodes.addInterval(descriptor.dimension, Interval.makeInterval(descriptor.start, endIndex, this.useLeftClosedIntervals, this.useRightClosedIntervals, false, false), descriptor.generator);
				}
			}
		}
		
		this.openIntervals.clear();
	}
	
	public AnnotatedBarcodeCollection<Integer, V> getAnnotatedBarcodes() {
		return (AnnotatedBarcodeCollection<Integer, V>) BasisTrackingUtility.union(this.getFiniteAnnotatedBarcodes(), this.getInfiniteAnnotatedBarcodes());
	}

	protected AnnotatedBarcodeCollection<Integer, V> getInfiniteAnnotatedBarcodes() {
		AnnotatedBarcodeCollection<Integer, V> collection = new AnnotatedBarcodeCollection<Integer, V>();
		for (K key: this.openIntervals.keySet()) {
			Descriptor<V> descriptor = this.openIntervals.get(key);
			this.annotatedBarcodes.addInterval(descriptor.dimension, Interval.makeInterval(descriptor.start, null, this.useLeftClosedIntervals, this.useRightClosedIntervals, false, true), descriptor.generator);
		}

		return collection;
	}

	protected AnnotatedBarcodeCollection<Integer, V> getFiniteAnnotatedBarcodes() {
		return this.annotatedBarcodes;
	}
	
	public BarcodeCollection<Integer> getBarcodes() {
		return BarcodeCollection.forgetGeneratorType(this.getAnnotatedBarcodes());
	}
	
	public BarcodeCollection<Integer> getInfiniteBarcodes() {
		return BarcodeCollection.forgetGeneratorType(this.getInfiniteAnnotatedBarcodes());
	}
	
	public BarcodeCollection<Integer> getFiniteBarcodes() {
		return BarcodeCollection.forgetGeneratorType(this.getFiniteAnnotatedBarcodes());
	}
	
	public static <L, U, V> AnnotatedIntervalTracker<L, V> join(AnnotatedIntervalTracker<L, U> X, AnnotatedIntervalTracker<L, V> Z, Map<L, L> map, int ZIndex) {
		AnnotatedIntervalTracker<L, V> result = new AnnotatedIntervalTracker<L, V>();

		Set<L> processedZIndices = new HashSet<L>();

		for (L X_key: X.openIntervals.keySet()) {
			Descriptor<U> X_descriptor = X.openIntervals.get(X_key);
			if (map.containsKey(X_key)) {
				L Z_key = map.get(X_key);
				Descriptor<V> Z_descriptor = Z.openIntervals.get(Z_key);

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

			Descriptor<V> Z_descriptor = Z.openIntervals.get(Z_key);

			result.startInterval(Z_key, Z_descriptor.start, Z_descriptor.dimension, Z_descriptor.generator);
		}
		
		for (Entry<Integer, List<ObjectObjectPair<Interval<Integer>, U>>> barcode: X.annotatedBarcodes) {
			int dimension = barcode.getKey();
			for (ObjectObjectPair<Interval<Integer>, U> intervalPair: barcode.getValue()) {
				result.annotatedBarcodes.addInterval(dimension, intervalPair.getFirst(), null);
			}
		}

		return result;
	}
}
