package edu.stanford.math.plex4.homology.mapping;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import edu.stanford.math.plex4.algebraic_structures.impl.DoubleFreeModule;
import edu.stanford.math.plex4.algebraic_structures.interfaces.DoubleLeftModule;
import edu.stanford.math.plex4.algebraic_structures.interfaces.GenericRing;
import edu.stanford.math.plex4.datastructures.DoubleFormalSum;
import edu.stanford.math.plex4.datastructures.pairs.GenericPair;
import edu.stanford.math.plex4.free_module.AbstractGenericFormalSum;
import edu.stanford.math.plex4.free_module.AbstractGenericFreeModule;
import edu.stanford.math.plex4.free_module.UnorderedGenericFormalSum;
import edu.stanford.math.plex4.free_module.UnorderedGenericFreeModule;
import edu.stanford.math.plex4.functional.FunctionalUtility;
import edu.stanford.math.plex4.functional.GenericDoubleFunction;
import edu.stanford.math.plex4.functional.GenericFunction;
import edu.stanford.math.plex4.homology.chain_basis.Simplex;
import edu.stanford.math.plex4.homology.streams.interfaces.AbstractFilteredStream;
import edu.stanford.math.plex4.homology.streams.utility.SkeletalMetric;
import edu.stanford.math.plex4.homology.utility.HomologyUtility;
import edu.stanford.math.plex4.utility.ExceptionUtility;
import edu.stanford.math.plex4.utility.Infinity;
import gnu.trove.iterator.TObjectDoubleIterator;
import gnu.trove.map.hash.THashMap;
import gnu.trove.map.hash.TObjectDoubleHashMap;
import gnu.trove.set.hash.THashSet;

public class MappingUtility {
	public static <R, M, N> AbstractGenericFormalSum<R, N> computeImage(AbstractGenericFormalSum<R, GenericPair<M, N>> homChain, M argument) {
		AbstractGenericFormalSum<R, N> result = new UnorderedGenericFormalSum<R, N>();

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

	public static <R extends Number, M> DoubleFormalSum<M> toDoubleFormalSum(AbstractGenericFormalSum<R, M> AbstractGenericFormalSum) {
		DoubleFormalSum<M> sum = new DoubleFormalSum<M>();

		for (Entry<M, R> entry: AbstractGenericFormalSum) {
			sum.put(entry.getValue().doubleValue(), entry.getKey());
		}

		return sum;
	}

	public static <R extends Number, M> List<DoubleFormalSum<M>> toDoubleFormalSumList(List<AbstractGenericFormalSum<R, M>> genericFormalSumList) {
		List<DoubleFormalSum<M>> doubleFormalSumList = new ArrayList<DoubleFormalSum<M>>();

		for (AbstractGenericFormalSum<R, M> AbstractGenericFormalSum: genericFormalSumList) {
			doubleFormalSumList.add(toDoubleFormalSum(AbstractGenericFormalSum));
		}

		return doubleFormalSumList;
	}

	public static <M, N> GenericFunction<M, DoubleFormalSum<N>> toFunctionObject(final DoubleFormalSum<GenericPair<M, N>> homChain){
		return new GenericFunction<M, DoubleFormalSum<N>>() {

			public DoubleFormalSum<N> evaluate(M argument) {
				return computeImage(homChain, argument);
			}

		};
	}

	public static <R, M, N> GenericFunction<M, AbstractGenericFormalSum<R, N>> toFunctionObject(final AbstractGenericFormalSum<R, GenericPair<M, N>> homChain) {
		return new GenericFunction<M, AbstractGenericFormalSum<R, N>>() {

			public AbstractGenericFormalSum<R, N> evaluate(M argument) {
				return computeImage(homChain, argument);
			}
		};
	}

	public static <R, M, N> List<GenericFunction<M, AbstractGenericFormalSum<R, N>>> toFunctionObjectList(final List<AbstractGenericFormalSum<R, GenericPair<M, N>>> homChainList) {
		List<GenericFunction<M, AbstractGenericFormalSum<R, N>>> functionObjectList = new ArrayList<GenericFunction<M, AbstractGenericFormalSum<R, N>>>();
		for (final AbstractGenericFormalSum<R, GenericPair<M, N>> formalSum: homChainList) {
			functionObjectList.add(new GenericFunction<M, AbstractGenericFormalSum<R, N>>() {

				public AbstractGenericFormalSum<R, N> evaluate(M argument) {
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

				public DoubleFormalSum<N> evaluate(M argument) {
					return computeImage(formalSum, argument);
				}
			}
			);
		}

		return functionObjectList;
	}

	public static <R, M, N> GenericFunction<AbstractGenericFormalSum<R, M>, AbstractGenericFormalSum<R, N>> linearExtension(final GenericFunction<M, AbstractGenericFormalSum<R, N>> basisFunction, final GenericRing<R> ring) {
		return new GenericFunction<AbstractGenericFormalSum<R, M>, AbstractGenericFormalSum<R, N>>() {

			public AbstractGenericFormalSum<R, N> evaluate(AbstractGenericFormalSum<R, M> argument) {
				AbstractGenericFreeModule<R, N> freeModuleStructure = new UnorderedGenericFreeModule<R, N>(ring);
				AbstractGenericFormalSum<R, N> sum = freeModuleStructure.createNewSum();

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

			public double evaluate(DoubleFormalSum<M> argument) {
				return norm(argument, p);
			}

		};
	}

	public static <M, N> GenericDoubleFunction<DoubleFormalSum<GenericPair<M, N>>> getDiscreteSimplicialityLossFunction(final AbstractFilteredStream<M> stream) {
		GenericDoubleFunction<DoubleFormalSum<GenericPair<M, N>>> function = new GenericDoubleFunction<DoubleFormalSum<GenericPair<M, N>>>() {

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

	public static <M, N> GenericDoubleFunction<DoubleFormalSum<GenericPair<M, N>>> getSimplicialityLossFunction(final AbstractFilteredStream<M> domainStream, final AbstractFilteredStream<N> codomainStream) {
		GenericDoubleFunction<DoubleFormalSum<GenericPair<M, N>>> function = new GenericDoubleFunction<DoubleFormalSum<GenericPair<M, N>>>() {

			public double evaluate(DoubleFormalSum<GenericPair<M, N>> argument) {
				TObjectDoubleHashMap<M> imageSizes = new TObjectDoubleHashMap<M>();
				TObjectDoubleHashMap<N> preimageSizes = new TObjectDoubleHashMap<N>();

				double maxImageSize = 0;
				double maxPreimageSize = 0;
				double sumImageSize = 0;
				double sumPreimageSize = 0;
				
				double chainNorm = 0;
				
				double twoNormSum = 0;
				
				double coefficientPenaltySum = 0;
				
				double minNonZeroCoefficient = Infinity.Double.getPositiveInfinity();
				
				double imageDiameterSum = 0;
				
				//SkeletalMetric domainSkeletalMetric = new SkeletalMetric(domainStream);
				
				
				for (TObjectDoubleIterator<GenericPair<M, N>> iterator = argument.iterator(); iterator.hasNext(); ) {
					iterator.advance();
					
					M domainElement = iterator.key().getFirst();
					N codomainElement = iterator.key().getSecond();
					
					imageSizes.put(domainElement, Math.abs(iterator.value()) + imageSizes.get(domainElement));
					preimageSizes.put(codomainElement, Math.abs(iterator.value()) + preimageSizes.get(codomainElement));
					
					chainNorm += Math.abs(iterator.value() * iterator.value());
					
					coefficientPenaltySum += Math.abs(iterator.value()) + Math.abs(iterator.value() - 1) + Math.abs(iterator.value() + 1);
					
					if (iterator.value() != 0) {
						minNonZeroCoefficient = Math.min(minNonZeroCoefficient, Math.abs(iterator.value()));
					}
					
					//sumImageSize += Math.abs(iterator.value());
					//maxImageSize = Math.max(maxImageSize, Math.abs(iterator.value()));
				}
				
				
				for (M i: domainStream) {
					DoubleFormalSum<N> image = MappingUtility.computeImage(argument, i);

					twoNormSum += Math.pow(Math.abs(norm(image, 2)), 1);
					
					/*
					for (TObjectDoubleIterator<N> iterator = image.iterator(); iterator.hasNext(); ) {
						iterator.advance();

						preimageSizes.put(iterator.key(), Math.abs(iterator.value()) + preimageSizes.get(iterator.key()));
					}
					*/
				}
				
				for (TObjectDoubleIterator<M> iterator = imageSizes.iterator(); iterator.hasNext(); ) {
					iterator.advance();
					//if (domainStream.getDimension(iterator.key()) == 0) {
						//sumImageSize += Math.abs(iterator.value() - 0) * Math.abs(iterator.value() - 0);
					sumImageSize += Math.abs(iterator.value() - 0);
					//}
					
					maxImageSize = Math.max(maxImageSize, Math.abs(iterator.value()));
				}
				
				for (TObjectDoubleIterator<N> iterator = preimageSizes.iterator(); iterator.hasNext(); ) {
					iterator.advance();
					
					//if (codomainStream.getDimension(iterator.key()) == 0) {
						//sumPreimageSize += Math.abs(iterator.value() - 0) * Math.abs(iterator.value() - 0);
					sumPreimageSize += Math.abs(iterator.value() - 0);
					//}
					
					maxPreimageSize = Math.max(maxPreimageSize, Math.abs(iterator.value()));
				}

				//return sumPreimageSize + sumImageSize;// + chainNorm;
				return chainNorm;
				//return maxPreimageSize + maxImageSize + chainNorm;
				//return maxPreimageSize + maxImageSize + sumPreimageSize + sumImageSize;
				//return twoNormSum + sumPreimageSize + sumImageSize;
				//return twoNormSum;
			}
			/*
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
			 */
		};

		return function;
	}
	
	public static GenericDoubleFunction<DoubleFormalSum<GenericPair<Simplex, Simplex>>> getDiameterLossFunction(final AbstractFilteredStream<Simplex> domainStream, final AbstractFilteredStream<Simplex> codomainStream) {
		GenericDoubleFunction<DoubleFormalSum<GenericPair<Simplex, Simplex>>> function = new GenericDoubleFunction<DoubleFormalSum<GenericPair<Simplex, Simplex>>>() {

			public double evaluate(DoubleFormalSum<GenericPair<Simplex, Simplex>> argument) {
				//TObjectDoubleHashMap<Simplex> imageDi = new TObjectDoubleHashMap<Simplex>();
				//TObjectDoubleHashMap<Simplex> preimageSizes = new TObjectDoubleHashMap<Simplex>();

				THashMap<Simplex, DoubleFormalSum<Simplex>> images = new THashMap<Simplex, DoubleFormalSum<Simplex>>();
				THashMap<Simplex, DoubleFormalSum<Simplex>> preimages = new THashMap<Simplex, DoubleFormalSum<Simplex>>();
				
				DoubleFreeModule<Simplex> module = new DoubleFreeModule<Simplex>();
				
				SkeletalMetric domainSkeletalMetric = new SkeletalMetric(domainStream);
				SkeletalMetric codomainSkeletalMetric = new SkeletalMetric(codomainStream);
				
				double imageDiameterSum = 0;
				double preimageDiameterSum = 0;
				
				double imageDiameterMax = 0;
				double preimageDiameterMax = 0;
				
				for (TObjectDoubleIterator<GenericPair<Simplex, Simplex>> iterator = argument.iterator(); iterator.hasNext(); ) {
					iterator.advance();
					
					Simplex domainElement = iterator.key().getFirst();
					Simplex codomainElement = iterator.key().getSecond();
					
					if (!images.containsKey(domainElement)) {
						images.put(domainElement, new DoubleFormalSum<Simplex>());
					}
					module.accumulate(images.get(domainElement), codomainElement, iterator.value());
					
					if (!preimages.containsKey(codomainElement)) {
						preimages.put(codomainElement, new DoubleFormalSum<Simplex>());
					}
					
					module.accumulate(preimages.get(codomainElement), domainElement, iterator.value());
				}
				
				for (Simplex domainElement: images.keySet()) {
					DoubleFormalSum<Simplex> image = images.get(domainElement);
					double diameter = codomainSkeletalMetric.getDiameter(image);
					
					imageDiameterSum += diameter;
					imageDiameterMax = Math.max(imageDiameterMax, diameter);
				}
				
				for (Simplex codomainElement: preimages.keySet()) {
					DoubleFormalSum<Simplex> preimage = preimages.get(codomainElement);
					double diameter = domainSkeletalMetric.getDiameter(preimage);
					
					preimageDiameterSum += diameter;
					preimageDiameterMax = Math.max(preimageDiameterMax, diameter);
				}
					
				//return imageDiameterMax + preimageDiameterMax;
				return imageDiameterSum + preimageDiameterSum;
			}
			
		};

		return function;
	}


	public static <M, N> GenericDoubleFunction<DoubleFormalSum<GenericPair<M, N>>> computeInducedFunction(final GenericDoubleFunction<DoubleFormalSum<N>> imagePenaltyFunction, 
			final AbstractFilteredStream<M> domainStream) {
		return new GenericDoubleFunction<DoubleFormalSum<GenericPair<M, N>>>() {

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

			public V evaluate(Y argument) {
				if (this.functionMap.containsKey(argument)) {
					return this.functionMap.get(argument);
				} else {
					return moduleStructure.getAdditiveIdentity();
				}
			}
		};
	}

	public static <X, Y> DoubleFormalSum<GenericPair<GenericPair<X, X>, GenericPair<Y, Y>>> functionTensorProduct(DoubleFormalSum<GenericPair<X, Y>> f, DoubleFormalSum<GenericPair<X, Y>> g) {
		DoubleFormalSum<GenericPair<GenericPair<X, X>, GenericPair<Y, Y>>> result = new DoubleFormalSum<GenericPair<GenericPair<X, X>, GenericPair<Y, Y>>>();

		for (TObjectDoubleIterator<GenericPair<X, Y>> f_iterator = f.iterator(); f_iterator.hasNext(); ) {
			f_iterator.advance();
			for (TObjectDoubleIterator<GenericPair<X, Y>> g_iterator = g.iterator(); g_iterator.hasNext(); ) {
				g_iterator.advance();
				GenericPair<X, X> source = new GenericPair<X, X>(f_iterator.key().getFirst(), g_iterator.key().getFirst());
				GenericPair<Y, Y> destination = new GenericPair<Y, Y>(f_iterator.key().getSecond(), g_iterator.key().getSecond());
				double coefficient = f_iterator.value() * g_iterator.value();
				result.put(coefficient, new GenericPair<GenericPair<X, X>, GenericPair<Y, Y>>(source, destination));
			}
		}

		return result;
	}

	public static DoubleFormalSum<GenericPair<Simplex, Simplex>> computeAlexanderWhitneyMap(Simplex element) {
		DoubleFormalSum<GenericPair<Simplex, Simplex>> result = new DoubleFormalSum<GenericPair<Simplex, Simplex>>();
		int[] vertices = element.getVertices();

		for (int i = 0; i < vertices.length; i++) {
			result.put(1, new GenericPair<Simplex, Simplex>(new Simplex(HomologyUtility.lowerEntries(vertices, i)), new Simplex(HomologyUtility.upperEntries(vertices, i))));
		}
		return result;
	}

	/**
	 * Delta(f(.)) - ((f x f)(D(.)))
	 * 
	 * @param chainMap
	 * @return
	 */
	public static DoubleFormalSum<GenericPair<Simplex, Simplex>> alexanderWhitneyDifference(DoubleFormalSum<GenericPair<Simplex, Simplex>> chainMap) {
		DoubleFormalSum<GenericPair<Simplex, Simplex>> difference = new DoubleFormalSum<GenericPair<Simplex, Simplex>>();



		return difference;
	}
	
	public static <X> DoubleFormalSum<X> roundCoefficients(DoubleFormalSum<X> chain) {
		DoubleFormalSum<X> result = new DoubleFormalSum<X>();
		
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
