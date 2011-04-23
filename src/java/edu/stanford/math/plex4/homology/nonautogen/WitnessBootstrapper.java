package edu.stanford.math.plex4.homology.nonautogen;

import edu.stanford.math.plex4.metric.interfaces.AbstractSearchableMetricSpace;

public class WitnessBootstrapper<T> {
	/**
	 * This is the metric space upon which the stream is built from.
	 */
	protected final AbstractSearchableMetricSpace<T> metricSpace;
	
	public WitnessBootstrapper(AbstractSearchableMetricSpace<T> metricSpace) {
		this.metricSpace = metricSpace;
	}

	
}
