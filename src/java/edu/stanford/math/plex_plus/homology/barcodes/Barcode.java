package edu.stanford.math.plex_plus.homology.barcodes;

import java.util.ArrayList;
import java.util.List;

import edu.stanford.math.plex_plus.utility.ExceptionUtility;

/**
 * This class implements the functionality of a barcode, which is an
 * ordered collection of persistence intervals along with a dimension 
 * specifier. 
 * 
 * @author Andrew Tausz
 *
 */
public class Barcode {
	private final int dimension;
	private List<PersistenceInterval> intervals = new ArrayList<PersistenceInterval>();
	
	/**
	 * This constructor initializes the barcode to be empty, with the
	 * specified dimension.
	 * 
	 * @param dimension the dimension to initialize to
	 */
	public Barcode(int dimension) {
		ExceptionUtility.verifyNonNegative(dimension);
		this.dimension = dimension;
	}
	
	/**
	 * Gets the dimension of the barcode.
	 * 
	 * @return the dimension of the barcode
	 */
	public int getDimension() {
		return this.dimension;
	}
	
	/**
	 * This function adds the specified interval to the barcode. The interval
	 * is added to the end of the already existing list of intervals.
	 * 
	 * @param interval the PersistentInterval to add
	 */
	public void addInterval(PersistenceInterval interval) {
		ExceptionUtility.verifyNonNull(interval);
		this.intervals.add(interval);
	}
	
	/**
	 * This function returns the number of "active" intervals at
	 * a particular point.
	 * 
	 * @param point the point to query
	 * @return the number of intervals containing the supplied point
	 */
	public int getSliceCardinality(double point) {
		int cardinality = 0;
		
		for (PersistenceInterval interval: this.intervals) {
			if (interval.containsPoint(point)) {
				cardinality++;
			}
		}
		
		return cardinality;
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
