/**
 * 
 */
package edu.stanford.math.plex4.graph.random;

import edu.stanford.math.primitivelib.autogen.array.DoubleArrayMath;


/**
 * Generates n random points in [0,1]^d and connects each point to its k-nearest
 * neighbors
 * 
 * @author Andrew Tausz
 */

public class KNearestNeighborsGraph extends ManifoldGraph {

	public KNearestNeighborsGraph(int size, int dimension, int k) {
		super(size, dimension, k);
	}

	@Override
	protected double distance(double[] x, double[] y) {
		return DoubleArrayMath.distance(x, y);
	}

	@Override
	protected void generatePoints() {
		this.generateUniformPoints();
	}

	@Override
	public String toString() {
		return "CubeKNN(" + this.size + "," + this.dimension + "," + this.k + ")";
	}

}
