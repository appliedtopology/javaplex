package edu.stanford.math.plex_plus.math.metric.utility;

import edu.stanford.math.plex_plus.math.metric.interfaces.IntFiniteMetricSpace;
import edu.stanford.math.plex_plus.utility.RandomUtility;

public class MetricUtility {
	
	/**
	 * This function estimates the diameter of the supplied metric space. The
	 * diameter is defined as the smallest number r such that d(x, y) <= r for all
	 * x, y in the space. The estimate is obtained by randomly choosing pairs
	 * (i, j), and taking the maximum distance over all pairs. 
	 * 
	 * @param metricSpace the metric space to estimate the diameter of
	 * @param trials the number of random pairs to choose
	 * @return an estimate of the diameter of the metric space
	 */
	public static double estimateDiameter(IntFiniteMetricSpace metricSpace, int trials) {
		double diameterEstimate = 0;
		int n = metricSpace.size();
		
		for (int trial = 0; trial < trials; trial++) {
			int i = RandomUtility.nextUniformInt(0, n - 1);
			int j = RandomUtility.nextUniformIntExcluded(0, n - 1, i);
			double distance = metricSpace.distance(i, j);
			diameterEstimate = Math.max(distance, diameterEstimate);
		}
		
		return diameterEstimate * (((double) trials + 1) / ((double) trials));
	}
	
	public static double estimateDiameter(IntFiniteMetricSpace metricSpace) {
		return estimateDiameter(metricSpace, Math.min(metricSpace.size(), 100));
	}
}
