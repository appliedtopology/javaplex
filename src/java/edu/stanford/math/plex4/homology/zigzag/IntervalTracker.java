package edu.stanford.math.plex4.homology.zigzag;

import java.util.HashMap;
import java.util.Map;

import edu.stanford.math.plex4.homology.barcodes.AnnotatedBarcodeCollection;
import edu.stanford.math.plex4.homology.barcodes.BarcodeCollection;
import edu.stanford.math.plex4.homology.barcodes.Interval;
import edu.stanford.math.plex4.homology.barcodes.PersistenceInvariantDescriptor;
import edu.stanford.math.primitivelib.utility.Infinity;


public class IntervalTracker<K, I extends Comparable<I>, G> implements AbstractPersistenceTracker<K, I, G> {
	
	protected final Map<K, IntervalDescriptor<I, G>> openIntervals = new HashMap<K, IntervalDescriptor<I, G>>();
	protected final AnnotatedBarcodeCollection<I, G> annotatedBarcodes = new AnnotatedBarcodeCollection<I, G>();

	protected boolean useLeftClosedIntervals = true;
	protected boolean useRightClosedIntervals = true;
	
	protected int maxDimension = Infinity.Int.getPositiveInfinity();
	
	public Map<K, IntervalDescriptor<I, G>> getActiveGenerators() {
		return this.openIntervals;
	}
	
	public AnnotatedBarcodeCollection<I, G> getInactiveGenerators() {
		return this.annotatedBarcodes;
	}
	
	public void setMaxDimension(int maxDimension) {
		this.maxDimension = maxDimension;
	}
	
	public void setUseLeftClosedIntervals(boolean value) {
		this.useLeftClosedIntervals = value;
	}
	
	public void setUseRightClosedIntervals(boolean value) {
		this.useRightClosedIntervals = value;
	}
	
	public boolean containsActiveInterval(K key) {
		return this.openIntervals.containsKey(key);
	}
	
	public void startInterval(K key, I startIndex, int dimension, G generator) {
		
		if (dimension > this.maxDimension) {
			return;
		}
		
		IntervalDescriptor<I, G> descriptor = new IntervalDescriptor<I, G>(startIndex, dimension, generator);
		this.openIntervals.put(key, descriptor);
	}
	
	public void endInterval(K key, I endIndex) {
		
		if (!this.openIntervals.containsKey(key)) {
			return;
		}
		
		IntervalDescriptor<I, G> descriptor = this.openIntervals.get(key);
		
		if (this.useLeftClosedIntervals && this.useRightClosedIntervals) {
			if (endIndex.compareTo(descriptor.start) >= 0) {
				this.annotatedBarcodes.addInterval(descriptor.dimension, Interval.makeInterval(descriptor.start, endIndex, this.useLeftClosedIntervals, this.useRightClosedIntervals, false, false), descriptor.generator);
			}
		} else {
			if (endIndex.compareTo(descriptor.start) > 0) {
				this.annotatedBarcodes.addInterval(descriptor.dimension, Interval.makeInterval(descriptor.start, endIndex, this.useLeftClosedIntervals, this.useRightClosedIntervals, false, false), descriptor.generator);
			}
		}
		
		this.openIntervals.remove(key);
	}
	
	public void endAllIntervals(I endIndex) {
		for (K key: this.openIntervals.keySet()) {
			if (!this.openIntervals.containsKey(key)) {
				continue;
			}
			
			IntervalDescriptor<I, G> descriptor = this.openIntervals.get(key);
			
			if (this.useLeftClosedIntervals && this.useRightClosedIntervals) {
				if (endIndex.compareTo(descriptor.start) >= 0) {
					this.annotatedBarcodes.addInterval(descriptor.dimension, Interval.makeInterval(descriptor.start, endIndex, this.useLeftClosedIntervals, this.useRightClosedIntervals, false, false), descriptor.generator);
				}
			} else {
				if (endIndex.compareTo(descriptor.start) > 0) {
					this.annotatedBarcodes.addInterval(descriptor.dimension, Interval.makeInterval(descriptor.start, endIndex, this.useLeftClosedIntervals, this.useRightClosedIntervals, false, false), descriptor.generator);
				}
			}
		}
		
		this.openIntervals.clear();
	}
	
	public AnnotatedBarcodeCollection<I, G> getAnnotatedBarcodes() {
		return (AnnotatedBarcodeCollection<I, G>) PersistenceInvariantDescriptor.union(this.getFiniteAnnotatedBarcodes(), this.getInfiniteAnnotatedBarcodes());
	}

	public AnnotatedBarcodeCollection<I, G> getInfiniteAnnotatedBarcodes() {
		AnnotatedBarcodeCollection<I, G> collection = new AnnotatedBarcodeCollection<I, G>();
		for (K key: this.openIntervals.keySet()) {
			IntervalDescriptor<I, G> descriptor = this.openIntervals.get(key);
			collection.addInterval(descriptor.dimension, Interval.makeInterval(descriptor.start, null, this.useLeftClosedIntervals, this.useRightClosedIntervals, false, true), descriptor.generator);
		}

		return collection;
	}

	public AnnotatedBarcodeCollection<I, G> getFiniteAnnotatedBarcodes() {
		return this.annotatedBarcodes;
	}
	
	public BarcodeCollection<I> getBarcodes() {
		return BarcodeCollection.forgetGeneratorType(this.getAnnotatedBarcodes());
	}
	
	public BarcodeCollection<I> getInfiniteBarcodes() {
		return BarcodeCollection.forgetGeneratorType(this.getInfiniteAnnotatedBarcodes());
	}
	
	public BarcodeCollection<I> getFiniteBarcodes() {
		return BarcodeCollection.forgetGeneratorType(this.getFiniteAnnotatedBarcodes());
	}
	
	@Override
	public String toString() {
		
		StringBuilder builder = new StringBuilder();
		
		for (K key: this.openIntervals.keySet()) {
			IntervalDescriptor<I, G> descriptor = this.openIntervals.get(key);
			builder.append(key.toString() + ": " + descriptor.toString());
			builder.append("\n");
		}
		
		builder.append(this.annotatedBarcodes.toString());
		
		return builder.toString();
	}
	
	/*
	public static <L, U, V> AnnotatedIntervalTracker<L, V> join(AnnotatedIntervalTracker<L, U> X, AnnotatedIntervalTracker<L, V> Z, Map<L, L> map, int ZIndex) {
		AnnotatedIntervalTracker<L, V> result = new AnnotatedIntervalTracker<L, V>();
		result.setUseRightClosedIntervals(true);
		Set<L> processedZIndices = new HashSet<L>();

		for (L X_key: X.openIntervals.keySet()) {
			IntervalDescriptor<Integer, U> X_descriptor = X.openIntervals.get(X_key);
			if (map.containsKey(X_key)) {
				L Z_key = map.get(X_key);
				IntervalDescriptor<Integer, V> Z_descriptor = Z.openIntervals.get(Z_key);

				result.startInterval(Z_key, X_descriptor.start, Z_descriptor.dimension, Z_descriptor.generator);

				processedZIndices.add(map.get(X_key));
			} else {
				result.startInterval(X_key, X_descriptor.start, X_descriptor.dimension, null);
				result.endInterval(X_key, ZIndex - 1);
			}
		}

		for (L Z_key: Z.openIntervals.keySet()) {
			if (processedZIndices.contains(Z_key)) {
				continue;
			}

			IntervalDescriptor<Integer, V> Z_descriptor = Z.openIntervals.get(Z_key);

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
	*/
}
