/**
 * 
 */
package edu.stanford.math.plex_plus.graph;

import edu.stanford.math.plex_plus.utility.ExceptionUtility;
import gnu.trove.map.hash.TIntDoubleHashMap;
import gnu.trove.map.hash.TIntObjectHashMap;
import gnu.trove.set.TIntSet;
import gnu.trove.set.hash.TIntHashSet;

/**
 * This class implements the functionality of an undirected weighted graph.
 * It uses an adjacency list representation. See the class
 * UndirectedListGraph for more information about the internal representation.
 * 
 * @author Andrew Tausz
 *
 */
public class UndirectedWeightedListGraph implements AbstractWeightedGraph {
	private final TIntObjectHashMap<TIntDoubleHashMap> adjacencySets = new TIntObjectHashMap<TIntDoubleHashMap>();
	private final int numVertices;
	
	/**
	 * Constructor which initializes the graph to consist of disconnected
	 * vertices. The number of vertices is initialized to numVertices.
	 * 
	 * @param numVertices the number of vertices to initialize the graph with
	 */
	public UndirectedWeightedListGraph(int numVertices) {
		this.numVertices = numVertices;
	}
	
	/* (non-Javadoc)
	 * @see edu.stanford.math.plex_plus.graph.AbstractWeightedGraph#addEdge(int, int, double)
	 */
	@Override
	public void addEdge(int i, int j, double weight) {
		ExceptionUtility.verifyIndex(this.numVertices, i);
		ExceptionUtility.verifyIndex(this.numVertices, j);
		int x = (i < j ? i : j);
		int y = (i < j ? j : i);
		if (!this.adjacencySets.containsKey(y)) {
			this.adjacencySets.put(y, new TIntDoubleHashMap());
		}
		this.adjacencySets.get(y).put(x, weight);
	}

	/* (non-Javadoc)
	 * @see edu.stanford.math.plex_plus.graph.AbstractWeightedGraph#getWeight(int, int)
	 */
	@Override
	public double getWeight(int i, int j) {
		ExceptionUtility.verifyIndex(this.numVertices, i);
		ExceptionUtility.verifyIndex(this.numVertices, j);
		int x = (i < j ? i : j);
		int y = (i < j ? j : i);
		if (!this.adjacencySets.containsKey(y)) {
			return 0;
		}
		return this.adjacencySets.get(y).get(x);
	}

	/* (non-Javadoc)
	 * @see edu.stanford.math.plex_plus.graph.AbstractGraph#addEdge(int, int)
	 */
	@Override
	public void addEdge(int i, int j) {
		this.addEdge(i, j, 1);
	}

	/* (non-Javadoc)
	 * @see edu.stanford.math.plex_plus.graph.AbstractGraph#containsEdge(int, int)
	 */
	@Override
	public boolean containsEdge(int i, int j) {
		return (this.getWeight(i, j) != 0);
	}

	/* (non-Javadoc)
	 * @see edu.stanford.math.plex_plus.graph.AbstractGraph#getNumEdges()
	 */
	@Override
	public int getNumEdges() {
		// TODO: complete
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see edu.stanford.math.plex_plus.graph.AbstractGraph#getNumVertices()
	 */
	@Override
	public int getNumVertices() {
		return this.numVertices;
	}

	/* (non-Javadoc)
	 * @see edu.stanford.math.plex_plus.graph.AbstractGraph#removeEdge(int, int)
	 */
	@Override
	public void removeEdge(int i, int j) {
		ExceptionUtility.verifyIndex(this.numVertices, i);
		ExceptionUtility.verifyIndex(this.numVertices, j);
		int x = (i < j ? i : j);
		int y = (i < j ? j : i);
		if (!this.adjacencySets.containsKey(y)) {
			return;
		}
		this.adjacencySets.get(y).remove(x);
	}
	
	/**
	 * This function returns the set of neighbors of vertex i in the graph
	 * which have indices less than i.
	 * 
	 * @param i the vertex to query
	 * @return the set of j such that j < i and i ~ j
	 */
	public TIntSet getLowerNeighbors(int i) {
		ExceptionUtility.verifyIndex(this.numVertices, i);
		if (this.adjacencySets.contains(i)) {
			return this.adjacencySets.get(i).keySet();
		} else {
			return new TIntHashSet();
		}
	}
}
