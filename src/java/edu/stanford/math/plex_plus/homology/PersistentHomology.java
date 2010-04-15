package edu.stanford.math.plex_plus.homology;

import edu.stanford.math.plex_plus.datastructures.IntFormalSum;
import edu.stanford.math.plex_plus.math.structures.impl.IntFreeModule;
import edu.stanford.math.plex_plus.math.structures.interfaces.IntField;
import edu.stanford.math.plex_plus.utility.ExceptionUtility;
import gnu.trove.iterator.TObjectIntIterator;
import gnu.trove.map.hash.THashMap;
import gnu.trove.set.hash.THashSet;

public class PersistentHomology {
	private final THashSet<Simplex> markedSimplices = new THashSet<Simplex>();
	private final THashMap<Simplex, IntFormalSum<Simplex>> T = new THashMap<Simplex, IntFormalSum<Simplex>>();
	private final IntField field;
	
	public PersistentHomology(IntField field) {
		this.field = field;
	}
	
	public BarcodeCollection computeIntervals(SimplexStream stream) {
		BarcodeCollection barcodeCollection = new BarcodeCollection();
	
		for (Simplex simplex : stream) {
			/*
			 * Translation from paper:
			 * 
			 * sigma_j = simplex
			 * sigma_i = d.maxObject();
			 * 
			 */

			this.T.remove(simplex);
			
			IntFormalSum<Simplex> d = this.removePivotRows(simplex);
			
			if (d.isEmpty()) {
				this.markedSimplices.add(simplex);
			} else {
				Simplex sigma_j = simplex;
				Simplex sigma_i = getMaximumObject(d);
				int k = sigma_i.getDimension();
				
				// store j and d in T[i]
				this.T.put(simplex, d);
				
				// store interval
				barcodeCollection.addInterval(k, sigma_i.getFiltrationIndex(), sigma_j.getFiltrationIndex());
			}
		}
		
		for (Simplex simplex : this.markedSimplices) {
			if (!this.T.containsKey(simplex)) {
				int k = simplex.getDimension();
				barcodeCollection.addInterval(k, simplex.getFiltrationIndex());
			}
		}
	
		return barcodeCollection;
	}
	
	private IntFormalSum<Simplex> removePivotRows(Simplex simplex) {
		ExceptionUtility.verifyNonNull(simplex);
		IntFormalSum<Simplex> d = createBoundaryChain(simplex.getBoundaryArray());
		IntFreeModule<Simplex> chainModule = new IntFreeModule<Simplex>(this.field);
		
		// remove unmarked terms from d
		for (TObjectIntIterator<Simplex> iterator = d.iterator(); iterator.hasNext(); ) {
			iterator.advance();
			if (!this.markedSimplices.contains(iterator.key())) {
				iterator.remove();
			}
		}
		
		while (!d.isEmpty()) {
			Simplex sigma_i = getMaximumObject(d);
			
			if (!this.T.containsKey(sigma_i)) {
				break;
			}
			
			int q = T.get(sigma_i).getCoefficient(sigma_i);
			
			d = chainModule.subtract(d, chainModule.multiply(field.invert(q), T.get(sigma_i)));
		}
		
		return d;
	}
	
	/**
	 * This static method creates a formal alternating sum
	 * @param boundaryObjects
	 * @return
	 */
	private static <M extends Comparable<M>> IntFormalSum<M> createBoundaryChain(M[] boundaryObjects) {
		ExceptionUtility.verifyNonNull(boundaryObjects);
		IntFormalSum<M> sum = new IntFormalSum<M>();
		
		for (int i = 0; i < boundaryObjects.length; i++) {
			sum.put((i % 2 == 0 ? 1 : -1), boundaryObjects[i]);
		}
		
		return sum;
	}
	
	private static <M extends Comparable<M>> M getMaximumObject(IntFormalSum<M> chain) {
		M maxObject = null;
		
		for (TObjectIntIterator<M> iterator = chain.iterator(); iterator.hasNext(); ) {
			iterator.advance();
			if (maxObject == null || iterator.key().compareTo(maxObject) > 0) {
				maxObject = iterator.key();
			}
		}
		
		return maxObject;
	}
}
