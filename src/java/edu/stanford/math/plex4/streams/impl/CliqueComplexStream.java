package edu.stanford.math.plex4.streams.impl;

import edu.stanford.math.plex4.graph.AbstractUndirectedGraph;
import edu.stanford.math.plex4.graph.UndirectedWeightedListGraph;
import edu.stanford.math.plex4.homology.filtration.StaticConverter;
import edu.stanford.math.primitivelib.autogen.pair.IntIntPair;

/**
 * This class implements a clique complex. A clique complex is a simplicial
 * complex created from an undirected graph as follows:
 * <ul>
 * <li>The 0-skeleton of the complex is the set of vertices in the graph.</li>
 * <li>The 1-skeleton of the complex is the set of edges in the graph.</li>
 * <li>A simplex [v_0, ... v_n] is in the complex iff the vertices v_0, ... v_n form a clique in the given graph.</li>
 * </ul>
 * 
 * @author Andrew Tausz
 *
 */
public class CliqueComplexStream extends FlagComplexStream {
	/**
	 * This is the underlying graph from which the complex is constructed.
	 */
	private final AbstractUndirectedGraph graph;
	
	/**
	 * This constructor initializes the complex with a given graph.
	 * 
	 * @param graph the graph to compute the clique complex from
	 * @param maxAllowableDimension the maximum dimension of the ocmplex
	 */
	public CliqueComplexStream(AbstractUndirectedGraph graph, int maxAllowableDimension) {
		super(maxAllowableDimension, StaticConverter.getInstance());
		this.graph = graph;
	}

	/* (non-Javadoc)
	 * @see edu.stanford.math.plex4.homology.streams.impl.FlagComplexStream#constructEdges()
	 */
	@Override
	protected UndirectedWeightedListGraph constructEdges() {
		UndirectedWeightedListGraph edgeGraph = new UndirectedWeightedListGraph(this.graph.getNumVertices());
		
		for (IntIntPair pair: this.graph) {
			edgeGraph.addEdge(pair.getFirst(), pair.getSecond(), 0);
		}
		
		return edgeGraph;
	}

}
