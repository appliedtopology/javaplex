package edu.stanford.math.plex4.homology;

import java.util.Comparator;
import java.util.Set;
import java.util.Map.Entry;

import edu.stanford.math.plex4.algebraic_structures.interfaces.GenericField;
import edu.stanford.math.plex4.free_module.AbstractGenericFormalSum;
import edu.stanford.math.plex4.homology.barcodes.AugmentedBarcodeCollection;
import edu.stanford.math.plex4.homology.barcodes.BarcodeCollection;
import edu.stanford.math.plex4.homology.streams.interfaces.AbstractFilteredStream;
import gnu.trove.map.hash.THashMap;

public class GenericAbsoluteCohomology<F, T> extends GenericPersistenceAlgorithm<F, T> {

	public GenericAbsoluteCohomology(GenericField<F> field, Comparator<T> comparator, int minDimension, int maxDimension) {
		super(field, comparator, minDimension, maxDimension);
	}
	
	public GenericAbsoluteCohomology(GenericField<F> field, Comparator<T> comparator, int maxDimension) {
		super(field, comparator, 0, maxDimension);
	}

	@Override
	public AugmentedBarcodeCollection<AbstractGenericFormalSum<F, T>> computeAugmentedIntervalsImpl(AbstractFilteredStream<T> stream) {
		return this.pCohAugmented(stream);
	}

	@Override
	public BarcodeCollection computeIntervalsImpl(AbstractFilteredStream<T> stream) {
		return this.pCoh(stream);
	}

	/**
	 * This function implements the pCoh algorithm described in the paper.
	 * 
	 * @param stream
	 * @param maxDimension
	 * @return
	 */
	public AugmentedBarcodeCollection<AbstractGenericFormalSum<F, T>> pCohAugmented(AbstractFilteredStream<T> stream) {
		AugmentedBarcodeCollection<AbstractGenericFormalSum<F, T>> barcodeCollection = new AugmentedBarcodeCollection<AbstractGenericFormalSum<F, T>>();

		THashMap<T, AbstractGenericFormalSum<F, T>> cocycles = new THashMap<T, AbstractGenericFormalSum<F, T>>();
		
		for (T sigma_k : stream) {
			/*
			 * Do not process simplices of higher dimension than maxDimension.
			 */
			if (stream.getDimension(sigma_k) > this.maxDimension + 1 || stream.getDimension(sigma_k) < this.minDimension) {
				break;
			}
			
			AbstractGenericFormalSum<F, T> boundary = chainModule.createSum(stream.getBoundaryCoefficients(sigma_k), stream.getBoundary(sigma_k));
			
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
				AbstractGenericFormalSum<F, T> cocycle = cocycles.get(sigma_i);
				
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
				AbstractGenericFormalSum<F, T> alpha_j = cocycles.get(sigma_j);
				
				for (T sigma_i: cocycles.keySet()) {
					if (this.basisComparator.compare(sigma_i, sigma_j) != 0) {
						AbstractGenericFormalSum<F, T> alpha_i = cocycles.get(sigma_i);
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
				int dimension = stream.getDimension(sigma_j);
				if (epsilon_k >= epsilon_j + this.minGranularity && (dimension >= this.minDimension) && (dimension <= this.maxDimension)) {
					barcodeCollection.addInterval(dimension, epsilon_j, epsilon_k, alpha_j);
				}
			}
		}
		
		// output the remaining cocycles as semi-infinite intervals
		for (T sigma_i: cocycles.keySet()) {
			int dimension = stream.getDimension(sigma_i);
			if ((dimension >= this.minDimension) && (dimension <= this.maxDimension)) {
				barcodeCollection.addRightInfiniteInterval(dimension, stream.getFiltrationValue(sigma_i), cocycles.get(sigma_i));
			}
		}
		
		return barcodeCollection;
	}
	
	public BarcodeCollection pCoh2(AbstractFilteredStream<T> stream) {
		BarcodeCollection barcodeCollection = new BarcodeCollection();

		THashMap<T, AbstractGenericFormalSum<F, T>> cocycles = new THashMap<T, AbstractGenericFormalSum<F, T>>();
		//THashSet<T> liveElements = new THashSet<T>();
		
		for (T sigma_k : stream) {
			/*
			 * Do not process simplices of higher dimension than maxDimension.
			 */
			if (stream.getDimension(sigma_k) > this.maxDimension + 1 || stream.getDimension(sigma_k) < this.minDimension) {
				break;
			}
			
			AbstractGenericFormalSum<F, T> boundary = chainModule.createSum(stream.getBoundaryCoefficients(sigma_k), stream.getBoundary(sigma_k));
			
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
			
			for (Entry<T, F> boundaryElementPair: boundary) {
				T boundaryObject = boundaryElementPair.getKey();
				F boundaryCoefficient = boundaryElementPair.getValue();
				
				for (T cocycleKey: cocycles.keySet()) {
					AbstractGenericFormalSum<F, T> cocycle = cocycles.get(cocycleKey);
					if (cocycle.containsObject(boundaryObject)) {
						liveCocycleCoefficients.put(cocycleKey, boundaryCoefficient);
						
						if (sigma_j == null || (this.filteredComparator.compare(sigma_j, cocycleKey) < 0)) {
							sigma_j = cocycleKey;
							c_j = boundaryCoefficient;
						}
					}
					
					
				}
			}
			
			// destroy the boundary since we no longer need it 
			boundary = null;

			if (liveCocycleCoefficients.isEmpty()) {
				// we have a new cocycle
				cocycles.put(sigma_k, this.chainModule.createNewSum(this.field.getOne(), sigma_k));
			} else {
				
				// kill the cocycle sigma_j
				AbstractGenericFormalSum<F, T> alpha_j = cocycles.get(sigma_j);
				
				for (T sigma_i: liveCocycleCoefficients.keySet()) {
					if (this.basisComparator.compare(sigma_i, sigma_j) != 0) {
						AbstractGenericFormalSum<F, T> alpha_i = cocycles.get(sigma_i);
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
				int dimension = stream.getDimension(sigma_j);
				if (epsilon_k >= epsilon_j + this.minGranularity && (dimension >= this.minDimension) && (dimension <= this.maxDimension)) {
					barcodeCollection.addInterval(dimension, epsilon_j, epsilon_k);
				}
			}
		}
		
		return barcodeCollection;
	}
	
	public BarcodeCollection pCoh(AbstractFilteredStream<T> stream) {
		BarcodeCollection barcodeCollection = new BarcodeCollection();

		THashMap<T, AbstractGenericFormalSum<F, T>> cocycles = new THashMap<T, AbstractGenericFormalSum<F, T>>();
		
		for (T sigma_k : stream) {
			/*
			 * Do not process simplices of higher dimension than maxDimension.
			 */
			if (stream.getDimension(sigma_k) > this.maxDimension + 1 || stream.getDimension(sigma_k) < this.minDimension) {
				break;
			}
			
			AbstractGenericFormalSum<F, T> boundary = chainModule.createSum(stream.getBoundaryCoefficients(sigma_k), stream.getBoundary(sigma_k));
			
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
				AbstractGenericFormalSum<F, T> cocycle = cocycles.get(sigma_i);
				
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
				AbstractGenericFormalSum<F, T> alpha_j = cocycles.get(sigma_j);
				
				for (T sigma_i: liveCocycleCoefficients.keySet()) {
					if (this.basisComparator.compare(sigma_i, sigma_j) != 0) {
						AbstractGenericFormalSum<F, T> alpha_i = cocycles.get(sigma_i);
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
}
