/**
 * 
 */
package edu.stanford.math.plex_plus.math.metric.interfaces;

import java.util.Set;

/**
 * @author Andrew Tausz
 *
 */
public interface GenericAbstractFiniteMetricSpace<T> extends GenericAbstractMetricSpace<T> {
	public int size();
	public T getNearestPoint(T queryPoint);
	public Set<T> getNeighborhood(T queryPoint, double epsilon);
	public Set<T> getKNearestNeighbors(T queryPoint, int k);
}
