package edu.stanford.math.plex_plus.homology;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import edu.stanford.math.plex_plus.algebraic_structures.impl.IntFreeModule;
import edu.stanford.math.plex_plus.algebraic_structures.interfaces.IntField;
import edu.stanford.math.plex_plus.datastructures.IntFormalSum;
import edu.stanford.math.plex_plus.datastructures.pairs.GenericPair;
import edu.stanford.math.plex_plus.homology.barcodes.AugmentedBarcodeCollection;
import edu.stanford.math.plex_plus.homology.barcodes.BarcodeCollection;
import edu.stanford.math.plex_plus.homology.simplex.ChainBasisElement;
import edu.stanford.math.plex_plus.homology.simplex_streams.SimplexStream;
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
 * @param <T>
 */
public class IntPersistentHomology<T extends ChainBasisElement> {
	private final IntField field;
	private final Comparator<T> comparator;

	public IntPersistentHomology(IntField field, Comparator<T> comparator) {
		this.field = field;
		this.comparator = comparator;
	}

	public AugmentedBarcodeCollection<IntFormalSum<T>> computeIntervals(SimplexStream<T> stream, int maxDimension) {
		return this.getIntervalsFromDecomposition(this.pHcol(stream, maxDimension), stream);
	}
	
	public GenericPair<THashMap<T, IntFormalSum<T>>, THashMap<T, IntFormalSum<T>>> pHcol(SimplexStream<T> stream, int maxDimension) {
		
		THashMap<T, IntFormalSum<T>> R = new THashMap<T, IntFormalSum<T>>();
		THashMap<T, IntFormalSum<T>> V = new THashMap<T, IntFormalSum<T>>();
		
		/**
		 * This maps a simplex to the set of columns containing the key as its low value.
		 */
		THashMap<T, THashSet<T>> lowMap = new THashMap<T, THashSet<T>>();
		
		IntFreeModule<T> chainModule = new IntFreeModule<T>(this.field);
		
		for (T i : stream) {
			/*
			 * Do not process simplices of higher dimension than maxDimension.
			 */
			if (i.getDimension() > maxDimension) {
				continue;
			}
			
			// initialize V to be the identity matrix
			V.put(i, new IntFormalSum<T>(i));
			
			// form the column R[i] which equals the boundary of the current simplex.
			// store the column as a column in R
			R.put(i, chainModule.createSum(stream.getBoundaryCoefficients(i), stream.getBoundary(i)));
			
			// compute low_R(i)
			T low_R_i = this.low(R.get(i));
			
			// if the boundary of i is empty, then continue to next iteration since there
			// is nothing to process
			if (low_R_i == null) {
				continue;
			}
			
			THashSet<T> matchingLowSimplices = lowMap.get(low_R_i);
			while (matchingLowSimplices != null && !matchingLowSimplices.isEmpty()) {
				T j = matchingLowSimplices.iterator().next();
				int c = field.divide(R.get(i).getCoefficient(low_R_i), R.get(j).getCoefficient(this.low(R.get(j))));
				
				R.put(i, chainModule.subtract(R.get(i), chainModule.multiply(c, R.get(j))));
				V.put(i, chainModule.subtract(V.get(i), chainModule.multiply(c, V.get(j))));
				
				// recompute low_R(i)
				low_R_i = this.low(R.get(i));
				// recompute matching indices
				matchingLowSimplices = lowMap.get(low_R_i);
			}
			
			// store the low value in the map
			if (low_R_i != null) {
				if (!lowMap.containsKey(low_R_i)) {
					lowMap.put(low_R_i, new THashSet<T>());
				}
				lowMap.get(low_R_i).add(i);
			}
		}
		
		// at this point we have computed the decomposition R = D * V
		// we return the pair (R, V)
		
		return new GenericPair<THashMap<T, IntFormalSum<T>>, THashMap<T, IntFormalSum<T>>>(R, V);
	}
	
	public AugmentedBarcodeCollection<IntFormalSum<T>> getIntervalsFromDecomposition(GenericPair<THashMap<T, IntFormalSum<T>>, THashMap<T, IntFormalSum<T>>> RV_pair, SimplexStream<T> stream) {
		AugmentedBarcodeCollection<IntFormalSum<T>> barcodeCollection = new AugmentedBarcodeCollection<IntFormalSum<T>>();
		
		THashMap<T, IntFormalSum<T>> R = RV_pair.getFirst();
		THashMap<T, IntFormalSum<T>> V = RV_pair.getSecond();
		
		/*
		 * We follow the naming convention used in Theorem 2.5 in "Dualities in Persistent (Co)homology".
		 * 
		 * Given our chain complex (C, d), we use the partition {0, ..., n-1} = F u G u H.
		 * Then the persistence diagram consists of intervals [a_f, \infinity) for f in F,
		 * and [a_g, a_h) for g in G, and h in H. 
		 * 
		 */
		Set<T> F = new THashSet<T>();
		Set<T> G = new THashSet<T>();
		Set<T> H = new THashSet<T>();
		
		Set<T> simplices = R.keySet();
		F.addAll(simplices);
		
		for (Iterator<T> iterator = simplices.iterator(); iterator.hasNext(); ) {
			T i = iterator.next();
			T low_R_i = this.low(R.get(i));
			if (low_R_i != null) {
				G.add(low_R_i);
				H.add(i);
				// remove i from F
				F.remove(i);
				// remove low_R_i from F as well
				F.remove(low_R_i);
				double start = stream.getFiltrationValue(low_R_i);
				double end = stream.getFiltrationValue(i);
				if (start < end) {
					barcodeCollection.addInterval(low_R_i.getDimension(), start, end, R.get(i));
				}
			}
		}
		
		// add the collection of semi-infinite intervals to the barcode collection
		for (T i: F) {
			barcodeCollection.addInterval(i.getDimension(), stream.getFiltrationValue(i), V.get(i));
		}
		
		return barcodeCollection;
	}
	
	public BarcodeCollection pCoh(SimplexStream<T> stream, int maxDimension) {
		BarcodeCollection barcodeCollection = new BarcodeCollection();
		IntFreeModule<T> chainModule = new IntFreeModule<T>(this.field);
		
		THashSet<T> Z_perp = new THashSet<T>();
		THashSet<T> markedSimplices = new THashSet<T>();
		THashSet<T> birth = new THashSet<T>();
		
		for (T simplex : stream) {
			
			if (simplex.getDimension() > maxDimension) {
				continue;
			}
			
			IntFormalSum<T> boundary = chainModule.createSum(stream.getBoundaryCoefficients(simplex), stream.getBoundary(simplex));
			List<T> indices = new ArrayList<T>();
			
			// form the set of indices
			// indices = [j | sigma*_i in d* z*_j, where z*_j is unmarked in Z_perp]
			for (TObjectIntIterator<T> iterator = boundary.iterator(); iterator.hasNext(); ) {
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
	
	public List<IntFormalSum<T>> getBoundaryColumns(SimplexStream<T> stream, int dimension) {
		List<IntFormalSum<T>> D = new ArrayList<IntFormalSum<T>>();
		IntFreeModule<T> chainModule = new IntFreeModule<T>(this.field);
		
		for (T i: stream) {
			if (i.getDimension() != dimension) {
				continue;
			}
			
			D.add(chainModule.createSum(stream.getBoundaryCoefficients(i), stream.getBoundary(i)));
		}
		
		return D;
	}
	
	/**
	 * This function computes the operation low_A(j) as described in the paper. Note that if
	 * the chain is empty (for example the column contains only zeros), then this function
	 * returns null.
	 * 
	 * @param formalSum
	 * @return
	 */
	private T low(IntFormalSum<T> chain) {
		T maxObject = null;

		for (TObjectIntIterator<T> iterator = chain.iterator(); iterator.hasNext(); ) {
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
