package edu.stanford.math.plex4.graph.metric;

import edu.stanford.math.plex4.graph.AbstractUndirectedGraph;
import edu.stanford.math.plex4.metric.interfaces.AbstractIntMetricSpace;

/**
 * This interface defines the functionality of a factory which creates a metric space on a 
 * given graph.
 * 
 * @author Andrew Tausz
 *
 */
public interface GraphMetricFactory {
	
	/**
	 * This function produces a finite metric space on the vertex set of a given graph.
	 * 
	 * @param graph the graph whose vertex set forms the point set of the metric space
	 * @return a metric space created from the graph
	 */
	public AbstractIntMetricSpace createMetricSpace(AbstractUndirectedGraph graph);
}
