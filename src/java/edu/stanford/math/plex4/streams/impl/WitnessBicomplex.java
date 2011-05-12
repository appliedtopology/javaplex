package edu.stanford.math.plex4.streams.impl;

import edu.stanford.math.plex4.homology.chain_basis.Simplex;
import edu.stanford.math.plex4.homology.chain_basis.SimplexPair;
import edu.stanford.math.plex4.homology.chain_basis.SimplexPairComparator;
import gnu.trove.TIntHashSet;
import gnu.trove.TIntIterator;

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
		boolean add = false;
		SimplexPair pair = null;
		for (Simplex sigma: L1) {
			int sigma_witness = L1.getWitnessIndex(sigma);
			int sigma_index = L1.getFiltrationIndex(sigma);
			TIntHashSet sigma_witnesses = L1.getAllWitnesses(sigma.getVertices());
			for (Simplex tau: L2) {
				add = false;

				int tau_witness = L2.getWitnessIndex(tau);
				int tau_index = L2.getFiltrationIndex(tau);

				TIntHashSet tau_witnesses = L2.getAllWitnesses(tau.getVertices());
				
				TIntHashSet intersection = this.intersect(sigma_witnesses, tau_witnesses);
				
				if (sigma_witness >= 0 && tau_witness >= 0 && (!intersection.isEmpty())) {
					add = true;
				}

				if (add) {
					pair = SimplexPair.createPair(sigma, tau);
					if (pair.getDimension() <= maxDimension)
						this.storageStructure.addElement(pair, Math.max(sigma_index, tau_index));
				}
			}
		}
	}
	
	private TIntHashSet intersect(TIntHashSet a, TIntHashSet b) {
		TIntHashSet result = new TIntHashSet();
		
		for (TIntIterator iterator = a.iterator(); iterator.hasNext(); ) {
			int x = iterator.next();
			
			if (b.contains(x)) {
				result.add(x);
			}
		}
		
		return result;
	}
}
