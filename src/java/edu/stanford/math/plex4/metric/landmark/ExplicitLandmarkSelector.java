package edu.stanford.math.plex4.metric.landmark;

import edu.stanford.math.plex4.metric.interfaces.AbstractSearchableMetricSpace;

public class ExplicitLandmarkSelector<T> extends LandmarkSelector<T> {
	public ExplicitLandmarkSelector(AbstractSearchableMetricSpace<T> metricSpace, int[] indices) {
		super(metricSpace, indices);
	}

	@Override
	protected int[] computeLandmarkSet() {
		return null;
	}
}
