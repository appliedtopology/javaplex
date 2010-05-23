package edu.stanford.math.plex_plus.homology.simplex_streams;


/**
 * This interface abstracts the functionality of a streaming filtered
 * simplicial complex. It extends the Iterable interface. An implementing
 * class must provide functionality for providing an iterator which
 * produces the simplicies in increasing order. Simplices are ordered 
 * first by their filtration value, and then by their dimension, and then
 * dictionary order of their vertices. 
 * 
 * @author Andrew Tausz
 *
 */
public interface SimplexStream<T> extends Iterable<T> {
	/**
	 * This function returns the filtration value of a simplex
	 * within a filtered simplicial complex. The filtration value
	 * is the threshold parameter at which the simplex appears within
	 * the filtration.
	 * 
	 * @param simplex the simplex to query
	 * @return the filtration value of the provided simplex
	 */
	public double getFiltrationValue(T simplex);
	
	/**
	 * This function prepares the stream for use by a consumer, such as the
	 * PersistentHomology class. After finalization, the stream must not be 
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
}
