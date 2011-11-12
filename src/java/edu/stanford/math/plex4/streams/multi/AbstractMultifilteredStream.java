package edu.stanford.math.plex4.streams.multi;

import java.util.Comparator;

import edu.stanford.math.plex4.homology.barcodes.Interval;
import edu.stanford.math.plex4.homology.barcodes.PersistenceInvariantDescriptor;


public interface AbstractMultifilteredStream<T> extends Iterable<T> {
	
	/**
	 * This function returns the filtration multi-index of the requested basis element.
	 * 
	 * @param basisElement the basis element to get the filtration multi-index for
	 * @return the filtration multi-index of the specified basis element
	 */
	public double[] getFiltrationValue(T basisElement);
	
	/**
	 * This function returns the elements in the boundary of the supplied
	 * basisElement, without the coefficients. We do not integrate the
	 * elements of the boundary array with their respective coefficients
	 * since this interface is designed to be independent of the algebraic
	 * environment.
	 * 
	 * @param basisElement the element to get the boundary of
	 * @return an array containing the boundary elements of the queried element
	 */
	public T[] getBoundary(T basisElement);
	
	/**
	 * This function returns the coefficients of the boundary of the supplied
	 * basis element. Note that in many cases, this will be the standard array
	 * consisting of alternating signs: [1, -1, 1, -1, ...]. However, this
	 * will not always be the case. Thus this must be left abstract. Also note
	 * that the coefficients will always be integers.
	 * 
	 * @param basisElement the element to get the boundary coefficients of
	 * @return an array containing the boundary coefficients of the queried element
	 */
	public int[] getBoundaryCoefficients(T basisElement);
	
	/**
	 * This function returns the dimension of a basis element within
	 * the chain complex.
	 * 
	 * @param basisElement the element to get the dimension of
	 * @return the dimension of the basis element within the chain complex
	 */
	public int getDimension(T basisElement);
	
	/**
	 * This function prepares the stream for use by a consumer, such as the
	 * PersistentHomology class. After finalization, the stream cannot not be 
	 * modified by adding or removing elements. In practice, the finalize
	 * procedure will probably do some sorting and construction of data
	 * structures necessary for iterating through the stream.  
	 */
	public void finalizeStream();
	
	/**
	 * This function checks whether the finalize() function has been called.
	 * 
	 * @return true if the stream has been finalized, and false otherwise
	 */
	public boolean isFinalized();
	
	/**
	 * This function returns the total number of elements in the stream.
	 * 
	 * @return the size of the stream
	 */
	public int getSize();
	
	/**
	 * This function returns a comparator on the underlying basis type.
	 * 
	 * @return a comparator on the underlying basis type
	 */
	public Comparator<T> getBasisComparator();
	
	/**
	 * This function transforms the given collection of filtration index barcodes into filtration value barcodes.
	 * 
	 * @param <G>
	 * @param barcodeCollection the set of filtration index barcodes
	 * @return the barcodes transformed into filtration value form
	 */
	public <G> PersistenceInvariantDescriptor<Interval<Double>, G> transform(PersistenceInvariantDescriptor<Interval<Integer>, G> barcodeCollection);
}
