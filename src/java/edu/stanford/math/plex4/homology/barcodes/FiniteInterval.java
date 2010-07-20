package edu.stanford.math.plex4.homology.barcodes;

import edu.stanford.math.plex4.utility.ExceptionUtility;
import edu.stanford.math.plex4.utility.MathUtility;

public class FiniteInterval implements HalfOpenInterval, Comparable<HalfOpenInterval> {
	private final double start;
	private final double end;
	
	public FiniteInterval(double start, double end) {
		ExceptionUtility.verifyLessThan(start, end);
		this.start = start;
		this.end = end;
	}
	
	public boolean containsPoint(double point) {
		return (start <= point && point < end);
	}

	public boolean isInfinite() {
		return false;
	}

	public boolean isLeftInfinite() {
		return false;
	}

	public boolean isRightInfinite() {
		return false;
	}

	
	public String toString() {
		return String.format("[%f, %f)", start, end);
	}

	public double getEnd() {
		return end;
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
		if (!(obj instanceof FiniteInterval))
			return false;
		FiniteInterval other = (FiniteInterval) obj;
		if (Double.doubleToLongBits(end) != Double.doubleToLongBits(other.end))
			return false;
		if (Double.doubleToLongBits(start) != Double.doubleToLongBits(other.start))
			return false;
		return true;
	}

	public int compareTo(HalfOpenInterval arg0) {
		if (arg0 instanceof RightInfiniteInterval) {
			return -1;
		} else if (arg0 instanceof LeftInfiniteInterval) {
			return 1;
		} else if (arg0 instanceof FiniteInterval) {
			if (this.start > arg0.getStart()) {
				return 1;
			} else {
				return MathUtility.signum(this.end - arg0.getEnd());
			}
		} else {
			throw new UnsupportedOperationException();
		}
	}
}
