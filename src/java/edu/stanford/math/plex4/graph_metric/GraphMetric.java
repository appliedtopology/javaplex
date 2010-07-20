package edu.stanford.math.plex4.graph_metric;

import edu.stanford.math.plex4.graph.AbstractUndirectedGraph;
import edu.stanford.math.plex4.math.metric.interfaces.IntFiniteMetricSpace;


public interface GraphMetric {
	public IntFiniteMetricSpace getMetricSpace(AbstractUndirectedGraph graph);
}
