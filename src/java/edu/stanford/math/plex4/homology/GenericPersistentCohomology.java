package edu.stanford.math.plex4.homology;

import java.util.Comparator;
import java.util.Map;
import java.util.Set;

import edu.stanford.math.plex4.algebraic_structures.interfaces.GenericField;
import edu.stanford.math.plex4.datastructures.pairs.GenericPair;
import edu.stanford.math.plex4.free_module.AbstractGenericFormalSum;
import edu.stanford.math.plex4.homology.barcodes.AugmentedBarcodeCollection;
import edu.stanford.math.plex4.homology.barcodes.BarcodeCollection;
import edu.stanford.math.plex4.homology.streams.interfaces.AbstractFilteredStream;
import gnu.trove.THashMap;
import gnu.trove.THashSet;

public abstract class GenericPersistentCohomology<F, T> extends GenericPersistenceAlgorithm<F, T> {
	public GenericPersistentCohomology(GenericField<F> field, Comparator<T> comparator, int minDimension, int maxDimension) {
		super(field, comparator, minDimension, maxDimension);
		// TODO Auto-generated constructor stub
	}

	public GenericPersistentCohomology(GenericField<F> field, Comparator<T> comparator, int maxDimension) {
		super(field, comparator, 0, maxDimension);
		// TODO Auto-generated constructor stub
	}

	@Override
	public AugmentedBarcodeCollection<AbstractGenericFormalSum<F, T>> computeAugmentedIntervalsImpl(AbstractFilteredStream<T> stream) {
		return this.getAugmentedIntervals(this.pHrow(stream), stream);
	}

	@Override
	public BarcodeCollection computeIntervalsImpl(AbstractFilteredStream<T> stream) {
		return this.getIntervals(this.pHrow(stream), stream);
	}
	
	private GenericPair<THashMap<T, AbstractGenericFormalSum<F, T>>, THashMap<T, AbstractGenericFormalSum<F, T>>> pHrow(AbstractFilteredStream<T> stream) {

		THashMap<T, AbstractGenericFormalSum<F, T>> R_perp = new THashMap<T, AbstractGenericFormalSum<F, T>>();
		THashMap<T, AbstractGenericFormalSum<F, T>> V_perp = new THashMap<T, AbstractGenericFormalSum<F, T>>();
		
		THashSet<T> dead_cocycles = new THashSet<T>();
		
		for (T i: stream) {
			/*
			 * Do not process simplices of higher dimension than maxDimension.
			 */
			if (stream.getDimension(i) < this.minDimension) {
				continue;
			}
			
			if (stream.getDimension(i) > this.maxDimension + 1) {
				break;
			}

			// initialize V to be the identity matrix
			V_perp.put(i, this.chainModule.createNewSum(this.field.valueOf(1), i));

			// form the row R^perp[i] which equals the boundary of the current simplex.
			AbstractGenericFormalSum<F, T> boundary = chainModule.createSum(stream.getBoundaryCoefficients(i), stream.getBoundary(i));
			//R_perp.put(i, boundary);
			
			if (boundary.size() == 0) {
				continue;
			}
			
			T pivot = this.low(boundary);
			F pivot_coefficient = boundary.getCoefficient(pivot);
			
			for (Map.Entry<T, F> boundary_entry: boundary) {
				T boundary_element = boundary_entry.getKey();
				// skip the pivot element and dead cocycles
				if (boundary_element.equals(pivot) || dead_cocycles.contains(boundary_element)) {
					continue;
				}
				F negative_c = this.field.negate(this.field.divide(boundary_entry.getValue(), pivot_coefficient));
				this.chainModule.accumulate(V_perp.get(boundary_element), V_perp.get(pivot), negative_c);
				//this.chainModule.accumulate(R_perp.get(boundary_element), V_perp.get(pivot), negative_c);
				dead_cocycles.add(boundary_element);
			}
			//dead_cocycles.add(pivot);
			/*
			for (Map.Entry<T, F> element: V_perp.get(pivot)) {
				dead_cocycles.add(element.getKey());
			}
			*/
			
			if (pivot != null) {
				//R_perp.get(pivot, this.chainModule.createNewSum(pivot_coefficient, i));
				if(R_perp.containsKey(pivot)) {
					this.chainModule.accumulate(R_perp.get(pivot), this.chainModule.createNewSum(pivot_coefficient, i));
				} else {
					R_perp.put(pivot, this.chainModule.createNewSum(pivot_coefficient, i));
				}
			}
		}
		GenericPair<THashMap<T, AbstractGenericFormalSum<F, T>>, THashMap<T, AbstractGenericFormalSum<F, T>>> pair = new GenericPair<THashMap<T, AbstractGenericFormalSum<F, T>>, THashMap<T, AbstractGenericFormalSum<F, T>>>(R_perp, V_perp);
		if (!this.verifyDecomposition(pair, stream)) {
			System.out.println("VERIFICATION FAILED!!!!");
		} else {
			System.out.println("VERIFICATION SUCCEEDED!!!!");
		}
		return pair;
	}
	
	protected abstract AugmentedBarcodeCollection<AbstractGenericFormalSum<F, T>> getAugmentedIntervals(GenericPair<THashMap<T, AbstractGenericFormalSum<F, T>>, THashMap<T, AbstractGenericFormalSum<F, T>>> RV_pair, AbstractFilteredStream<T> stream);

	protected abstract BarcodeCollection getIntervals(GenericPair<THashMap<T, AbstractGenericFormalSum<F, T>>, THashMap<T, AbstractGenericFormalSum<F, T>>> RV_pair, AbstractFilteredStream<T> stream);

	protected AugmentedBarcodeCollection<AbstractGenericFormalSum<F, T>> getAugmentedIntervals(
			GenericPair<THashMap<T, AbstractGenericFormalSum<F, T>>, THashMap<T, AbstractGenericFormalSum<F, T>>> RV_pair, AbstractFilteredStream<T> stream, boolean absolute) {
		AugmentedBarcodeCollection<AbstractGenericFormalSum<F, T>> barcodeCollection = new AugmentedBarcodeCollection<AbstractGenericFormalSum<F, T>>();

		THashMap<T, AbstractGenericFormalSum<F, T>> R_perp = RV_pair.getFirst();
		THashMap<T, AbstractGenericFormalSum<F, T>> V_perp = RV_pair.getSecond();

		Set<T> births = new THashSet<T>();
		Set<T> deaths = new THashSet<T>();
		
		AbstractGenericFormalSum<F, T> R_i = null;
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
			GenericPair<THashMap<T, AbstractGenericFormalSum<F, T>>, THashMap<T, AbstractGenericFormalSum<F, T>>> RV_pair, AbstractFilteredStream<T> stream, boolean absolute) {
		BarcodeCollection barcodeCollection = new BarcodeCollection();

		THashMap<T, AbstractGenericFormalSum<F, T>> R_perp = RV_pair.getFirst();

		Set<T> births = new THashSet<T>();
		Set<T> deaths = new THashSet<T>();
		
		AbstractGenericFormalSum<F, T> R_i = null;
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
	
	protected boolean verifyDecomposition(GenericPair<THashMap<T, AbstractGenericFormalSum<F, T>>, THashMap<T, AbstractGenericFormalSum<F, T>>> RV_pair, AbstractFilteredStream<T> stream) {
		THashMap<T, AbstractGenericFormalSum<F, T>> R_perp = RV_pair.getFirst();
		THashMap<T, AbstractGenericFormalSum<F, T>> V_perp = RV_pair.getSecond();
		
		for (T i: stream) {
			AbstractGenericFormalSum<F, T> D_row = chainModule.createSum(stream.getBoundaryCoefficients(i), stream.getBoundary(i));
			for (T j: stream) {
				AbstractGenericFormalSum<F, T> V_col = V_perp.get(j);
				F product_entry = this.chainModule.innerProduct(D_row, V_col);
				F R_entry;
				if (R_perp.contains(j)) {
					R_entry = R_perp.get(j).getCoefficient(i);
					if (R_entry == null) {
						R_entry = this.field.getZero();
					}
				} else {
					R_entry = this.field.getZero();
				}
				if (!R_entry.equals(product_entry)) {
					return false;
				}
			}
		}
		
		return true;
	}
}
