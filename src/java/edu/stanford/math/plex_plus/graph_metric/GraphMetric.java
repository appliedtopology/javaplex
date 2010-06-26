package edu.stanford.math.plex_plus.graph_metric;

import edu.stanford.math.plex_plus.graph.AbstractUndirectedGraph;
import edu.stanford.math.plex_plus.math.metric.interfaces.IntFiniteMetricSpace;


public interface GraphMetric {
	public IntFiniteMetricSpace getMetricSpace(AbstractUndirectedGraph graph);
}
