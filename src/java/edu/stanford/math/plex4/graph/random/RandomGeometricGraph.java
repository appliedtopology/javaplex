/**
 * 
 */
package edu.stanford.math.plex4.graph.random;

import edu.stanford.math.plex4.graph.AbstractUndirectedGraph;
import edu.stanford.math.plex4.utility.RandomUtility;

/**
 * Generates n random points in [0,1]^d and connects an edge between points x
 * and y if d(x,y)<r.
 * 
 * @author Tim Harrington
 * @date Mar 8, 2009
 */

public class RandomGeometricGraph extends GraphInstanceGenerator {

	protected int numNodes;
	protected int dimension;
	double radius;

	/**
	 * @param n
	 *            the number of nodes
	 * @param d
	 *            the dimension of unit cube to uniformly put points in
	 * @param r
	 *            the radius used to determine if an edge between points exists
	 */
	public RandomGeometricGraph(int n, int d, double r) {
		this.numNodes = n;
		this.dimension = d;
		this.radius = r;

	}

	@Override
	public AbstractUndirectedGraph generate() {
		AbstractUndirectedGraph graph = this.initializeGraph(numNodes);
		
		double[][] points = new double[this.numNodes][this.dimension];
		for (int i = 0; i < this.numNodes; i++) {
			for (int j = 0; j < this.dimension; j++) {
				points[i][j] = RandomUtility.nextUniform();
			}
		}

		double dist;
		for (int i = 0; i < this.numNodes; i++) {
			for (int j = 0; j < this.numNodes; j++) {
				if (i == j)
					continue;
				dist = distance(points[i], points[j]);
				if (dist < this.radius) {
					graph.addEdge(i, j);
				}
			}
		}

		return graph;
	}

	protected double distance(double[] x, double[] y) {
		double dist = 0;
		for (int j = 0; j < x.length; j++) {
			dist += (x[j] - y[j]) * (x[j] - y[j]);
		}
		dist = Math.sqrt(dist);
		return dist;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "RandomGeometricGraph(" + this.numNodes + "," + this.dimension
				+ "," + this.radius + ")";
	}
}
