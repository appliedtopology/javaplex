package edu.stanford.math.plex4.homology;

import java.util.Comparator;
import java.util.Iterator;
import java.util.Set;
import java.util.Map.Entry;

import edu.stanford.math.plex4.homology.barcodes.AugmentedBarcodeCollection;
import edu.stanford.math.plex4.homology.barcodes.BarcodeCollection;
import edu.stanford.math.plex4.homology.streams.interfaces.AbstractFilteredStream;
import edu.stanford.math.primitivelib.autogen.algebraic.ObjectAbstractField;
import edu.stanford.math.primitivelib.autogen.formal_sum.ObjectSparseFormalSum;
import edu.stanford.math.primitivelib.autogen.pair.ObjectObjectPair;
import gnu.trove.THashMap;
import gnu.trove.THashSet;

public abstract class GenericPersistentCohomology<F, T> extends GenericPersistenceAlgorithm<F, T> {
	public GenericPersistentCohomology(ObjectAbstractField<F> field, Comparator<T> comparator, int minDimension, int maxDimension) {
		super(field, comparator, minDimension, maxDimension);
		// TODO Auto-generated constructor stub
	}

	public GenericPersistentCohomology(ObjectAbstractField<F> field, Comparator<T> comparator, int maxDimension) {
		super(field, comparator, 0, maxDimension);
		// TODO Auto-generated constructor stub
	}

	@Override
	public AugmentedBarcodeCollection<ObjectSparseFormalSum<F, T>> computeAugmentedIntervalsImpl(AbstractFilteredStream<T> stream) {
		//return this.getAugmentedIntervals(this.pHrow(stream), stream);
		return null;
	}

	@Override
	public BarcodeCollection computeIntervalsImpl(AbstractFilteredStream<T> stream) {
		//return this.getIntervals(this.pHrow(stream), stream);
		return this.pCoh(stream);
	}
	
	public BarcodeCollection pCoh(AbstractFilteredStream<T> stream) {
		BarcodeCollection barcodeCollection = new BarcodeCollection();

		THashMap<T, ObjectSparseFormalSum<F, T>> cocycles = new THashMap<T, ObjectSparseFormalSum<F, T>>();

		for (T sigma_k : stream) {
			//Do not process simplices of higher dimension than maxDimension.
			if (stream.getDimension(sigma_k) < this.minDimension) {
				continue;
			}

			if (stream.getDimension(sigma_k) > this.maxDimension + 1) {
				break;
			}

			ObjectSparseFormalSum<F, T> boundary = chainModule.createNewSum(stream.getBoundaryCoefficients(sigma_k), stream.getBoundary(sigma_k));

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

			THashMap<T, F> cocycleCoefficients = new THashMap<T, F>();
			
			/*
			for (Entry<T, F> boundaryElementPair: boundary) {
				T boundaryObject = boundaryElementPair.getKey();
				F boundaryCoefficient = boundaryElementPair.getValue();
				if (cocycleKeySet.contains(boundaryObject)) {
					if (sigma_j == null || (this.filteredComparator.compare(boundaryObject, sigma_j) > 0)) {
						sigma_j = boundaryObject;
						c_j = boundaryCoefficient;
					}
				}
			}*/
			
			//System.out.println("Cocycle set size: " + cocycles.size());
			
			for (T sigma_i: cocycleKeySet) {
				ObjectSparseFormalSum<F, T> cocycle = cocycles.get(sigma_i);

				F c_i = this.field.getZero();
				for (Iterator<Entry<T, F>> iterator = boundary.iterator(); iterator.hasNext(); ) {
					Entry<T, F> boundaryElementPair  = iterator.next();
					T boundaryObject = boundaryElementPair.getKey();
					F boundaryCoefficient = boundaryElementPair.getValue();

					if (cocycle.containsObject(boundaryObject)) {
						c_i = this.field.add(c_i, this.field.multiply(boundaryCoefficient, cocycle.getCoefficient(boundaryObject)));
					}
				}

				if (!this.field.isZero(c_i)) {
					liveCocycleCoefficients.put(sigma_i, c_i);

					if (sigma_j == null || (this.filteredComparator.compare(sigma_i, sigma_j) > 0)) {
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
				cocycles.put(sigma_k, this.chainModule.createNewSum(this.field.getOne(), sigma_k));
			} else {

				// kill the cocycle sigma_j
				ObjectSparseFormalSum<F, T> alpha_j = cocycles.get(sigma_j);

				for (T sigma_i: liveCocycleCoefficients.keySet()) {
					if (this.basisComparator.compare(sigma_i, sigma_j) != 0) {
						ObjectSparseFormalSum<F, T> alpha_i = cocycles.get(sigma_i);
						F c_i = this.field.getZero();
						if (liveCocycleCoefficients.containsKey(sigma_i)) {
							c_i = liveCocycleCoefficients.get(sigma_i);
						}
						cocycles.remove(sigma_i);
						cocycles.put(sigma_i, this.chainModule.subtract(alpha_i, this.chainModule.multiply(this.field.divide(c_i, c_j), alpha_j)));
					}
				}

				cocycles.remove(sigma_j);

				// output the interval [a_j, a_k)
				double epsilon_j = stream.getFiltrationValue(sigma_j);
				double epsilon_k = stream.getFiltrationValue(sigma_k);
				int dimension = stream.getDimension(sigma_j);
				if (epsilon_k >= epsilon_j + this.minGranularity && (dimension >= this.minDimension) && (dimension <= this.maxDimension)) {
					barcodeCollection.addInterval(dimension, epsilon_j, epsilon_k);
				}
			}
		}

		// output the remaining cocycles as semi-infinite intervals
		for (T sigma_i: cocycles.keySet()) {
			int dimension = stream.getDimension(sigma_i);
			if ((dimension >= this.minDimension) && (dimension <= this.maxDimension)) {
				barcodeCollection.addRightInfiniteInterval(dimension, stream.getFiltrationValue(sigma_i));
			}
		}

		return barcodeCollection;
	}

	/*
	private BarcodeCollection pCoh(AbstractFilteredStream<T> stream) {
		THashSet<T> marked = new THashSet<T>();
		List<ObjectSparseFormalSum<F, T>> Z_perp = new ArrayList<ObjectSparseFormalSum<F, T>>();
		List<T> birth = new ArrayList<T>();

		for (T i: stream) {

			//Do not process simplices of higher dimension than maxDimension.
			if (stream.getDimension(i) < this.minDimension) {
				continue;
			}

			if (stream.getDimension(i) > this.maxDimension + 1) {
				break;
			}

			ObjectSparseFormalSum<F, T> boundary = chainModule.createSum(stream.getBoundaryCoefficients(i), stream.getBoundary(i))

			THashSet<T> candidates = new THashSet<T>();
			for (Map.Entry<T, F> boundary_entry: boundary) {
				T boundary_element = boundary_entry.getKey();
				F boundary_coefficient = boundary_entry.getValue();
				if (!marked.contains(boundary_element)) {
					candidates.add(boundary_element);
				}
			}

			if (candidates.isEmpty()) {
				Z_perp.add(0, chainModule.createNewSum(field.getOne(), i));
				birth.add(0, i);
			} else {

			}
		}

		return null;
	}
	/*
	private ObjectObjectPair<THashMap<T, ObjectSparseFormalSum<F, T>>, THashMap<T, ObjectSparseFormalSum<F, T>>> pHrow(AbstractFilteredStream<T> stream) {

		THashMap<T, ObjectSparseFormalSum<F, T>> R_perp = new THashMap<T, ObjectSparseFormalSum<F, T>>();
		THashMap<T, ObjectSparseFormalSum<F, T>> V_perp = new THashMap<T, ObjectSparseFormalSum<F, T>>();

		THashSet<T> dead_columns = new THashSet<T>();

		THashMap<T, ObjectObjectPair<F, T>> eliminations = new THashMap<T, ObjectObjectPair<F, T>>();

		for (T i: stream) {

			// Do not process simplices of higher dimension than maxDimension.
			if (stream.getDimension(i) < this.minDimension) {
				continue;
			}

			if (stream.getDimension(i) > this.maxDimension + 1) {
				break;
			}

			// initialize V to be the identity matrix
			V_perp.put(i, this.chainModule.createNewSum(this.field.valueOf(1), i));

			// form the row R^perp[i] which equals the boundary of the current simplex.
			ObjectSparseFormalSum<F, T> boundary = chainModule.createSum(stream.getBoundaryCoefficients(i), stream.getBoundary(i));
			//R_perp.put(i, boundary);

			if (boundary.size() == 0) {
				continue;
			}

			T pivot = this.low(boundary);
			F pivot_coefficient = boundary.getCoefficient(pivot);

			if (dead_columns.contains(pivot)) {
				ObjectSparseFormalSum<F, T> new_boundary = chainModule.createNewSum();
				// perform elimination
				for (Map.Entry<T, F> boundary_entry: boundary) {
					T boundary_element = boundary_entry.getKey();
					F boundary_coefficient = boundary_entry.getValue();
					// skip the pivot element
					if (boundary_element.equals(pivot)) {
						this.chainModule.accumulate(new_boundary, boundary_element, boundary_coefficient);
						continue;
					}
					if (eliminations.containsKey(boundary_element)) {
						//this.chainModule.addObject(boundary, eliminations.get(boundary_element), boundary_element);
						//F new_coefficient = field.multiply(a, b)

						this.chainModule.accumulate(new_boundary, boundary_element, field.multiply(eliminations.get(boundary_element), pivot_coefficient));
					} else {
						this.chainModule.accumulate(new_boundary, boundary_element, boundary_coefficient);
					}
				}

				for (Map.Entry<T, F> boundary_entry: new_boundary) {
					T boundary_element = boundary_entry.getKey();
					F boundary_coefficient = boundary_entry.getValue();
					if (!R_perp.containsKey(boundary_element)) {
						R_perp.put(boundary_element, chainModule.createNewSum());
					}
					this.chainModule.accumulate(R_perp.get(boundary_element), i, boundary_coefficient);
				}

				continue;
			} else {
				for (Map.Entry<T, F> boundary_entry: boundary) {
					T boundary_element = boundary_entry.getKey();
					// skip the pivot element and dead cocycles
					if (boundary_element.equals(pivot)) {
						continue;
					}
					F c = this.field.divide(boundary_entry.getValue(), pivot_coefficient);
					F negative_c = this.field.negate(c);
					this.chainModule.accumulate(V_perp.get(boundary_element), V_perp.get(pivot), negative_c);
					if (!eliminations.containsKey(boundary_element)) {
						eliminations.put(boundary_element, field.getZero());
					}
					eliminations.put(boundary_element, field.add(negative_c, eliminations.get(boundary_element)));
				}
				dead_columns.add(pivot);
				if (!R_perp.containsKey(pivot)) {
					R_perp.put(pivot, chainModule.createNewSum());
				}
				this.chainModule.accumulate(R_perp.get(pivot), i, pivot_coefficient);
			}
		}

		ModuleMorphismRepresentation<F, T, T> rep = new ModuleMorphismRepresentation<F, T,T>(stream, stream);
		System.out.println(ArrayPrinting.toString(rep.toMatrix(R_perp, this.field.getZero())));

		ObjectObjectPair<THashMap<T, ObjectSparseFormalSum<F, T>>, THashMap<T, ObjectSparseFormalSum<F, T>>> pair = new ObjectObjectPair<THashMap<T, ObjectSparseFormalSum<F, T>>, THashMap<T, ObjectSparseFormalSum<F, T>>>(R_perp, V_perp);
		if (!this.verifyDecomposition(pair, stream)) {
			System.out.println("VERIFICATION FAILED!!!!");
		} else {
			System.out.println("VERIFICATION SUCCEEDED!!!!");
		}
		return pair;
	}*/

	protected abstract AugmentedBarcodeCollection<ObjectSparseFormalSum<F, T>> getAugmentedIntervals(ObjectObjectPair<THashMap<T, ObjectSparseFormalSum<F, T>>, THashMap<T, ObjectSparseFormalSum<F, T>>> RV_pair, AbstractFilteredStream<T> stream);

	protected abstract BarcodeCollection getIntervals(ObjectObjectPair<THashMap<T, ObjectSparseFormalSum<F, T>>, THashMap<T, ObjectSparseFormalSum<F, T>>> RV_pair, AbstractFilteredStream<T> stream);

	protected AugmentedBarcodeCollection<ObjectSparseFormalSum<F, T>> getAugmentedIntervals(
			ObjectObjectPair<THashMap<T, ObjectSparseFormalSum<F, T>>, THashMap<T, ObjectSparseFormalSum<F, T>>> RV_pair, AbstractFilteredStream<T> stream, boolean absolute) {
		AugmentedBarcodeCollection<ObjectSparseFormalSum<F, T>> barcodeCollection = new AugmentedBarcodeCollection<ObjectSparseFormalSum<F, T>>();

		THashMap<T, ObjectSparseFormalSum<F, T>> R_perp = RV_pair.getFirst();
		THashMap<T, ObjectSparseFormalSum<F, T>> V_perp = RV_pair.getSecond();

		Set<T> births = new THashSet<T>();
		Set<T> deaths = new THashSet<T>();

		ObjectSparseFormalSum<F, T> R_i = null;
		T low_R_perp_i = null;
		for (T i: stream) {
			if (deaths.contains(i)) {
				continue;
			}
			if (!R_perp.containsKey(i)) {
				R_i = null;
				low_R_perp_i = null;
			} else {
				R_i = R_perp.get(i);
				low_R_perp_i = this.high(R_i);
			}
			int dimension = stream.getDimension(i);
			if (low_R_perp_i == null) {
				if (dimension <= this.maxDimension && dimension >= this.minDimension) {
					births.add(i);
				}
			} else {
				deaths.add(low_R_perp_i);
				double end = stream.getFiltrationValue(low_R_perp_i);
				double start = stream.getFiltrationValue(i);
				if (end >= start + this.minGranularity) {
					if (absolute) {
						barcodeCollection.addInterval(stream.getDimension(i), start, end, V_perp.get(i));
					} else {
						barcodeCollection.addInterval(stream.getDimension(low_R_perp_i), start, end, R_i);
					}
				}
			}
		}

		// the elements in birth are the ones that are never killed
		// these correspond to semi-infinite intervals
		for (T i: births) {
			if (deaths.contains(i)) {
				continue;
			}
			if (absolute) {
				barcodeCollection.addRightInfiniteInterval(stream.getDimension(i), stream.getFiltrationValue(i), V_perp.get(i));
			} else {
				barcodeCollection.addLeftInfiniteInterval(stream.getDimension(i), stream.getFiltrationValue(i), V_perp.get(i));
			}
		}

		return barcodeCollection;
	}

	protected BarcodeCollection getIntervals(
			ObjectObjectPair<THashMap<T, ObjectSparseFormalSum<F, T>>, THashMap<T, ObjectSparseFormalSum<F, T>>> RV_pair, AbstractFilteredStream<T> stream, boolean absolute) {
		BarcodeCollection barcodeCollection = new BarcodeCollection();

		THashMap<T, ObjectSparseFormalSum<F, T>> R_perp = RV_pair.getFirst();

		Set<T> births = new THashSet<T>();
		Set<T> deaths = new THashSet<T>();

		ObjectSparseFormalSum<F, T> R_i = null;
		T low_R_perp_i = null;
		for (T i: stream) {
			if (deaths.contains(i)) {
				continue;
			}
			if (!R_perp.containsKey(i)) {
				R_i = null;
				low_R_perp_i = null;
			} else {
				R_i = R_perp.get(i);
				low_R_perp_i = this.high(R_i);
			}
			int dimension = stream.getDimension(i);
			if (low_R_perp_i == null) {
				if (dimension <= this.maxDimension && dimension >= this.minDimension) {
					births.add(i);
				}
			} else {
				deaths.add(low_R_perp_i);
				deaths.add(i);
				double end = stream.getFiltrationValue(low_R_perp_i);
				double start = stream.getFiltrationValue(i);
				if (end >= start + this.minGranularity) {
					if (absolute) {
						barcodeCollection.addInterval(stream.getDimension(i), start, end);
					} else {
						barcodeCollection.addInterval(stream.getDimension(low_R_perp_i), start, end);
					}
				}
			}
		}

		// the elements in birth are the ones that are never killed
		// these correspond to semi-infinite intervals
		for (T i: births) {
			if (deaths.contains(i)) {
				continue;
			}
			if (absolute) {
				barcodeCollection.addRightInfiniteInterval(stream.getDimension(i), stream.getFiltrationValue(i));
			} else {
				barcodeCollection.addLeftInfiniteInterval(stream.getDimension(i), stream.getFiltrationValue(i));
			}
		}

		return barcodeCollection;
	}
}
