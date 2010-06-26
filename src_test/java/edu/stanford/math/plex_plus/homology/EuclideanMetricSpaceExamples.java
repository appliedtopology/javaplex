package edu.stanford.math.plex_plus.homology;

import edu.stanford.math.plex_plus.math.metric.impl.EuclideanMetricSpace;
import edu.stanford.math.plex_plus.utility.RandomUtility;

public class EuclideanMetricSpaceExamples {
	/**
	 * House example in the plex tutorial by Henry Adams.
	 */
	public static EuclideanMetricSpace getHouseExample() {
		double[][] points = new double[][]{new double[]{0, 3}, 
				new double[]{1, 2},
				new double[]{1, 0},
				new double[]{-1, 0},
				new double[]{-1, 2}};
		
		EuclideanMetricSpace metricSpace = new EuclideanMetricSpace(points);
		
		return metricSpace;
	}
	
	public static EuclideanMetricSpace getSquare() {
		double[][] points = new double[][]{new double[]{0, 0}, 
				new double[]{0, 1},
				new double[]{1, 0},
				new double[]{1, 1}};
		
		EuclideanMetricSpace metricSpace = new EuclideanMetricSpace(points);
		return metricSpace;
	}
	
	public static EuclideanMetricSpace getSphere(int n, int d) {
		double[][] points = new double[n][d];
		double denom = 0;
		
		for (int i = 0; i < n; i++) {
			points[i] = RandomUtility.normalArray(d);
			denom = 0;
			for (int j = 0; j < d; j++) {
				denom += points[i][j] * points[i][j];
			}
			denom = Math.sqrt(denom);
			for (int j = 0; j < d; j++) {
				points[i][j] /= denom;;
			}
		}
		
		EuclideanMetricSpace metricSpace = new EuclideanMetricSpace(points);
		return metricSpace;
	}
}
