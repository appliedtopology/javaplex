package edu.stanford.math.plex4.homology.barcodes;

/**
 * This interface defines the functionality of an interval of the form [a, b), where
 * either a or b are finite or infinite.
 * 
 * @author Andrew Tausz
 *
 */
public interface DoubleHalfOpenInterval extends Comparable<DoubleHalfOpenInterval> {
	
	/**
	 * This function returns true if the interval is left-infinite and false otherwise.
	 * 
	 * @return true if the interval is left-infinite
	 */
	public boolean isLeftInfinite();
	
	/**
	 * This function returns true if the interval is right-infinite and false otherwise.
	 * 
	 * @return true if the interval is right-infinite
	 */
	public boolean isRightInfinite();
	
	/**
	 * This function returns true if the interval is infinite and false otherwise.
	 * 
	 * @return true if the interval is infinite
	 */
	public boolean isInfinite();
	
	/**
	 * This function returns true if the interval contains the supplied point and
	 * false otherwise.
	 * 
	 * @param point the point to test 
	 * @return true if the interval contains the given point
	 */
	public boolean containsPoint(double point);
	
	/**
	 * Returns the start of the interval.
	 * 
	 * @return the starting point of the interval
	 */
	public double getStart();
	
	/**
	 * Returns the end of the interval.
	 * 
	 * @return the ending point of the interval
	 */
	public double getEnd();
}
