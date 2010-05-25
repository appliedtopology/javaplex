package edu.stanford.math.plex_plus.math.metric.impl;

import edu.stanford.math.plex_plus.kd.KDTree;
import edu.stanford.math.plex_plus.math.metric.interfaces.GenericAbstractFiniteMetricSpace;
import edu.stanford.math.plex_plus.utility.ArrayUtility;
import edu.stanford.math.plex_plus.utility.ExceptionUtility;
import gnu.trove.set.hash.TIntHashSet;

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
		this.tree.constructTree();
	}
	
	@Override
	public TIntHashSet getKNearestNeighbors(double[] queryPoint, int k) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getNearestPoint(double[] queryPoint) {
		return this.tree.nearestNeighborSearch(queryPoint);
	}

	@Override
	public TIntHashSet getNeighborhood(double[] queryPoint, double epsilon) {
		return this.tree.epsilonNeighborhoodSearch(queryPoint, epsilon);
	}

	@Override
	public int size() {
		return dataPoints.length;
	}

	@Override
	public double distance(double[] a, double[] b) {
		return Math.sqrt(ArrayUtility.squaredDistance(a, b));
	}

	@Override
	public double[] getPoint(int index) {
		ExceptionUtility.verifyIndex(this.dataPoints.length, index);
		return this.dataPoints[index];
	}

	@Override
	public double distance(int i, int j) {
		return this.distance(this.dataPoints[i], this.dataPoints[j]);
	}
	
}
