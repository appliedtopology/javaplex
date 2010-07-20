package edu.stanford.math.plex4.graph;

/**
 * This interface defines functionality for a weighted graph.
 * The graph may be directed or undirected.
 * 
 * @author Andrew Tausz
 *
 */
public interface AbstractWeightedUndirectedGraph extends AbstractUndirectedGraph {
	/**
	 * This function returns the weight of the edge (i, j).
	 * 
	 * @param i the index of the first vertex of the edge
	 * @param j the index of the second vertex of the edge
	 * @return the weight of the edge (i, j)
	 */
	public double getWeight(int i, int j);
	
	/**
	 * This function adds an edge with specified weight.
	 * 
	 * @param i the index of the first vertex of the edge
	 * @param j the index of the second vertex of the edge
	 * @param weight the weight of the edge
	 */
	public void addEdge(int i, int j, double weight);
}
