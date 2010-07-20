/**
 * 
 */
package edu.stanford.math.plex4.embedding;

import edu.stanford.math.plex4.graph.AbstractUndirectedGraph;
import edu.stanford.math.plex4.graph_metric.GraphMetric;
import edu.stanford.math.plex4.utility.ExceptionUtility;

/**
 * @author Andris
 *
 */
public class GraphMetricEmbedding implements GraphEmbedding {
	private final GraphMetric graphMetric;
	private final MetricSpaceEmbedding metricSpaceEmbedding;
	
	public GraphMetricEmbedding(GraphMetric graphMetric, MetricSpaceEmbedding metricSpaceEmbedding) {
		ExceptionUtility.verifyNonNull(graphMetric);
		ExceptionUtility.verifyNonNull(metricSpaceEmbedding);
		this.graphMetric = graphMetric;
		this.metricSpaceEmbedding = metricSpaceEmbedding;
	}
	/* (non-Javadoc)
	 * @see edu.stanford.math.plex_plus.embedding.GraphEmbedding#computeEmbedding(edu.stanford.math.plex_plus.graph.AbstractUndirectedGraph, int)
	 */
	public double[][] computeEmbedding(AbstractUndirectedGraph graph, int dimension) {
		return this.metricSpaceEmbedding.computedEmbedding(graphMetric.getMetricSpace(graph), dimension);
	}

}
