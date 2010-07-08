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
import edu.stanford.math.plex_plus.datastructures.pairs.DoubleGenericPair;
import edu.stanford.math.plex_plus.datastructures.pairs.DoubleGenericPairComparator;
import edu.stanford.math.plex_plus.datastructures.pairs.GenericPair;
import edu.stanford.math.plex_plus.homology.barcodes.AugmentedBarcodeCollection;
import edu.stanford.math.plex_plus.homology.streams.interfaces.AbstractFilteredStream;
import gnu.trove.map.hash.THashMap;
import gnu.trove.set.hash.THashSet;

/**
 * This class implements the persistent homology and cohomology algorithms
 * described in the paper "Dualities in Persistent (Co)homology" by Vin
 * de Silva, Dmitriy Morozov, and Mikael Vejdemo-Johansson. In the above
 * paper, the authors describe algorithms for computing barcodes and 
 * generating (co)cycles for each of the four persistent objects:
 * - absolute homology
 * - absolute cohomology
 * - relative homology
 * - relative cohomology
 * 
 * In most cases, the user will probably be interested in computing barcodes
 * for persistent absolute homology. In this case, it turns out that the 
 * barcodes produced by absolute cohomology are the same. Due to the performance
 * benefits of computing persistent cohomology over homology (as described in the
 * above paper), we suggest that the user try the persistent cohomology
 * algorithm in cases where only barcodes are needed. 
 * 
 * @author Andrew Tausz
 *
 * @param <F> the type of the field over which we perform the computations 
 * @param <T> the type of the basis elements in the chain complex vector spaces
 */
public class GenericPersistentHomology<F, T> {
	/**
	 * This is the field over which we perform the arithmetic computations.
	 */
	private final GenericField<F> field;
	
	/**
	 * This comparator defines the ordering on the basis elements.
	 */
	private final Comparator<T> comparator;
	
	/**
	 * This comparator provides the dictionary ordering on filtration value - basis element
	 * pairs.
	 */
	private final DoubleGenericPairComparator<T> filteredComparator;
	
	/**
	 * This objects performs the chain computations.
	 */
	private final GenericFreeModule<F, T> chainModule;

	/**
	 * This stores the minimum dimension for which to compute (co)homology.
	 */
	private int minDimension = 0;
	
	/**
	 * This stores the maximum dimension for which to compute (co)homology.
	 */
	private int maxDimension = 2;	
	
	/**
	 * This constructor initializes the object with its essential field values.
	 * 
	 * @param field the field over which to perform arithmetic
	 * @param comparator a Comparator for ordering the basis elements
	 * @param maxDimension the maximum dimension to compute to
	 */
	public GenericPersistentHomology(GenericField<F> field, Comparator<T> comparator, int maxDimension) {
		this.field = field;
		this.comparator = comparator;
		this.filteredComparator = new DoubleGenericPairComparator<T>(this.comparator);
		this.chainModule = new GenericFreeModule<F, T>(field);
		this.maxDimension = maxDimension;
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
	public GenericPair<THashMap<T, GenericFormalSum<F, T>>, THashMap<T, GenericFormalSum<F, T>>> pHcol(AbstractFilteredStream<T> stream) {

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
				continue;
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
	
	/**
	 * This function implements the pCoh algorithm described in the paper.
	 * 
	 * @param stream
	 * @param maxDimension
	 * @return
	 */
	public AugmentedBarcodeCollection<GenericFormalSum<F, T>> pCoh(AbstractFilteredStream<T> stream, int maxDimension) {
		AugmentedBarcodeCollection<GenericFormalSum<F, T>> barcodeCollection = new AugmentedBarcodeCollection<GenericFormalSum<F, T>>();

		THashMap<T, GenericFormalSum<F, T>> cocycles = new THashMap<T, GenericFormalSum<F, T>>();
		
		for (T sigma_k : stream) {
			/*
			 * Do not process simplices of higher dimension than maxDimension.
			 */
			if (stream.getDimension(sigma_k) > maxDimension || stream.getDimension(sigma_k) < this.minDimension) {
				continue;
			}
			
			GenericFormalSum<F, T> boundary = chainModule.createSum(stream.getBoundaryCoefficients(sigma_k), stream.getBoundary(sigma_k));
			
			/**
			 * This maintains the coboundary coefficients of the live cocycles. Only nonzero coefficients
			 * are stored.
			 */
			THashMap<T, F> liveCocycleCoefficients = new THashMap<T, F>();
			
			/**
			 * This is this largest live cocycle with nonzero coefficient.
			 */
			T sigma_j = null;			
			F c_j = this.field.getZero();
			
			/*
			 * Compute the coefficients of the the current cocycle sigma_i* within the coboundaries of the
			 * live cocycles.
			 */
			
			Set<T> cocycleKeySet = cocycles.keySet();
			
			for (T sigma_i: cocycleKeySet) {
				GenericFormalSum<F, T> cocycle = cocycles.get(sigma_i);
				
				F c_i = this.field.getZero();
				for (Entry<T, F> boundaryElementPair: boundary) {
					T boundaryObject = boundaryElementPair.getKey();
					F boundaryCoefficient = boundaryElementPair.getValue();
					
					if (cocycle.containsObject(boundaryObject)) {
						c_i = this.field.add(c_i, boundaryCoefficient);
					}
				}
				
				if (!this.field.isZero(c_i)) {
					liveCocycleCoefficients.put(sigma_i, c_i);
					
					if (sigma_j == null ||(this.compare(sigma_i, sigma_j, stream) > 0)) {
						sigma_j = sigma_i;
						c_j = c_i;
					}
				}
			}
			
			// destroy the boundary since we no longer need it 
			boundary = null;
			cocycleKeySet = null;
			
			if (liveCocycleCoefficients.isEmpty()) {
				// we have a new cocycle
				cocycles.put(sigma_k, new GenericFormalSum<F, T>(this.field.getOne(), sigma_k));
			} else {
				
				// kill the cocycle sigma_j
				GenericFormalSum<F, T> alpha_j = cocycles.get(sigma_j);
				
				for (T sigma_i: cocycles.keySet()) {
					if (this.comparator.compare(sigma_i, sigma_j) != 0) {
						GenericFormalSum<F, T> alpha_i = cocycles.get(sigma_i);
						F c_i = this.field.getZero();
						if (liveCocycleCoefficients.containsKey(sigma_i)) {
							c_i = liveCocycleCoefficients.get(sigma_i);
						}
						
						cocycles.put(sigma_i, this.chainModule.subtract(alpha_i, this.chainModule.multiply(this.field.divide(c_i, c_j), alpha_j)));
					}
				}
				
				cocycles.remove(sigma_j);
				
				// output the interval [a_j, a_k)
				double epsilon_j = stream.getFiltrationValue(sigma_j);
				double epsilon_k = stream.getFiltrationValue(sigma_k);
				if (epsilon_k != epsilon_j) {
					barcodeCollection.addInterval(stream.getDimension(sigma_j), epsilon_j, epsilon_k, alpha_j);
				}
			}
		}
		
		// output the remaining cocycles as semi-infinite intervals
		for (T sigma_i: cocycles.keySet()) {
			barcodeCollection.addRightInfiniteInterval(stream.getDimension(sigma_i), stream.getFiltrationValue(sigma_i), cocycles.get(sigma_i));
		}
		
		return barcodeCollection;
	}

	public AugmentedBarcodeCollection<GenericFormalSum<F, T>> computeIntervals(AbstractFilteredStream<T> stream) {
		return this.getAbsoluteHomologyIntervals(this.pHcol(stream), stream);
	}
	
	public AugmentedBarcodeCollection<GenericFormalSum<F, T>> getAbsoluteHomologyIntervals(GenericPair<THashMap<T, GenericFormalSum<F, T>>, THashMap<T, GenericFormalSum<F, T>>> RV_pair, AbstractFilteredStream<T> stream) {
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

	/**
	 * This function returns the columns of the boundary matrix of specified dimension.
	 * 
	 * @param stream the filtered chain complex
	 * @param dimension the dimension at which to get the boundary matrix
	 * @return the columns of the boundary matrix at the specified dimension
	 */
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

	
	
	private int compare(T a, T b, AbstractFilteredStream<T> stream) {
		return this.filteredComparator.compare(new DoubleGenericPair<T>(stream.getFiltrationValue(a), a), new DoubleGenericPair<T>(stream.getFiltrationValue(b), b));
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
