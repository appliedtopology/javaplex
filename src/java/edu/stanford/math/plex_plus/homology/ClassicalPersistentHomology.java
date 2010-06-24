package edu.stanford.math.plex_plus.homology;

import java.util.Comparator;

import edu.stanford.math.plex_plus.algebraic_structures.impl.IntFreeModule;
import edu.stanford.math.plex_plus.algebraic_structures.interfaces.IntField;
import edu.stanford.math.plex_plus.datastructures.IntFormalSum;
import edu.stanford.math.plex_plus.homology.barcodes.BarcodeCollection;
import edu.stanford.math.plex_plus.homology.simplex.ChainBasisElement;
import edu.stanford.math.plex_plus.homology.simplex_streams.SimplexStream;
import edu.stanford.math.plex_plus.utility.ExceptionUtility;
import gnu.trove.iterator.TObjectIntIterator;
import gnu.trove.map.hash.THashMap;
import gnu.trove.set.hash.THashSet;

/**
 * This class performs the persistent homology algorithm as outlined in
 * the paper "Computing Persistent Homology" by Zomorodian and Carlsson.
 * 
 * @author Andrew Tausz
 *
 * @param <BasisElementType>
 */
public class ClassicalPersistentHomology<BasisElementType extends ChainBasisElement> {
	private final THashSet<BasisElementType> markedSimplices = new THashSet<BasisElementType>();
	private final THashMap<BasisElementType, IntFormalSum<BasisElementType>> T = new THashMap<BasisElementType, IntFormalSum<BasisElementType>>();
	private final IntField field;
	private SimplexStream<BasisElementType> currentStream = null;
	private final Comparator<BasisElementType> comparator;
	
	public ClassicalPersistentHomology(IntField field, Comparator<BasisElementType> comparator) {
		this.field = field;
		this.comparator = comparator;
	}
	
	public BarcodeCollection computeIntervals(SimplexStream<BasisElementType> stream, int maxDimension) {
		BarcodeCollection barcodeCollection = new BarcodeCollection();

		this.currentStream = stream;
		
		for (BasisElementType simplex : stream) {
			
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
			
			IntFormalSum<BasisElementType> d = this.removePivotRows(simplex);
			
			if (d.isEmpty()) {
				this.markedSimplices.add(simplex);
			} else {
				BasisElementType sigma_j = simplex;
				BasisElementType sigma_i = getMaximumObject(d);
				int k = sigma_i.getDimension();
				
				// store j and d in T[i]
				this.T.put(sigma_i, d);
				
				// store interval
				double degree_i = stream.getFiltrationValue(sigma_i);
				double degree_j = stream.getFiltrationValue(sigma_j);
				
				if (degree_j < degree_i) {
					System.out.println("ERROR!!!!!!!!!!!!!");
				}
				
				// don't store intervals that are simultaneously created and destroyed
				if (degree_i != degree_j && k < maxDimension) {
					barcodeCollection.addInterval(k, degree_i, degree_j);
				}
			}
		}
		
		for (BasisElementType simplex : this.markedSimplices) {
			if (!this.T.containsKey(simplex) || this.T.get(simplex).isEmpty()) {
				int k = simplex.getDimension();
				if (k < maxDimension) {
					barcodeCollection.addInterval(k, stream.getFiltrationValue(simplex));
				}
			}
		}
	
		System.out.println("T");
		System.out.println(this.T);
		System.out.println("marked simplices");
		System.out.println(this.markedSimplices);
		
		return barcodeCollection;
	}
	
	private IntFormalSum<BasisElementType> removePivotRows(BasisElementType simplex) {
		ExceptionUtility.verifyNonNull(simplex);
		//IntFormalSum<BasisElementType> d = createBoundaryChain(simplex.getBoundaryArray());
		IntFreeModule<BasisElementType> chainModule = new IntFreeModule<BasisElementType>(this.field);
		IntFormalSum<BasisElementType> d = chainModule.createSum(simplex.getBoundaryCoefficients(), (BasisElementType[]) simplex.getBoundaryArray());

		// remove unmarked terms from d
		for (TObjectIntIterator<BasisElementType> iterator = d.iterator(); iterator.hasNext(); ) {
			iterator.advance();
			if (!this.markedSimplices.contains(iterator.key())) {
				iterator.remove();
			}
		}
		
		while (!d.isEmpty()) {
			BasisElementType sigma_i = getMaximumObject(d);
			
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
	 * 
	 * @param abstractChainBasisElements
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private IntFormalSum<BasisElementType> createBoundaryChain(ChainBasisElement[] abstractChainBasisElements) {
		ExceptionUtility.verifyNonNull(abstractChainBasisElements);
		IntFormalSum<BasisElementType> sum = new IntFormalSum<BasisElementType>();
		IntFreeModule<BasisElementType> chainModule = new IntFreeModule<BasisElementType>(this.field);
		
		for (int i = 0; i < abstractChainBasisElements.length; i++) {
			chainModule.addObject(sum, (i % 2 == 0 ? 1 : -1), (BasisElementType) abstractChainBasisElements[i]);
		}
		
		return sum;
	}
	
	/*
	private static <M extends Comparable<M>> IntFormalSum<M> createBoundaryChain(M[] boundaryObjects) {
		ExceptionUtility.verifyNonNull(boundaryObjects);
		IntFormalSum<M> sum = new IntFormalSum<M>();
		
		for (int i = 0; i < boundaryObjects.length; i++) {
			sum.put((i % 2 == 0 ? 1 : -1), boundaryObjects[i]);
		}
		
		return sum;
	}
	*/
	
	/*
	private AbstractSimplex getMaximumObject(IntFormalSum<AbstractSimplex> chain) {
		AbstractSimplex maxObject = null;
		double maxFiltration = Infinity.getNegativeInfinity();
		
		for (TObjectIntIterator<AbstractSimplex> iterator = chain.iterator(); iterator.hasNext(); ) {
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
	
	private BasisElementType getMaximumObject(IntFormalSum<BasisElementType> chain) {
		BasisElementType maxObject = null;

		for (TObjectIntIterator<BasisElementType> iterator = chain.iterator(); iterator.hasNext(); ) {
			iterator.advance();
			if (maxObject == null) {
				maxObject = iterator.key();
			}
			
			if (this.comparator.compare(iterator.key(), maxObject) > 0) {
				maxObject = iterator.key();
			}
		}
		
		return maxObject;
	}
}
