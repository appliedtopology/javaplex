package edu.stanford.math.plex4.homology.barcodes;

import edu.stanford.math.plex4.utility.ComparisonUtility;
import edu.stanford.math.plex4.utility.MathUtility;

/**
 * This class implements a right-infinite interval of the form [start, infinity)
 * 
 * @author Andrew Tausz
 *
 */
public class DoubleRightInfiniteInterval implements DoubleHalfOpenInterval {
	
	/**
	 * The starting point of the interval.
	 */
	private final double start;

	/**
	 * Constructor which initializes the interval to [start, infinity)
	 * 
	 * @param start the starting point of the interval
	 */
	public DoubleRightInfiniteInterval(double start) {
		this.start = start;
	}

	/* (non-Javadoc)
	 * @see edu.stanford.math.plex4.homology.barcodes.DoubleHalfOpenInterval#containsPoint(double)
	 */
	public boolean containsPoint(double point) {
		return (start <= point);
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
		return false;
	}

	/* (non-Javadoc)
	 * @see edu.stanford.math.plex4.homology.barcodes.DoubleHalfOpenInterval#isRightInfinite()
	 */
	public boolean isRightInfinite() {
		return true;
	}

	/* (non-Javadoc)
	 * @see edu.stanford.math.plex4.homology.barcodes.DoubleHalfOpenInterval#getStart()
	 */
	public double getStart() {
		return this.start;
	}
	
	/* (non-Javadoc)
	 * @see edu.stanford.math.plex4.homology.barcodes.DoubleHalfOpenInterval#getEnd()
	 */
	public double getEnd() {
		throw new UnsupportedOperationException();
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return String.format("[%f, infinity)", start);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(start);
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
		if (!(obj instanceof DoubleRightInfiniteInterval))
			return false;
		DoubleRightInfiniteInterval other = (DoubleRightInfiniteInterval) obj;
		if (!ComparisonUtility.compareDoublesGuardedRelative(start, other.start, ComparisonUtility.MED_PRECISION))
			return false;
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(DoubleHalfOpenInterval arg0) {
		if (arg0 instanceof DoubleLeftInfiniteInterval) {
			return 1;
		} else if (arg0 instanceof DoubleFiniteInterval) {
			return 1;
		} else if (arg0 instanceof DoubleRightInfiniteInterval) {
			DoubleRightInfiniteInterval other = (DoubleRightInfiniteInterval) arg0;
			
			// return the one of greater length
			return MathUtility.signum(other.start - this.start);
		} else {
			throw new UnsupportedOperationException();
		}
	}
}
