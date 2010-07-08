/**
 * 
 */
package edu.stanford.math.plex_plus.graph_metric;

import edu.stanford.math.plex_plus.graph.AbstractUndirectedGraph;
import edu.stanford.math.plex_plus.graph.utility.GraphUtility;
import edu.stanford.math.plex_plus.math.metric.impl.ExplicitMetricSpace;
import edu.stanford.math.plex_plus.math.metric.interfaces.IntFiniteMetricSpace;

/**
 * @author Andris
 *
 */
public class ShortestPathMetric implements GraphMetric {
	private static final ShortestPathMetric instance = new ShortestPathMetric();
	private ShortestPathMetric(){}
	
	public static ShortestPathMetric getInstance() {
		return instance;
	}
	
	public IntFiniteMetricSpace getMetricSpace(AbstractUndirectedGraph graph) {
		return new ExplicitMetricSpace(GraphUtility.computeShortestPaths(graph));
	}

}
