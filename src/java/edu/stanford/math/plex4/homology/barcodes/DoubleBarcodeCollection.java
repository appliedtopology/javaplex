package edu.stanford.math.plex4.homology.barcodes;

import edu.stanford.math.plex4.utility.ExceptionUtility;
import edu.stanford.math.primitivelib.utility.Infinity;
import gnu.trove.TIntIntHashMap;
import gnu.trove.TIntObjectHashMap;
import gnu.trove.TIntObjectIterator;

/**
 * This class implements functionality for storing a collection of barcodes. 
 * It is designed to be the output of a persistent homology or cohomology algorithm
 * so that it contains the persistence intervals at each dimension.
 * 
 * @author Andrew Tausz
 *
 */
public class DoubleBarcodeCollection {
	private final TIntObjectHashMap<DoubleBarcode> barcodeMap = new TIntObjectHashMap<DoubleBarcode>();
	
	/**
	 * This function returns a barcode collection containing only the infinite intervals.
	 * 
	 * @return a barcode collection with only inifinite intervals
	 */
	public DoubleBarcodeCollection getInfiniteIntervals() {
		DoubleBarcodeCollection collection = new DoubleBarcodeCollection();
		
		for (TIntObjectIterator<DoubleBarcode> iterator = this.barcodeMap.iterator(); iterator.hasNext(); ) {
			iterator.advance();
			collection.barcodeMap.put(iterator.key(), iterator.value().getInfiniteIntervals());
		}
		
		return collection;
	}
	
	/**
	 * This function adds an interval at the specified dimension.
	 * 
	 * @param dimension the dimension to add to
	 * @param interval the interval to add
	 */
	public void addInterval(int dimension, DoubleHalfOpenInterval interval) {
		ExceptionUtility.verifyNonNull(interval);
		if (!this.barcodeMap.containsKey(dimension)) {
			this.barcodeMap.put(dimension, new DoubleBarcode(dimension));
		}
		this.barcodeMap.get(dimension).addInterval(interval);
	}
	
	/**
	 * This function adds the specified finite interval [start, end] at the
	 * supplied dimension.
	 * 
	 * @param dimension the dimension to add to
	 * @param start the starting point of the interval
	 * @param end the ending point of the interval
	 */
	public void addInterval(int dimension, double start, double end) {
		this.addInterval(dimension, new DoubleFiniteInterval(start, end));
	}
	
	/**
	 * This function adds the specified semi-infinite interval [start, infinity)
	 * at the supplied dimension.
	 * 
	 * @param dimension the dimension to add to
	 * @param start the starting point of the interval
	 */
	public void addRightInfiniteInterval(int dimension, double start) {
		this.addInterval(dimension, new DoubleRightInfiniteInterval(start));
	}
	
	/**
	 * This function adds the specified semi-infinite intervals [-infinity, end)
	 * at the supplied dimension
	 * 
	 * @param dimension the dimension to add to
	 * @param end the ending point of the interval
	 */
	public void addLeftInfiniteInterval(int dimension, double end) {
		this.addInterval(dimension, new DoubleLeftInfiniteInterval(end));
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		
		for (TIntObjectIterator<DoubleBarcode> iterator = this.barcodeMap.iterator(); iterator.hasNext(); ) {
			iterator.advance();
			builder.append(iterator.value().toString());
			builder.append("\n");
		}
		
		return builder.toString();
	}
	
	/**
	 * This function computes the Betti numbers for a particular filtration
	 * value. It returns the results as a map which maps the dimension to the
	 * Betti number.
	 * 
	 * @param filtrationValue the filtrationValue to compute the Betti numbers at
	 * @return a TIntIntHashMap mapping dimension to the Betti number
	 */
	public TIntIntHashMap getBettiNumbersMap(double filtrationValue) {
		TIntIntHashMap map = new TIntIntHashMap();
		
		for (TIntObjectIterator<DoubleBarcode> iterator = this.barcodeMap.iterator(); iterator.hasNext(); ) {
			iterator.advance();
			map.put(iterator.key(), iterator.value().getSliceCardinality(filtrationValue));
		}
		
		return map;
	}
	
	public int[] getBettiSequence() {
		int maxDimension = Infinity.Int.getNegativeInfinity();
		int minDimension = Infinity.Int.getPositiveInfinity();
		
		for (TIntObjectIterator<DoubleBarcode> iterator = this.barcodeMap.iterator(); iterator.hasNext(); ) {
			iterator.advance();
			maxDimension = Math.max(maxDimension, iterator.key());
			minDimension = Math.min(minDimension, iterator.key());
		}
		
		int[] bettiNumbers = new int[maxDimension + 1];
		
		for (TIntObjectIterator<DoubleBarcode> iterator = this.barcodeMap.iterator(); iterator.hasNext(); ) {
			iterator.advance();
			int dimension = iterator.key();
			if (dimension >= 0) {
				bettiNumbers[dimension] = iterator.value().getCardinality();
			}
		}
		
		return bettiNumbers;
	}
	
	public String getBettiNumbers() {
		StringBuilder builder = new StringBuilder();
		int maxDimension = Infinity.Int.getNegativeInfinity();
		int minDimension = Infinity.Int.getPositiveInfinity();
		
		for (TIntObjectIterator<DoubleBarcode> iterator = this.barcodeMap.iterator(); iterator.hasNext(); ) {
			iterator.advance();
			maxDimension = Math.max(maxDimension, iterator.key());
			minDimension = Math.min(minDimension, iterator.key());
		}
		
		int[] bettiNumbers = new int[maxDimension - minDimension + 1];
		
		for (TIntObjectIterator<DoubleBarcode> iterator = this.barcodeMap.iterator(); iterator.hasNext(); ) {
			iterator.advance();
			bettiNumbers[iterator.key() - minDimension] = iterator.value().getCardinality();
		}
		
		builder.append("{");
		
		for (int i = 0; i < bettiNumbers.length; i++) {
			if (i > 0) {
				builder.append(", ");
			}
			builder.append((i + minDimension) + ": " + bettiNumbers[i]);
		}
		
		builder.append("}");
		
		return builder.toString();
	}
	
	public String getSliceBettiNumbers(double point) {
		StringBuilder builder = new StringBuilder();
		int maxDimension = Infinity.Int.getNegativeInfinity();
		int minDimension = Infinity.Int.getPositiveInfinity();
		
		for (TIntObjectIterator<DoubleBarcode> iterator = this.barcodeMap.iterator(); iterator.hasNext(); ) {
			iterator.advance();
			maxDimension = Math.max(maxDimension, iterator.key());
			minDimension = Math.min(minDimension, iterator.key());
		}
		
		int[] bettiNumbers = new int[maxDimension - minDimension + 1];
		
		for (TIntObjectIterator<DoubleBarcode> iterator = this.barcodeMap.iterator(); iterator.hasNext(); ) {
			iterator.advance();
			bettiNumbers[iterator.key() - minDimension] = iterator.value().getSliceCardinality(point);
		}
		
		builder.append("{");
		
		for (int i = 0; i < bettiNumbers.length; i++) {
			if (i > 0) {
				builder.append(", ");
			}
			builder.append((i + minDimension) + ": " + bettiNumbers[i]);
		}
		
		builder.append("}");
		
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
		DoubleBarcodeCollection other = (DoubleBarcodeCollection) obj;
		if (barcodeMap == null) {
			if (other.barcodeMap != null)
				return false;
		} else if (!barcodeMap.equals(other.barcodeMap))
			return false;
		return true;
	}

}
