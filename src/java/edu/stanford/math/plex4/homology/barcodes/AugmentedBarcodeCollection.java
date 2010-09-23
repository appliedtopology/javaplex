package edu.stanford.math.plex4.homology.barcodes;

import edu.stanford.math.plex4.utility.ExceptionUtility;
import gnu.trove.TIntIntHashMap;
import gnu.trove.TIntObjectHashMap;
import gnu.trove.TIntObjectIterator;

public class AugmentedBarcodeCollection<T> {
	private final TIntObjectHashMap<AugmentedBarcode<T>> barcodeMap = new TIntObjectHashMap<AugmentedBarcode<T>>();
	
	/**
	 * This function adds an interval at the specified dimension.
	 * 
	 * @param dimension the dimension to add to
	 * @param interval the interval to add
	 */
	public void addInterval(int dimension, HalfOpenInterval interval, T generatingCycle) {
		ExceptionUtility.verifyNonNull(interval);
		if (!this.barcodeMap.containsKey(dimension)) {
			this.barcodeMap.put(dimension, new AugmentedBarcode<T>(dimension));
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
	 */
	public void addInterval(int dimension, double start, double end, T generatingCycle) {
		this.addInterval(dimension, new FiniteInterval(start, end), generatingCycle);
	}
	
	/**
	 * This function adds the specified semi-infinite interval [start, infinity)
	 * at the supplied dimension.
	 * 
	 * @param dimension the dimension to add to
	 * @param start the starting point of the interval
	 */
	public void addRightInfiniteInterval(int dimension, double start, T generatingCycle) {
		this.addInterval(dimension, new RightInfiniteInterval(start), generatingCycle);
	}
	
	public void addLeftInfiniteInterval(int dimension, double end, T generatingCycle) {
		this.addInterval(dimension, new LeftInfiniteInterval(end), generatingCycle);
	}
	
	public AugmentedBarcodeCollection<T> getInfiniteIntervals() {
		AugmentedBarcodeCollection<T> collection = new AugmentedBarcodeCollection<T>();
		
		for (TIntObjectIterator<AugmentedBarcode<T>> iterator = this.barcodeMap.iterator(); iterator.hasNext(); ) {
			iterator.advance();
			collection.barcodeMap.put(iterator.key(), iterator.value().getInfiniteIntervals());
		}
		
		return collection;
	}
	
	/**
	 * This function returns the barcode at the specified dimension.
	 * 
	 * @param dimension
	 * @return
	 */
	public AugmentedBarcode<T> getBarcode(int dimension) {
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
		
		for (TIntObjectIterator<AugmentedBarcode<T>> iterator = this.barcodeMap.iterator(); iterator.hasNext(); ) {
			iterator.advance();
			map.put(iterator.key(), iterator.value().getSliceCardinality(filtrationValue));
		}
		
		return map;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		
		for (TIntObjectIterator<AugmentedBarcode<T>> iterator = this.barcodeMap.iterator(); iterator.hasNext(); ) {
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
		AugmentedBarcodeCollection<?> other = (AugmentedBarcodeCollection<?>) obj;
		if (barcodeMap == null) {
			if (other.barcodeMap != null)
				return false;
		} else if (!barcodeMap.equals(other.barcodeMap))
			return false;
		return true;
	}
}
