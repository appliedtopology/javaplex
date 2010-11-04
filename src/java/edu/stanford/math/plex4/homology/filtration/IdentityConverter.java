package edu.stanford.math.plex4.homology.filtration;

import edu.stanford.math.plex4.homology.barcodes.DoubleFiniteInterval;
import edu.stanford.math.plex4.homology.barcodes.DoubleHalfOpenInterval;
import edu.stanford.math.plex4.homology.barcodes.DoubleLeftInfiniteInterval;
import edu.stanford.math.plex4.homology.barcodes.DoubleRightInfiniteInterval;
import edu.stanford.math.plex4.homology.barcodes.IntFiniteInterval;
import edu.stanford.math.plex4.homology.barcodes.IntHalfOpenInterval;
import edu.stanford.math.plex4.homology.barcodes.IntLeftInfiniteInterval;
import edu.stanford.math.plex4.homology.barcodes.IntRightInfiniteInterval;

public class IdentityConverter extends FiltrationConverter {
	private static IdentityConverter instance = new IdentityConverter();
	private IdentityConverter(){}
	
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
