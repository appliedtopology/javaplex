/**
 * 
 */
package edu.stanford.math.plex4.graph.metric;

import edu.stanford.math.plex4.graph.AbstractUndirectedGraph;
import edu.stanford.math.plex4.graph.utility.GraphUtility;
import edu.stanford.math.plex4.metric.impl.ExplicitMetricSpace;
import edu.stanford.math.plex4.metric.interfaces.AbstractIntMetricSpace;

/**
 * This class produces a metric space that is defined by the shortest-path distance
 * between vertices in a graph. It implements the GraphMetricFactory interface, meaning
 * that it produces a metric space, but is not a metric space.
 * 
 * @author Andrew Tausz
 *
 */
public class ShortestPathMetric implements GraphMetricFactory {
	private static final ShortestPathMetric instance = new ShortestPathMetric();
	private ShortestPathMetric(){}
	
	public static ShortestPathMetric getInstance() {
		return instance;
	}
	
	public AbstractIntMetricSpace createMetricSpace(AbstractUndirectedGraph graph) {
		return new ExplicitMetricSpace(GraphUtility.computeShortestPaths(graph));
	}

}
