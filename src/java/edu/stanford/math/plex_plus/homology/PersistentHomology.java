package edu.stanford.math.plex_plus.homology;

import edu.stanford.math.plex_plus.datastructures.IntFormalSum;
import edu.stanford.math.plex_plus.homology.barcodes.BarcodeCollection;
import edu.stanford.math.plex_plus.homology.simplex.Simplex;
import edu.stanford.math.plex_plus.homology.simplex_streams.SimplexStream;
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
	private SimplexStream currentStream = null;
	
	public PersistentHomology(IntField field) {
		this.field = field;
	}
	
	public BarcodeCollection computeIntervals(SimplexStream stream, int maxDimension) {
		BarcodeCollection barcodeCollection = new BarcodeCollection();

		this.currentStream = stream;
		
		for (Simplex simplex : stream) {
			
			if (simplex.getDimension() > maxDimension) {
				break;
			}
			
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
				this.T.put(sigma_i, d);
				
				// store interval
				double degree_i = stream.getFiltrationIndex(sigma_i);
				double degree_j = stream.getFiltrationIndex(sigma_j);
				
				if (degree_j < degree_i) {
					System.out.println("ERROR!!!!!!!!!!!!!");
				}
				
				// don't store intervals that are simultaneously created and destroyed
				if (degree_i != degree_j) {
					barcodeCollection.addInterval(k, degree_i, degree_j);
				}
			}
		}
		
		for (Simplex simplex : this.markedSimplices) {
			if (!this.T.containsKey(simplex) || this.T.get(simplex).isEmpty()) {
				int k = simplex.getDimension();
				barcodeCollection.addInterval(k, stream.getFiltrationIndex(simplex));
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
			
			if (!this.T.containsKey(sigma_i) || this.T.get(sigma_i).isEmpty()) {
				break;
			}
			
			int q = T.get(sigma_i).getCoefficient(sigma_i);
			
			if (q == 0) {
				break;
			}
			
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
	
	/*
	private Simplex getMaximumObject(IntFormalSum<Simplex> chain) {
		Simplex maxObject = null;
		double maxFiltration = Infinity.getNegativeInfinity();
		
		for (TObjectIntIterator<Simplex> iterator = chain.iterator(); iterator.hasNext(); ) {
			iterator.advance();
			if (maxObject == null) {
				maxObject = iterator.key();
				maxFiltration = this.currentStream.getFiltrationIndex(maxObject);
			}
			double currentFiltration = this.currentStream.getFiltrationIndex(iterator.key());
			if (currentFiltration > maxFiltration) {
				maxFiltration = currentFiltration;
				maxObject = iterator.key();
			} else if (currentFiltration == maxFiltration) {
				if (iterator.key().compareTo(maxObject) > 0) {
					maxFiltration = currentFiltration;
					maxObject = iterator.key();
				}
			}
		}
		
		return maxObject;
	}
	*/
	
	private Simplex getMaximumObject(IntFormalSum<Simplex> chain) {
		Simplex maxObject = null;

		for (TObjectIntIterator<Simplex> iterator = chain.iterator(); iterator.hasNext(); ) {
			iterator.advance();
			if (maxObject == null) {
				maxObject = iterator.key();
			}
			if (iterator.key().compareTo(maxObject) > 0) {
				maxObject = iterator.key();
			}
		}
		
		return maxObject;
	}
}
