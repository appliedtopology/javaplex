package edu.stanford.math.plex_plus.homology.mapping_deprecated;

import java.util.Iterator;
import java.util.Map.Entry;

import edu.stanford.math.plex_plus.algebraic_structures.interfaces.GenericOrderedField;
import edu.stanford.math.plex_plus.algebraic_structures.interfaces.IntRing;
import edu.stanford.math.plex_plus.datastructures.GenericFormalSum;
import edu.stanford.math.plex_plus.datastructures.IntFormalSum;
import edu.stanford.math.plex_plus.datastructures.pairs.GenericPair;
import edu.stanford.math.plex_plus.functional.GenericFunction;
import edu.stanford.math.plex_plus.homology.chain_basis.PrimitiveBasisElement;
import edu.stanford.math.plex_plus.homology.chain_basis.Simplex;
import edu.stanford.math.plex_plus.homology.utility.HomologyUtility;
import edu.stanford.math.plex_plus.utility.ExceptionUtility;
import gnu.trove.iterator.TObjectIntIterator;

@Deprecated
public class MappingUtilityOld {
	public static <T, U> IntFormalSum<U> computeImage(IntFormalSum<GenericPair<T, U>> function, T element) {
		IntFormalSum<U> result = new IntFormalSum<U>();
		for (TObjectIntIterator<GenericPair<T, U>> iterator = function.iterator(); iterator.hasNext(); ) {
			iterator.advance();
			if (iterator.key().getFirst().equals(element)) {
				result.put(iterator.value(), iterator.key().getSecond());
			}
		}
		
		return result;
	}
	
	public static <F, T, U> GenericFormalSum<F, U> computeImage(GenericFormalSum<F, GenericPair<T, U>> function, T element) {
		GenericFormalSum<F, U> result = new GenericFormalSum<F, U>();
		for (Iterator<Entry<GenericPair<T, U>, F>> iterator = function.iterator(); iterator.hasNext(); ) {
			Entry<GenericPair<T, U>, F> entry = iterator.next();
			if (entry.getKey().getFirst().equals(element)) {
				result.put(entry.getValue(), entry.getKey().getSecond());
			}
		}
		
		return result;
	}
	
	public static <F, T, U> GenericFunction<T, GenericFormalSum<F, U>> toFunctional(final GenericFormalSum<F, GenericPair<T, U>> formalSum) {
		return new GenericFunction<T, GenericFormalSum<F, U>>() {

			@Override
			public GenericFormalSum<F, U> evaluate(T argument) {
				return computeImage(formalSum, argument);
			}
			
		};
	}
	
	public static <F, T> F norm(GenericFormalSum<F, T> chain, int p, GenericOrderedField<F> field) {
		ExceptionUtility.verifyNonNull(chain);
		ExceptionUtility.verifyNonNegative(p);
		
		if (p == 0) {
			return field.valueOf(chain.size());
		} else {
			F norm = field.getZero();
			for (Iterator<Entry<T, F>> iterator = chain.iterator(); iterator.hasNext(); ) {
				Entry<T, F> entry = iterator.next();
				norm = field.add(norm, field.power(field.abs(entry.getValue()), p));
			}
			return norm;
		}
	}
	
	public static <T extends PrimitiveBasisElement, U extends PrimitiveBasisElement> IntFormalSum<GenericPair<GenericPair<T, T>, GenericPair<U, U>>> functionTensorProduct(IntFormalSum<GenericPair<T, U>> f, IntFormalSum<GenericPair<T, U>> g) {
		IntFormalSum<GenericPair<GenericPair<T, T>, GenericPair<U, U>>> result = new IntFormalSum<GenericPair<GenericPair<T, T>, GenericPair<U, U>>>();
		
		for (TObjectIntIterator<GenericPair<T, U>> f_iterator = f.iterator(); f_iterator.hasNext(); ) {
			f_iterator.advance();
			for (TObjectIntIterator<GenericPair<T, U>> g_iterator = g.iterator(); g_iterator.hasNext(); ) {
				g_iterator.advance();
				GenericPair<T, T> source = new GenericPair<T, T>(f_iterator.key().getFirst(), g_iterator.key().getFirst());
				GenericPair<U, U> destination = new GenericPair<U, U>(f_iterator.key().getSecond(), g_iterator.key().getSecond());
				int coefficient = f_iterator.value() * g_iterator.value();
				result.put(coefficient, new GenericPair<GenericPair<T, T>, GenericPair<U, U>>(source, destination));
			}
		}
		
		return result;
	}
	
	public static IntFormalSum<GenericPair<Simplex, Simplex>> alexanderWhitneyMap(Simplex element) {
		IntFormalSum<GenericPair<Simplex, Simplex>> result = new IntFormalSum<GenericPair<Simplex, Simplex>>();
		int[] vertices = element.getVertices();
		
		for (int i = 0; i < vertices.length; i++) {
			result.put(1, new GenericPair<Simplex, Simplex>(new Simplex(HomologyUtility.lowerEntries(vertices, i)), new Simplex(HomologyUtility.upperEntries(vertices, i))));
		}
		return result;
	}
	
	/*
	public static IntFormalSum<TensorProductPair<Simplex, Simplex>> alexanderWhitneyMap(IntFormalSum<Simplex> element) {
		IntFormalSum<TensorProductPair<Simplex, Simplex>> result = new IntFormalSum<TensorProductPair<Simplex, Simplex>>();
		
	}
	*/
	
	/**
	 * This function computes the quantity
	 * |\Delta (f (\sigma)) - f x f (\Delta (\sigma))| 
	 * 
	 * @param function
	 * @param sigma
	 * @return
	 */
	/*
	public static int alexanderWhitneyNorm(IntFormalSum<GenericPair<Simplex, Simplex>> f, Simplex sigma) {
		IntFormalSum<TensorProductPair<Simplex, Simplex>> Delta_sigma = alexanderWhitneyMap(sigma);
		IntFormalSum<GenericPair<TensorProductPair<Simplex, Simplex>, TensorProductPair<Simplex, Simplex>>> f_tensor_f = functionTensorProduct(f, f);
		
		IntFormalSum<TensorProductPair<Simplex, Simplex>> term1 = alexanderWhitneyMap(computeImage(f, sigma));
	}
	*/
	public static <T> int chainCardinality(IntFormalSum<T> chain) {
		return chain.size();
	}
	
	public static <T> int norm(IntFormalSum<T> chain, int p, IntRing ring) {
		ExceptionUtility.verifyNonNull(chain);
		ExceptionUtility.verifyNonNegative(p);
		
		if (p == 0) {
			return chain.size();
		} else {
			int norm = 0;
			for (TObjectIntIterator<T> iterator = chain.iterator(); iterator.hasNext(); ) {
				iterator.advance();
				norm += ring.power(Math.abs(iterator.value()), p);
			}
			return norm;
		}
	}
	
	public static <T> int infinityNorm(IntFormalSum<T> chain) {
		int norm = 0;
		for (TObjectIntIterator<T> iterator = chain.iterator(); iterator.hasNext(); ) {
			iterator.advance();
			norm = Math.max(norm, iterator.value());
		}
		return norm;
	}
	
	
}
