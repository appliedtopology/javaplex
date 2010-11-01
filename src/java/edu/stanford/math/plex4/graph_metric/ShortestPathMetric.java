/**
 * 
 */
package edu.stanford.math.plex4.graph_metric;

import edu.stanford.math.plex4.graph.AbstractUndirectedGraph;
import edu.stanford.math.plex4.graph.utility.GraphUtility;
import edu.stanford.math.primitivelib.metric.impl.ExplicitIntMetricSpace;
import edu.stanford.math.primitivelib.metric.interfaces.AbstractIntMetricSpace;

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
	
	public AbstractIntMetricSpace getMetricSpace(AbstractUndirectedGraph graph) {
		return new ExplicitIntMetricSpace(GraphUtility.computeShortestPaths(graph));
	}

}
