package edu.stanford.math.plex4.math.metric.impl;

import edu.stanford.math.plex4.kd.KDTree;
import edu.stanford.math.plex4.math.metric.interfaces.SearchableFiniteMetricSpace;
import edu.stanford.math.plex4.utility.ArrayUtility2;
import edu.stanford.math.plex4.utility.ExceptionUtility;
import edu.stanford.math.plex4.utility.Infinity;
import gnu.trove.set.hash.TIntHashSet;

/**
 * @author Andrew Tausz
 *
 */
public class KDEuclideanMetricSpace implements SearchableFiniteMetricSpace<double[]> {
	private final double[][] dataPoints;
	private final KDTree tree;
	private final int dimension;
	
	public KDEuclideanMetricSpace(double[][] dataPoints) {
		ExceptionUtility.verifyNonNull(dataPoints);
		ExceptionUtility.verifyPositive(dataPoints.length);
		this.dataPoints = dataPoints;
		this.tree = new KDTree(dataPoints);
		this.dimension = dataPoints[0].length;
	}
	
	public double[][] getPoints() {
		return this.dataPoints;
	}
	
	public TIntHashSet getKNearestNeighbors(double[] queryPoint, int k) {
		// TODO Auto-generated method stub
		return null;
	}

	public int getNearestPoint(double[] queryPoint) {
		return this.tree.nearestNeighborSearch(queryPoint);
	}

	public TIntHashSet getOpenNeighborhood(double[] queryPoint, double epsilon) {
		return this.tree.epsilonNeighborhoodSearch(queryPoint, epsilon, true);
	}
	
	public TIntHashSet getClosedNeighborhood(double[] queryPoint, double epsilon) {
		return this.tree.epsilonNeighborhoodSearch(queryPoint, epsilon, false);
	}

	public int size() {
		return dataPoints.length;
	}

	public double distance(double[] a, double[] b) {
		return Math.sqrt(ArrayUtility2.squaredDistance(a, b));
	}

	public double[] getPoint(int index) {
		ExceptionUtility.verifyIndex(this.dataPoints.length, index);
		return this.dataPoints[index];
	}

	public double distance(int i, int j) {
		return this.distance(this.getPoint(i), this.getPoint(j));
	}
	
	public double[] getMaximumCoordinates() {
		double[] maxima = new double[this.dimension];
		for (int j = 0; j < this.dimension; j++) {
			maxima[j] = Infinity.Double.getNegativeInfinity();
		}
		for (int i = 0; i < this.dataPoints.length; i++) {
			for (int j = 0; j < this.dimension; j++) {
				maxima[j] = Math.max(maxima[j], this.dataPoints[i][j]);
			}
		}
		
		return maxima;
	}
	
	public double[] getMinimumCoordinates() {
		double[] minima = new double[this.dimension];
		for (int j = 0; j < this.dimension; j++) {
			minima[j] = Infinity.Double.getPositiveInfinity();
		}
		for (int i = 0; i < this.dataPoints.length; i++) {
			for (int j = 0; j < this.dimension; j++) {
				minima[j] = Math.min(minima[j], this.dataPoints[i][j]);
			}
		}
		
		return minima;
	}
}
