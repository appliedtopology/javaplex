/**
 * 
 */
package edu.stanford.math.plex_plus.math.interfaces;

/**
 * This interface defines the functionality of
 * a metric space over a generic type. 
 * 
 * @author Andrew Tausz
 *
 * @param <T> The type over which the metric space is defined
 */
public interface GenericMetricSpace<T> {
	public double distance(T a, T b);
}
