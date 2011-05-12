package edu.stanford.math.plex4.streams.impl;

import edu.stanford.math.plex4.homology.chain_basis.Simplex;
import edu.stanford.math.plex4.metric.interfaces.AbstractSearchableMetricSpace;
import edu.stanford.math.plex4.metric.landmark.LandmarkSelector;
import edu.stanford.math.primitivelib.autogen.pair.IntDoublePair;

public class WitnessStream<T> extends AbstractWitnessStream<T> {

	public WitnessStream(AbstractSearchableMetricSpace<T> metricSpace, LandmarkSelector<T> landmarkSelector, int maxDimension, double maxDistance, int nu, int numDivisions) {
		super(metricSpace, landmarkSelector, maxDimension, maxDistance, nu, numDivisions);
	}

	public WitnessStream(AbstractSearchableMetricSpace<T> metricSpace, LandmarkSelector<T> landmarkSelector, int maxDimension, double maxDistance, int numDivisions) {
		super(metricSpace, landmarkSelector, maxDimension, maxDistance, numDivisions);
	}

	public WitnessStream(AbstractSearchableMetricSpace<T> metricSpace, LandmarkSelector<T> landmarkSelector, int maxDimension, double maxDistance, int[] indices) {
		super(metricSpace, landmarkSelector, maxDimension, maxDistance, indices);
	}

	@Override
	protected boolean isMember(Simplex simplex) {
		boolean isMember = false;
		
		int[] vertices = simplex.getVertices();
		
		IntDoublePair witnessAndDistance = this.getWitnessAndDistance(vertices);
		int n_star = witnessAndDistance.getFirst();
		double e_ij = witnessAndDistance.getSecond();
		
		if (e_ij <= this.maxDistance  + this.epsilon) {
			isMember = true;
			this.witnesses.put(convertIndices(simplex, indices), n_star);
		}
		
		return isMember;
	}
}
