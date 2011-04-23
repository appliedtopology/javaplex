package edu.stanford.math.plex4.metric.impl;

import edu.stanford.math.primitivelib.autogen.array.DoubleArrayMath;

/**
 * This class implements the standard metric space structure on R^n given by the 
 * Euclidean metric.
 * 
 * @author Andrew Tausz
 *
 */
public class EuclideanMetricSpace extends ObjectSearchableFiniteMetricSpace<double[]> {

	/**
	 * This constructor initializes the class with an array of elements.
	 * 
	 * @param array the array of elements that will be the points in the metric space
	 */
	public EuclideanMetricSpace(double[][] array) {
		super(array);
	}

	@Override
	public double distance(double[] a, double[] b) {
		return DoubleArrayMath.distance(a, b);
	}
}
