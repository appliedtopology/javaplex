package edu.stanford.math.plex_plus.embedding;

import edu.stanford.math.plex_plus.math.metric.interfaces.IntFiniteMetricSpace;

public interface MetricSpaceEmbedding {
	public double[][] computedEmbedding(IntFiniteMetricSpace metricSpace, int dimension);
}
