package edu.stanford.math.plex4.streams.impl;

import java.util.List;

import edu.stanford.math.plex4.homology.chain_basis.Simplex;
import edu.stanford.math.plex4.homology.chain_basis.SimplexPair;
import edu.stanford.math.plex4.homology.chain_basis.SimplexPairComparator;

public class WitnessBicomplex<T> extends ExplicitStream<SimplexPair> {
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
		// L1 and L2 should have the same vertex set
		
		if (L1.N != L2.N){
			return;
		}
		
		SimplexPair pair = null;
		
		for (int n = 0; n < L1.N; n++) {
			List<Simplex> L1Simplices = L1.getAssociatedSimplices(n);
			List<Simplex> L2Simplices = L2.getAssociatedSimplices(n);
			
			if (L1Simplices == null || L2Simplices == null || L1Simplices.isEmpty() || L2Simplices.isEmpty()) {
				continue;
			}
			
			for (Simplex sigma: L1Simplices) {
				int sigma_index = L1.getFiltrationIndex(sigma);
				for (Simplex tau: L2Simplices) {
					int tau_index = L2.getFiltrationIndex(tau);
					
					pair = SimplexPair.createPair(sigma, tau);
					if (pair.getDimension() <= maxDimension && (!this.storageStructure.containsElement(pair))) {
						this.storageStructure.addElement(pair, Math.max(sigma_index, tau_index));
					}
				}
			}
		}
	}
}
