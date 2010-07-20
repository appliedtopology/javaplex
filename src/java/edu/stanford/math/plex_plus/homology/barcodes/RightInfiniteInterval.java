package edu.stanford.math.plex_plus.homology.barcodes;

import edu.stanford.math.plex_plus.utility.Infinity;
import edu.stanford.math.plex_plus.utility.MathUtility;

public class RightInfiniteInterval implements HalfOpenInterval {
	private final double start;
	
	public RightInfiniteInterval(double start) {
		this.start = start;
	}
	
	public boolean containsPoint(double point) {
		return (point >= start);
	}

	public boolean isInfinite() {
		return true;
	}

	public boolean isLeftInfinite() {
		return false;
	}

	public boolean isRightInfinite() {
		return true;
	}
	
	public String toString() {
		return String.format("[%f, infinity)", start);
	}

	public double getEnd() {
		return Infinity.Double.getPositiveInfinity();
	}

	public double getStart() {
		return start;
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
		if (!(obj instanceof RightInfiniteInterval))
			return false;
		RightInfiniteInterval other = (RightInfiniteInterval) obj;
		if (Double.doubleToLongBits(start) != Double.doubleToLongBits(other.start))
			return false;
		return true;
	}
	
	public int compareTo(HalfOpenInterval arg0) {
		if (arg0 instanceof LeftInfiniteInterval) {
			return 1;
		} else if (arg0 instanceof FiniteInterval) {
			return 1;
		} else if (arg0 instanceof LeftInfiniteInterval) {
			return MathUtility.signum(this.start - arg0.getStart());
		} else {
			throw new UnsupportedOperationException();
		}
	}
}
