package edu.stanford.math.plex4.metric.interfaces;

/**
 * This interface defines a metric on the type T. Note that
 * an implementing class should obey the axioms of a metric space -
 * namely symmetry, positivity and the triangle inequality.
 * 
 * @author Andrew Tausz
 *
 * @param <T> the underlying type of the metric
 */
public interface AbstractObjectMetric<T> {
	
	/**
	 * This function returns the distance between two points.
	 * 
	 * @param a the first point
	 * @param b the second point
	 * @return the distance between the two points
	 */
	public double distance(T a, T b);
}
