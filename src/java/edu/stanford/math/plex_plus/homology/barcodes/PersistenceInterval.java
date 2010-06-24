package edu.stanford.math.plex_plus.homology.barcodes;

import edu.stanford.math.plex_plus.utility.ExceptionUtility;
import edu.stanford.math.plex_plus.utility.Infinity;

/**
 * This class implements the operations of an interval. It can 
 * either represent the interval [start, end], or [start, infinity).
 * 
 * @author Andrew Tausz
 *
 */
public class PersistenceInterval {
	private final double start;
	private final double end;
	
	/**
	 * This constructor initializes the interval to be the finite
	 * interval [start, end].
	 * 
	 * @param start the starting point of the interval
	 * @param end the ending point of the interval
	 */
	public PersistenceInterval(double start, double end) {
		ExceptionUtility.verifyLessThanOrEqual(start, end);
		this.start = start;
		this.end = end;
	}
	
	/**
	 * This constructor initializes the interval to be [start, infinity)
	 * 
	 * @param start the starting point of the interval
	 */
	public PersistenceInterval(double start) {
		this.start = start;
		this.end = Infinity.Double.getPositiveInfinity();
	}
	
	/**
	 * This function returns true if the interval is of the form
	 * [start, infinity), and false otherwise.
	 * 
	 * @return returns true if the interval is infinite
	 */
	public boolean isInfinite() {
		return Infinity.Double.isPositiveInfinity(this.end);
	}
	
	/**
	 * Gets the starting point of the interval.
	 * 
	 * @return the starting point of the interval
	 */
	public double getStart() {
		return this.start;
	}
	
	/**
	 * Gets the ending point of the interval.
	 * 
	 * @return the ending point of the interval
	 */
	public double getEnd() {
		return this.end;
	}
	
	/**
	 * This functions return true if the supplied point is a
	 * member of the interval.
	 * 
	 * @param point the point to query
	 * @return true if the point is in the interval and false otherwise
	 */
	public boolean containsPoint(double point) {
		return (point >= this.start && point < this.end);
	}
	
	@Override
	public String toString() {
		if (this.isInfinite()) {
			return ("[" + start + ", infinity)");
		} else {
			return ("[" + start + ", " + end + ")");
		}
	}
}
