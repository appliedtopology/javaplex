package edu.stanford.math.plex4.streams.utility;

import java.util.Comparator;

import edu.stanford.math.plex4.streams.interfaces.AbstractFilteredStream;

/**
 * This class provides filtration based comparison of basis elements. It implements
 * a dictionary ordering where first the filtration indices are compared, and then
 * the actual basis elements are compared using the supplied comparator.
 * 
 * @author Andrew Tausz
 *
 * @param <M>
 */
public class FilteredComparator<M> implements Comparator<M> {
	/**
	 * The filtered complex which provides the filtration index information.
	 */
	private final AbstractFilteredStream<M> stream;
	
	/**
	 * The comparator for the basis type.
	 */
	private final Comparator<M> basisComparator;
	
	/**
	 * This constructor initializes the class with a filtered chain complex, and a comparator
	 * on the basis elements.
	 * 
	 * @param stream the filtered chain complex
	 * @param basisComparator a comparator on the basis elements
	 */
	public FilteredComparator(AbstractFilteredStream<M> stream, Comparator<M> basisComparator) {
		this.stream = stream;
		this.basisComparator = basisComparator;
	}
	
	/* (non-Javadoc)
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	public int compare(M arg0, M arg1) {
		int filtration0 = stream.getFiltrationIndex(arg0);
		int filtration1 = stream.getFiltrationIndex(arg1);
		
		if (filtration0 < filtration1) {
			return -1;
		} else if (filtration0 > filtration1) {
			return 1;
		} else {
			return this.basisComparator.compare(arg0, arg1);
		}
	}
}
