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
public interface GenericAbstractFiniteMetricSpace<T> extends GenericAbstractMetricSpace<T> {
	
	/**
	 * Returns the number of the points in the metric space.
	 * 
	 * @return the number of points in the metric space
	 */
	public int size();
	public double distance(int i, int j);
	public T getPoint(int index);
	public int getNearestPoint(T queryPoint);
	public TIntHashSet getNeighborhood(T queryPoint, double epsilon);
	public TIntHashSet getKNearestNeighbors(T queryPoint, int k);
}
