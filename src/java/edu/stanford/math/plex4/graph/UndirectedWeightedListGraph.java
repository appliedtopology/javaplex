/**
 * 
 */
package edu.stanford.math.plex4.graph;

import edu.stanford.math.plex4.utility.ExceptionUtility;
import edu.stanford.math.primitivelib.autogen.pair.IntIntPair;
import gnu.trove.TIntDoubleHashMap;
import gnu.trove.TIntDoubleIterator;
import gnu.trove.TIntHashSet;
import gnu.trove.TIntIntHashMap;
import gnu.trove.TIntObjectHashMap;
import gnu.trove.TIntObjectIterator;

import java.util.Iterator;

/**
 * This class implements the functionality of an undirected weighted graph.
 * It uses an adjacency list representation. See the class
 * UndirectedListGraph for more information about the internal representation.
 * 
 * @author Andrew Tausz
 *
 */
public class UndirectedWeightedListGraph implements AbstractWeightedUndirectedGraph {
	private static final long serialVersionUID = -4885036152039543245L;
	private final TIntObjectHashMap<TIntDoubleHashMap> adjacencySets = new TIntObjectHashMap<TIntDoubleHashMap>();
	private final int numVertices;
	private final TIntIntHashMap degrees = new TIntIntHashMap();
	
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
	public void addEdge(int i, int j, double weight) {
		ExceptionUtility.verifyIndex(this.numVertices, i);
		ExceptionUtility.verifyIndex(this.numVertices, j);
		int x = (i < j ? i : j);
		int y = (i < j ? j : i);
		if (!this.adjacencySets.containsKey(y)) {
			this.adjacencySets.put(y, new TIntDoubleHashMap());
		}
		this.adjacencySets.get(y).put(x, weight);
		
		if (!this.degrees.containsKey(x)) {
			this.degrees.put(x, 0);
		}
		if (!this.degrees.containsKey(y)) {
			this.degrees.put(y, 0);
		}
		this.degrees.put(x, this.degrees.get(x) + 1);
		this.degrees.put(y, this.degrees.get(y) + 1);
	}

	/* (non-Javadoc)
	 * @see edu.stanford.math.plex_plus.graph.AbstractWeightedGraph#getWeight(int, int)
	 */
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
	public void addEdge(int i, int j) {
		this.addEdge(i, j, 1);
	}

	/* (non-Javadoc)
	 * @see edu.stanford.math.plex_plus.graph.AbstractGraph#containsEdge(int, int)
	 */
	public boolean containsEdge(int i, int j) {
		return (this.getWeight(i, j) != 0);
	}

	/* (non-Javadoc)
	 * @see edu.stanford.math.plex_plus.graph.AbstractGraph#getNumEdges()
	 */
	public int getNumEdges() {
		// TODO: complete
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see edu.stanford.math.plex_plus.graph.AbstractGraph#getNumVertices()
	 */
	public int getNumVertices() {
		return this.numVertices;
	}

	/* (non-Javadoc)
	 * @see edu.stanford.math.plex_plus.graph.AbstractGraph#removeEdge(int, int)
	 */
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
	public TIntHashSet getLowerNeighbors(int i) {
		ExceptionUtility.verifyIndex(this.numVertices, i);
		if (this.adjacencySets.contains(i)) {
			int [] array = this.adjacencySets.get(i).keys();
			return new TIntHashSet(array);
		} else {
			return new TIntHashSet();
		}
	}

	public TIntObjectHashMap<TIntHashSet> getUnweightedAdjacencySets() {
		TIntObjectHashMap<TIntHashSet> result = new TIntObjectHashMap<TIntHashSet>();
		
		TIntObjectIterator<TIntDoubleHashMap> iterator = adjacencySets.iterator();
		
		while(iterator.hasNext()) {
			iterator.advance();
			
			int i = iterator.key();
			TIntDoubleHashMap value = iterator.value();
			
			TIntDoubleIterator iterator2 = value.iterator();
			
			TIntHashSet neighbors = new TIntHashSet();
			
			while(iterator2.hasNext()) {
				iterator2.advance();
				
				int j = iterator2.key();
				neighbors.add(j);
			}
			
			result.put(i, neighbors);
		}
		
		return result;
	}
	
	public UndirectedListGraph toUnweightedGraph() {
		return new UndirectedListGraph(this.numVertices, this.getUnweightedAdjacencySets());
	}
	
	public Iterator<IntIntPair> iterator() {
		return new UndirectedListEdgeIterator(this.getUnweightedAdjacencySets());
	}

	public int getDegree(int v) {
		return this.degrees.get(v);
	}
	
	public int[] getDegreeSequence() {
		int[] sequence = new int[this.numVertices];
		
		for (int i = 0; i < this.numVertices; i++) {
			sequence[i] = this.degrees.get(i);
		}
		
		return sequence;
	}

	public int[] getNeighbors(int v) {
		TIntHashSet set = new TIntHashSet();
		set.addAll(this.getLowerNeighbors(v).toArray());
		
		for (int i = v + 1; i < this.numVertices; i++) {
			if (this.adjacencySets.containsKey(i)) {
				if (this.adjacencySets.get(i).containsKey(v)) {
					set.add(i);
				}
			}
		}
		
		return set.toArray();
	}
}
