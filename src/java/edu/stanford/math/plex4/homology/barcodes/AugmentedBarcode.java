package edu.stanford.math.plex4.homology.barcodes;

import java.util.ArrayList;
import java.util.List;

import edu.stanford.math.plex4.datastructures.pairs.GenericPair;
import edu.stanford.math.plex4.utility.ExceptionUtility;

public class AugmentedBarcode<T> {
	private final int dimension;
	private final List<GenericPair<HalfOpenInterval, T>> intervals = new ArrayList<GenericPair<HalfOpenInterval, T>>();
	/**
	 * This constructor initializes the barcode to be empty, with the
	 * specified dimension.
	 * 
	 * @param dimension the dimension to initialize to
	 */
	public AugmentedBarcode(int dimension) {
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
	
	public int getSize() {
		return this.intervals.size();
	}
	
	public HalfOpenInterval getInterval(int index) {
		return this.intervals.get(index).getFirst();
	}
	
	public T getGeneratingCycle(int index) {
		return this.intervals.get(index).getSecond();
	}
	
	public AugmentedBarcode<T> getInfiniteIntervals() {
		AugmentedBarcode<T> infiniteBarcode = new AugmentedBarcode<T>(this.dimension);
		for (GenericPair<HalfOpenInterval, T> pair: this.intervals) {
			if (pair.getFirst().isInfinite()) {
				infiniteBarcode.addInterval(pair.getFirst(), pair.getSecond());
			}
		}
		return infiniteBarcode;
	}
	
	/**
	 * This function adds the specified interval to the barcode. The interval
	 * is added to the end of the already existing list of intervals.
	 * 
	 * @param interval the PersistentInterval to add
	 */
	public void addInterval(HalfOpenInterval interval, T representative) {
		ExceptionUtility.verifyNonNull(interval);
		ExceptionUtility.verifyNonNull(representative);
		this.intervals.add(new GenericPair<HalfOpenInterval, T>(interval, representative));
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
		
		for (GenericPair<HalfOpenInterval, T> pair: this.intervals) {
			if (pair.getFirst().containsPoint(point)) {
				cardinality++;
			}
		}
		
		return cardinality;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		
		builder.append("Dimension: " + this.dimension + "\n");
		for (GenericPair<HalfOpenInterval, T> pair: this.intervals) {
			builder.append(pair.getFirst().toString());
			builder.append(": ");
			builder.append(pair.getSecond().toString());
			builder.append("\n");
		}
		
		return builder.toString();
	}
	
}
