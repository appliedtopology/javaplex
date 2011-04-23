package edu.stanford.math.plex4.metric.impl;

import edu.stanford.math.plex4.metric.interfaces.AbstractIntMetricSpace;

/**
 * This class implements the AbstractIntMetricSpace interface. The distances between points
 * is given by a distance matrix.
 * 
 * @author Andrew Tausz
 *
 */
public class ExplicitMetricSpace implements AbstractIntMetricSpace {
	private final double[][] distanceMatrix;
	
	/**
	 * This constructor initializes the class with the given distance matrix.
	 * 
	 * @param distanceMatrix the distance matrix to initialize with
	 */
	public ExplicitMetricSpace(double[][] distanceMatrix) {
		this.distanceMatrix = distanceMatrix;
	}

	public double distance(int i, int j) {
		return distanceMatrix[i][j];
	}

	public int size() {
		return distanceMatrix.length;
	}
}
