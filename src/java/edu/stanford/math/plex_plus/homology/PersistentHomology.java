package edu.stanford.math.plex_plus.homology;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

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
 * This class computes persistent (co)homology based on the paper
 * "Dualities in persistent (co)homology" by de Silva, Morozov, and
 * Vejdemo-Johansson. The algorithms described in this paper offer
 * significant performance gains and provide a more complete understanding
 * of persistent homology.
 * 
 * @author Andrew Tausz
 *
 * @param <BasisElementType>
 */
public class PersistentHomology<BasisElementType extends ChainBasisElement> {
	private final IntField field;
	private final Comparator<BasisElementType> comparator;

	public PersistentHomology(IntField field, Comparator<BasisElementType> comparator) {
		this.field = field;
		this.comparator = comparator;
	}

	public void pHcol(SimplexStream<BasisElementType> stream, int maxDimension) {
		
		THashMap<BasisElementType, IntFormalSum<BasisElementType>> R = new THashMap<BasisElementType, IntFormalSum<BasisElementType>>();
		THashMap<BasisElementType, IntFormalSum<BasisElementType>> V = new THashMap<BasisElementType, IntFormalSum<BasisElementType>>();
		
		/**
		 * This maps a simplex to the set of columns containing the key as its low value.
		 */
		THashMap<BasisElementType, THashSet<BasisElementType>> lowMap = new THashMap<BasisElementType, THashSet<BasisElementType>>();
		
		IntFreeModule<BasisElementType> chainModule = new IntFreeModule<BasisElementType>(this.field);
		
		for (BasisElementType i : stream) {

			/*
			 * Do not process simplices of higher dimension than maxDimension.
			 */
			if (i.getDimension() > maxDimension) {
				continue;
			}
			
			// form the column R[i] which equals the boundary of the current simplex.
			//IntFormalSum<BasisElementType> boundary = this.createBoundaryChain(i.getBoundaryArray());
			// store the column as a column in R
			R.put(i, this.createBoundaryChain(i.getBoundaryArray()));
			
			// compute low_R(i)
			BasisElementType low_R_i = this.low(R.get(i));
			
			THashSet<BasisElementType> matchingLowSimplices = lowMap.get(low_R_i);
			while (matchingLowSimplices != null && !matchingLowSimplices.isEmpty()) {
				BasisElementType j = matchingLowSimplices.iterator().next();
				int c = field.divide(R.get(i).getCoefficient(low_R_i), R.get(j).getCoefficient(this.low(R.get(j))));
				
				R.put(i, chainModule.subtract(R.get(i), chainModule.multiply(c, R.get(j))));
				R.put(i, chainModule.subtract(V.get(i), chainModule.multiply(c, R.get(j))));
				
				// recompute low_R(i)
				low_R_i = this.low(R.get(i));
				// recompute matching indices
				matchingLowSimplices = lowMap.get(low_R_i);
			}
			
			// store the low value in the map
			if (!lowMap.containsKey(low_R_i)) {
				lowMap.put(low_R_i, new THashSet<BasisElementType>());
			}
			lowMap.get(low_R_i).add(i);
		}
	}
	
	public BarcodeCollection pCoh(SimplexStream<BasisElementType> stream, int maxDimension) {
		BarcodeCollection barcodeCollection = new BarcodeCollection();

		THashSet<BasisElementType> Z_perp = new THashSet<BasisElementType>();
		THashSet<BasisElementType> markedSimplices = new THashSet<BasisElementType>();
		THashSet<BasisElementType> birth = new THashSet<BasisElementType>();
		
		for (BasisElementType simplex : stream) {
			
			if (simplex.getDimension() > maxDimension) {
				continue;
			}
			
			IntFormalSum<BasisElementType> boundary = this.createBoundaryChain(simplex.getBoundaryArray());
			List<BasisElementType> indices = new ArrayList<BasisElementType>();
			
			// form the set of indices
			// indices = [j | sigma*_i in d* z*_j, where z*_j is unmarked in Z_perp]
			for (TObjectIntIterator<BasisElementType> iterator = boundary.iterator(); iterator.hasNext(); ) {
				iterator.advance();
				
				if (!markedSimplices.contains(iterator.key()) && Z_perp.contains(iterator.key())) {
					indices.add(iterator.key());
				}
			}
			
			if (indices.isEmpty()) {
				Z_perp.add(simplex);
				birth.add(simplex);
			} else {
				
			}
			
		}
		
		return barcodeCollection;
	}
	
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
	
	/**
	 * This function computes the operation low_A(j) as described in the paper.
	 * 
	 * @param formalSum
	 * @return
	 */
	public BasisElementType low(IntFormalSum<BasisElementType> chain) {
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
