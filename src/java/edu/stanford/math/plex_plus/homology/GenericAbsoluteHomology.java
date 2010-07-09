package edu.stanford.math.plex_plus.homology;

import java.util.Comparator;
import java.util.Iterator;
import java.util.Set;

import edu.stanford.math.plex_plus.algebraic_structures.interfaces.GenericField;
import edu.stanford.math.plex_plus.datastructures.GenericFormalSum;
import edu.stanford.math.plex_plus.datastructures.pairs.GenericPair;
import edu.stanford.math.plex_plus.homology.barcodes.AugmentedBarcodeCollection;
import edu.stanford.math.plex_plus.homology.barcodes.BarcodeCollection;
import edu.stanford.math.plex_plus.homology.streams.interfaces.AbstractFilteredStream;
import gnu.trove.iterator.hash.TObjectHashIterator;
import gnu.trove.map.hash.THashMap;
import gnu.trove.set.hash.THashSet;

public class GenericAbsoluteHomology<F, T> extends GenericPersistenceAlgorithm<F, T> {

	public GenericAbsoluteHomology(GenericField<F> field, Comparator<T> comparator, int minDimension, int maxDimension) {
		super(field, comparator, minDimension, maxDimension);
		// TODO Auto-generated constructor stub
	}
	
	public GenericAbsoluteHomology(GenericField<F> field, Comparator<T> comparator, int maxDimension) {
		super(field, comparator, 0, maxDimension);
		// TODO Auto-generated constructor stub
	}

	@Override
	public AugmentedBarcodeCollection<GenericFormalSum<F, T>> computeAugmentedIntervals(AbstractFilteredStream<T> stream) {
		return this.getAugmentedIntervals(this.pHcol(stream), stream);
	}

	@Override
	public BarcodeCollection computeIntervals(AbstractFilteredStream<T> stream) {
		return this.getIntervals(this.pHcol(stream), stream);
	}

	/**
	 * This function implements the pHcol algorithm described in the paper. It computes the decomposition
	 * R = D * V, where D is the boundary matrix, R is reduced, and is invertible and upper triangular.
	 * This function returns the pair (R, V). Note that in our implementation, we represent a matrix by
	 * a hash map which maps a generating object to a formal sum which corresponds to a column in the matrix.
	 * Note that this is simply a sparse representation of a linear transformation on a vector space with
	 * free basis consisting of elements of type T.
	 * 
	 * @param stream the filtered chain complex which provides elements in increasing filtration order
	 * @return a GenericPair containing the matrices R and V
	 */
	private GenericPair<THashMap<T, GenericFormalSum<F, T>>, THashMap<T, GenericFormalSum<F, T>>> pHcol(AbstractFilteredStream<T> stream) {

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
			if (stream.getDimension(i) > this.maxDimension || stream.getDimension(i) < this.minDimension) {
				break;
			}

			// initialize V to be the identity matrix
			V.put(i, new GenericFormalSum<F, T>(this.field.valueOf(1), i));

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
				T j = null;
				TObjectHashIterator<T> iterator = matchingLowSimplices.iterator();
				if (matchingLowSimplices.size() == 1) {
					j = iterator.next();
					if (i.equals(j)) {
						break;
					}
				} else {
					j = iterator.next();
					if ((i.equals(j))) {
						j = iterator.next();
					}
				}
				
				F c = field.divide(R.get(i).getCoefficient(low_R_i), R.get(j).getCoefficient(this.low(R.get(j))));

				R.put(i, chainModule.subtract(R.get(i), chainModule.multiply(c, R.get(j))));
				V.put(i, chainModule.subtract(V.get(i), chainModule.multiply(c, V.get(j))));
				
				// remove old low_R(i) entry
				lowMap.get(low_R_i).remove(i);
				
				// recompute low_R(i)
				low_R_i = this.low(R.get(i));
				
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

	private AugmentedBarcodeCollection<GenericFormalSum<F, T>> getAugmentedIntervals(GenericPair<THashMap<T, GenericFormalSum<F, T>>, THashMap<T, GenericFormalSum<F, T>>> RV_pair, AbstractFilteredStream<T> stream) {
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
			barcodeCollection.addRightInfiniteInterval(stream.getDimension(i), stream.getFiltrationValue(i), V.get(i));
		}

		return barcodeCollection;
	}
	
	private BarcodeCollection getIntervals(GenericPair<THashMap<T, GenericFormalSum<F, T>>, THashMap<T, GenericFormalSum<F, T>>> RV_pair, AbstractFilteredStream<T> stream) {
		BarcodeCollection barcodeCollection = new BarcodeCollection();

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
					barcodeCollection.addInterval(stream.getDimension(low_R_i), start, end);
				}
			}
		}

		// add the collection of semi-infinite intervals to the barcode collection
		for (T i: F) {
			barcodeCollection.addRightInfiniteInterval(stream.getDimension(i), stream.getFiltrationValue(i));
		}

		return barcodeCollection;
	}	
}
