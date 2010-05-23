package edu.stanford.math.plex_plus.graph;

/**
 * This interface abstracts the functionality of an unweighted graph.
 * The graph may be directed or undirected. The vertices of the graph
 * are labeled {0, ..., n - 1}, where n is the number of vertices.
 * 
 * @author Andrew Tausz
 *
 */
public interface AbstractGraph {
	
	/**
	 * Gets the number of vertices in the graph.
	 * 
	 * @return the number of vertices in the graph
	 */
	public int getNumVertices();
	
	/**
	 * Gets the number of edges in the graph.
	 * 
	 * @return the number of edges in the graph
	 */
	public int getNumEdges();
	
	/**
	 * This function returns true if the edge (i, j) is in the graph,
	 * and false otherwise.
	 * 
	 * @param i the first vertex of the edge
	 * @param j the second vertex of the edge
	 * @return true if (i, j) is an edge in the graph and false otherwise
	 */
	public boolean containsEdge(int i, int j);
	
	/**
	 * This function adds the edge (i, j) to the graph.
	 * 
	 * @param i the first vertex of the edge to add
	 * @param j the second vertex of the edge to add
	 */
	public void addEdge(int i, int j);
	
	/**
	 * This function removes the edge (i, j) from the graph.
	 * 
	 * @param i the first vertex of the edge to add
	 * @param j the second vertex of the edge to add
	 */
	public void removeEdge(int i, int j);
}
