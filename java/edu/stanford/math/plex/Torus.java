package edu.stanford.math.plex;

import static java.lang.Math.PI;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;

/**
 * A <code>Torus</code> instance is  discrete simulation of a d-dimensional torus. For testing.
 *
 * <p>Simplest implementation of a d-dimensional torus. Intended for testing.
 *
 * @version $Id$
 */

public class Torus extends PointData.NSpace {

	static final double COORD_LIMIT = 2.0 * PI;

	final double[] points;
	final int dimensions;

	/**
	 * The number of dimensions of the space.
	 *
	 * <p>
	 *
	 * @return     the dimension of the data.
	 */
	public int dimension() {
		return dimensions;
	}

	/**
	 * i-th coordinate of point p;
	 *
	 * <p>
	 * @param      p   the point
	 * @param      i   the coordinate index
	 * @return     i-th coordinate
	 */
	public double coordinate(int p, int i) {
		int p_base = dimensions * p;
		if ((i >= dimensions) || (i < 0))
			throw new IllegalArgumentException("Coordinate index " + i + 
					"must be in range [0, " + 
					(dimensions-1) + "].");
		if ((i % 1) == 1) 
			return sin(points[p_base + i]);
		else
			return cos(points[p_base + i]);
	}

	/**
	 * The number of data points.
	 *
	 * <p>
	 *
	 * @return     the number of points
	 */
	public int count() {
		if (points != null)
			return (points.length/dimensions)-1;
		else
			return 0;
	}

	/**
	 * Toroidal distance.
	 *
	 * <p>
	 *
	 * @param      p1   the first point
	 * @param      p2   the second point
	 * @return     the distance between p1 and p2 on the torus.
	 */
	public double distance (int p1, int p2) {
		if (p1 == p2)
			return 0.0;
		else if ((p1 == 0) || (p2 == 0))
			return Double.MAX_VALUE;
		else {
			int p1_base = dimensions * p1;
			int p2_base = dimensions * p2;
			double accum = 0.0;
			for (int i = 0; i < dimensions; i++) {
				double p1ix = cos(points[p1_base + i]);
				double p1iy = sin(points[p1_base + i]);
				double p2ix = cos(points[p2_base + i]);
				double p2iy = sin(points[p2_base + i]);
				accum  += (((p1ix - p2ix) * (p1ix - p2ix)) + 
						((p1iy - p2iy) * (p1iy - p2iy)));
			}
			return sqrt(accum);
		}
	}

	// Make sure that the data and dimension arguments are not absurd.
	private boolean pointsAreConsistent(double[] data, int dimension) {
		if (dimension < 1)
			return false;
		if ((data.length % dimension) != 0)
			return false;
		if (data.length <= dimension)
			return false;
		for (int i = 0; i < dimension; i++) {
			if (data[i] != 0.0)
				return false;
		}
		return true;
	}

	/**
	 * Make a Torus with a given data array.
	 *
	 * <p>
	 *
	 * @param     data double[] of point data.
	 */
	public Torus(double[] data, int d) {
		points = data;
		dimensions = d;
		assert pointsAreConsistent(data, dimensions);
	}

	// exponentiation of integers
	private int expt(int n, int k) {
		assert (n >= 0) && (k >= 0);
		if (k == 0) {
			return 1;
		} else if (n == 0) {
			return 0;
		} else {
			int accum = 1;
			for (int i = 0; i < k; i++) {
				assert (accum > 0) && ((n * accum) > 0);
				accum = n * accum;
			}
			return accum;
		}
	}

	// Increment a d-dimensional array of points so that we systematically
	// go through all of the points in an incr-length grid.
	private boolean incr_pt(double[] pt, double incr) {
		for (int i = 0; i < pt.length; i++) {
			if ((incr + pt[i]) < COORD_LIMIT) {
				pt[i] += incr; 
				// clear the previous indices
				while(i > 0) pt[--i] = 0.0;
				return true;
			} 
		}
		return false;
	}

	/**
	 * Make an n-way grid on the unit Torus.
	 *
	 * <p>
	 *
	 * @param      n   grid size parameter
	 */
	public Torus(int n, int d) {
		assert (n > 0) && (d > 0);
		int pt_counter = expt(n, d);
		double[] data = new double[d * (pt_counter + 1)];
		double[] pt = new double[d];
		double incr = COORD_LIMIT/((double) n);
		for (int i = 1; i <= pt_counter; i++) {
			for (int j = 0; j < d; j++) {
				data[(d * i) + j] = pt[j];
			}
			boolean success = incr_pt(pt, incr);
			assert (i == pt_counter) || success;
		}
		assert(pointsAreConsistent(data, d));
		points = data;
		dimensions = d;
	}

	// Argless constructor for this class Test use only.
	public Torus() {
		double[] data = new double[] {0, 0, 0, 0, .5, .5, 0, .5, .5, 0, 
				.25, 0, 0, .25, .25, .25, .25, .5};
		dimensions = 2;
		assert(pointsAreConsistent(data, dimensions));
		points = data;
	}
}
