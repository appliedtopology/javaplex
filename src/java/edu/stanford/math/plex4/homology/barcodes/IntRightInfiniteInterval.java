package edu.stanford.math.plex4.homology.barcodes;

import edu.stanford.math.plex4.utility.MathUtility;

/**
 * This class implements a right-infinite interval of the form [start, infinity)
 * 
 * @author Andrew Tausz
 *
 */
public class IntRightInfiniteInterval implements IntHalfOpenInterval {
	
	/**
	 * The starting point of the interval.
	 */
	private final int start;

	/**
	 * Constructor which initializes the interval to [start, infinity)
	 * 
	 * @param start the starting point of the interval
	 */
	public IntRightInfiniteInterval(int start) {
		this.start = start;
	}

	/* (non-Javadoc)
	 * @see edu.stanford.math.plex4.homology.barcodes.IntHalfOpenInterval#containsPoint(int)
	 */
	public boolean containsPoint(int point) {
		return (start <= point);
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
		return false;
	}

	/* (non-Javadoc)
	 * @see edu.stanford.math.plex4.homology.barcodes.IntHalfOpenInterval#isRightInfinite()
	 */
	public boolean isRightInfinite() {
		return true;
	}

	/* (non-Javadoc)
	 * @see edu.stanford.math.plex4.homology.barcodes.DoubleHalfOpenInterval#getStart()
	 */
	public int getStart() {
		return this.start;
	}
	
	/* (non-Javadoc)
	 * @see edu.stanford.math.plex4.homology.barcodes.DoubleHalfOpenInterval#getEnd()
	 */
	public int getEnd() {
		throw new UnsupportedOperationException();
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return String.format("[%d, infinity)", start);
	}


	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(IntHalfOpenInterval arg0) {
		if (arg0 instanceof IntLeftInfiniteInterval) {
			return 1;
		} else if (arg0 instanceof IntFiniteInterval) {
			return 1;
		} else if (arg0 instanceof IntRightInfiniteInterval) {
			IntRightInfiniteInterval other = (IntRightInfiniteInterval) arg0;
			
			// return the one of greater length
			return MathUtility.signum(other.start - this.start);
		} else {
			throw new UnsupportedOperationException();
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + start;
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
		IntRightInfiniteInterval other = (IntRightInfiniteInterval) obj;
		if (start != other.start)
			return false;
		return true;
	}
	
	public void draw() {
		for (int i = 0; i < this.start; i++) {
			System.out.print(' ');
		}
		
		System.out.println("[->");
	}
}
