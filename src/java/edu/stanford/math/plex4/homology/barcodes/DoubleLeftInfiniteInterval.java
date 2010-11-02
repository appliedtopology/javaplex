package edu.stanford.math.plex4.homology.barcodes;

import edu.stanford.math.plex4.utility.ComparisonUtility;
import edu.stanford.math.plex4.utility.MathUtility;

/**
 * This class implements an interval of the form [-infinity, end).
 * 
 * @author Andrew Tausz
 *
 */
public class DoubleLeftInfiniteInterval implements DoubleHalfOpenInterval {
	
	/**
	 * The end point of the interval.
	 */
	private final double end;
	
	/**
	 * This constructor initializes the class with the specified end point of the
	 * interval.
	 * 
	 * @param end the end point of the interval
	 */
	public DoubleLeftInfiniteInterval(double end) {
		this.end = end;
	}
	
	/* (non-Javadoc)
	 * @see edu.stanford.math.plex4.homology.barcodes.DoubleHalfOpenInterval#containsPoint(double)
	 */
	public boolean containsPoint(double point) {
		return (point < end);
	}

	/* (non-Javadoc)
	 * @see edu.stanford.math.plex4.homology.barcodes.DoubleHalfOpenInterval#isInfinite()
	 */
	public boolean isInfinite() {
		return true;
	}

	/* (non-Javadoc)
	 * @see edu.stanford.math.plex4.homology.barcodes.DoubleHalfOpenInterval#isLeftInfinite()
	 */
	public boolean isLeftInfinite() {
		return true;
	}

	/* (non-Javadoc)
	 * @see edu.stanford.math.plex4.homology.barcodes.DoubleHalfOpenInterval#isRightInfinite()
	 */
	public boolean isRightInfinite() {
		return false;
	}
	
	/* (non-Javadoc)
	 * @see edu.stanford.math.plex4.homology.barcodes.DoubleHalfOpenInterval#getStart()
	 */
	public double getStart() {
		throw new UnsupportedOperationException();
	}
	
	/* (non-Javadoc)
	 * @see edu.stanford.math.plex4.homology.barcodes.DoubleHalfOpenInterval#getEnd()
	 */
	public double getEnd() {
		return this.end;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return String.format("[-infinity, %f)", end);
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(end);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof DoubleLeftInfiniteInterval))
			return false;
		DoubleLeftInfiniteInterval other = (DoubleLeftInfiniteInterval) obj;
		if (!ComparisonUtility.compareDoublesGuardedRelative(end, other.end, ComparisonUtility.MED_PRECISION))
			return false;
		return true;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(DoubleHalfOpenInterval arg0) {
		if (arg0 instanceof DoubleRightInfiniteInterval) {
			return -1;
		} else if (arg0 instanceof DoubleFiniteInterval) {
			return 1;
		} else if (arg0 instanceof DoubleLeftInfiniteInterval) {
			DoubleLeftInfiniteInterval other = (DoubleLeftInfiniteInterval) arg0;
			
			// return the one of greater length
			return MathUtility.signum(this.end - other.end);
		} else {
			throw new UnsupportedOperationException();
		}
	}
}
