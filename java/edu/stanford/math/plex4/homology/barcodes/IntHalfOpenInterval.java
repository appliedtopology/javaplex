package edu.stanford.math.plex4.homology.barcodes;

/**
 * This interface defines the functionality of an interval of the form [a, b), where
 * either a or b are finite or infinite.
 * 
 * @author Andrew Tausz
 *
 */
public interface IntHalfOpenInterval extends Comparable<IntHalfOpenInterval> {
	
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
	public boolean containsPoint(int point);
	
	/**
	 * Returns the start of the interval.
	 * 
	 * @return the starting point of the interval
	 */
	public int getStart();
	
	/**
	 * Returns the end of the interval.
	 * 
	 * @return the ending point of the interval
	 */
	public int getEnd();
}
