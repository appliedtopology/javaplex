/**
 * 
 */
package edu.stanford.math.plex_plus.math.metric.impl;

import java.util.Set;

import edu.stanford.math.plex_plus.math.metric.interfaces.GenericAbstractFiniteMetricSpace;
import edu.stanford.math.plex_plus.utility.ArrayUtility;
import edu.stanford.math.plex_plus.utility.ExceptionUtility;

/**
 * @author Andrew Tausz
 *
 */
public class EuclideanMetricSpace implements GenericAbstractFiniteMetricSpace<double[]> {
	private final double[][] dataPoints;
	private final KDTree tree;
	
	public EuclideanMetricSpace(double[][] dataPoints) {
		ExceptionUtility.verifyNonNull(dataPoints);
		this.dataPoints = dataPoints;
		this.tree = new KDTree(dataPoints);
	}
	
	@Override
	public Set<double[]> getKNearestNeighbors(double[] queryPoint, int k) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public double[] getNearestPoint(double[] queryPoint) {
		return this.tree.nearestNeighborSearch(queryPoint);
	}

	@Override
	public Set<double[]> getNeighborhood(double[] queryPoint, double epsilon) {
		return this.tree.epsilonNeighborhoodSearch(queryPoint, epsilon);
	}

	@Override
	public int size() {
		return dataPoints.length;
	}

	@Override
	public double distance(double[] a, double[] b) {
		return ArrayUtility.squaredDistance(a, b);
	}
	
}
