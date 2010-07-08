package edu.stanford.math.plex_plus.homology;

import edu.stanford.math.plex_plus.math.metric.impl.EuclideanMetricSpace;
import edu.stanford.math.plex_plus.math.metric.interfaces.SearchableFiniteMetricSpace;
import edu.stanford.math.plex_plus.utility.RandomUtility;

public class EuclideanMetricSpaceExamples {
	/**
	 * House example in the plex tutorial by Henry Adams.
	 */
	public static SearchableFiniteMetricSpace<double[]> getHouseExample() {
		double[][] points = new double[][]{new double[]{0, 3}, 
				new double[]{1, 2},
				new double[]{1, 0},
				new double[]{-1, 0},
				new double[]{-1, 2}};
		
		EuclideanMetricSpace metricSpace = new EuclideanMetricSpace(points);
		
		return metricSpace;
	}
	
	public static SearchableFiniteMetricSpace<double[]> getSquare() {
		double[][] points = new double[][]{new double[]{0, 0}, 
				new double[]{0, 1},
				new double[]{1, 0},
				new double[]{1, 1}};
		
		EuclideanMetricSpace metricSpace = new EuclideanMetricSpace(points);
		return metricSpace;
	}
	
	public static SearchableFiniteMetricSpace<double[]> getEquispacedCirclePoints(int n) {
		double[][] points = new double[n][2];
		
		for (int i = 0; i < n; i++) {
			points[i][0] = Math.cos(2 * Math.PI * ((double) i / (double) n));
			points[i][1] = Math.sin(2 * Math.PI * ((double) i / (double) n));
		}
		
		EuclideanMetricSpace metricSpace = new EuclideanMetricSpace(points);
		return metricSpace;
	}
	
	public static SearchableFiniteMetricSpace<double[]> getRandomSpherePoints(int n, int d) {
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
	
	public static SearchableFiniteMetricSpace<double[]> getRandomTorusPoints(int n, double r, double R) {
		double[][] points = new double[n][3];
		
		for (int i = 0; i < n; i++) {
			double u = RandomUtility.nextUniform() * 2 * Math.PI;
			double v = RandomUtility.nextUniform() * 2 * Math.PI;
			points[i][0] = (R + r * Math.cos(v)) * Math.cos(u);
			points[i][1] = (R + r * Math.cos(v)) * Math.sin(u);
			points[i][2] = r * Math.sin(v);
		}
		
		EuclideanMetricSpace metricSpace = new EuclideanMetricSpace(points);
		return metricSpace;
	}
}
