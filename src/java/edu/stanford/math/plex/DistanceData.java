package edu.stanford.math.plex;

/**
 * The <code>DistanceData</code> class is the simplest implementation
 * of NSpace. Intended for smallish data sets.
 *
 * @version $Id$
 */

public class DistanceData extends PointData {

	final double[][] d;
	final int numPoints;

	/**
	 * The number of data points.
	 *
	 * <p>
	 *
	 * @return     the number of points
	 */
	public int count() {
		if (d != null)
			return (d.length);
		else
			return 0;
	}

	/**
	 * Euclidean distance.
	 *
	 * <p>
	 *
	 * @param      p1   the first point
	 * @param      p2   the second point
	 * @return     the distance between p1 and p2.
	 */
	public double distance (int p1, int p2) {
		if (p1 == p2)
			return 0.0;
		else {
			return d[p1-1][p2-1];
		}

	}

	// Constructor for this class.
	public DistanceData(double[][] data) {
		super();
		d = data;
		numPoints = d.length;
	}


}
