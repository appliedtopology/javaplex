package edu.stanford.math.plex_plus.homology.barcodes;

import edu.stanford.math.plex_plus.utility.ExceptionUtility;
import gnu.trove.iterator.TIntObjectIterator;
import gnu.trove.map.hash.TIntIntHashMap;
import gnu.trove.map.hash.TIntObjectHashMap;

/**
 * This class implements functionality for storing a collection of barcodes. 
 * It is designed to be the output of a persistent homology or cohomology algorithm
 * so that it contains the persistence intervals at each dimension.
 * 
 * @author Andrew Tausz
 *
 */
public class BarcodeCollection {
	private final TIntObjectHashMap<Barcode> barcodeMap = new TIntObjectHashMap<Barcode>();
	
	/**
	 * This function adds an interval at the specified dimension.
	 * 
	 * @param dimension the dimension to add to
	 * @param interval the interval to add
	 */
	public void addInterval(int dimension, PersistenceInterval interval) {
		ExceptionUtility.verifyNonNull(interval);
		if (!this.barcodeMap.containsKey(dimension)) {
			this.barcodeMap.put(dimension, new Barcode(dimension));
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
		this.addInterval(dimension, new PersistenceInterval(start, end));
	}
	
	/**
	 * This function addes the specified semi-infinite interval [start, infinity)
	 * at the supplied dimension.
	 * 
	 * @param dimension the dimension to add to
	 * @param start the starting point of the interval
	 */
	public void addInterval(int dimension, double start) {
		this.addInterval(dimension, new PersistenceInterval(start));
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
		
		for (TIntObjectIterator<Barcode> iterator = this.barcodeMap.iterator(); iterator.hasNext(); ) {
			iterator.advance();
			map.put(iterator.key(), iterator.value().getSliceCardinality(filtrationValue));
		}
		
		return map;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		
		for (TIntObjectIterator<Barcode> iterator = this.barcodeMap.iterator(); iterator.hasNext(); ) {
			iterator.advance();
			builder.append(iterator.value().toString());
			builder.append("\n");
		}
		
		return builder.toString();
	}
}
