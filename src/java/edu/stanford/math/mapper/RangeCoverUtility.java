package edu.stanford.math.mapper;

import edu.stanford.math.plex4.homology.barcodes.Interval;
import edu.stanford.math.plex4.streams.filter.IntFilterFunction;

public class RangeCoverUtility {
	
	public static Iterable<Interval<Double>> createUniformIntervalCover(IntFilterFunction filter, int n, double overlap) {
		return new BoundedIntervalCover(filter.getMinValue(), filter.getMaxValue(), n, overlap);
	}
	
	
}
