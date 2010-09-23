/**
 * 
 */
package edu.stanford.math.plex4.graph;

import java.util.Iterator;

import edu.stanford.math.plex4.datastructures.pairs.IntIntPair;
import gnu.trove.TIntHashSet;
import gnu.trove.TIntIterator;
import gnu.trove.TIntObjectHashMap;
import gnu.trove.TIntObjectIterator;

/**
 * @author Andrew Tausz
 *
 */
public class UndirectedListEdgeIterator implements Iterator<IntIntPair> {
	private final TIntObjectHashMap<TIntHashSet> adjacencySets;
	
	/**
	 * This iterates through the different adjacency lists.
	 */
	private TIntObjectIterator<TIntHashSet> listIterator;
	
	/**
	 * This iterates through a particular adjacency list. Ie. given
	 * a vertex, this iterates through the lower-neighbors of the vertex.
	 */
	private TIntIterator elementIterator;
	
	public UndirectedListEdgeIterator(TIntObjectHashMap<TIntHashSet> adjacencySets) {
		this.adjacencySets = adjacencySets;
		this.listIterator = this.adjacencySets.iterator();
		this.listIterator.advance();
		this.elementIterator = this.listIterator.value().iterator();
	}
	
	/* (non-Javadoc)
	 * @see java.util.Iterator#hasNext()
	 */
	public boolean hasNext() {
		if (this.elementIterator.hasNext()) {
			return true;
		}
		return (this.listIterator.hasNext());
	}

	/* (non-Javadoc)
	 * @see java.util.Iterator#next()
	 */
	public IntIntPair next() {
		int j = this.listIterator.key();
		int i = this.elementIterator.next();
		return new IntIntPair(i, j);
	}

	/* (non-Javadoc)
	 * @see java.util.Iterator#remove()
	 */
	public void remove() {
		this.elementIterator.remove();
	}

}
