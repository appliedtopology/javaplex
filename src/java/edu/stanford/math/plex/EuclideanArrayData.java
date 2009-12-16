package edu.stanford.math.plex;

import java.util.*;

/**
 * The <code>EuclideanArrayData</code> class is the simplest implementation
 * of NSpace. Intended for smallish data sets. Instances of this class are
 * implemented at 0-based arrays of double, but the first dimension()
 * entries are ignored, since PointData instances are indexed from 1 to
 * count(), inclusive. This is occasionally awkward, and it is the reason
 * that constructors that provide the data in the form of double[][], do
 * <i>not</i> require "blank space" to be provided.
 *
 * @version $Id$
 */

public class EuclideanArrayData extends PointData.NSpace {

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
	 * The number of data points.
	 *
	 * <p>
	 *
	 * @return     the number of points
	 */
	public int count() {
		if (points != null)
			return ((points.length/dimensions)-1);
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
	 * @return     the Euclidean distance between p1 and p2.
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
				double diff = points[p1_base + i] - points[p2_base + i];
				accum += diff * diff;
			}

			return Math.sqrt(accum);
		}

	}

	/**
	 * Get coordinates. Not much checking.
	 *
	 * <p>
	 *
	 * @param      p   the point
	 * @param      i   the coordinate
	 * @return     The i-th coordinate of p;
	 */
	public double coordinate (int p, int i) {
		if ((i >= dimensions) || (i < 0))
			throw new IllegalArgumentException("Coordinate index " + i + 
					"must be in range [0, " + 
					(dimensions-1) + "].");
		return points[(dimensions * p) + i];
	}

	/**
	 * Get point. No checking.
	 *
	 * <p>
	 *
	 * @param      p   the point
	 * @param      vec   place to store point
	 * @return     Coordinates of p;
	 */
	public double[] get_pt (int p, double[] vec) {
		assert(dimensions == vec.length);
		for (int i = 0; i < dimensions; i++) {
			vec[i] = points[(dimensions * p)+i];
		}
		return vec;
	}

	// Make sure that the data and dimension arguments are not absurd.
	protected boolean pointsAreConsistent(double[] data, int dimension) {
		if (dimension < 1) {
			throw new IllegalArgumentException("dimension, " + dimension +
			", must be a positive integer.");
		}
		if (((data.length % dimension) != 0) ||
				(data.length <= dimension)) {
			throw new IllegalArgumentException("data.length, " + data.length +
					", must be a multiple of, and " +
					"greater than, dimension, " +
					dimension + ".");
		}
		return true;
	}

	// Make sure that the data and dimension arguments are not absurd.
	protected boolean pointsAreConsistent(double[][] data) {
		boolean data_length_loser = (data.length < 2);
		if (!data_length_loser) {
			int dim = data[0].length;
			if (dim < 1) {
				throw new IllegalArgumentException("dimension, " + dim +
				", must be a positive integer.");
			}
			for (int i = 0; i < data.length; i++) {
				if (data[i].length != dim) {
					throw new IllegalArgumentException
					("data[" + i + "].length must be " +
							", must be constant for all elements of data, " +
							"and == " + dim + ".");
				}
			}
		} else {
			throw new IllegalArgumentException("data.length, " + data.length +
			", must be greater than 1.");
		}
		return true;
	}

	// Don't call this one.
	protected EuclideanArrayData() {
		super();
		points = null;
		dimensions = 0;
	}

	/**
	 * The obvious constructor for this class.  <p> This is more likely to be
	 * called programmatically. The first dimension entries in data are
	 * ignored.
	 *
	 * @param      data The first dimension entries of this array will be ignored.
	 * @param      dimension The number of dimensions.
	 */
	public EuclideanArrayData(double[] data, int dimension) {
		super();
		pointsAreConsistent(data, dimension);
		dimensions = dimension;
		points = data;
	}


	/**
	 * Construct a EuclideanArrayData instance from a double[][].  <p> This
	 * constructor requires taht the double[][] be of consistent dimenions;
	 * that is, data[i].length must be constant -- this will be the dimension
	 * of the instance. The instance will be initialize with data[0] being
	 * the first "point", so data.length will be count().
	 *
	 * @param      data A "rectangular matrix" of data; data[0] is the first point.
	 */
	public EuclideanArrayData(double[][] data) {
		super();
		pointsAreConsistent(data);
		dimensions = data[0].length;
		points = new double[(data.length + 1) * dimensions];
		for (int i = 0; i < data.length; i++) {
			for (int j = 0; j < dimensions; j++) {
				points[((i+1) * dimensions) + j] = data[i][j];
			}
		}
	}

	// Constructor for test use.
	public EuclideanArrayData(int num_pts, int dimension) {
		super();
		Random rand = new Random(0L);
		double[] data = new double[(num_pts + 1) * dimension];
		for (int i = 1; i <= num_pts; i++) {
			for (int j = 0; j < dimension; j++) {
				data[(i * dimension) + j] = rand.nextDouble();
			}
		}
		pointsAreConsistent(data, dimension);
		dimensions = dimension;
		points = data;
	}

	// Constructor for test use.
	public EuclideanArrayData(int num_pts, int dimension, double min, double max) {
		super();
		Random rand = new Random(0L);
		double[] data = new double[(num_pts + 1) * dimension];
		double len = max - min;
		for (int i = 1; i <= num_pts; i++) {
			for (int j = 0; j < dimension; j++) {
				data[(i * dimension) + j] = (len * rand.nextDouble()) + min;
			}
		}
		pointsAreConsistent(data, dimension);
		dimensions = dimension;
		points = data;
	}
}
