package edu.stanford.math.plex;

/**
 * The <code>DiscreteSpace</code> class implements a finite discrete metric
 * space. Used for testing.
 *
 * @version $Id$
 */
public class DiscreteSpace extends PointData {

	public final int N;

	/**
	 * The number of data points.
	 *
	 * <p>
	 *
	 * @return     the number of points
	 */
	public int count() {
		return N;
	}

	/**
	 * Discrete distance.
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
		else 
			return 1.0;
	}

	// Constructor for this class.
	public DiscreteSpace(int N) {
		this.N = N;
	}

}
