package edu.stanford.math.plex_plus.embedding;

import edu.stanford.math.plex_plus.graph.AbstractUndirectedGraph;

public interface GraphEmbedding {
	public double[][] computeEmbedding(AbstractUndirectedGraph graph, int dimension);
}
