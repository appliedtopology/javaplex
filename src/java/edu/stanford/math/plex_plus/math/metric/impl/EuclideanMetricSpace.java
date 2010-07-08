package edu.stanford.math.plex_plus.math.metric.impl;

import edu.stanford.math.plex_plus.utility.ArrayUtility2;

public class EuclideanMetricSpace extends GenericFiniteMetricSpace<double[]> {
	
	public EuclideanMetricSpace(double[][] array) {
		super(array);
	}

	@Override
	public double distance(double[] a, double[] b) {
		return Math.sqrt(ArrayUtility2.squaredDistance(a, b));
	}

}
