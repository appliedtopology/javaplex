package edu.stanford.math.plex4.homology.barcodes;

import java.util.ArrayList;
import java.util.List;

import edu.stanford.math.plex4.utility.ComparisonUtility;
import edu.stanford.math.plex4.utility.ExceptionUtility;
import edu.stanford.math.primitivelib.autogen.pair.ObjectObjectPair;

/**
 * This class implements the functionality of a barcode, which is an
 * ordered collection of persistence intervals along with a dimension 
 * specifier. Additionally, each interval is annotated with a generating
 * object (usually a generating cycle).
 * 
 * @author Andrew Tausz
 *
 */
public class DoubleAugmentedBarcode<T> {
	private final int dimension;
	private final List<ObjectObjectPair<DoubleHalfOpenInterval, T>> intervals = new ArrayList<ObjectObjectPair<DoubleHalfOpenInterval, T>>();
	/**
	 * This constructor initializes the barcode to be empty, with the
	 * specified dimension.
	 * 
	 * @param dimension the dimension to initialize to
	 */
	public DoubleAugmentedBarcode(int dimension) {
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
	 * Gets the number of intervals in the barcode.
	 * 
	 * @return the number of intervals in the barcode
	 */
	public int getSize() {
		return this.intervals.size();
	}
	
	/**
	 * Gets the interval at the given index.
	 * 
	 * @param index the index
	 * @return the interval at the given index
	 */
	public DoubleHalfOpenInterval getInterval(int index) {
		return this.intervals.get(index).getFirst();
	}
	
	/**
	 * Gets the generating cycle at the given index.
	 * 
	 * @param index the index
	 * @return the generating cycle at the given index
	 */
	public T getGeneratingCycle(int index) {
		return this.intervals.get(index).getSecond();
	}
	
	/**
	 * This returns an augmented collection containing only the infinite intervals and their
	 * generators.
	 * 
	 * @return an augmented barcode containing the infinite intervals
	 */
	public DoubleAugmentedBarcode<T> getInfiniteIntervals() {
		DoubleAugmentedBarcode<T> infiniteBarcode = new DoubleAugmentedBarcode<T>(this.dimension);
		for (ObjectObjectPair<DoubleHalfOpenInterval, T> pair: this.intervals) {
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
	public void addInterval(DoubleHalfOpenInterval interval, T representative) {
		ExceptionUtility.verifyNonNull(interval);
		ExceptionUtility.verifyNonNull(representative);
		this.intervals.add(new ObjectObjectPair<DoubleHalfOpenInterval, T>(interval, representative));
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
		
		for (ObjectObjectPair<DoubleHalfOpenInterval, T> pair: this.intervals) {
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
		for (ObjectObjectPair<DoubleHalfOpenInterval, T> pair: this.intervals) {
			builder.append(pair.getFirst().toString());
			builder.append(": ");
			builder.append(pair.getSecond().toString());
			builder.append("\n");
		}
		
		return builder.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + dimension;
		result = prime * result + ((intervals == null) ? 0 : intervals.hashCode());
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
		DoubleAugmentedBarcode<?> other = (DoubleAugmentedBarcode<?>) obj;
		if (dimension != other.dimension)
			return false;
		if (intervals == null) {
			if (other.intervals != null)
				return false;
		} else if (!ComparisonUtility.setEquals(intervals, other.intervals))
			return false;
		return true;
	}
}
