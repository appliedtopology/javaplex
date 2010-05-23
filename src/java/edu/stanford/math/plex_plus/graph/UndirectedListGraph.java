/**
 * 
 */
package edu.stanford.math.plex_plus.graph;

import edu.stanford.math.plex_plus.utility.ExceptionUtility;
import gnu.trove.map.hash.TIntObjectHashMap;
import gnu.trove.set.TIntSet;
import gnu.trove.set.hash.TIntHashSet;

/**
 * This class implements the functionality of an undirected graph.
 * It uses an adjacency list data structure. That is, for each 
 * vertex u, it stores the set of vertices {v_1, .., v_k} such that
 * u is connected to v_i and v_i < u.
 * 
 * For example consider the following graph:
 * <code>
 * 0 -- 1
 * |	|
 * 2 -- 3 -- 4
 * </code>
 * 
 * The following storage scheme is used:
 * 1: {0}
 * 2: {0}
 * 3: {1, 2}
 * 4: {3}
 * 
 * 
 * @author Andrew Tausz
 *
 */
public class UndirectedListGraph implements AbstractGraph {
	/**
	 * This is the adjacency set structure 
	 */
	private final TIntObjectHashMap<TIntHashSet> adjacencySets;
	
	/**
	 * Stores the number of vertices in the graph - must be prespecified
	 */
	private final int numVertices;
	
	/**
	 * Constructor which initializes the graph to consist of disconnected
	 * vertices. The number of vertices is initialized to numVertices.
	 * 
	 * @param numVertices the number of vertices to initialize the graph with
	 */
	public UndirectedListGraph(int numVertices) {
		ExceptionUtility.verifyNonNegative(numVertices);
		this.numVertices = numVertices;
		this.adjacencySets = new TIntObjectHashMap<TIntHashSet>();
	}
	
	/* (non-Javadoc)
	 * @see edu.stanford.math.plex_plus.graph.AbstractGraph#addEdge(int, int)
	 */
	@Override
	public void addEdge(int i, int j) {
		ExceptionUtility.verifyIndex(this.numVertices, i);
		ExceptionUtility.verifyIndex(this.numVertices, j);
		ExceptionUtility.verifyNonEqual(i, j);
		int x = (i < j ? i : j);
		int y = (i < j ? j : i);
		if (!this.adjacencySets.containsKey(y)) {
			this.adjacencySets.put(y, new TIntHashSet());
		}
		this.adjacencySets.get(y).add(x);
	}

	/* (non-Javadoc)
	 * @see edu.stanford.math.plex_plus.graph.AbstractGraph#containsEdge(int, int)
	 */
	@Override
	public boolean containsEdge(int i, int j) {
		ExceptionUtility.verifyIndex(this.numVertices, i);
		ExceptionUtility.verifyIndex(this.numVertices, j);
		int x = (i < j ? i : j);
		int y = (i < j ? j : i);
		if (!this.adjacencySets.containsKey(y)) {
			return false;
		}
		return this.adjacencySets.get(y).contains(x);
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
		ExceptionUtility.verifyNonEqual(i, j);
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
			return this.adjacencySets.get(i);
		} else {
			return new TIntHashSet();
		}
	}

}
