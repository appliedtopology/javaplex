package edu.stanford.math.plex4.homology.barcodes;

import edu.stanford.math.plex4.utility.ExceptionUtility;
import gnu.trove.TIntIntHashMap;
import gnu.trove.TIntObjectHashMap;
import gnu.trove.TIntObjectIterator;

/**
 * This class implements functionality for storing a collection of barcodes with 
 * their generators. It is designed to be the output of a persistent homology or 
 * cohomology algorithm so that it contains the persistence intervals at each dimension.
 * 
 * @author Andrew Tausz
 *
 */
public class IntAugmentedBarcodeCollection<T> {
	private final TIntObjectHashMap<IntAugmentedBarcode<T>> barcodeMap = new TIntObjectHashMap<IntAugmentedBarcode<T>>();
	
	/**
	 * This function adds an interval at the specified dimension.
	 * 
	 * @param dimension the dimension to add to
	 * @param interval the interval to add
	 * @param generatingCycle the generating cycle
	 */
	public void addInterval(int dimension, IntHalfOpenInterval interval, T generatingCycle) {
		ExceptionUtility.verifyNonNull(interval);
		if (!this.barcodeMap.containsKey(dimension)) {
			this.barcodeMap.put(dimension, new IntAugmentedBarcode<T>(dimension));
		}
		this.barcodeMap.get(dimension).addInterval(interval, generatingCycle);
	}
	
	/**
	 * This function adds the specified finite interval [start, end] at the
	 * supplied dimension.
	 * 
	 * @param dimension the dimension to add to
	 * @param start the starting point of the interval
	 * @param end the ending point of the interval
	 * @param generatingCycle the generating cycle
	 */
	public void addInterval(int dimension, int start, int end, T generatingCycle) {
		this.addInterval(dimension, new IntFiniteInterval(start, end), generatingCycle);
	}
	
	/**
	 * This function adds the specified semi-infinite interval [start, infinity)
	 * at the supplied dimension.
	 * 
	 * @param dimension the dimension to add to
	 * @param start the starting point of the interval
	 * @param generatingCycle the generating cycle
	 */
	public void addRightInfiniteInterval(int dimension, int start, T generatingCycle) {
		this.addInterval(dimension, new IntRightInfiniteInterval(start), generatingCycle);
	}
	
	/**
	 * This function adds the specified semi-infinite intervals [-infinity, end)
	 * at the supplied dimension
	 * 
	 * @param dimension the dimension to add to
	 * @param end the ending point of the interval
	 * @param generatingCycle the generating cycle
	 */
	public void addLeftInfiniteInterval(int dimension, int end, T generatingCycle) {
		this.addInterval(dimension, new IntLeftInfiniteInterval(end), generatingCycle);
	}
	
	/**
	 * This function returns a barcode collection containing only the infinite intervals.
	 * 
	 * @return a barcode collection with only inifinite intervals
	 */
	public IntAugmentedBarcodeCollection<T> getInfiniteIntervals() {
		IntAugmentedBarcodeCollection<T> collection = new IntAugmentedBarcodeCollection<T>();
		
		for (TIntObjectIterator<IntAugmentedBarcode<T>> iterator = this.barcodeMap.iterator(); iterator.hasNext(); ) {
			iterator.advance();
			collection.barcodeMap.put(iterator.key(), iterator.value().getInfiniteIntervals());
		}
		
		return collection;
	}
	
	/**
	 * This function returns the barcode at the specified dimension.
	 * 
	 * @param dimension
	 * @return the barcode at the specified dimension
	 */
	public IntAugmentedBarcode<T> getBarcode(int dimension) {
		return this.barcodeMap.get(dimension);
	}
	
	/**
	 * This function computes the Betti numbers for a particular filtration
	 * value. It returns the results as a map which maps the dimension to the
	 * Betti number.
	 * 
	 * @param filtrationValue the filtrationValue to compute the Betti numbers at
	 * @return a TIntIntHashMap mapping dimension to the Betti number
	 */
	public TIntIntHashMap getBettiNumbers(int filtrationValue) {
		TIntIntHashMap map = new TIntIntHashMap();
		
		for (TIntObjectIterator<IntAugmentedBarcode<T>> iterator = this.barcodeMap.iterator(); iterator.hasNext(); ) {
			iterator.advance();
			map.put(iterator.key(), iterator.value().getSliceCardinality(filtrationValue));
		}
		
		return map;
	}
	
	/**
	 * Returns an iterator.
	 * 
	 * @return an iterator
	 */
	public TIntObjectIterator<IntAugmentedBarcode<T>> iterator() {
		return this.barcodeMap.iterator();
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		
		for (TIntObjectIterator<IntAugmentedBarcode<T>> iterator = this.barcodeMap.iterator(); iterator.hasNext(); ) {
			iterator.advance();
			builder.append(iterator.value().toString());
			builder.append("\n");
		}
		
		return builder.toString();
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((barcodeMap == null) ? 0 : barcodeMap.hashCode());
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
		IntAugmentedBarcodeCollection<?> other = (IntAugmentedBarcodeCollection<?>) obj;
		if (barcodeMap == null) {
			if (other.barcodeMap != null)
				return false;
		} else if (!barcodeMap.equals(other.barcodeMap))
			return false;
		return true;
	}
}
