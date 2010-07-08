package edu.stanford.math.plex_plus.homology.barcodes;

import edu.stanford.math.plex_plus.utility.ExceptionUtility;

public class FiniteInterval implements HalfOpenInterval {
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
		return ("[" + start + ", " + end + ")");
	}

	public double getEnd() {
		return end;
	}

	public double getStart() {
		return start;
	}
}
