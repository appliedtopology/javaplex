package edu.stanford.math.plex4.metric.interfaces;

/**
 * This interface defines the functionality of a finite metric space where
 * the objects are indexed by integers. Note that the indices are designed
 * to be in the range {0, ..., size - 1}. 
 * 
 * @author Andrew Tausz
 *
 */
public interface AbstractIntMetricSpace {
	
	/**
	 * This function returns the distance between the object at index i and the
	 * object at index j.
	 * 
	 * @param i the index of the first object
	 * @param j the index of the second object
	 * @return the distance between the objects at the two given indices
	 */
	public double distance(int i, int j);
	
	/**
	 * This function returns the number of points in the metric space.
	 * 
	 * @return the number of points in the metric space.
	 */
	public int size();
}
