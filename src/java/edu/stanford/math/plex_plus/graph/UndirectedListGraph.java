/**
 * 
 */
package edu.stanford.math.plex_plus.graph;

import edu.stanford.math.plex_plus.utility.ExceptionUtility;
import gnu.trove.map.hash.TIntObjectHashMap;
import gnu.trove.set.hash.TIntHashSet;

/**
 * @author Andrew Tausz
 *
 */
public class UndirectedListGraph implements AbstractGraph {
	private final TIntObjectHashMap<TIntHashSet> matrix;
	private final int numVertices;
	
	public UndirectedListGraph(int numVertices) {
		ExceptionUtility.verifyNonNegative(numVertices);
		this.numVertices = numVertices;
		this.matrix = new TIntObjectHashMap<TIntHashSet>();
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
		if (!this.matrix.containsKey(y)) {
			this.matrix.put(y, new TIntHashSet());
		}
		this.matrix.get(y).add(x);
	}

	/* (non-Javadoc)
	 * @see edu.stanford.math.plex_plus.graph.AbstractGraph#containsEdge(int, int)
	 */
	@Override
	public boolean containsEdge(int i, int j) {
		int x = (i < j ? i : j);
		int y = (i < j ? j : i);
		if (!this.matrix.containsKey(y)) {
			return false;
		}
		return this.matrix.get(y).contains(x);
	}

	/* (non-Javadoc)
	 * @see edu.stanford.math.plex_plus.graph.AbstractGraph#getNumEdges()
	 */
	@Override
	public int getNumEdges() {
		return 0;
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
		if (!this.matrix.containsKey(y)) {
			return;
		}
		this.matrix.get(y).remove(x);
	}
	
	/**
	 * This function returns the set of neighbors of vertex i in G
	 * which have indices less than i.
	 * 
	 * @param i
	 * @return
	 */
	public TIntHashSet getLowerNeighbors(int i) {
		ExceptionUtility.verifyIndex(this.numVertices, i);
		if (this.matrix.contains(i)) {
			return this.matrix.get(i);
		} else {
			return new TIntHashSet();
		}
	}

}
