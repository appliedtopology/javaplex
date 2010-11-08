package edu.stanford.math.plex4.homology.filtration;

import edu.stanford.math.plex4.homology.barcodes.DoubleFiniteInterval;
import edu.stanford.math.plex4.homology.barcodes.DoubleHalfOpenInterval;
import edu.stanford.math.plex4.homology.barcodes.DoubleLeftInfiniteInterval;
import edu.stanford.math.plex4.homology.barcodes.DoubleRightInfiniteInterval;
import edu.stanford.math.plex4.homology.barcodes.IntFiniteInterval;
import edu.stanford.math.plex4.homology.barcodes.IntHalfOpenInterval;
import edu.stanford.math.plex4.homology.barcodes.IntLeftInfiniteInterval;
import edu.stanford.math.plex4.homology.barcodes.IntRightInfiniteInterval;

/**
 * This class defines a filtration value conversion which simply defines the filtration
 * value to be equal to the filtration index. (i.e. f_i = i)
 * 
 * @author Andrew Tausz
 *
 */
public class IdentityConverter extends FiltrationConverter {
	/**
	 * This is the single instance of the class
	 */
	private static IdentityConverter instance = new IdentityConverter();
	
	/**
	 * Private constructor to prevent instantiation.
	 */
	private IdentityConverter(){}
	
	/**
	 * This returns the single instance of the class.
	 * 
	 * @return the single instance of the class
	 */
	public static IdentityConverter getInstance() {
		return instance;
	}
	
	@Override
	public int getFiltrationIndex(double filtrationValue) {
		return (int) filtrationValue;
	}

	@Override
	public double getFiltrationValue(int filtrationIndex) {
		return filtrationIndex;
	}

	@Override
	public double computeInducedFiltrationValue(double filtrationValue1, double filtrationValue2) {
		return Math.max(filtrationValue1, filtrationValue2);
	}

	@Override
	public double getInitialFiltrationValue() {
		return 0;
	}

	@Override
	public DoubleHalfOpenInterval transform(IntHalfOpenInterval interval) {
		if (interval instanceof IntLeftInfiniteInterval) {
			IntLeftInfiniteInterval castedInterval = (IntLeftInfiniteInterval) interval;
			return new DoubleLeftInfiniteInterval(this.getFiltrationValue(castedInterval.getEnd()));
		} else if (interval instanceof IntRightInfiniteInterval) {
			IntRightInfiniteInterval castedInterval = (IntRightInfiniteInterval) interval;
			return new DoubleRightInfiniteInterval(this.getFiltrationValue(castedInterval.getStart()));
		} else if (interval instanceof IntFiniteInterval) {
			IntFiniteInterval castedInterval = (IntFiniteInterval) interval;
			return new DoubleFiniteInterval(this.getFiltrationValue(castedInterval.getStart()), this.getFiltrationValue(castedInterval.getEnd()));
		}
		return null;
	}

}
