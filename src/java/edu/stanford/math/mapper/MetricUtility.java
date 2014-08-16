package edu.stanford.math.mapper;

import edu.stanford.math.plex4.metric.interfaces.AbstractIntMetricSpace;
import gnu.trove.TIntHashSet;

public class MetricUtility {
	public static AbstractIntMetricSpace createSubMetricSpace(final AbstractIntMetricSpace metricSpace, final int[] indices) {

		return new AbstractIntMetricSpace() {

			public double distance(int i, int j) {
				return metricSpace.distance(indices[i], indices[j]);
			}

			public int size() {
				return indices.length;
			}
		};
	}

	public static AbstractIntMetricSpace createSubMetricSpace(final AbstractIntMetricSpace metricSpace, final TIntHashSet indices) {
		int[] indicesArray = indices.toArray();

		return MetricUtility.createSubMetricSpace(metricSpace, indicesArray);
	}
}
