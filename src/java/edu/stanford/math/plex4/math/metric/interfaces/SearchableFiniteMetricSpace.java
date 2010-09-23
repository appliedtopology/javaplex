/**
 * 
 */
package edu.stanford.math.plex4.math.metric.interfaces;

import gnu.trove.TIntHashSet;

/**
 * This class abstracts the functionality of a finite metric space. The elements
 * are indexed by the integers {0, ..., n - 1}, where n is the size of the metric space.
 * 
 * @author Andrew Tausz
 *
 */
public interface SearchableFiniteMetricSpace<T> extends FiniteMetricSpace<T> {
	public int getNearestPoint(T queryPoint);
	public TIntHashSet getOpenNeighborhood(T queryPoint, double epsilon);
	public TIntHashSet getClosedNeighborhood(T queryPoint, double epsilon);
	public TIntHashSet getKNearestNeighbors(T queryPoint, int k);
}
