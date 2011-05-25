package edu.stanford.math.plex4.homology.interfaces;

import edu.stanford.math.plex4.homology.barcodes.AnnotatedBarcodeCollection;
import edu.stanford.math.plex4.streams.interfaces.AbstractFilteredStream;

/**
 * This interface defines the functionality of a persistence algorithm that also produces generators
 * for each calculated interval.
 * 
 * @author Andrew Tausz
 *
 * @param <T> the underlying basis type of the filtered chain complex
 * @param <B> the type of the generators of the persistence intervals
 */
public interface AbstractPersistenceBasisAlgorithm<T, B> extends AbstractPersistenceAlgorithm<T> {
	
	/**
	 * This function computes the augmented persistence intervals for a supplied filtered chain complex.
	 * The augmented persistence intervals are the same as the regular persistence intervals, except that
	 * each interval is annotated with a generator of type B.
	 * 
	 * @param stream the filtered chain complex
	 * @return the augmented persistence intervals
	 */
	public AnnotatedBarcodeCollection<Integer, B> computeAnnotatedIntervals(AbstractFilteredStream<T> stream);
}
