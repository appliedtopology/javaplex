package edu.stanford.math.plex_plus.homology;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Map.Entry;

import edu.stanford.math.plex_plus.algebraic_structures.impl.GenericFreeModule;
import edu.stanford.math.plex_plus.algebraic_structures.interfaces.GenericField;
import edu.stanford.math.plex_plus.datastructures.GenericFormalSum;
import edu.stanford.math.plex_plus.datastructures.pairs.GenericPair;
import edu.stanford.math.plex_plus.homology.barcodes.AugmentedBarcodeCollection;
import edu.stanford.math.plex_plus.homology.streams.interfaces.AbstractFilteredStream;
import gnu.trove.map.hash.THashMap;
import gnu.trove.set.hash.THashSet;

public class GenericPersistentHomology<F, T> {
	private final GenericField<F> field;
	private final Comparator<T> comparator;
	private final GenericFreeModule<F, T> chainModule;

	private int minDimension = 0;
	
	public GenericPersistentHomology(GenericField<F> field, Comparator<T> comparator) {
		this.field = field;
		this.comparator = comparator;
		this.chainModule = new GenericFreeModule<F, T>(field);
	}

	public AugmentedBarcodeCollection<GenericFormalSum<F, T>> computeIntervals(AbstractFilteredStream<T> stream, int maxDimension) {
		return this.getIntervalsFromDecomposition(this.pHcol(stream, maxDimension), stream);
	}

	public GenericPair<THashMap<T, GenericFormalSum<F, T>>, THashMap<T, GenericFormalSum<F, T>>> pHcol(AbstractFilteredStream<T> stream, int maxDimension) {

		THashMap<T, GenericFormalSum<F, T>> R = new THashMap<T, GenericFormalSum<F, T>>();
		THashMap<T, GenericFormalSum<F, T>> V = new THashMap<T, GenericFormalSum<F, T>>();

		/**
		 * This maps a simplex to the set of columns containing the key as its low value.
		 */
		THashMap<T, THashSet<T>> lowMap = new THashMap<T, THashSet<T>>();

		for (T i : stream) {
			/*
			 * Do not process simplices of higher dimension than maxDimension.
			 */
			if (stream.getDimension(i) > maxDimension || stream.getDimension(i) < this.minDimension) {
				continue;
			}

			// initialize V to be the identity matrix
			V.put(i, new GenericFormalSum<F, T>(field.valueOf(1), i));

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
				F c = field.divide(R.get(i).getCoefficient(low_R_i), R.get(j).getCoefficient(this.low(R.get(j))));

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

		return new GenericPair<THashMap<T, GenericFormalSum<F, T>>, THashMap<T, GenericFormalSum<F, T>>>(R, V);
	}

	public AugmentedBarcodeCollection<GenericFormalSum<F, T>> getIntervalsFromDecomposition(GenericPair<THashMap<T, GenericFormalSum<F, T>>, THashMap<T, GenericFormalSum<F, T>>> RV_pair, AbstractFilteredStream<T> stream) {
		AugmentedBarcodeCollection<GenericFormalSum<F, T>> barcodeCollection = new AugmentedBarcodeCollection<GenericFormalSum<F, T>>();

		THashMap<T, GenericFormalSum<F, T>> R = RV_pair.getFirst();
		THashMap<T, GenericFormalSum<F, T>> V = RV_pair.getSecond();

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
					barcodeCollection.addInterval(stream.getDimension(low_R_i), start, end, R.get(i));
				}
			}
		}

		// add the collection of semi-infinite intervals to the barcode collection
		for (T i: F) {
			barcodeCollection.addInterval(stream.getDimension(i), stream.getFiltrationValue(i), V.get(i));
		}

		return barcodeCollection;
	}

	public List<GenericFormalSum<F, T>> getBoundaryColumns(AbstractFilteredStream<T> stream, int dimension) {
		List<GenericFormalSum<F, T>> D = new ArrayList<GenericFormalSum<F, T>>();

		for (T i: stream) {
			if (stream.getDimension(i) != dimension) {
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
	private T low(GenericFormalSum<F, T> chain) {
		T maxObject = null;

		for (Iterator<Entry<T, F>> iterator = chain.iterator(); iterator.hasNext(); ) {
			Entry<T, F> entry = iterator.next();
			if (maxObject == null) {
				maxObject = entry.getKey();
			}

			if (this.comparator.compare(entry.getKey(), maxObject) > 0) {
				maxObject = entry.getKey();
			}
		}

		return maxObject;
	}
}
