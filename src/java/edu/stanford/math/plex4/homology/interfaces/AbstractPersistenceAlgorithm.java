package edu.stanford.math.plex4.homology.interfaces;

import edu.stanford.math.plex4.homology.barcodes.BarcodeCollection;
import edu.stanford.math.plex4.streams.interfaces.AbstractFilteredStream;

/**
 * This interface defines the functionality of a persistence algorithm. Namely it is a mapping
 * which takes a filtered chain complex to a barcode collection.
 * 
 * @author Andrew Tausz
 *
 * @param <T> the underlying basis type of the filtered chain complex
 */
public abstract class AbstractPersistenceAlgorithm<T> {
	/**
	 * This function computes the persistence index intervals of a filtered chain complex.
	 * In other words, it returns intervals corresponding to the internal filtration
	 * indices of the basis elements. 
	 * 
	 * @param stream the filtered chain complex 
	 * @return the persistence intervals of the given complex
	 */
	public abstract BarcodeCollection<Integer> computeIndexIntervals(AbstractFilteredStream<T> stream);
	
	/**
	 * This function computes the persistence intervals of a filtered chain complex.
	 * 
	 * @param stream the filtered chain complex 
	 * @return the persistence intervals of the given complex
	 */
	public BarcodeCollection<Double> computeIntervals(AbstractFilteredStream<T> stream) {
		BarcodeCollection<Integer> integerIntervals = this.computeIndexIntervals(stream);
		return (BarcodeCollection<Double>) stream.transform(integerIntervals);
	}
}
