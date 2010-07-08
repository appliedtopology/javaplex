package edu.stanford.math.plex_plus.homology.barcodes;

import edu.stanford.math.plex_plus.utility.Infinity;

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
		return ("[-infinity, " + end + ")");
	}
	
	public double getEnd() {
		return end;
	}

	public double getStart() {
		return Infinity.Double.getNegativeInfinity();
	}
}
