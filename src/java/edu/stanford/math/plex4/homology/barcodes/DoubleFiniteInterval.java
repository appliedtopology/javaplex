package edu.stanford.math.plex4.homology.barcodes;

import edu.stanford.math.plex4.utility.ComparisonUtility;
import edu.stanford.math.plex4.utility.ExceptionUtility;
import edu.stanford.math.plex4.utility.MathUtility;

/**
 * This class defines the functionality of a finite interval of 
 * the form [start, end).
 * 
 * @author Andrew Tausz
 *
 */
public class DoubleFiniteInterval implements DoubleHalfOpenInterval, Comparable<DoubleHalfOpenInterval> {
	private final double start;
	private final double end;
	
	/**
	 * This constructor initializes the class with the given start and end points.
	 * 
	 * @param start the starting point
	 * @param end the ending point
	 */
	public DoubleFiniteInterval(double start, double end) {
		ExceptionUtility.verifyLessThan(start, end);
		this.start = start;
		this.end = end;
	}
	
	/* (non-Javadoc)
	 * @see edu.stanford.math.plex4.homology.barcodes.DoubleHalfOpenInterval#containsPoint(double)
	 */
	public boolean containsPoint(double point) {
		return (start <= point && point < end);
	}

	/* (non-Javadoc)
	 * @see edu.stanford.math.plex4.homology.barcodes.DoubleHalfOpenInterval#isInfinite()
	 */
	public boolean isInfinite() {
		return false;
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
		return false;
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
		return this.end;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return String.format("[%f, %f)", start, end);
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
		if (!(obj instanceof DoubleFiniteInterval))
			return false;
		DoubleFiniteInterval other = (DoubleFiniteInterval) obj;
		if (!ComparisonUtility.compareDoublesGuardedRelative(start, other.start, ComparisonUtility.MED_PRECISION))
			return false;
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
		} else if (arg0 instanceof DoubleLeftInfiniteInterval) {
			return -1;
		} else if (arg0 instanceof DoubleFiniteInterval) {
			DoubleFiniteInterval other = (DoubleFiniteInterval) arg0;
			double thisLength = this.end - this.start;
			double otherLength = other.end - other.start;

			// compare the lengths of the intervals - the longer one is "greater"
			if (thisLength != otherLength) {
				return MathUtility.signum(thisLength - otherLength);
			}
			
			// if they are of equal length, return the one that starts first
			return MathUtility.signum(this.start - other.start);
		} else {
			throw new UnsupportedOperationException();
		}
	}
}
