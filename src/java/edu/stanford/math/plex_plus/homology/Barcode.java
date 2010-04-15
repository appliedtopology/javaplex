package edu.stanford.math.plex_plus.homology;

import java.util.ArrayList;
import java.util.List;

import edu.stanford.math.plex_plus.utility.ExceptionUtility;

public class Barcode {
	private final int dimension;
	private List<PersistenceInterval> intervals = new ArrayList<PersistenceInterval>();
	
	public Barcode(int dimension) {
		ExceptionUtility.verifyNonNegative(dimension);
		this.dimension = dimension;
	}
	
	public int getDimension() {
		return this.dimension;
	}
	
	public void addInterval(PersistenceInterval interval) {
		ExceptionUtility.verifyNonNull(interval);
		this.intervals.add(interval);
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		
		builder.append("Dimension: " + this.dimension + "\n");
		for (PersistenceInterval interval : this.intervals) {
			builder.append(interval.toString());
			builder.append("\n");
		}
		
		return builder.toString();
	}
}
