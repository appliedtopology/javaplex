package edu.stanford.math.plex4.homology.barcodes;

import java.util.Comparator;

public class IntervalLengthComparator implements Comparator<Interval<Double>> {
	
	private static IntervalLengthComparator instance = new IntervalLengthComparator();
	private IntervalLengthComparator() {}
	
	public static IntervalLengthComparator getInstance() {
		return instance;
	}
	
	public int compare(Interval<Double> arg0, Interval<Double> arg1) {
		if (arg0.isInfinite() && arg1.isInfinite()) {
			return 0;
		}

		if (arg0.isInfinite()) {
			return 1;
		}

		if (arg1.isInfinite()) {
			return -1;
		}

		// both finite

		Double length0 = arg0.getEnd() - arg0.getStart();
		Double length1 = arg1.getEnd() - arg1.getStart();
		if (length0 < length1) {
			return -1;
		} else if (length0 > length1) {
			return 1;
		} else {
			return 0;
		}
	}
}
