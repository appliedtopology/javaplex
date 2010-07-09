package edu.stanford.math.plex_plus.homology;

import java.util.Comparator;
import java.util.Set;
import java.util.Map.Entry;

import edu.stanford.math.plex_plus.algebraic_structures.interfaces.GenericField;
import edu.stanford.math.plex_plus.datastructures.GenericFormalSum;
import edu.stanford.math.plex_plus.homology.barcodes.AugmentedBarcodeCollection;
import edu.stanford.math.plex_plus.homology.barcodes.BarcodeCollection;
import edu.stanford.math.plex_plus.homology.streams.interfaces.AbstractFilteredStream;
import gnu.trove.map.hash.THashMap;

public class GenericAbsoluteCohomology<F, T> extends GenericPersistenceAlgorithm<F, T> {

	public GenericAbsoluteCohomology(GenericField<F> field, Comparator<T> comparator, int minDimension, int maxDimension) {
		super(field, comparator, minDimension, maxDimension);
	}
	
	public GenericAbsoluteCohomology(GenericField<F> field, Comparator<T> comparator, int maxDimension) {
		super(field, comparator, 0, maxDimension);
	}

	@Override
	public AugmentedBarcodeCollection<GenericFormalSum<F, T>> computeAugmentedIntervals(AbstractFilteredStream<T> stream) {
		return this.pCohAugmented(stream);
	}

	@Override
	public BarcodeCollection computeIntervals(AbstractFilteredStream<T> stream) {
		return this.pCoh(stream);
	}

	/**
	 * This function implements the pCoh algorithm described in the paper.
	 * 
	 * @param stream
	 * @param maxDimension
	 * @return
	 */
	public AugmentedBarcodeCollection<GenericFormalSum<F, T>> pCohAugmented(AbstractFilteredStream<T> stream) {
		AugmentedBarcodeCollection<GenericFormalSum<F, T>> barcodeCollection = new AugmentedBarcodeCollection<GenericFormalSum<F, T>>();

		THashMap<T, GenericFormalSum<F, T>> cocycles = new THashMap<T, GenericFormalSum<F, T>>();
		
		for (T sigma_k : stream) {
			/*
			 * Do not process simplices of higher dimension than maxDimension.
			 */
			if (stream.getDimension(sigma_k) > this.maxDimension || stream.getDimension(sigma_k) < this.minDimension) {
				break;
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
	
	public BarcodeCollection pCoh(AbstractFilteredStream<T> stream) {
		BarcodeCollection barcodeCollection = new BarcodeCollection();

		THashMap<T, GenericFormalSum<F, T>> cocycles = new THashMap<T, GenericFormalSum<F, T>>();
		
		for (T sigma_k : stream) {
			/*
			 * Do not process simplices of higher dimension than maxDimension.
			 */
			if (stream.getDimension(sigma_k) > this.maxDimension || stream.getDimension(sigma_k) < this.minDimension) {
				break;
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
					
					if (sigma_j == null || (this.compare(sigma_i, sigma_j, stream) > 0)) {
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
				
				for (T sigma_i: liveCocycleCoefficients.keySet()) {
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
					barcodeCollection.addInterval(stream.getDimension(sigma_j), epsilon_j, epsilon_k);
				}
			}
		}
		
		// output the remaining cocycles as semi-infinite intervals
		for (T sigma_i: cocycles.keySet()) {
			barcodeCollection.addRightInfiniteInterval(stream.getDimension(sigma_i), stream.getFiltrationValue(sigma_i));
		}
		
		return barcodeCollection;
	}
}
