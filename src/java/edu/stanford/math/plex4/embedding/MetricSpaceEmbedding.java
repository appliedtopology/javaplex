package edu.stanford.math.plex4.embedding;

import edu.stanford.math.plex4.math.metric.interfaces.IntFiniteMetricSpace;

public interface MetricSpaceEmbedding {
	public double[][] computedEmbedding(IntFiniteMetricSpace metricSpace, int dimension);
}
