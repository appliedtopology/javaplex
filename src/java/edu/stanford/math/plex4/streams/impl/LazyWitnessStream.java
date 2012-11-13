package edu.stanford.math.plex4.streams.impl;

import edu.stanford.math.plex4.homology.chain_basis.Simplex;
import edu.stanford.math.plex4.metric.interfaces.AbstractSearchableMetricSpace;
import edu.stanford.math.plex4.metric.landmark.LandmarkSelector;
import edu.stanford.math.plex4.utility.ExceptionUtility;

/**
 * This class implements the lazy witness complex described in the paper
 * "Topological estimation using witness complexes", by Vin de Silva and
 * Gunnar Carlsson. The details of the construction are described in this
 * paper. Note that a lazy witness complex is fully described by its
 * 1-skeleton, therefore we simply derive from the FlagComplexStream class.
 *
 * @author Andrew Tausz
 *
 * @param <T> the type of the underlying metric space
 */
public class LazyWitnessStream<T> extends AbstractWitnessStream<T> {
	public LazyWitnessStream(AbstractSearchableMetricSpace<T> metricSpace, LandmarkSelector<T> landmarkSelector, int maxDimension, double maxDistance, int nu, int numDivisions) {
		super(metricSpace, landmarkSelector, maxDimension, maxDistance, nu, numDivisions);
		ExceptionUtility.verifyNonNegative(nu);
	}

	public LazyWitnessStream(AbstractSearchableMetricSpace<T> metricSpace, LandmarkSelector<T> landmarkSelector, int maxDimension, double maxDistance, int numDivisions) {
		super(metricSpace, landmarkSelector, maxDimension, maxDistance, numDivisions);
	}

	public LazyWitnessStream(AbstractSearchableMetricSpace<T> metricSpace, LandmarkSelector<T> landmarkSelector, int maxDimension, double maxDistance, int[] indices) {
		super(metricSpace, landmarkSelector, maxDimension, maxDistance, indices);
	}

	@Override
	protected boolean isMember(Simplex simplex) {
		return true;
	}
}
