/**
 * 
 */
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
 * This class implements the FiltrationConverter interface for static complexes. 
 * It simply sets all of the filtration values and indices to 0.
 * 
 * @author Andrew Tausz
 *
 */
public class StaticConverter extends FiltrationConverter {
	private static StaticConverter instance = new StaticConverter();
	private StaticConverter(){}
	
	public static StaticConverter getInstance() {
		return instance;
	}
	
	/* (non-Javadoc)
	 * @see edu.stanford.math.plex4.homology.filtration.FiltrationConverter#getFiltrationIndex(double)
	 */
	public int getFiltrationIndex(double filtrationValue) {
		return 0;
	}

	/* (non-Javadoc)
	 * @see edu.stanford.math.plex4.homology.filtration.FiltrationConverter#getFiltrationValue(int)
	 */
	public double getFiltrationValue(int filtrationIndex) {
		return 0;
	}

	/* (non-Javadoc)
	 * @see edu.stanford.math.plex4.homology.filtration.FiltrationConverter#computeInducedFiltrationValue(double, double)
	 */
	public double computeInducedFiltrationValue(double filtrationValue1, double filtrationValue2) {
		return 0;
	}

	/* (non-Javadoc)
	 * @see edu.stanford.math.plex4.homology.filtration.FiltrationConverter#getInitialFiltrationValue()
	 */
	public double getInitialFiltrationValue() {
		return 0;
	}
	
	/* (non-Javadoc)
	 * @see edu.stanford.math.plex4.homology.filtration.FiltrationConverter#transformInterval(edu.stanford.math.plex4.homology.barcodes.IntHalfOpenInterval)
	 */
	public DoubleHalfOpenInterval transformInterval(IntHalfOpenInterval interval) {
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
