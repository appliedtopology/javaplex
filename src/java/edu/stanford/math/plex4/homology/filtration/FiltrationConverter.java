package edu.stanford.math.plex4.homology.filtration;

import edu.stanford.math.plex4.homology.barcodes.Interval;
import edu.stanford.math.plex4.homology.barcodes.PersistenceInvariantDescriptor;
import edu.stanford.math.primitivelib.autogen.functional.ObjectObjectFunction;



/**
 * This interface defines a mapping between filtration values and filtration indices.
 * 
 * @author Andrew Tausz
 *
 */
public abstract class FiltrationConverter implements ObjectObjectFunction<Interval<Integer>, Interval<Double>> {
	
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
	 * @return the filtration value that is induced by two filtration values
	 */
	public abstract double computeInducedFiltrationValue(double filtrationValue1, double filtrationValue2);
	
	/**
	 * This function returns the filtration at index 0.
	 * 
	 * @return the filtration at index 0
	 */
	public abstract double getInitialFiltrationValue();
	
	public <G> PersistenceInvariantDescriptor<Interval<Double>, G> transform(PersistenceInvariantDescriptor<Interval<Integer>, G> invariantDescriptor) {
		return FiltrationUtility.transform(invariantDescriptor, this);
	}
	
	/* (non-Javadoc)
	 * @see edu.stanford.math.plex4.homology.filtration.FiltrationConverter#transformInterval(edu.stanford.math.plex4.homology.barcodes.IntHalfOpenInterval)
	 */
	public Interval<Double> evaluate(Interval<Integer> interval) {
		if (interval.isLeftInfinite() && interval.isRightInfinite()) {
			return Interval.makeInterval(null, null, interval.isLeftClosed(), interval.isRightClosed(), interval.isLeftInfinite(), interval.isRightInfinite());
		}
		
		if (interval.isLeftInfinite()) {
			return Interval.makeInterval(null, this.getFiltrationValue(interval.getEnd()), interval.isLeftClosed(), interval.isRightClosed(), interval.isLeftInfinite(), interval.isRightInfinite());
		}
		
		if (interval.isRightInfinite()) {
			return Interval.makeInterval(this.getFiltrationValue(interval.getStart()), null, interval.isLeftClosed(), interval.isRightClosed(), interval.isLeftInfinite(), interval.isRightInfinite());
		}
		
		return Interval.makeInterval(this.getFiltrationValue(interval.getStart()), this.getFiltrationValue(interval.getEnd()), interval.isLeftClosed(), interval.isRightClosed(), interval.isLeftInfinite(), interval.isRightInfinite());
	}
}
