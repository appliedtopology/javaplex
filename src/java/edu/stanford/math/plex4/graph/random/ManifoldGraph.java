package edu.stanford.math.plex4.graph.random;

import edu.stanford.math.plex4.graph.AbstractUndirectedGraph;
import edu.stanford.math.plex4.utility.RandomUtility;
import edu.stanford.math.primitivelib.autogen.array.DoubleArrayQuery;
import edu.stanford.math.primitivelib.autogen.array.IntArrayQuery;

/**
 * This class is used to create a k-nearest neighbors graph on a manifold.
 * It is abstract, and subclasses are required to implement the metric
 * on the manifold.
 * 
 * @author Andrew Tausz
 *
 */
public abstract class ManifoldGraph extends GraphInstanceGenerator {

	private static final long serialVersionUID = -5873098569927731498L;
	protected double points[][];
	int size;
	int dimension;
	int k;
	
	/**
	 * Constructor for the ManifoldGraph class.
	 * 
	 * @param size the number of points in the graph
	 * @param dimension the dimension of the manifold
	 * @param k the number of points to connect to 
	 */
	protected ManifoldGraph(int size, int dimension, int k) {
		this.size = size;
		this.dimension = dimension;
		this.k = k;
	}
	
	@Override
	public AbstractUndirectedGraph generate() {
		// generate random points the manifold
		this.generatePoints();
		
		// construct graph
		AbstractUndirectedGraph graph = this.initializeGraph(this.size);
		
		// connect each point to its k closest neighbors
		for (int i = 0; i < this.size; i++) {
			int[] nearestNeighbors = this.getNearestNeighbors(i);
			
			for (int j = 0; j < k; j++) {
				graph.addEdge(i, nearestNeighbors[j]);
			}
		}
		
		return graph;
	}
	
	
	/**
	 * This function generates points on the d-dimensional cube [0, 1]^d.
	 * It is intended for manifolds represented by squares (or cubes) with
	 * their edges identified some way.
	 */
	protected void generateUniformPoints() {
		this.points = RandomUtility.uniformMatrix(this.size, this.dimension);
	}
	
	/**
	 * This function must generate the set of random points on the manifold.
	 */
	protected abstract void generatePoints();
	
	/**
	 * This function must implement the distance function on the manifold.
	 * 
	 * @param x the first point
	 * @param y the second point
	 * @return the distance between x and y in the manifold
	 */
	protected abstract double distance(double[] x, double[] y);
	
	/**
	 * This function gets the indices of the k nearest neighbors
	 * to the point at the specified index.
	 * 
	 * @param index the index of the point 
	 * @return the indices of the k nearest neighbors 
	 */
	protected int[] getNearestNeighbors(int index) {
		double[] x = this.points[index];
		double[] distances = new double[this.size];
		int[] nearestNeighbors = new int[this.k];
		
		for (int i = 0; i < this.size; i++) {
			distances[i] = this.distance(x, this.points[i]);
		}
		nearestNeighbors = DoubleArrayQuery.getMinimumIndices(distances, k + 1);
		nearestNeighbors = IntArrayQuery.removeElement(nearestNeighbors, index);
		
		return nearestNeighbors;
	}
	
}
