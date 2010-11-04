package edu.stanford.math.plex4.autogen.homology;

import java.util.Comparator;
import java.util.Iterator;
import java.util.Set;

import edu.stanford.math.plex4.homology.barcodes.IntAugmentedBarcodeCollection;
import edu.stanford.math.plex4.homology.barcodes.IntBarcodeCollection;
import edu.stanford.math.plex4.streams.interfaces.AbstractFilteredStream;
import edu.stanford.math.primitivelib.autogen.algebraic.IntAbstractField;
import edu.stanford.math.primitivelib.autogen.formal_sum.IntSparseFormalSum;
import edu.stanford.math.primitivelib.autogen.pair.ObjectObjectPair;
import gnu.trove.THashMap;
import gnu.trove.THashSet;





/**
 * This class defines the functionality for a persistence algorithm with underlying
 * field type being int and underlying basis element type being U.
 * It acts as an intermediate layer between the interface AbstractPersistenceBasisAlgorithm
 * and the actual implementations of the persistent homology/cohomology algorithms.
 * 
 * @author autogen
 *
 * @param <int> the underlying type of the field
 * @param <U> the type of the basis elements
 */
public abstract class IntPersistentHomology<U> extends IntPersistenceAlgorithm<U> {

		/**
	 * This constructor initializes the object with a field and a comparator on the basis type.
	 * 
	 * @param field a field structure on the type int
	 * @param basisComparator a comparator on the basis type U
	 * @param minDimension the minimum dimension to compute 
	 * @param maxDimension the maximum dimension to compute
	 */
	public IntPersistentHomology(IntAbstractField field, Comparator<U> basisComparator, int minDimension, int maxDimension) {
		super(field, basisComparator, minDimension, maxDimension);
	}
	
	@Override
	protected IntAugmentedBarcodeCollection<IntSparseFormalSum<U>> computeAugmentedIntervalsImpl(AbstractFilteredStream<U> stream) {
		return this.getAugmentedIntervals(this.pHcol(stream), stream);
	}

	@Override
	protected IntBarcodeCollection computeIntervalsImpl(AbstractFilteredStream<U> stream) {
		return this.getIntervals(this.pHcol(stream), stream);
	}
	
	/**
	 * This function implements the pHcol algorithm described in the paper. It computes the decomposition
	 * R = D * V, where D is the boundary matrix, R is reduced, and is invertible and upper triangular.
	 * This function returns the pair (R, V). Note that in our implementation, we represent a matrix by
	 * a hash map which maps a generating object to a formal sum which corresponds to a column in the matrix.
	 * Note that this is simply a sparse representation of a linear transformation on a vector space with
	 * free basis consisting of elements of type U.
	 * 
	 * @param stream the filtered chain complex which provides elements in increasing filtration order
	 * @return a ObjectObjectPair containing the matrices R and V
	 */
	private ObjectObjectPair<THashMap<U, IntSparseFormalSum<U>>, THashMap<U, IntSparseFormalSum<U>>> pHcol(AbstractFilteredStream<U> stream) {

		THashMap<U, IntSparseFormalSum<U>> R = new THashMap<U, IntSparseFormalSum<U>>();
		THashMap<U, IntSparseFormalSum<U>> V = new THashMap<U, IntSparseFormalSum<U>>();

		/**
		 * This maps a simplex to the set of columns containing the key as its low value.
		 */
		THashMap<U, THashSet<U>> lowMap = new THashMap<U, THashSet<U>>();

		for (U i : stream) {
			/*
			 * Do not process simplices of higher dimension than maxDimension.
			 */
			if (stream.getDimension(i) < this.minDimension) {
				continue;
			}
			
			if (stream.getDimension(i) > this.maxDimension + 1) {
				continue;
			}

			// initialize V to be the identity matrix
						V.put(i, this.chainModule.createNewSum(this.field.valueOf(1), i));
			
			// form the column R[i] which equals the boundary of the current simplex.
			// store the column as a column in R
			R.put(i, chainModule.createNewSum(stream.getBoundaryCoefficients(i), stream.getBoundary(i)));

			// compute low_R(i)
			U low_R_i = this.low(R.get(i));

			// if the boundary of i is empty, then continue to next iteration since there
			// is nothing to process
			if (low_R_i == null) {
				continue;
			}

			THashSet<U> matchingLowSimplices = lowMap.get(low_R_i);
			while (matchingLowSimplices != null && !matchingLowSimplices.isEmpty()) {
				Iterator<U> iterator = matchingLowSimplices.iterator();
				/**
				 * TODO: Is this the right thing to do???
				 * Ie. should the iterator.next go at the end of the loop?
				 */
				U j = iterator.next();

								int c = field.divide(R.get(i).getCoefficient(low_R_i), R.get(j).getCoefficient(low_R_i));
				int negative_c = field.negate(c);
								//R.put(i, chainModule.subtract(R.get(i), chainModule.multiply(c, R.get(j))));
				//V.put(i, chainModule.subtract(V.get(i), chainModule.multiply(c, V.get(j))));
				this.chainModule.accumulate(R.get(i), R.get(j), negative_c);
				this.chainModule.accumulate(V.get(i), V.get(j), negative_c);

				// remove old low_R(i) entry
				//lowMap.get(low_R_i).remove(i);

				// recompute low_R(i)
				low_R_i = this.low(R.get(i));

				matchingLowSimplices = lowMap.get(low_R_i);
			}

			// store the low value in the map
			if (low_R_i != null) {
				if (!lowMap.containsKey(low_R_i)) {
					lowMap.put(low_R_i, new THashSet<U>());
				}
				lowMap.get(low_R_i).add(i);
			}
		}

		// at this point we have computed the decomposition R = D * V
		// we return the pair (R, V)

		return new ObjectObjectPair<THashMap<U, IntSparseFormalSum<U>>, THashMap<U, IntSparseFormalSum<U>>>(R, V);
	}
	
	protected abstract IntAugmentedBarcodeCollection<IntSparseFormalSum<U>> getAugmentedIntervals(ObjectObjectPair<THashMap<U, IntSparseFormalSum<U>>, 
			THashMap<U, IntSparseFormalSum<U>>> RV_pair, 
			AbstractFilteredStream<U> stream);

	protected abstract IntBarcodeCollection getIntervals(ObjectObjectPair<THashMap<U, IntSparseFormalSum<U>>, 
			THashMap<U, IntSparseFormalSum<U>>> RV_pair, 
			AbstractFilteredStream<U> stream);

	protected IntAugmentedBarcodeCollection<IntSparseFormalSum<U>> getAugmentedIntervals(
			ObjectObjectPair<THashMap<U, IntSparseFormalSum<U>>, 
			THashMap<U, IntSparseFormalSum<U>>> RV_pair, 
			AbstractFilteredStream<U> stream, 
			boolean absolute) {
			
		IntAugmentedBarcodeCollection<IntSparseFormalSum<U>> barcodeCollection = new IntAugmentedBarcodeCollection<IntSparseFormalSum<U>>();

		THashMap<U, IntSparseFormalSum<U>> R = RV_pair.getFirst();
		THashMap<U, IntSparseFormalSum<U>> V = RV_pair.getSecond();

		Set<U> births = new THashSet<U>();

		for (U i: stream) {
			if (!R.containsKey(i)) {
				continue;
			}
			U low_R_i = this.low(R.get(i));
			int dimension = stream.getDimension(i);
			if (low_R_i == null) {
				if (dimension <= this.maxDimension && dimension >= this.minDimension) {
					births.add(i);
				}
			} else {
				// simplex i kills low_R_i
				births.remove(low_R_i);
				births.remove(i);
				int start = stream.getFiltrationIndex(low_R_i);
				int end = stream.getFiltrationIndex(i);
				if (end > start) {
					if (absolute) {
						dimension = stream.getDimension(low_R_i);
						if (dimension < this.maxDimension && dimension >= this.minDimension)
							barcodeCollection.addInterval(stream.getDimension(low_R_i), start, end, R.get(i));
					} else {
						dimension = stream.getDimension(i);
						if (dimension < this.maxDimension && dimension >= this.minDimension)
							barcodeCollection.addInterval(dimension, start, end, V.get(i));
					}
				}
			}
		}

		// the elements in birth are the ones that are never killed
		// these correspond to semi-infinite intervals
		for (U i: births) {
			int dimension = stream.getDimension(i);
			if (dimension < this.maxDimension && dimension >= this.minDimension) {
				if (absolute) {
					barcodeCollection.addRightInfiniteInterval(stream.getDimension(i), stream.getFiltrationIndex(i), V.get(i));
				} else {
					barcodeCollection.addLeftInfiniteInterval(stream.getDimension(i), stream.getFiltrationIndex(i), V.get(i));
				}
			}
		}

		return barcodeCollection;
	}

	protected IntBarcodeCollection getIntervals(
			ObjectObjectPair<THashMap<U, IntSparseFormalSum<U>>, 
			THashMap<U, IntSparseFormalSum<U>>> RV_pair, 
			AbstractFilteredStream<U> stream, 
			boolean absolute) {
			
		IntBarcodeCollection barcodeCollection = new IntBarcodeCollection();

		THashMap<U, IntSparseFormalSum<U>> R = RV_pair.getFirst();

		Set<U> births = new THashSet<U>();

		for (U i: stream) {
			if (!R.containsKey(i)) {
				continue;
			}
			U low_R_i = this.low(R.get(i));
			int dimension = stream.getDimension(i);
			if (low_R_i == null) {
				if (dimension <= this.maxDimension && dimension >= this.minDimension) {
					births.add(i);
				}
			} else {
				// simplex i kills low_R_i
				births.remove(low_R_i);
				births.remove(i);
				int start = stream.getFiltrationIndex(low_R_i);
				int end = stream.getFiltrationIndex(i);
				if (end > start) {
					if (absolute) {
						dimension = stream.getDimension(low_R_i);
						if (dimension < this.maxDimension && dimension >= this.minDimension)
							barcodeCollection.addInterval(stream.getDimension(low_R_i), start, end);
					} else {
						dimension = stream.getDimension(i);
						if (dimension < this.maxDimension && dimension >= this.minDimension)
							barcodeCollection.addInterval(dimension, start, end);
					}
				}
			}
		}

		// the elements in birth are the ones that are never killed
		// these correspond to semi-infinite intervals
		for (U i: births) {
			int dimension = stream.getDimension(i);
			if (dimension < this.maxDimension && dimension >= this.minDimension) {
				if (absolute) {
					barcodeCollection.addRightInfiniteInterval(stream.getDimension(i), stream.getFiltrationIndex(i));
				} else {
					barcodeCollection.addLeftInfiniteInterval(stream.getDimension(i), stream.getFiltrationIndex(i));
				}
			}
		}

		return barcodeCollection;
	}
}
