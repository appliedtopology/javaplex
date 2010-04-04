/**
 * 
 */
package edu.stanford.math.plex_plus.math.metric;

import edu.stanford.math.plex_plus.tree.BinaryNode;
import edu.stanford.math.plex_plus.utility.ExceptionUtility;

/**
 * @author Andrew Tausz
 *
 */
public class KDTree {
	private final int size;
	private final int dimension;
	private BinaryNode root;

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
		this.kdIteration(0, 0, this.size);
	}

	/**
	 * 
	 * @param depth
	 * @param startIndex
	 * @param endIndex one past the last legal index
	 */
	private void kdIteration(int depth, int startIndex, int endIndex) {
		int axis = depth % this.dimension;
		
		/**
		 * TODO: Implement
		 */
	}

	private double getMedian(int axis, int startIndex, int endIndex) {
		double median = 0;
		/**
		 * TODO: Implement
		 */
		return median;
	}
}
