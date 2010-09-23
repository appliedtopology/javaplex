package edu.stanford.math.plex4.math.metric.impl;

import edu.stanford.math.plex4.array_utility.DoubleArrayMath;

public class EuclideanMetricSpace extends GenericFiniteMetricSpace<double[]> {
	
	public EuclideanMetricSpace(double[][] array) {
		super(array);
	}

	@Override
	public double distance(double[] a, double[] b) {
		return Math.sqrt(DoubleArrayMath.squaredDistance(a, b));
	}

	public int getDimension() {
		return this.elements[0].length;
	}
}
