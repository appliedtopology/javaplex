package edu.stanford.math.plex4.kd;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import edu.stanford.math.plex4.utility.ExceptionUtility;
import edu.stanford.math.primitivelib.autogen.array.DoubleArrayMath;
import edu.stanford.math.primitivelib.autogen.array.IntArrayGeneration;
import edu.stanford.math.primitivelib.autogen.array.IntArrayUtility;
import edu.stanford.math.primitivelib.autogen.pair.ObjectObjectPair;
import gnu.trove.TIntHashSet;

/**
 * This class implements the functionality of a KD-tree. It is 
 * designed for the efficient searching of Euclidean spaces.
 * 
 * @author Andrew Tausz
 *
 */
public class KDTree {
	private final int size;
	private final int dimension;
	private KDNode root;
	private static Random random = new Random();
	
	/**
	 * There are two options for constructing the KD-tree. One is
	 * to partition by medians, and the other is to partition randomly.
	 * Partitioning by medians produces a more balanced tree, but is
	 * more expensive to construct.
	 */
	private boolean useMedian = true;
	
	/**
	 * Points are stored as rows in the dataPoints array
	 */
	private final double[][] dataPoints;

	/**
	 * Constructor for initializing the KD-tree with a set of points.
	 * @param dataPoints
	 */
	public KDTree(double[][] dataPoints) {
		ExceptionUtility.verifyNonNull(dataPoints);
		ExceptionUtility.verifyPositive(dataPoints.length);
		this.dataPoints = dataPoints;
		this.size = dataPoints.length;
		this.dimension = dataPoints[0].length;
		
		// construct KD tree
		this.root = this.construct(IntArrayUtility.toList(IntArrayGeneration.range(0, this.size)), 0);
	}
	
	/**
	 * This function recursively constructs the tree by partitioning the
	 * Euclidean space axis-by-axis.
	 * 
	 * @param points the list of points to partition
	 * @param depth the current depth of the iteration
	 * @return a KDNode for the current subtree
	 */
	private KDNode construct(List<Integer> points, int depth) {
		int axis = depth % this.dimension;
		
		if (points.isEmpty()) {
			return null;
		}
		if (points.size() == 1) {
			return new KDNode(points.get(0), axis);
		}
		
		int splitIndex = 0;
		if (this.useMedian) {
			splitIndex = this.getMedianSplitIndex(points, axis);
		} else {
			splitIndex = this.getRandomSplitIndex(points);
		}
		
		ObjectObjectPair<List<Integer>, List<Integer>> pointListPair = this.splitPoints(points, axis, splitIndex);
		
		points = null;
		
		KDNode kdNode = new KDNode(splitIndex, axis);
		
		
		kdNode.setLeft(this.construct(pointListPair.getFirst(), depth + 1));
		kdNode.setRight(this.construct(pointListPair.getSecond(), depth + 1));
		
		return kdNode;
	}
	
	/**
	 * This function simply returns a random element within the list of indices.
	 * 
	 * @param pointIndices the list to select from
	 * @return a random element from the list
	 */
	private int getRandomSplitIndex(List<Integer> pointIndices) {
		return pointIndices.get(random.nextInt(pointIndices.size()));
	}
	
	/**
	 * This function selects the index that is the median of the values when sorted 
	 * by the specified axis.
	 * 
	 * @param pointIndices
	 * @param axis the axis to find the median along
	 * @return the index of the median
	 */
	private int getMedianSplitIndex(List<Integer> pointIndices, final int axis) {
		Comparator<Integer> comparator = getAxisComparator(axis);
		java.util.Collections.sort(pointIndices, comparator);
		return pointIndices.get((pointIndices.size() + 1) / 2);
	}
	
	/**
	 * This function splits the points into two groups - those that are less than the one at the split index,
	 * and those that are greater than or equal.
	 * 
	 * @param pointIndices the list of points to divide
	 * @param axis the axis to compare along
	 * @param splitIndex the index to split on
	 * @return a GenericPair containing the two lists described above
	 */
	private ObjectObjectPair<List<Integer>, List<Integer>> splitPoints(List<Integer> pointIndices, final int axis, int splitIndex) {
		Comparator<Integer> comparator = getAxisComparator(axis);
		List<Integer> lessThan = new ArrayList<Integer>();
		List<Integer> greaterThanOrEqual = new ArrayList<Integer>();
		
		for (Integer index: pointIndices) {
			if (comparator.compare(index, splitIndex) < 0) {
				lessThan.add(index);
			} else {
				greaterThanOrEqual.add(index);
			}
		}
		
		return new ObjectObjectPair<List<Integer>, List<Integer>>(lessThan, greaterThanOrEqual);
	}
	
	/**
	 * This function returns a comparator used for ordering the points by their locations along
	 * a particular axis.
	 * 
	 * @param axis the axis to order by
	 * @return a comparator which compares points by a particular coordinate
	 */
	private Comparator<Integer> getAxisComparator(final int axis) {
		return new Comparator<Integer>() {

			public int compare(Integer o1, Integer o2) {
				if (dataPoints[o1][axis] < dataPoints[o2][axis]) {
					return -1;
				} else if (dataPoints[o1][axis] > dataPoints[o2][axis]) {
					return 1;
				} else {
					return 0;
				}
			}
		};
	}
	
	/**
	 * This function returns the index of the nearest neighbor to the query point.
	 * 
	 * @param queryPoint the point to find the closest point to
	 * @return the index of the nearest point to the query point
	 */
	public int nearestNeighborSearch(double[] queryPoint) {
		return nearestNeighborSearch(this.root, queryPoint, this.root.getIndex(), 0);
	}

	/**
	 * This function performs the nearest neighborhood search recursively.
	 * It is an internal helper function.
	 * 
	 * @param node the node to search in
	 * @param queryPoint the reference point
	 * @param bestIndex the best point so far
	 * @param depth the recursion depth
	 * @return the index of the nearest point to the query point
	 */
	private int nearestNeighborSearch(KDNode node, double[] queryPoint, int bestIndex, int depth) {
		if (node == null) {
			return bestIndex;
		}
		double[] bestPoint = this.dataPoints[bestIndex];

		// get the current axis
		int axis = node.getSplitAxis();
		// get the current point
		int currentIndex = node.getIndex();
		double[] currentPoint = this.dataPoints[currentIndex];

		// Calculate whether current node is the best so far
		double currentSquaredDistance = DoubleArrayMath.squaredDistance(queryPoint, currentPoint);
		double bestSquaredDistance = DoubleArrayMath.squaredDistance(queryPoint, bestPoint);
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
		double separatingPlaneDistance = DoubleArrayMath.squaredDistance(queryPoint[axis], currentPoint[axis]);
		if (bestSquaredDistance > separatingPlaneDistance) {
			bestIndex = nearestNeighborSearch(farChild, queryPoint, bestIndex, depth + 1);
		}

		return bestIndex;		
	}

	/**
	 * This function finds all points within an open or closed neighborhood of the query point.
	 * 
	 * @param queryPoint the center of the ball to query
	 * @param epsilon the radius of the ball
	 * @param openNeighborhood true if the neighborhood is open and false otherwise
	 * @return the indices of those points that fall within the ball centered at the query point
	 */
	public TIntHashSet epsilonNeighborhoodSearch(double[] queryPoint, double epsilon, boolean openNeighborhood) {
		TIntHashSet neighborhood = new TIntHashSet();
		epsilonNeighborhoodSearch(this.root, queryPoint, neighborhood, 0, epsilon * epsilon, openNeighborhood);
		return neighborhood;
	}

	/**
	 * This is a helper function which performs the recursive search for the neighborhood search.
	 * 
	 * @param node the current KD-tree node
	 * @param queryPoint the reference point
	 * @param neighborhood the current set of points found
	 * @param depth the recursion depth
	 * @param epsilonSquared the square of the radius of the ball
	 * @param openNeighborhood true if the neighborhood is open and false otherwise
	 */
	private void epsilonNeighborhoodSearch(KDNode node, double[] queryPoint, TIntHashSet neighborhood, int depth, double epsilonSquared, boolean openNeighborhood) {		
		if (node == null) {
			return;
		}

		// get the current axis
		int axis = node.getSplitAxis();
		// get the current point
		int currentIndex = node.getIndex();
		double[] currentPoint = this.dataPoints[currentIndex];

		// calculate whether the current node belongs in the epsilon-neighborhood
		// of the query point
		double currentSquaredDistance = DoubleArrayMath.squaredDistance(queryPoint, currentPoint);
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
		if (openNeighborhood) {
			if (epsilonSquared > squaredAxisDistance) {
				// yes there is overlap - so search far half-space
				epsilonNeighborhoodSearch(farChild, queryPoint, neighborhood, depth + 1, epsilonSquared, openNeighborhood);
			}
		} else {
			if (epsilonSquared >= squaredAxisDistance) {
				// yes there is overlap - so search far half-space
				epsilonNeighborhoodSearch(farChild, queryPoint, neighborhood, depth + 1, epsilonSquared, openNeighborhood);
			}
		}

		// search the near half-space for more neighborhood members
		epsilonNeighborhoodSearch(nearChild, queryPoint, neighborhood, depth + 1, epsilonSquared, openNeighborhood);
	}
}
