package edu.stanford.math.plex4.homology.streams.impl;

import edu.stanford.math.plex4.datastructures.pairs.IntIntPair;
import edu.stanford.math.plex4.graph.AbstractUndirectedGraph;
import edu.stanford.math.plex4.graph.UndirectedWeightedListGraph;

public class CliqueComplexStream extends FlagComplexStream {
	private final AbstractUndirectedGraph graph;
	
	public CliqueComplexStream(AbstractUndirectedGraph graph, int maxAllowableDimension, double maxDistance, int numDivisions) {
		super(maxAllowableDimension, maxDistance, numDivisions);
		this.graph = graph;
	}

	@Override
	protected UndirectedWeightedListGraph constructEdges() {
		UndirectedWeightedListGraph edgeGraph = new UndirectedWeightedListGraph(this.graph.getNumVertices());
		
		for (IntIntPair pair: this.graph) {
			edgeGraph.addEdge(pair.getFirst(), pair.getSecond(), 0);
		}
		
		return edgeGraph;
	}

}
