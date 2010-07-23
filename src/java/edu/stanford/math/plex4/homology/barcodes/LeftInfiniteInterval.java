package edu.stanford.math.plex4.homology.barcodes;

import edu.stanford.math.plex4.utility.ComparisonUtility;
import edu.stanford.math.plex4.utility.MathUtility;

public class LeftInfiniteInterval implements HalfOpenInterval {
	private final double end;
	
	public LeftInfiniteInterval(double end) {
		this.end = end;
	}
	
	public boolean containsPoint(double point) {
		return (point < end);
	}

	public boolean isInfinite() {
		return true;
	}

	public boolean isLeftInfinite() {
		return true;
	}

	public boolean isRightInfinite() {
		return false;
	}
	
	public String toString() {
		return String.format("[-infinity, %f)", end);
	}
	
	public double getEnd() {
		return end;
	}

	public double getStart() {
		throw new UnsupportedOperationException();
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
		if (!(obj instanceof LeftInfiniteInterval))
			return false;
		LeftInfiniteInterval other = (LeftInfiniteInterval) obj;
		if (!ComparisonUtility.compareDoublesGuardedRelative(end, other.end, ComparisonUtility.MED_PRECISION))
			return false;
		return true;
	}
	
	public int compareTo(HalfOpenInterval arg0) {
		if (arg0 instanceof RightInfiniteInterval) {
			return -1;
		} else if (arg0 instanceof FiniteInterval) {
			return -1;
		} else if (arg0 instanceof LeftInfiniteInterval) {
			return MathUtility.signum(this.end - arg0.getEnd());
		} else {
			throw new UnsupportedOperationException();
		}
	}
}
