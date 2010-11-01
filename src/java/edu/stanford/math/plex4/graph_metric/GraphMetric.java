package edu.stanford.math.plex4.graph_metric;

import edu.stanford.math.plex4.graph.AbstractUndirectedGraph;
import edu.stanford.math.primitivelib.metric.interfaces.AbstractIntMetricSpace;


public interface GraphMetric {
	public AbstractIntMetricSpace getMetricSpace(AbstractUndirectedGraph graph);
}
