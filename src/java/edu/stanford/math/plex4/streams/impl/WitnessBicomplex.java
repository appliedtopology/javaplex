package edu.stanford.math.plex4.streams.impl;

import edu.stanford.math.plex4.homology.chain_basis.Simplex;
import edu.stanford.math.plex4.homology.chain_basis.SimplexPair;
import edu.stanford.math.plex4.homology.chain_basis.SimplexPairComparator;
import edu.stanford.math.plex4.streams.interfaces.PrimitiveStream;

public class WitnessBicomplex<T> extends PrimitiveStream<SimplexPair> {
	protected final WitnessStream<T> L1;
	protected final WitnessStream<T> L2;
	protected final int maxDimension;

	public WitnessBicomplex(WitnessStream<T> L1, WitnessStream<T> L2, int maxDimension) {
		super(SimplexPairComparator.getInstance());
		this.L1 = L1;
		this.L2 = L2;
		this.maxDimension = maxDimension;
	}

	@Override
	protected void constructComplex() {
		boolean add = false;
		SimplexPair pair = null;
		for (Simplex sigma: L1) {
			int sigma_witness = L1.getWitnessIndex(sigma);
			int sigma_index = L1.getFiltrationIndex(sigma);
			for (Simplex tau: L2) {
				add = false;

				int tau_witness = L2.getWitnessIndex(tau);
				int tau_index = L2.getFiltrationIndex(tau);

				if (sigma.getDimension() == 0 && tau.getDimension() == 0) {
					add = true;
				} else if (sigma_witness >= 0 && tau_witness >= 0 && sigma_witness == tau_witness) {
					add = true;
				}

				if (add) {
					pair = SimplexPair.createPair(sigma, tau);
					//if (pair.getDimension() <= maxDimension)
					this.storageStructure.addElement(pair, Math.max(sigma_index, tau_index));
				}
			}
		}
	}
}
