package edu.stanford.math.plex4.homology.barcodes;

import edu.stanford.math.plex4.utility.MathUtility;

/**
 * This class implements an interval of the form [-infinity, end).
 * 
 * @author Andrew Tausz
 *
 */
public class IntLeftInfiniteInterval implements IntHalfOpenInterval {
	
	/**
	 * The end point of the interval.
	 */
	private final int end;
	
	/**
	 * This constructor initializes the class with the specified end point of the
	 * interval.
	 * 
	 * @param end the end point of the interval
	 */
	public IntLeftInfiniteInterval(int end) {
		this.end = end;
	}
	
	/* (non-Javadoc)
	 * @see edu.stanford.math.plex4.homology.barcodes.IntHalfOpenInterval#containsPoint(int)
	 */
	public boolean containsPoint(int point) {
		return (point < end);
	}

	/* (non-Javadoc)
	 * @see edu.stanford.math.plex4.homology.barcodes.IntHalfOpenInterval#isInfinite()
	 */
	public boolean isInfinite() {
		return true;
	}

	/* (non-Javadoc)
	 * @see edu.stanford.math.plex4.homology.barcodes.IntHalfOpenInterval#isLeftInfinite()
	 */
	public boolean isLeftInfinite() {
		return true;
	}

	/* (non-Javadoc)
	 * @see edu.stanford.math.plex4.homology.barcodes.IntHalfOpenInterval#isRightInfinite()
	 */
	public boolean isRightInfinite() {
		return false;
	}
	
	/* (non-Javadoc)
	 * @see edu.stanford.math.plex4.homology.barcodes.DoubleHalfOpenInterval#getStart()
	 */
	public int getStart() {
		throw new UnsupportedOperationException();
	}
	
	/* (non-Javadoc)
	 * @see edu.stanford.math.plex4.homology.barcodes.DoubleHalfOpenInterval#getEnd()
	 */
	public int getEnd() {
		return this.end;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return String.format("[-infinity, %d)", end);
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(IntHalfOpenInterval arg0) {
		if (arg0 instanceof IntRightInfiniteInterval) {
			return -1;
		} else if (arg0 instanceof IntFiniteInterval) {
			return 1;
		} else if (arg0 instanceof IntLeftInfiniteInterval) {
			IntLeftInfiniteInterval other = (IntLeftInfiniteInterval) arg0;
			
			// return the one of greater length
			return MathUtility.signum(this.end - other.end);
		} else {
			throw new UnsupportedOperationException();
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + end;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		IntLeftInfiniteInterval other = (IntLeftInfiniteInterval) obj;
		if (end != other.end)
			return false;
		return true;
	}
}
