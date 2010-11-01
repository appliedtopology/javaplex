package edu.stanford.math.plex4.embedding;

import edu.stanford.math.primitivelib.metric.interfaces.AbstractIntMetricSpace;


public interface MetricSpaceEmbedding {
	public double[][] computedEmbedding(AbstractIntMetricSpace metricSpace, int dimension);
}
