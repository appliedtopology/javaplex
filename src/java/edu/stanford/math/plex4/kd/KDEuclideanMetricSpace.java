package edu.stanford.math.plex4.kd;

import edu.stanford.math.plex4.metric.impl.ObjectSearchableFiniteMetricSpace;
import edu.stanford.math.primitivelib.autogen.array.DoubleArrayMath;
import edu.stanford.math.primitivelib.utility.Infinity;
import gnu.trove.TIntHashSet;

/**
 * This class implements the AbstractSearchableMetricSpace interface for providing query functions
 * for a Euclidean metric space. The underlying search operations are performed with the help of a KD-tree.
 * 
 * @author Andrew Tausz
 *
 */
public class KDEuclideanMetricSpace extends ObjectSearchableFiniteMetricSpace<double[]> {
	private final KDTree tree;
	private final int dimension;
	
	public KDEuclideanMetricSpace(double[][] dataPoints) {
		super(dataPoints);
		this.tree = new KDTree(dataPoints);
		this.dimension = dataPoints[0].length;
	}
	
	public double[][] getPoints() {
		return this.elements;
	}

	public int getNearestPointIndex(double[] queryPoint) {
		return this.tree.nearestNeighborSearch(queryPoint);
	}

	public TIntHashSet getOpenNeighborhood(double[] queryPoint, double epsilon) {
		return this.tree.epsilonNeighborhoodSearch(queryPoint, epsilon, true);
	}
	
	public TIntHashSet getClosedNeighborhood(double[] queryPoint, double epsilon) {
		return this.tree.epsilonNeighborhoodSearch(queryPoint, epsilon, false);
	}

	public int size() {
		return elements.length;
	}

	public double distance(double[] a, double[] b) {
		return Math.sqrt(DoubleArrayMath.squaredDistance(a, b));
	}

	public double[] getPoint(int index) {
		return this.elements[index];
	}

	public double distance(int i, int j) {
		return this.distance(this.getPoint(i), this.getPoint(j));
	}
	
	public double[] getMaximumCoordinates() {
		double[] maxima = new double[this.dimension];
		for (int j = 0; j < this.dimension; j++) {
			maxima[j] = Infinity.Double.getNegativeInfinity();
		}
		for (int i = 0; i < this.elements.length; i++) {
			for (int j = 0; j < this.dimension; j++) {
				maxima[j] = Math.max(maxima[j], this.elements[i][j]);
			}
		}
		
		return maxima;
	}
	
	public double[] getMinimumCoordinates() {
		double[] minima = new double[this.dimension];
		for (int j = 0; j < this.dimension; j++) {
			minima[j] = Infinity.Double.getPositiveInfinity();
		}
		for (int i = 0; i < this.elements.length; i++) {
			for (int j = 0; j < this.dimension; j++) {
				minima[j] = Math.min(minima[j], this.elements[i][j]);
			}
		}
		
		return minima;
	}
}
