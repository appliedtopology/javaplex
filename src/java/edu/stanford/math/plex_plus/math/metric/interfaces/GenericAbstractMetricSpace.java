/**
 * 
 */
package edu.stanford.math.plex_plus.math.metric.interfaces;

/**
 * This interface defines the functionality of
 * a metric space over a generic type. 
 * 
 * @author Andrew Tausz
 *
 * @param <T> The type over which the metric space is defined
 */
public interface GenericAbstractMetricSpace<T> {
	public double distance(T a, T b);
}
