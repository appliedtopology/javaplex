package edu.stanford.math.plex_plus.homology.barcodes;

import edu.stanford.math.plex_plus.datastructures.IntFormalSum;
import edu.stanford.math.plex_plus.utility.ExceptionUtility;
import gnu.trove.iterator.TIntObjectIterator;
import gnu.trove.map.hash.TIntIntHashMap;
import gnu.trove.map.hash.TIntObjectHashMap;

public class AugmentedBarcodeCollection<T> {
	private final TIntObjectHashMap<AugmentedBarcode<T>> barcodeMap = new TIntObjectHashMap<AugmentedBarcode<T>>();
	
	/**
	 * This function adds an interval at the specified dimension.
	 * 
	 * @param dimension the dimension to add to
	 * @param interval the interval to add
	 */
	public void addInterval(int dimension, PersistenceInterval interval, IntFormalSum<T> generatingCycle) {
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
	public void addInterval(int dimension, double start, double end, IntFormalSum<T> generatingCycle) {
		this.addInterval(dimension, new PersistenceInterval(start, end), generatingCycle);
	}
	
	/**
	 * This function adds the specified semi-infinite interval [start, infinity)
	 * at the supplied dimension.
	 * 
	 * @param dimension the dimension to add to
	 * @param start the starting point of the interval
	 */
	public void addInterval(int dimension, double start, IntFormalSum<T> generatingCycle) {
		this.addInterval(dimension, new PersistenceInterval(start), generatingCycle);
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
}
