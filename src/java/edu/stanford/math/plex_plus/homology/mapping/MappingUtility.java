package edu.stanford.math.plex_plus.homology.mapping;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import edu.stanford.math.plex_plus.algebraic_structures.impl.GenericFreeModule;
import edu.stanford.math.plex_plus.algebraic_structures.interfaces.DoubleLeftModule;
import edu.stanford.math.plex_plus.algebraic_structures.interfaces.GenericRing;
import edu.stanford.math.plex_plus.datastructures.DoubleFormalSum;
import edu.stanford.math.plex_plus.datastructures.GenericFormalSum;
import edu.stanford.math.plex_plus.datastructures.pairs.GenericPair;
import edu.stanford.math.plex_plus.functional.FunctionalUtility;
import edu.stanford.math.plex_plus.functional.GenericDoubleFunction;
import edu.stanford.math.plex_plus.functional.GenericFunction;
import edu.stanford.math.plex_plus.homology.streams.interfaces.AbstractFilteredStream;
import edu.stanford.math.plex_plus.utility.ExceptionUtility;
import gnu.trove.iterator.TObjectDoubleIterator;
import gnu.trove.map.hash.THashMap;
import gnu.trove.map.hash.TObjectDoubleHashMap;
import gnu.trove.set.hash.THashSet;

public class MappingUtility {
	public static <R, M, N> GenericFormalSum<R, N> computeImage(GenericFormalSum<R, GenericPair<M, N>> homChain, M argument) {
		GenericFormalSum<R, N> result = new GenericFormalSum<R, N>();

		for (Entry<GenericPair<M, N>, R> entry: homChain) {
			if (entry.getKey().getFirst().equals(argument)) {
				result.put(entry.getValue(), entry.getKey().getSecond());
			}
		}

		return result;
	}

	public static <M, N> DoubleFormalSum<N> computeImage(DoubleFormalSum<GenericPair<M, N>> homChain, M argument) {
		DoubleFormalSum<N> result = new DoubleFormalSum<N>();

		for (TObjectDoubleIterator<GenericPair<M, N>> iterator = homChain.iterator(); iterator.hasNext(); ) {
			iterator.advance();
			if (iterator.key().getFirst().equals(argument)) {
				result.put(iterator.value(), iterator.key().getSecond());
			}
		}

		return result;
	}

	public static <R extends Number, M> DoubleFormalSum<M> toDoubleFormalSum(GenericFormalSum<R, M> genericFormalSum) {
		DoubleFormalSum<M> sum = new DoubleFormalSum<M>();

		for (Entry<M, R> entry: genericFormalSum) {
			sum.put(entry.getValue().doubleValue(), entry.getKey());
		}

		return sum;
	}

	public static <R extends Number, M> List<DoubleFormalSum<M>> toDoubleFormalSumList(List<GenericFormalSum<R, M>> genericFormalSumList) {
		List<DoubleFormalSum<M>> doubleFormalSumList = new ArrayList<DoubleFormalSum<M>>();

		for (GenericFormalSum<R, M> genericFormalSum: genericFormalSumList) {
			doubleFormalSumList.add(toDoubleFormalSum(genericFormalSum));
		}

		return doubleFormalSumList;
	}

	public static <M, N> GenericFunction<M, DoubleFormalSum<N>> toFunctionObject(final DoubleFormalSum<GenericPair<M, N>> homChain){
		return new GenericFunction<M, DoubleFormalSum<N>>() {

			@Override
			public DoubleFormalSum<N> evaluate(M argument) {
				return computeImage(homChain, argument);
			}

		};
	}

	public static <R, M, N> GenericFunction<M, GenericFormalSum<R, N>> toFunctionObject(final GenericFormalSum<R, GenericPair<M, N>> homChain) {
		return new GenericFunction<M, GenericFormalSum<R, N>>() {
			@Override
			public GenericFormalSum<R, N> evaluate(M argument) {
				return computeImage(homChain, argument);
			}
		};
	}

	public static <R, M, N> List<GenericFunction<M, GenericFormalSum<R, N>>> toFunctionObjectList(final List<GenericFormalSum<R, GenericPair<M, N>>> homChainList) {
		List<GenericFunction<M, GenericFormalSum<R, N>>> functionObjectList = new ArrayList<GenericFunction<M, GenericFormalSum<R, N>>>();
		for (final GenericFormalSum<R, GenericPair<M, N>> formalSum: homChainList) {
			functionObjectList.add(new GenericFunction<M, GenericFormalSum<R, N>>() {
				@Override
				public GenericFormalSum<R, N> evaluate(M argument) {
					return computeImage(formalSum, argument);
				}
			}
			);
		}

		return functionObjectList;
	}

	public static <M, N> List<GenericFunction<M, DoubleFormalSum<N>>> toDoubleFunctionObjectList(final List<DoubleFormalSum<GenericPair<M, N>>> homChainList) {
		List<GenericFunction<M, DoubleFormalSum<N>>> functionObjectList = new ArrayList<GenericFunction<M, DoubleFormalSum<N>>>();
		for (final DoubleFormalSum<GenericPair<M, N>> formalSum: homChainList) {
			functionObjectList.add(new GenericFunction<M, DoubleFormalSum<N>>() {
				@Override
				public DoubleFormalSum<N> evaluate(M argument) {
					return computeImage(formalSum, argument);
				}
			}
			);
		}

		return functionObjectList;
	}

	public static <R, M, N> GenericFunction<GenericFormalSum<R, M>, GenericFormalSum<R, N>> linearExtension(final GenericFunction<M, GenericFormalSum<R, N>> basisFunction, final GenericRing<R> ring) {
		return new GenericFunction<GenericFormalSum<R, M>, GenericFormalSum<R, N>>() {

			@Override
			public GenericFormalSum<R, N> evaluate(GenericFormalSum<R, M> argument) {
				GenericFreeModule<R, N> freeModuleStructure = new GenericFreeModule<R, N>(ring);
				GenericFormalSum<R, N> sum = new GenericFormalSum<R, N>();

				for (Iterator<Map.Entry<M, R>> iterator = argument.iterator(); iterator.hasNext(); ) {
					Entry<M, R> entry = iterator.next();
					sum = freeModuleStructure.add(sum, freeModuleStructure.multiply(entry.getValue(), basisFunction.evaluate(entry.getKey())));
				}

				return sum;
			}
		};
	}

	public static <R extends Number, M> GenericDoubleFunction<M> toDoubleFunction(final GenericFunction<M, R> genericFunction) {
		return new GenericDoubleFunction<M>() {

			@Override
			public double evaluate(M argument) {
				return genericFunction.evaluate(argument).doubleValue();
			}

		};
	}

	public static <R extends Number, M> List<GenericDoubleFunction<M>> toDoubleFunctionList(final List<GenericFunction<M, R>> genericFunctionList) {
		List<GenericDoubleFunction<M>> doubleFunctionList = new ArrayList<GenericDoubleFunction<M>>();

		for (GenericFunction<M, R> genericFunction: genericFunctionList) {
			doubleFunctionList.add(toDoubleFunction(genericFunction));
		}

		return doubleFunctionList;
	}

	public static <M> double sumFunctionOverStream(final GenericDoubleFunction<M> function, final AbstractFilteredStream<M> stream) {
		double sum = 0;
		for (M element: stream) {
			sum += function.evaluate(element);
		}
		return sum;
	}

	public static <M> GenericDoubleFunction<M> sumFunctions(final List<GenericDoubleFunction<M>> functions) {
		return new GenericDoubleFunction<M>() {

			@Override
			public double evaluate(M argument) {
				double sum = 0;
				for (GenericDoubleFunction<M> function: functions) {
					sum += function.evaluate(argument);
				}
				return sum;
			}

		};
	}

	public static <M> GenericDoubleFunction<M> sumFunctions(final List<GenericDoubleFunction<M>> functions, final double[] coefficients) {
		return new GenericDoubleFunction<M>() {

			@Override
			public double evaluate(M argument) {
				double sum = 0;
				int i = 0;
				for (GenericDoubleFunction<M> function: functions) {
					sum += coefficients[i] * function.evaluate(argument);
					i++;
				}
				return sum;
			}

		};
	}

	public static <M> double norm(DoubleFormalSum<M> chain, int p) {
		ExceptionUtility.verifyNonNull(chain);
		ExceptionUtility.verifyNonNegative(p);

		if (p == 0) {
			return chain.size();
		} else {
			double norm = 0;
			for (TObjectDoubleIterator<M> iterator = chain.iterator(); iterator.hasNext(); ) {
				iterator.advance();
				norm += Math.pow(Math.abs(iterator.value()), p);
			}
			return norm;
		}
	}

	public static <M> GenericDoubleFunction<DoubleFormalSum<M>> getNormFunction(final int p) {
		return new GenericDoubleFunction<DoubleFormalSum<M>>() {

			@Override
			public double evaluate(DoubleFormalSum<M> argument) {
				return norm(argument, p);
			}

		};
	}
	
	public static <M, N> GenericDoubleFunction<DoubleFormalSum<GenericPair<M, N>>> getDiscreteSimplicialityLossFunction(final AbstractFilteredStream<M> stream) {
		GenericDoubleFunction<DoubleFormalSum<GenericPair<M, N>>> function = new GenericDoubleFunction<DoubleFormalSum<GenericPair<M, N>>>() {

			@Override
			public double evaluate(DoubleFormalSum<GenericPair<M, N>> argument) {
				THashSet<N> domainMap = new THashSet<N>();
				double penalty = 0;
				for (M i: stream) {
					DoubleFormalSum<N> image = MappingUtility.computeImage(argument, i);

					for (TObjectDoubleIterator<N> iterator = image.iterator(); iterator.hasNext(); ) {
						iterator.advance();

						if (domainMap.contains(iterator.key())) {
							penalty++;
						} else {
							domainMap.add(iterator.key());
						}						
					}
				}

				return penalty;
			}

		};

		return function;
	}
	
	private static double primitiveSimplicialityPenalty(double value, double lambda) {
		double abs_value = Math.abs(value);
		
		if (abs_value <= 0.5) {
			return lambda * abs_value;
		} else if (abs_value <= 1.0) {
			return lambda * (1 - abs_value);
		} else {
			return (abs_value - 1);
		}
	}

	public static <M, N> GenericDoubleFunction<DoubleFormalSum<GenericPair<M, N>>> getSimplicialityLossFunction(final AbstractFilteredStream<M> stream) {
		GenericDoubleFunction<DoubleFormalSum<GenericPair<M, N>>> function = new GenericDoubleFunction<DoubleFormalSum<GenericPair<M, N>>>() {

			@Override
			public double evaluate(DoubleFormalSum<GenericPair<M, N>> argument) {
				TObjectDoubleHashMap<M> domainLossMap = new TObjectDoubleHashMap<M>();
				TObjectDoubleHashMap<N> codomainLossMap = new TObjectDoubleHashMap<N>();
				double penaltyMax = 0;
				double penaltySum = 0;
				
				double coefficientLossSum = 0;
				
				for (M i: stream) {
					DoubleFormalSum<N> image = MappingUtility.computeImage(argument, i);

					double imageNorm = MappingUtility.norm(image, 1);
					
					domainLossMap.put(i, domainLossMap.get(i) + imageNorm);
					
					for (TObjectDoubleIterator<N> iterator = image.iterator(); iterator.hasNext(); ) {
						iterator.advance();

						coefficientLossSum += primitiveSimplicialityPenalty(iterator.value(), 2);
						
						double penalty = 0;
						
						if (!codomainLossMap.containsKey(iterator.key())) {
							penalty = Math.abs(iterator.value());
						} else {
							penalty = codomainLossMap.get(iterator.key()) + Math.abs(iterator.value());
						}
						
						codomainLossMap.put(iterator.key(), penalty);
						
						penaltyMax = Math.max(penalty, penaltyMax);
						penaltySum += Math.abs(iterator.value());
					}
				}

				double simplicialityPenaltySum = 0;
				for (TObjectDoubleIterator<N> iterator = codomainLossMap.iterator(); iterator.hasNext(); ) {
					iterator.advance();
					simplicialityPenaltySum += primitiveSimplicialityPenalty(iterator.value(), 5);
				}
				
				return penaltyMax;
			}

		};

		return function;
	}


	public static <M, N> GenericDoubleFunction<DoubleFormalSum<GenericPair<M, N>>> computeInducedFunction(final GenericDoubleFunction<DoubleFormalSum<N>> imagePenaltyFunction, 
			final AbstractFilteredStream<M> domainStream) {
		return new GenericDoubleFunction<DoubleFormalSum<GenericPair<M, N>>>() {

			@Override
			public double evaluate(DoubleFormalSum<GenericPair<M, N>> argument) {
				GenericFunction<M, DoubleFormalSum<N>> newCycleFunctionObject = MappingUtility.toFunctionObject(argument);

				GenericDoubleFunction<M> preimagePenaltyFunction = FunctionalUtility.compose(newCycleFunctionObject, imagePenaltyFunction);

				return MappingUtility.sumFunctionOverStream(preimagePenaltyFunction, domainStream);
			}

		};
	}

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
	public static <X, Y> GenericDoubleFunction<Y> pushforward(final GenericFunction<X, DoubleFormalSum<Y>> mapping,
			final GenericDoubleFunction<X> functional, 
			final Iterable<X> domainBasis) {

		return new GenericDoubleFunction<Y>() {
			private TObjectDoubleHashMap<Y> functionalMap = new TObjectDoubleHashMap<Y>();

			{	
				// instance initializer for anonymous class
				for (X domainElement: domainBasis) {
					DoubleFormalSum<Y> image = mapping.evaluate(domainElement);

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

			@Override
			public double evaluate(Y argument) {
				if (this.functionalMap.containsKey(argument)) {
					return this.functionalMap.get(argument);
				} else {
					return 0;
				}
			}
		};
	}
	
	public static <X, Y, V> GenericFunction<Y, V> pushforward(final GenericFunction<X, DoubleFormalSum<Y>> mapping,
			final GenericFunction<X, V> function, 
			final Iterable<X> domainBasis,
			final DoubleLeftModule<V> moduleStructure) {

		return new GenericFunction<Y, V>() {
			private THashMap<Y, V> functionMap = new THashMap<Y, V>();

			{	
				// instance initializer for anonymous class
				for (X domainElement: domainBasis) {
					DoubleFormalSum<Y> image = mapping.evaluate(domainElement);

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

			@Override
			public V evaluate(Y argument) {
				if (this.functionMap.containsKey(argument)) {
					return this.functionMap.get(argument);
				} else {
					return moduleStructure.getAdditiveIdentity();
				}
			}
		};
	}
}
