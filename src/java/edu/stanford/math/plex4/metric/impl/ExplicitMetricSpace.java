package edu.stanford.math.plex4.metric.impl;

import java.util.List;

import edu.stanford.math.plex4.metric.interfaces.AbstractSearchableMetricSpace;
import edu.stanford.math.primitivelib.utility.Infinity;
import gnu.trove.TIntHashSet;

/**
 * This class implements the AbstractIntMetricSpace interface. The distances between points
 * is given by a distance matrix.
 * 
 * @author Andrew Tausz
 *
 */
public class ExplicitMetricSpace implements AbstractSearchableMetricSpace<Integer> {
	private final double[][] distanceMatrix;
	
	/**
	 * This constructor initializes the class with the given distance matrix.
	 * The supplied distance matrix must be square, and must define a valid metric.
	 * Ie. it must be symmetric, have zeros exactly on the diagonal, and satisfy
	 * the triangle inequality. Due to reasons of efficiency, these properties
	 * are not checked, and the user is required make sure that they hold.
	 * If the properties do not hold, no guarantees can be made about the
	 * behaviour of the algorithms.
	 * 
	 * @see edu.stanford.math.plex4.metric.interfaces.AbstractSearchableMetricSpace
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

	public TIntHashSet getClosedNeighborhood(Integer queryPoint, double epsilon) {
		TIntHashSet result = new TIntHashSet();
		
		for (int i = 0; i < distanceMatrix[queryPoint].length; i++) {
			double distance = distanceMatrix[queryPoint][i];
			if (distance <= epsilon) {
				result.add(i);
			}
		}
		
		return result;
	}

	public TIntHashSet getKNearestNeighbors(Integer queryPoint, int k) {
		TruncatedPriorityQueue<Integer> tpq = new TruncatedPriorityQueue<Integer>(k);
		
		for (int i = 0; i < distanceMatrix[queryPoint].length; i++) {
			if (queryPoint.equals(i)) {
				continue;
			}
			double distance = distanceMatrix[queryPoint][i];
			tpq.insert(i, distance);
		}
		
		TIntHashSet result = new TIntHashSet();
		
		List<Integer> indices = tpq.getIndices();
		for (Integer index: indices) {
			result.add(index);
		}
		
		return result;
	}

	public int getNearestPointIndex(Integer queryPoint) {
		double minDistance = Infinity.Double.getPositiveInfinity();
		int minIndex = -1;
		
		for (int i = 0; i < distanceMatrix[queryPoint].length; i++) {
			if (queryPoint.equals(i)) {
				continue;
			}
			double distance = distanceMatrix[queryPoint][i];
			if (distance < minDistance) {
				minDistance = distance;
				minIndex = i;
			}
		}

		return minIndex;
	}

	public TIntHashSet getOpenNeighborhood(Integer queryPoint, double epsilon) {
		TIntHashSet result = new TIntHashSet();
		
		for (int i = 0; i < distanceMatrix[queryPoint].length; i++) {
			double distance = distanceMatrix[queryPoint][i];
			if (distance < epsilon) {
				result.add(i);
			}
		}
		
		return result;
	}

	public Integer getPoint(int index) {
		return index;
	}

	public Integer[] getPoints() {
		Integer[] result = new Integer[distanceMatrix.length];
		for (int i = 0; i < distanceMatrix.length; i++) {
			result[i] = i;
		}
		
		return result;
	}

	public double distance(Integer a, Integer b) {
		return distanceMatrix[a][b];
	}
}
