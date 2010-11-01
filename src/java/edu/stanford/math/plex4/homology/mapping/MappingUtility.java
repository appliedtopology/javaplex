package edu.stanford.math.plex4.homology.mapping;

import edu.stanford.math.plex4.homology.chain_basis.Simplex;
import edu.stanford.math.plex4.homology.utility.HomologyUtility;
import edu.stanford.math.primitivelib.autogen.algebraic.DoubleAbstractModule;
import edu.stanford.math.primitivelib.autogen.formal_sum.DoubleSparseFormalSum;
import edu.stanford.math.primitivelib.autogen.functional.ObjectDoubleFunction;
import edu.stanford.math.primitivelib.autogen.functional.ObjectObjectFunction;
import edu.stanford.math.primitivelib.autogen.pair.ObjectObjectPair;
import gnu.trove.THashMap;
import gnu.trove.TObjectDoubleHashMap;
import gnu.trove.TObjectDoubleIterator;

public class MappingUtility {
	

	/**
	 * This function computes the pushforward of a functional on the free module through linear transformation. Suppose
	 * we have a functional phi: X -> double, and a linear transformation defined on the basis T: X - > Y.
	 * The pushforward of phi through the map T, is a new functional psi: Y -> double, defined by psi(y) = phi(T^{-1}(y)).
	 * 
	 * @param <X> the type of the basis for the domain
	 * @param <Y> the type of the basis for the codomain
	 * @param mapping the linear tranformation defined on the set of basis elements of type X
	 * @param functional the functional mapping X to the real numbers (which in this case we represent as doubles)
	 * @param domainBasis an Iterable collection of basis elements of the domain 
	 * 
	 * @return a function object which computes the pushforward of the supplied map
	 */
	public static <X, Y> ObjectDoubleFunction<Y> pushforward(final ObjectObjectFunction<X, DoubleSparseFormalSum<Y>> mapping,
			final ObjectDoubleFunction<X> functional, 
			final Iterable<X> domainBasis) {

		return new ObjectDoubleFunction<Y>() {
			private TObjectDoubleHashMap<Y> functionalMap = new TObjectDoubleHashMap<Y>();

			{	
				// instance initializer for anonymous class
				for (X domainElement: domainBasis) {
					DoubleSparseFormalSum<Y> image = mapping.evaluate(domainElement);

					for (TObjectDoubleIterator<Y> iterator = image.iterator(); iterator.hasNext(); ) {
						iterator.advance();

						double coefficient = iterator.value();
						Y imageElement = iterator.key();
						double increment = coefficient * functional.evaluate(domainElement);

						if (!this.functionalMap.containsKey(imageElement)) {
							this.functionalMap.put(imageElement, increment);
						} else {
							this.functionalMap.put(imageElement, this.functionalMap.get(imageElement) + increment);
						}
					}
				}
			}

			public double evaluate(Y argument) {
				if (this.functionalMap.containsKey(argument)) {
					return this.functionalMap.get(argument);
				} else {
					return 0;
				}
			}
		};
	}

	public static <X, Y, V> ObjectObjectFunction<Y, V> pushforward(final ObjectObjectFunction<X, DoubleSparseFormalSum<Y>> mapping,
			final ObjectObjectFunction<X, V> function, 
			final Iterable<X> domainBasis,
			final DoubleAbstractModule<V> moduleStructure) {

		return new ObjectObjectFunction<Y, V>() {
			private THashMap<Y, V> functionMap = new THashMap<Y, V>();

			{	
				// instance initializer for anonymous class
				for (X domainElement: domainBasis) {
					DoubleSparseFormalSum<Y> image = mapping.evaluate(domainElement);

					for (TObjectDoubleIterator<Y> iterator = image.iterator(); iterator.hasNext(); ) {
						iterator.advance();

						double coefficient = iterator.value();
						Y imageElement = iterator.key();
						V increment = moduleStructure.multiply(coefficient, function.evaluate(domainElement));

						if (!this.functionMap.containsKey(imageElement)) {
							this.functionMap.put(imageElement, increment);
						} else {
							this.functionMap.put(imageElement, moduleStructure.add(increment, this.functionMap.get(imageElement)));
						}
					}
				}
			}

			public V evaluate(Y argument) {
				if (this.functionMap.containsKey(argument)) {
					return this.functionMap.get(argument);
				} else {
					return moduleStructure.getAdditiveIdentity();
				}
			}
		};
	}

	public static <X, Y> DoubleSparseFormalSum<ObjectObjectPair<ObjectObjectPair<X, X>, ObjectObjectPair<Y, Y>>> functionTensorProduct(DoubleSparseFormalSum<ObjectObjectPair<X, Y>> f, DoubleSparseFormalSum<ObjectObjectPair<X, Y>> g) {
		DoubleSparseFormalSum<ObjectObjectPair<ObjectObjectPair<X, X>, ObjectObjectPair<Y, Y>>> result = new DoubleSparseFormalSum<ObjectObjectPair<ObjectObjectPair<X, X>, ObjectObjectPair<Y, Y>>>();

		for (TObjectDoubleIterator<ObjectObjectPair<X, Y>> f_iterator = f.iterator(); f_iterator.hasNext(); ) {
			f_iterator.advance();
			for (TObjectDoubleIterator<ObjectObjectPair<X, Y>> g_iterator = g.iterator(); g_iterator.hasNext(); ) {
				g_iterator.advance();
				ObjectObjectPair<X, X> source = new ObjectObjectPair<X, X>(f_iterator.key().getFirst(), g_iterator.key().getFirst());
				ObjectObjectPair<Y, Y> destination = new ObjectObjectPair<Y, Y>(f_iterator.key().getSecond(), g_iterator.key().getSecond());
				double coefficient = f_iterator.value() * g_iterator.value();
				result.put(coefficient, new ObjectObjectPair<ObjectObjectPair<X, X>, ObjectObjectPair<Y, Y>>(source, destination));
			}
		}

		return result;
	}

	public static DoubleSparseFormalSum<ObjectObjectPair<Simplex, Simplex>> computeAlexanderWhitneyMap(Simplex element) {
		DoubleSparseFormalSum<ObjectObjectPair<Simplex, Simplex>> result = new DoubleSparseFormalSum<ObjectObjectPair<Simplex, Simplex>>();
		int[] vertices = element.getVertices();

		for (int i = 0; i < vertices.length; i++) {
			result.put(1, new ObjectObjectPair<Simplex, Simplex>(new Simplex(HomologyUtility.lowerEntries(vertices, i)), new Simplex(HomologyUtility.upperEntries(vertices, i))));
		}
		return result;
	}

	/**
	 * Delta(f(.)) - ((f x f)(D(.)))
	 * 
	 * @param chainMap
	 * @return
	 */
	public static DoubleSparseFormalSum<ObjectObjectPair<Simplex, Simplex>> alexanderWhitneyDifference(DoubleSparseFormalSum<ObjectObjectPair<Simplex, Simplex>> chainMap) {
		DoubleSparseFormalSum<ObjectObjectPair<Simplex, Simplex>> difference = new DoubleSparseFormalSum<ObjectObjectPair<Simplex, Simplex>>();



		return difference;
	}
	
	public static <X> DoubleSparseFormalSum<X> roundCoefficients(DoubleSparseFormalSum<X> chain) {
		DoubleSparseFormalSum<X> result = new DoubleSparseFormalSum<X>();
		
		for (TObjectDoubleIterator<X> iterator = chain.iterator(); iterator.hasNext(); ) {
			iterator.advance();
			double roundedCoefficient = Math.round(iterator.value());
			if (roundedCoefficient != 0) {
				result.put(roundedCoefficient, iterator.key());
			}
		}
		
		return result;
	}
	
	public static double[] round(double[] array) {
		double denominator = 1;
		double[] result = new double[array.length];
		for (int i = 0; i < array.length; i++) {
			result[i] = Math.round(array[i] * denominator) / denominator;
		}
		//return result;
		return array;
	}
}
