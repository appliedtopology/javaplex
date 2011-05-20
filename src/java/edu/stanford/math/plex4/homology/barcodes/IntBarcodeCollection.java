package edu.stanford.math.plex4.homology.barcodes;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import edu.stanford.math.plex4.utility.ExceptionUtility;
import gnu.trove.TIntIntHashMap;

/**
 * This class implements functionality for storing a collection of barcodes. 
 * It is designed to be the output of a persistent homology or cohomology algorithm
 * so that it contains the persistence intervals at each dimension.
 * 
 * @author Andrew Tausz
 *
 */
public class IntBarcodeCollection implements Iterable<Entry<Integer, IntBarcode>> {
	private final Map<Integer, IntBarcode> barcodeMap = new HashMap<Integer, IntBarcode>();
	
	/**
	 * This function returns a barcode collection containing only the infinite intervals.
	 * 
	 * @return a barcode collection with only inifinite intervals
	 */
	public IntBarcodeCollection getInfiniteIntervals() {
		IntBarcodeCollection collection = new IntBarcodeCollection();
		
		for (Integer dimension: this.barcodeMap.keySet()) {
			collection.barcodeMap.put(dimension, this.barcodeMap.get(dimension).getInfiniteIntervals());
		}
		
		return collection;
	}
	
	public IntBarcodeCollection filterByMaxDimension(int maxDimension) {
		IntBarcodeCollection collection = new IntBarcodeCollection();
		
		for (Integer dimension: this.barcodeMap.keySet()) {
			if (dimension <= maxDimension) {
				collection.barcodeMap.put(dimension, this.barcodeMap.get(dimension));
			}
		}
		
		return collection;
	}
	
	/**
	 * This function adds an interval at the specified dimension.
	 * 
	 * @param dimension the dimension to add to
	 * @param interval the interval to add
	 */
	public void addInterval(int dimension, IntHalfOpenInterval interval) {
		ExceptionUtility.verifyNonNull(interval);
		if (!this.barcodeMap.containsKey(dimension)) {
			this.barcodeMap.put(dimension, new IntBarcode(dimension));
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
	public void addInterval(int dimension, int start, int end) {
		this.addInterval(dimension, new IntFiniteInterval(start, end));
	}
	
	/**
	 * This function adds the specified semi-infinite interval [start, infinity)
	 * at the supplied dimension.
	 * 
	 * @param dimension the dimension to add to
	 * @param start the starting point of the interval
	 */
	public void addRightInfiniteInterval(int dimension, int start) {
		this.addInterval(dimension, new IntRightInfiniteInterval(start));
	}
	
	/**
	 * This function adds the specified semi-infinite intervals [-infinity, end)
	 * at the supplied dimension
	 * 
	 * @param dimension the dimension to add to
	 * @param end the ending point of the interval
	 */
	public void addLeftInfiniteInterval(int dimension, int end) {
		this.addInterval(dimension, new IntLeftInfiniteInterval(end));
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		
		for (Integer dimension: this.barcodeMap.keySet()) {
			builder.append(this.barcodeMap.get(dimension).toString());
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
	public TIntIntHashMap getBettiNumbersMap(int filtrationValue) {
		TIntIntHashMap map = new TIntIntHashMap();
		
		for (Integer dimension: this.barcodeMap.keySet()) {
			map.put(dimension, this.barcodeMap.get(dimension).getSliceCardinality(filtrationValue));
		}
		
		return map;
	}

	public void draw() {
		for (Integer dimension: this.barcodeMap.keySet()) {
			System.out.println("Dimension: " + dimension);
			this.barcodeMap.get(dimension).draw();
		}
	}

	public Iterator<Entry<Integer, IntBarcode>> iterator() {
		return this.barcodeMap.entrySet().iterator();
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
		IntBarcodeCollection other = (IntBarcodeCollection) obj;
		if (barcodeMap == null) {
			if (other.barcodeMap != null)
				return false;
		} else if (!barcodeMap.equals(other.barcodeMap))
			return false;
		return true;
	}
}
