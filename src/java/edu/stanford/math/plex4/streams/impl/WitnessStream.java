package edu.stanford.math.plex4.streams.impl;

import edu.stanford.math.plex4.homology.chain_basis.Simplex;
import edu.stanford.math.plex4.metric.interfaces.AbstractSearchableMetricSpace;
import edu.stanford.math.plex4.metric.landmark.LandmarkSelector;
import edu.stanford.math.primitivelib.autogen.pair.BooleanDoublePair;
import edu.stanford.math.primitivelib.autogen.pair.IntDoublePair;

public class WitnessStream<T> extends AbstractWitnessStream<T> {

	public WitnessStream(AbstractSearchableMetricSpace<T> metricSpace, LandmarkSelector<T> landmarkSelector, int maxDimension, double maxDistance, int numDivisions) {
		super(metricSpace, landmarkSelector, maxDimension, maxDistance, -1, numDivisions);
	}

	public WitnessStream(AbstractSearchableMetricSpace<T> metricSpace, LandmarkSelector<T> landmarkSelector, int maxDimension, double maxDistance, int[] indices) {
		super(metricSpace, landmarkSelector, maxDimension, maxDistance, -1, indices);
	}

	@Override
	protected BooleanDoublePair isMember(Simplex simplex) {
		boolean isMember = false;

		int[] vertices = simplex.getVertices();

		IntDoublePair witnessAndDistance = this.getWitnessAndDistance(vertices);
		int n_star = witnessAndDistance.getFirst();
		double e_ij = witnessAndDistance.getSecond();

		if (e_ij <= this.maxDistance  + this.epsilon) {
			isMember = true;
			//if (!this.isWitness(n_star, simplex)) {
				this.updateWitnessInformationInternalIndices(n_star, e_ij, simplex.getVertices());
			//}
		}

		return new BooleanDoublePair(isMember, e_ij);
	}
}
