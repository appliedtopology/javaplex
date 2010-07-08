package edu.stanford.math.plex_plus.homology.barcodes;

import edu.stanford.math.plex_plus.utility.Infinity;

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
		return ("[" + start + ", infinity)");
	}

	public double getEnd() {
		return Infinity.Double.getPositiveInfinity();
	}

	public double getStart() {
		return start;
	}
}
