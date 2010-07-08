/**
 * 
 */
package edu.stanford.math.plex_plus.math.metric.impl;

import edu.stanford.math.plex_plus.math.metric.interfaces.IntFiniteMetricSpace;
import edu.stanford.math.plex_plus.utility.ExceptionUtility;

/**
 * @author Andrew Tausz
 *
 */
public class ExplicitMetricSpace implements IntFiniteMetricSpace {
	private final double[][] distanceMatrix;
	public ExplicitMetricSpace(double[][] distanceMatrix) {
		ExceptionUtility.verifyNonNull(distanceMatrix);
		
		this.distanceMatrix = distanceMatrix;
	}
	
	/* (non-Javadoc)
	 * @see edu.stanford.math.plex_plus.math.metric.interfaces.IntFiniteMetricSpace#size()
	 */
	public int size() {
		return this.distanceMatrix.length;
	}

	/* (non-Javadoc)
	 * @see edu.stanford.math.plex_plus.math.metric.interfaces.IntMetricSpace#distance(int, int)
	 */
	public double distance(int i, int j) {
		return this.distanceMatrix[i][j];
	}

}
