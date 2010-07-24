package edu.stanford.math.plex4.homology;

import java.util.Comparator;
import java.util.Set;

import edu.stanford.math.plex4.algebraic_structures.interfaces.GenericField;
import edu.stanford.math.plex4.datastructures.pairs.GenericPair;
import edu.stanford.math.plex4.free_module.AbstractGenericFormalSum;
import edu.stanford.math.plex4.homology.barcodes.AugmentedBarcodeCollection;
import edu.stanford.math.plex4.homology.barcodes.BarcodeCollection;
import edu.stanford.math.plex4.homology.streams.interfaces.AbstractFilteredStream;
import gnu.trove.iterator.hash.TObjectHashIterator;
import gnu.trove.map.hash.THashMap;
import gnu.trove.set.hash.THashSet;

public abstract class GenericPersistentHomology<F, T> extends GenericPersistenceAlgorithm<F, T> {
	public GenericPersistentHomology(GenericField<F> field, Comparator<T> comparator, int minDimension, int maxDimension) {
		super(field, comparator, minDimension, maxDimension);
		// TODO Auto-generated constructor stub
	}

	public GenericPersistentHomology(GenericField<F> field, Comparator<T> comparator, int maxDimension) {
		super(field, comparator, 0, maxDimension);
		// TODO Auto-generated constructor stub
	}

	@Override
	public AugmentedBarcodeCollection<AbstractGenericFormalSum<F, T>> computeAugmentedIntervalsImpl(AbstractFilteredStream<T> stream) {
		return this.getAugmentedIntervals(this.pHcol(stream), stream);
	}

	@Override
	public BarcodeCollection computeIntervalsImpl(AbstractFilteredStream<T> stream) {
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
	private GenericPair<THashMap<T, AbstractGenericFormalSum<F, T>>, THashMap<T, AbstractGenericFormalSum<F, T>>> pHcol(AbstractFilteredStream<T> stream) {

		THashMap<T, AbstractGenericFormalSum<F, T>> R = new THashMap<T, AbstractGenericFormalSum<F, T>>();
		THashMap<T, AbstractGenericFormalSum<F, T>> V = new THashMap<T, AbstractGenericFormalSum<F, T>>();

		/**
		 * This maps a simplex to the set of columns containing the key as its low value.
		 */
		THashMap<T, THashSet<T>> lowMap = new THashMap<T, THashSet<T>>();

		for (T i : stream) {
			/*
			 * Do not process simplices of higher dimension than maxDimension.
			 */
			if (stream.getDimension(i) > this.maxDimension + 1 || stream.getDimension(i) < this.minDimension) {
				continue;
			}

			// initialize V to be the identity matrix
			V.put(i, this.chainModule.createNewSum(this.field.valueOf(1), i));

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
				TObjectHashIterator<T> iterator = matchingLowSimplices.iterator();
				T j = iterator.next();

				F c = field.divide(R.get(i).getCoefficient(low_R_i), R.get(j).getCoefficient(low_R_i));

				R.put(i, chainModule.subtract(R.get(i), chainModule.multiply(c, R.get(j))));
				V.put(i, chainModule.subtract(V.get(i), chainModule.multiply(c, V.get(j))));

				// remove old low_R(i) entry
				//lowMap.get(low_R_i).remove(i);

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

		return new GenericPair<THashMap<T, AbstractGenericFormalSum<F, T>>, THashMap<T, AbstractGenericFormalSum<F, T>>>(R, V);
	}

	protected abstract AugmentedBarcodeCollection<AbstractGenericFormalSum<F, T>> getAugmentedIntervals(GenericPair<THashMap<T, AbstractGenericFormalSum<F, T>>, THashMap<T, AbstractGenericFormalSum<F, T>>> RV_pair, AbstractFilteredStream<T> stream);

	protected abstract BarcodeCollection getIntervals(GenericPair<THashMap<T, AbstractGenericFormalSum<F, T>>, THashMap<T, AbstractGenericFormalSum<F, T>>> RV_pair, AbstractFilteredStream<T> stream);

	protected AugmentedBarcodeCollection<AbstractGenericFormalSum<F, T>> getAugmentedIntervals(
			GenericPair<THashMap<T, AbstractGenericFormalSum<F, T>>, THashMap<T, AbstractGenericFormalSum<F, T>>> RV_pair, AbstractFilteredStream<T> stream, boolean absolute) {
		AugmentedBarcodeCollection<AbstractGenericFormalSum<F, T>> barcodeCollection = new AugmentedBarcodeCollection<AbstractGenericFormalSum<F, T>>();

		THashMap<T, AbstractGenericFormalSum<F, T>> R = RV_pair.getFirst();
		THashMap<T, AbstractGenericFormalSum<F, T>> V = RV_pair.getSecond();

		Set<T> births = new THashSet<T>();

		for (T i: stream) {
			if (!R.containsKey(i)) {
				continue;
			}
			T low_R_i = this.low(R.get(i));
			int dimension = stream.getDimension(i);
			if (low_R_i == null) {
				if (dimension <= this.maxDimension && dimension >= this.minDimension) {
					births.add(i);
				}
			} else {
				// simplex i kills low_R_i
				births.remove(low_R_i);
				births.remove(i);
				double start = stream.getFiltrationValue(low_R_i);
				double end = stream.getFiltrationValue(i);
				if (end >= start + this.minGranularity) {
					if (absolute) {
						barcodeCollection.addInterval(stream.getDimension(low_R_i), start, end, R.get(i));
					} else {
						barcodeCollection.addInterval(stream.getDimension(i), start, end, V.get(i));
					}
				}
			}
		}

		// the elements in birth are the ones that are never killed
		// these correspond to semi-infinite intervals
		for (T i: births) {
			if (absolute) {
				barcodeCollection.addRightInfiniteInterval(stream.getDimension(i), stream.getFiltrationValue(i), V.get(i));
			} else {
				barcodeCollection.addLeftInfiniteInterval(stream.getDimension(i), stream.getFiltrationValue(i), V.get(i));
			}
		}

		return barcodeCollection;
	}

	protected BarcodeCollection getIntervals(
			GenericPair<THashMap<T, AbstractGenericFormalSum<F, T>>, THashMap<T, AbstractGenericFormalSum<F, T>>> RV_pair, AbstractFilteredStream<T> stream, boolean absolute) {
		BarcodeCollection barcodeCollection = new BarcodeCollection();

		THashMap<T, AbstractGenericFormalSum<F, T>> R = RV_pair.getFirst();

		Set<T> births = new THashSet<T>();

		for (T i: stream) {
			if (!R.containsKey(i)) {
				continue;
			}
			T low_R_i = this.low(R.get(i));
			int dimension = stream.getDimension(i);
			if (low_R_i == null) {
				if (dimension <= this.maxDimension && dimension >= this.minDimension) {
					births.add(i);
				}
			} else {
				// simplex i kills low_R_i
				births.remove(low_R_i);
				births.remove(i);
				double start = stream.getFiltrationValue(low_R_i);
				double end = stream.getFiltrationValue(i);
				if (end >= start + this.minGranularity) {
					if (absolute) {
						barcodeCollection.addInterval(stream.getDimension(low_R_i), start, end);
					} else {
						barcodeCollection.addInterval(stream.getDimension(i), start, end);
					}
				}
			}
		}

		// the elements in birth are the ones that are never killed
		// these correspond to semi-infinite intervals
		for (T i: births) {
			if (absolute) {
				barcodeCollection.addRightInfiniteInterval(stream.getDimension(i), stream.getFiltrationValue(i));
			} else {
				barcodeCollection.addLeftInfiniteInterval(stream.getDimension(i), stream.getFiltrationValue(i));
			}
		}

		return barcodeCollection;
	}	
}

