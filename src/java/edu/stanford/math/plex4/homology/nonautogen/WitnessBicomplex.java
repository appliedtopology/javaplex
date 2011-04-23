package edu.stanford.math.plex4.homology.nonautogen;

import java.util.Comparator;

import edu.stanford.math.plex4.homology.chain_basis.SimplexPair;
import edu.stanford.math.plex4.metric.interfaces.AbstractSearchableMetricSpace;
import edu.stanford.math.plex4.metric.landmark.LandmarkSelector;
import edu.stanford.math.plex4.streams.interfaces.PrimitiveStream;

public class WitnessBicomplex<T> extends PrimitiveStream<SimplexPair> {
	/**
	 * This is the metric space upon which the stream is built from.
	 */
	protected final AbstractSearchableMetricSpace<T> metricSpace;

	/**
	 * This is the selection of landmark points
	 */
	protected final LandmarkSelector<T> L;
	protected final LandmarkSelector<T> M;
	
	
	protected WitnessBicomplex(AbstractSearchableMetricSpace<T> metricSpace, LandmarkSelector<T> L, LandmarkSelector<T> M, Comparator<SimplexPair> comparator) {
		super(comparator);
		this.metricSpace = metricSpace;
		this.L = L;
		this.M = M;
	}

	@Override
	protected void constructComplex() {
		
	}
}
