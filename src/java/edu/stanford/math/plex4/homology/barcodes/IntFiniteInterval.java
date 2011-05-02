package edu.stanford.math.plex4.homology.barcodes;

import edu.stanford.math.plex4.utility.ExceptionUtility;
import edu.stanford.math.plex4.utility.MathUtility;

/**
 * This class defines the functionality of a finite interval of 
 * the form [start, end).
 * 
 * @author Andrew Tausz
 *
 */
public class IntFiniteInterval implements IntHalfOpenInterval, Comparable<IntHalfOpenInterval> {
	private final int start;
	private final int end;
	
	/**
	 * This constructor initializes the class with the given start and end points.
	 * 
	 * @param start the starting point
	 * @param end the ending point
	 */
	public IntFiniteInterval(int start, int end) {
		ExceptionUtility.verifyLessThan(start, end);
		this.start = start;
		this.end = end;
	}
	
	/* (non-Javadoc)
	 * @see edu.stanford.math.plex4.homology.barcodes.IntHalfOpenInterval#containsPoint(int)
	 */
	public boolean containsPoint(int point) {
		return (start <= point && point < end);
	}

	/* (non-Javadoc)
	 * @see edu.stanford.math.plex4.homology.barcodes.IntHalfOpenInterval#isInfinite()
	 */
	public boolean isInfinite() {
		return false;
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
		return false;
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
		return this.end;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return String.format("[%d, %d)", start, end);
	}


	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(IntHalfOpenInterval arg0) {
		if (arg0 instanceof IntRightInfiniteInterval) {
			return -1;
		} else if (arg0 instanceof IntLeftInfiniteInterval) {
			return -1;
		} else if (arg0 instanceof IntFiniteInterval) {
			IntFiniteInterval other = (IntFiniteInterval) arg0;
			int thisLength = this.end - this.start;
			int otherLength = other.end - other.start;

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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + end;
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
		IntFiniteInterval other = (IntFiniteInterval) obj;
		if (end != other.end)
			return false;
		if (start != other.start)
			return false;
		return true;
	}

	public void draw() {
		for (int i = 0; i < this.start; i++) {
			System.out.print(' ');
		}
		
		System.out.print('[');
		
		for (int i = 0; i < ((this.end - this.start) - 1); i++) {
			System.out.print('-');
		}
		
		System.out.print(')');
		System.out.print('\n');
	}
}
