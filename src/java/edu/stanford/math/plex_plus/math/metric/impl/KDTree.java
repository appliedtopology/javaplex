/**
 * 
 */
package edu.stanford.math.plex_plus.math.metric.impl;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import edu.stanford.math.plex_plus.tree.KDNode;
import edu.stanford.math.plex_plus.utility.ArrayUtility;
import edu.stanford.math.plex_plus.utility.ExceptionUtility;

/**
 * @author Andrew Tausz
 *
 */
public class KDTree {
	private final int size;
	private final int dimension;
	private KDNode root;
	private static Random random = new Random();

	/**
	 * Points are stored as rows in the dataPoints array
	 */
	private final double[][] dataPoints;

	public KDTree(double[][] dataPoints) {
		ExceptionUtility.verifyNonNull(dataPoints);
		ExceptionUtility.verifyPositive(dataPoints.length);
		this.dataPoints = dataPoints;
		this.size = dataPoints.length;
		this.dimension = dataPoints[0].length;
	}

	public void constructTree() {
		this.root = this.kdIteration(0, 0, this.size - 1);
	}

	/**
	 * 
	 * @param depth
	 * @param startIndex
	 * @param endIndex the last legal index
	 */
	private KDNode kdIteration(int depth, int startIndex, int endIndex) {
		if (startIndex > endIndex) {
			return null;
		}
		if (startIndex == endIndex) {
			System.out.println(ArrayUtility.toString(dataPoints[startIndex]));
			return new KDNode(dataPoints[startIndex]);
		}
		int axis = depth % this.dimension;
		int medianIndex = startIndex + (endIndex - startIndex + 1) / 2;
		
		double[] medianPoint = randomizedSelect(dataPoints, startIndex, endIndex, medianIndex, axis);
		System.out.println("StartIndex: " + startIndex + " EndIndex: " + endIndex);
		//System.out.println(ArrayUtility.toString(dataPoints));
		System.out.println(ArrayUtility.toString(medianPoint));
		System.out.println("Median index: " + medianIndex);
		KDNode node = new KDNode(medianPoint);
		
		node.setLeft(this.kdIteration(depth + 1, startIndex, medianIndex - 1));
		node.setRight(this.kdIteration(depth + 1, medianIndex + 1, endIndex));

		return node;
	}
	
	public double[] nearestNeighborSearch(double[] queryPoint) {
		return nearestNeighborSearch(this.root, queryPoint, this.root.getPoint(), 0);
	}
	
	private double[] nearestNeighborSearch(KDNode node, double[] queryPoint, double[] bestPoint, int depth) {
		if (node == null) {
			return bestPoint;
		}
		if (bestPoint == null) {
			bestPoint = node.getPoint();
		}
		
		// get the current axis
		int axis = depth % this.dimension;
		// get the current point
		double[] currentPoint = node.getPoint();
		
		// Calculate whether current node is the best so far
		double currentSquaredDistance = ArrayUtility.squaredDistance(queryPoint, currentPoint);
		double bestSquaredDistance = ArrayUtility.squaredDistance(queryPoint, bestPoint);
		if (currentSquaredDistance < bestSquaredDistance) {
			bestPoint = currentPoint;
		}
		
		// determine which side of the hyperplane the query point is in
		// if side > 0 then the query point is on the "right" side of the hyperplane
		double axisDifference = queryPoint[axis] - currentPoint[axis];
		// the near child is the node for the half space containing the query point
		KDNode nearChild = null;
		// the far child is the node for the half space not containing the query point
		KDNode farChild = null;
		if (axisDifference > 0) {
			nearChild = node.getRight();
			farChild = node.getLeft();
		} else {
			nearChild = node.getLeft();
			farChild = node.getRight();
		}
		
		// search the current half-space (the half-space containing the query point)
		bestPoint = nearestNeighborSearch(nearChild, queryPoint, bestPoint, depth + 1);
		
		// test to see if we need to search other half-space
		double separatingPlaneDistance = ArrayUtility.squaredDistance(queryPoint[axis], currentPoint[axis]);
		if (bestSquaredDistance > separatingPlaneDistance) {
			bestPoint = nearestNeighborSearch(farChild, queryPoint, bestPoint, depth + 1);
		}
		
		return bestPoint;		
	}
	
	public Set<double[]> epsilonNeighborhoodSearch(double[] queryPoint, double epsilon) {
		Set<double[]> neighborhood = new HashSet<double[]>();
		epsilonNeighborhoodSearch(this.root, queryPoint, neighborhood, 0, epsilon * epsilon);
		return neighborhood;
	}
	
	private void epsilonNeighborhoodSearch(KDNode node, double[] queryPoint, Set<double[]> neighborhood, int depth, double epsilonSquared) {		
		if (node == null) {
			return;
		}
		
		// get the current axis
		int axis = depth % this.dimension;
		// get the current point
		double[] currentPoint = node.getPoint();

		// calculate whether the current node belongs in the epsilon-neighborhood
		// of the query point
		double currentSquaredDistance = ArrayUtility.squaredDistance(queryPoint, currentPoint);
		if (currentSquaredDistance < epsilonSquared) {
			neighborhood.add(currentPoint);
		}
		
		// determine which side of the hyperplane the query point is in
		// if side > 0 then the query point is on the "right" side of the hyperplane
		double axisDifference = queryPoint[axis] - currentPoint[axis];
		double squaredAxisDistance = axisDifference * axisDifference;
		// the near child is the node for the half space containing the query point
		KDNode nearChild = null;
		// the far child is the node for the half space not containing the query point
		KDNode farChild = null;
		if (axisDifference > 0) {
			nearChild = node.getRight();
			farChild = node.getLeft();
		} else {
			nearChild = node.getLeft();
			farChild = node.getRight();
		}
		
		// determine whether the epsilon-neighborhood intersects the far half-space
		if (epsilonSquared > squaredAxisDistance) {
			// yes there is overlap - so search far half-space
			epsilonNeighborhoodSearch(farChild, queryPoint, neighborhood, depth + 1, epsilonSquared);
		}
		
		// search the near half-space for more neighborhood members
		epsilonNeighborhoodSearch(nearChild, queryPoint, neighborhood, depth + 1, epsilonSquared);
	}
	
	private static int partition(double[][] array, int startIndex, int endIndex, int axis) {
		ExceptionUtility.verifyNonNegative(startIndex);
		ExceptionUtility.verifyGreaterThanOrEqual(endIndex, startIndex);
		ExceptionUtility.verifyLessThan(endIndex, array.length);

		double pivotValue = array[startIndex][axis];
		int i = startIndex;
		int j = endIndex;

		while (true) {
			while (array[j][axis] > pivotValue) {
				j--;
			}
			while (array[i][axis] < pivotValue) {
				i++;
			}
			if (i < j) {
				ArrayUtility.swap(array, i, j);
				i++;
				j--;
			} else {
				return j;
			}
		}
	}

	private static int randomizedPartition(double[][] array, int startIndex, int endIndex, int axis) {
		int i = startIndex + random.nextInt(endIndex - startIndex);
		ArrayUtility.swap(array, i, startIndex);
		return partition(array, startIndex, endIndex, axis);
	}

	private static double[] randomizedSelect(double[][] array, int startIndex, int endIndex, int i, int axis) {
		if (startIndex == endIndex) {
			return array[startIndex];
		}

		int partitionBoundaryIndex = randomizedPartition(array, startIndex, endIndex, axis);
		int k = partitionBoundaryIndex - startIndex + 1;
		if (i < k) {
			return randomizedSelect(array, startIndex, partitionBoundaryIndex, i, axis);
		} else {
			return randomizedSelect(array, partitionBoundaryIndex + 1, endIndex, i - k, axis);
		}
	}
}
