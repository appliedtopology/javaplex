package edu.stanford.math.plex_plus.homology.barcodes;

import edu.stanford.math.plex_plus.utility.Infinity;

public class PersistenceInterval {
	private final double start;
	private final double end;
	
	public PersistenceInterval(double start, double end) {
		//ExceptionUtility.verifyLessThanOrEqual(start, end);
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
		this.end = Infinity.getPositiveInfinity();
	}
	
	public boolean isInfinite() {
		return Infinity.isPositiveInfinity(this.end);
	}
	
	public double getStart() {
		return this.start;
	}
	
	public double getEnd() {
		return this.end;
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
