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
public interface AbstractPersistenceAlgorithm<T> {
	/**
	 * This function computes the persistence intervals of a filtered chain complex.
	 * 
	 * @param stream the filtered chain complex 
	 * @return the persistence intervals of the given complex
	 */
	public BarcodeCollection<Integer> computeIntervals(AbstractFilteredStream<T> stream);
}
