/**
 * 
 */
package edu.stanford.math.plex_plus.graph;

import edu.stanford.math.plex_plus.utility.BitPacking;
import gnu.trove.set.hash.TLongHashSet;

/**
 * @author Andrew Tausz
 *
 */
public class UndirectedListGraph implements AbstractGraph {
	private final TLongHashSet hashSet = new TLongHashSet();
	private final int numVertices;
	
	public UndirectedListGraph(int numVertices) {
		this.numVertices = numVertices;
	}
	
	/* (non-Javadoc)
	 * @see edu.stanford.math.plex_plus.graph.AbstractGraph#addEdge(int, int)
	 */
	@Override
	public void addEdge(int i, int j) {
		int x = (i < j ? i : j);
		int y = (i < j ? j : i);
		this.hashSet.add(BitPacking.pack(x, y));
	}

	/* (non-Javadoc)
	 * @see edu.stanford.math.plex_plus.graph.AbstractGraph#containsEdge(int, int)
	 */
	@Override
	public boolean containsEdge(int i, int j) {
		int x = (i < j ? i : j);
		int y = (i < j ? j : i);
		return this.hashSet.contains(BitPacking.pack(x, y));
	}

	/* (non-Javadoc)
	 * @see edu.stanford.math.plex_plus.graph.AbstractGraph#getNumEdges()
	 */
	@Override
	public int getNumEdges() {
		return this.hashSet.size();
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
		int x = (i < j ? i : j);
		int y = (i < j ? j : i);
		this.hashSet.remove(BitPacking.pack(x, y));
	}

}
