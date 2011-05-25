package edu.stanford.math.plex4.homology.zigzag;


public class IntervalTracker<K> extends AnnotatedIntervalTracker<K, Object>  {
	
	public void startInterval(K key, int startIndex, int dimension) {
		Descriptor<Object> descriptor = new Descriptor<Object>(startIndex, dimension, null);
		this.openIntervals.put(key, descriptor);
	}
}
