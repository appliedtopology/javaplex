package edu.stanford.math.plex4.embedding;

import edu.stanford.math.plex4.graph.AbstractUndirectedGraph;

public interface GraphEmbedding {
	public double[][] computeEmbedding(AbstractUndirectedGraph graph, int dimension);
}
