package edu.stanford.math.plex4.metric.interfaces;

import gnu.trove.TIntHashSet;

/**
 * This interface defines the functionality of a metric space with various querying 
 * capabilities. 
 * 
 * @author Andrew Tausz
 *
 * @param <T> the type of the points in the metric space
 */
public interface AbstractSearchableMetricSpace<T> extends AbstractObjectMetricSpace<T> {
	
	/**
	 * This function returns the index of the point within the metric space nearest to 
	 * the given query point.
	 * 
	 * @param queryPoint the query point
	 * @return the index of the nearest point in the metric space
	 */
	public int getNearestPointIndex(T queryPoint);
	
	/**
	 * This function returns the set of indices of points that are contained in the open ball
	 * of radius epsilon, centered at the specified query point. That is it returns all points
	 * satisfying d(p_i, q) < epsilon, where q is the query point.
	 * 
	 * @param queryPoint the center of the open ball
	 * @param epsilon the radius of the open ball
	 * @return the set of indices of points contained in the open ball
	 */
	public TIntHashSet getOpenNeighborhood(T queryPoint, double epsilon);
	
	/**
	 * This function returns the set of indices of points that are contained in the closed ball
	 * of radius epsilon, centered at the specified query point. That is it returns all points
	 * satisfying d(p_i, q) <= epsilon, where q is the query point.
	 * 
	 * @param queryPoint the center of the closed ball
	 * @param epsilon the radius of the closed ball
	 * @return the set of indices of points contained in the closed ball
	 */
	public TIntHashSet getClosedNeighborhood(T queryPoint, double epsilon);
	
	/**
	 * This function returns the indices of the k points that are closest to the given query point.
	 * 
	 * @param queryPoint the reference point
	 * @param k the number of nearest neighbors to get
	 * @return the k-nearest neighbors of the query point
	 */
	public TIntHashSet getKNearestNeighbors(T queryPoint, int k);
}
