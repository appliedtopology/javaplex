/**
 * 
 */
package edu.stanford.math.plex_plus.math.metric;

/**
 * @author Andrew Tausz
 *
 */
public class EuclideanMetricSpace implements IntFiniteMetricSpace {
	/**
	 * Data points are stored as rows in the array points; 
	 */
	private double[][] points;
	private int dimension;
	private int numPoints;
	
	public EuclideanMetricSpace(){}
	
	/* (non-Javadoc)
	 * @see edu.stanford.math.plex_plus.math.metric.IntFiniteMetricSpace#size()
	 */
	@Override
	public int size() {
		return this.numPoints;
	}

	/* (non-Javadoc)
	 * @see edu.stanford.math.plex_plus.math.metric.IntMetricSpace#distance(int, int)
	 */
	@Override
	public double distance(int i, int j) {
		return 0;
	}

	public int getDimension() {
		return this.dimension;
	}
}
