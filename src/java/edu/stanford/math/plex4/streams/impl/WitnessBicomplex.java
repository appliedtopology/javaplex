package edu.stanford.math.plex4.streams.impl;

import edu.stanford.math.plex4.homology.chain_basis.Simplex;
import edu.stanford.math.plex4.homology.chain_basis.SimplexPair;
import edu.stanford.math.plex4.homology.chain_basis.SimplexPairComparator;
import edu.stanford.math.plex4.streams.interfaces.PrimitiveStream;

public class WitnessBicomplex<T> extends PrimitiveStream<SimplexPair> {
	protected final WitnessStream<T> L1;
	protected final WitnessStream<T> L2;
	
	public WitnessBicomplex(WitnessStream<T> L1, WitnessStream<T> L2) {
		super(SimplexPairComparator.getInstance());
		this.L1 = L1;
		this.L2 = L2;
	}
	
	@Override
	protected void constructComplex() {
		boolean add = false;
		
		for (Simplex sigma: L1) {
			int sigma_witness = L1.getWitnessIndex(sigma);
			int sigma_index = L1.getFiltrationIndex(sigma);
			for (Simplex tau: L2) {
				add = false;
				
				int tau_witness = L2.getWitnessIndex(tau);
				int tau_index = L2.getFiltrationIndex(tau);
				
				if (sigma.getDimension() == 0 && tau.getDimension() == 0) {
					add = true;
				} else if (sigma_witness == tau_witness) {
					add = true;
				}
				
				if (add) {
					this.storageStructure.addElement(SimplexPair.createPair(sigma, tau), Math.max(sigma_index, tau_index));
				}
			}
		}
	}
}
