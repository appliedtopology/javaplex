package edu.stanford.math.plex4.homology.filtration;

import edu.stanford.math.plex4.homology.barcodes.DoubleHalfOpenInterval;
import edu.stanford.math.plex4.homology.barcodes.IntHalfOpenInterval;


/**
 * This interface defines a mapping between filtration values and 
 * filtration indices.
 * 
 * @author Andrew Tausz
 *
 */
public abstract class FiltrationConverter {
	
	/**
	 * This function computes the index based on a filtration value.
	 * 
	 * @param filtrationValue the value to convert
	 * @return the filtration index of the given value
	 */
	public abstract int getFiltrationIndex(double filtrationValue);
	
	/**
	 * This function computes the filtration value from a filtration index.
	 * 
	 * @param filtrationIndex the index to convert
	 * @return the filtration value for the particular index
	 */
	public abstract double getFiltrationValue(int filtrationIndex);
	
	/**
	 * This function computes the filtration value that is consistent with the
	 * ordering of filtration indices. For example, for filtration values that are
	 * increasing with filtration indices, this should be the max function.
	 * 
	 * @param filtrationValue1
	 * @param filtrationValue2
	 * @return
	 */
	public abstract double computeInducedFiltrationValue(double filtrationValue1, double filtrationValue2);
	
	/**
	 * This function returns the filtration at index 0.
	 * 
	 * @return the filtration at index 0
	 */
	public abstract double getInitialFiltrationValue();
	
	/**
	 * This function converts a filtration index interval to a filtration value interval.
	 * 
	 * @param interval the integer interval to convert
	 * @return the filtration value function applied to the interval
	 */
	public abstract DoubleHalfOpenInterval transformInterval(IntHalfOpenInterval interval);
}
