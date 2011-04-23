package edu.stanford.math.plex4.metric.interfaces;

/**
 * This interface defines the functionality of a finite metric space whose points are
 * of type T. The points in the metric space are indexed by the values {0, ..., size - 1}
 * 
 * @author Andrew Tausz
 *
 * @param <T> the type of the points in the metric space
 */
public interface AbstractObjectMetricSpace<T> extends AbstractIntMetricSpace, AbstractObjectMetric<T> {
	
	/**
	 * This function returns the point at the specified index.
	 * 
	 * @param index the index of the point to retrieve
	 * @return the point at the given index
	 */
	public T getPoint(int index);
	
	/**
	 * This function returns the set of points in the metric space as an array. The returned
	 * array satisfies the requirement that the indices of the points in the array are equal to the
	 * indices of the points within the metric space.
	 * 
	 * @return an array containing the points of the metric space
	 */
	public T[] getPoints();
}
