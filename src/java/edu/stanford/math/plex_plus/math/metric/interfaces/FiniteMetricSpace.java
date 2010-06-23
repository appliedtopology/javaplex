/**
 * 
 */
package edu.stanford.math.plex_plus.math.metric.interfaces;

import gnu.trove.set.hash.TIntHashSet;

/**
 * This class abstracts the functionality of a finite metric space. The elements
 * are indexed by the integers {0, ..., n - 1}, where n is the size of the metric space.
 * 
 * @author Andrew Tausz
 *
 */
public interface FiniteMetricSpace<T> extends IntFiniteMetricSpace {
	public T getPoint(int index);
	public int getNearestPoint(T queryPoint);
	public TIntHashSet getNeighborhood(T queryPoint, double epsilon);
	public TIntHashSet getKNearestNeighbors(T queryPoint, int k);
}
