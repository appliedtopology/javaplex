package edu.stanford.math.plex4.homology.filtration;

import java.util.Arrays;

/**
 * <p>This class implements a conversion between filtration values and filtration
 * indices that is given as an argument at instantiation of the
 * converter. The given filtration values array will be sorted.
 * </p>
 * 
 * @author Mikael Vejdemo-Johansson
 *
 */
public class ExternalConverter extends FiltrationConverter {
    private final double[] fvalues;
    private int n;
    private double maxfv;
    private double minfv;

    public ExternalConverter(double[] fvalues) {
	this.fvalues = fvalues.clone();
	Arrays.sort(this.fvalues);
	this.n = this.fvalues.length;
	this.maxfv = this.fvalues[this.n-1];
	this.minfv = this.fvalues[0];
    }
	
    /**
     * This function computes the index based on a filtration value.
     * 
     * @param filtrationValue the value to convert
     * @return the filtration index of the given value
     */
    public int getFiltrationIndex(double filtrationValue) {
	int lo = 0, hi = this.n-1, mid;
	while(lo != hi) {
	    mid = (lo+hi)/2;
	    if(this.fvalues[mid] > filtrationValue) 
		lo = mid;

	    if(this.fvalues[mid] < filtrationValue) 
		hi = mid;

	    if(this.fvalues[mid] == filtrationValue) 
		return mid;
	}
	return lo;
    }
	
    /**
     * This function computes the filtration value from a filtration index.
     * 
     * @param filtrationIndex the index to convert
     * @return the filtration value for the particular index
     */
    public double getFiltrationValue(int filtrationIndex) {
	return this.fvalues[filtrationIndex];
    }
	
	/* (non-Javadoc)
	 * @see edu.stanford.math.plex4.homology.filtration.FiltrationConverter#computeInducedFiltrationValue(double, double)
	 */
    public double computeInducedFiltrationValue(double filtrationValue1, double filtrationValue2) {
	return Math.max(filtrationValue1, filtrationValue2);
    }
	
    /**
     * This function returns the filtration at index 0.
     * 
     * @return the filtration at index 0
     */
    public double getInitialFiltrationValue() {
	return this.fvalues[0];
    }
}
