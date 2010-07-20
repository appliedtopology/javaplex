/**
 * 
 */
package edu.stanford.math.plex4.kd;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import edu.stanford.math.plex4.array_utility.ArrayGeneration;
import edu.stanford.math.plex4.utility.ArrayUtility2;
import edu.stanford.math.plex4.utility.ExceptionUtility;
import edu.stanford.math.plex4.utility.ListUtility;
import gnu.trove.set.hash.TIntHashSet;

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
	
	private List<Integer> indexTranslation;

	public KDTree(double[][] dataPoints) {
		ExceptionUtility.verifyNonNull(dataPoints);
		ExceptionUtility.verifyPositive(dataPoints.length);
		this.dataPoints = dataPoints;
		this.size = dataPoints.length;
		this.dimension = dataPoints[0].length;
		this.indexTranslation = ListUtility.toList(ArrayGeneration.range(0, this.size));
	}

	public List<Integer> getIndexTranslation() {
		return this.invertPermutation(this.indexTranslation);
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
			//System.out.println(ArrayUtility.toString(dataPoints[startIndex]));
			return new KDNode(startIndex);
		}
		int axis = depth % this.dimension;
		int medianIndex = startIndex + (endIndex - startIndex + 1) / 2;
		
		int randomizedIndex = randomizedSelect(dataPoints, startIndex, endIndex, medianIndex, axis);
		//System.out.println("StartIndex: " + startIndex + " EndIndex: " + endIndex);
		//System.out.println(ArrayUtility.toString(dataPoints));
		//System.out.println(ArrayUtility.toString(this.dataPoints[randomizedIndex]));
		//System.out.println("Median index: " + medianIndex);
		KDNode node = new KDNode(randomizedIndex);
		
		node.setLeft(this.kdIteration(depth + 1, startIndex, medianIndex - 1));
		node.setRight(this.kdIteration(depth + 1, medianIndex + 1, endIndex));

		return node;
	}
	
	public int nearestNeighborSearch(double[] queryPoint) {
		return nearestNeighborSearch(this.root, queryPoint, this.root.getIndex(), 0);
	}
	
	private int nearestNeighborSearch(KDNode node, double[] queryPoint, int bestIndex, int depth) {
		if (node == null) {
			return bestIndex;
		}
		double[] bestPoint = this.dataPoints[bestIndex];
		
		// get the current axis
		int axis = depth % this.dimension;
		// get the current point
		int currentIndex = node.getIndex();
		double[] currentPoint = this.dataPoints[currentIndex];
		
		// Calculate whether current node is the best so far
		double currentSquaredDistance = ArrayUtility2.squaredDistance(queryPoint, currentPoint);
		double bestSquaredDistance = ArrayUtility2.squaredDistance(queryPoint, bestPoint);
		if (currentSquaredDistance < bestSquaredDistance) {
			bestPoint = currentPoint;
			bestIndex = currentIndex;
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
		bestIndex = nearestNeighborSearch(nearChild, queryPoint, bestIndex, depth + 1);
		
		// test to see if we need to search other half-space
		double separatingPlaneDistance = ArrayUtility2.squaredDistance(queryPoint[axis], currentPoint[axis]);
		if (bestSquaredDistance > separatingPlaneDistance) {
			bestIndex = nearestNeighborSearch(farChild, queryPoint, bestIndex, depth + 1);
		}
		
		return bestIndex;		
	}
	
	public TIntHashSet epsilonNeighborhoodSearch(double[] queryPoint, double epsilon) {
		TIntHashSet neighborhood = new TIntHashSet();
		epsilonNeighborhoodSearch(this.root, queryPoint, neighborhood, 0, epsilon * epsilon);
		return neighborhood;
	}
	
	private void epsilonNeighborhoodSearch(KDNode node, double[] queryPoint, TIntHashSet neighborhood, int depth, double epsilonSquared) {		
		if (node == null) {
			return;
		}
		
		// get the current axis
		int axis = depth % this.dimension;
		// get the current point
		int currentIndex = node.getIndex();
		double[] currentPoint = this.dataPoints[currentIndex];

		// calculate whether the current node belongs in the epsilon-neighborhood
		// of the query point
		double currentSquaredDistance = ArrayUtility2.squaredDistance(queryPoint, currentPoint);
		if (currentSquaredDistance < epsilonSquared) {
			neighborhood.add(currentIndex);
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
	
	private int partition(double[][] array, int startIndex, int endIndex, int axis) {
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
				ArrayUtility2.swap(array, i, j);
				this.indexTranslation = ArrayUtility2.swap(this.indexTranslation, i, j);
				i++;
				j--;
			} else {
				return j;
			}
		}
	}
	
	private List<Integer> invertPermutation(List<Integer> permutation) {
		List<Integer> inverse = new ArrayList<Integer>();
		for (int i = 0; i < permutation.size(); i++) {
			inverse.add(0);
		}
		for (int i = 0; i < permutation.size(); i++) {
			inverse.set(permutation.get(i), i);
		}
		return inverse;
	}

	private int randomizedPartition(double[][] array, int startIndex, int endIndex, int axis) {
		int i = startIndex + random.nextInt(endIndex - startIndex);
		ArrayUtility2.swap(array, i, startIndex);
		this.indexTranslation = ArrayUtility2.swap(this.indexTranslation, i, startIndex);
		return partition(array, startIndex, endIndex, axis);
	}

	private int randomizedSelect(double[][] array, int startIndex, int endIndex, int i, int axis) {
		if (startIndex == endIndex) {
			return startIndex;
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
