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
public class DoubleAugmentedBarcodeCollection<T> {
	private final TIntObjectHashMap<DoubleAugmentedBarcode<T>> barcodeMap = new TIntObjectHashMap<DoubleAugmentedBarcode<T>>();
	
	/**
	 * This function adds an interval at the specified dimension.
	 * 
	 * @param dimension the dimension to add to
	 * @param interval the interval to add
	 * @param generatingCycle the generating cycle
	 */
	public void addInterval(int dimension, DoubleHalfOpenInterval interval, T generatingCycle) {
		ExceptionUtility.verifyNonNull(interval);
		if (!this.barcodeMap.containsKey(dimension)) {
			this.barcodeMap.put(dimension, new DoubleAugmentedBarcode<T>(dimension));
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
	public void addInterval(int dimension, double start, double end, T generatingCycle) {
		this.addInterval(dimension, new DoubleFiniteInterval(start, end), generatingCycle);
	}
	
	/**
	 * This function adds the specified semi-infinite interval [start, infinity)
	 * at the supplied dimension.
	 * 
	 * @param dimension the dimension to add to
	 * @param start the starting point of the interval
	 * @param generatingCycle the generating cycle
	 */
	public void addRightInfiniteInterval(int dimension, double start, T generatingCycle) {
		this.addInterval(dimension, new DoubleRightInfiniteInterval(start), generatingCycle);
	}
	
	/**
	 * This function adds the specified semi-infinite intervals [-infinity, end)
	 * at the supplied dimension
	 * 
	 * @param dimension the dimension to add to
	 * @param end the ending point of the interval
	 * @param generatingCycle the generating cycle
	 */
	public void addLeftInfiniteInterval(int dimension, double end, T generatingCycle) {
		this.addInterval(dimension, new DoubleLeftInfiniteInterval(end), generatingCycle);
	}
	
	/**
	 * This function returns a barcode collection containing only the infinite intervals.
	 * 
	 * @return a barcode collection with only inifinite intervals
	 */
	public DoubleAugmentedBarcodeCollection<T> getInfiniteIntervals() {
		DoubleAugmentedBarcodeCollection<T> collection = new DoubleAugmentedBarcodeCollection<T>();
		
		for (TIntObjectIterator<DoubleAugmentedBarcode<T>> iterator = this.barcodeMap.iterator(); iterator.hasNext(); ) {
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
	public DoubleAugmentedBarcode<T> getBarcode(int dimension) {
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
	public TIntIntHashMap getBettiNumbers(double filtrationValue) {
		TIntIntHashMap map = new TIntIntHashMap();
		
		for (TIntObjectIterator<DoubleAugmentedBarcode<T>> iterator = this.barcodeMap.iterator(); iterator.hasNext(); ) {
			iterator.advance();
			map.put(iterator.key(), iterator.value().getSliceCardinality(filtrationValue));
		}
		
		return map;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		
		for (TIntObjectIterator<DoubleAugmentedBarcode<T>> iterator = this.barcodeMap.iterator(); iterator.hasNext(); ) {
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
		DoubleAugmentedBarcodeCollection<?> other = (DoubleAugmentedBarcodeCollection<?>) obj;
		if (barcodeMap == null) {
			if (other.barcodeMap != null)
				return false;
		} else if (!barcodeMap.equals(other.barcodeMap))
			return false;
		return true;
	}
}
