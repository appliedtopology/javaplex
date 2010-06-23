/**
 * 
 */
package edu.stanford.math.plex_plus.graph;

import java.util.Iterator;

import edu.stanford.math.plex_plus.datastructures.pairs.IntIntPair;

/**
 * This interface defines the functionality for an edge iterator on
 * a graph. An implementation must iterate through all edges in the
 * underlying graph exactly once.
 * 
 * @author Andrew Tausz
 */
public interface AbstractEdgeIterator extends Iterator<IntIntPair> {

}
