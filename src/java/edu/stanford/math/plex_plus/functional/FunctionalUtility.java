package edu.stanford.math.plex_plus.functional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import edu.stanford.math.plex_plus.datastructures.ParallelIterator;
import edu.stanford.math.plex_plus.datastructures.pairs.GenericPair;

public class FunctionalUtility {
	public static <X, Y, Z> GenericFunction<X, Z> compose(final GenericFunction<X, Y> f, final GenericFunction<Y, Z> g) {
		return new GenericFunction<X, Z>() {
			@Override
			public Z evaluate(X argument) {
				return g.evaluate(f.evaluate(argument));
			}
		};
	}
	
	public static <X, Y> GenericDoubleFunction<X> compose(final GenericFunction<X, Y> f, final GenericDoubleFunction<Y> g) {
		return new GenericDoubleFunction<X>() {
			@Override
			public double evaluate(X argument) {
				return g.evaluate(f.evaluate(argument));
			}
		};
	}
	
	public static <X, Y> GenericIntFunction<X> compose(final GenericFunction<X, Y> f, final GenericIntFunction<Y> g) {
		return new GenericIntFunction<X>() {
			@Override
			public int evaluate(X argument) {
				return g.evaluate(f.evaluate(argument));
			}
		};
	}
	
	public static <X, Y> GenericFunction<List<X>, List<Y>> genericProductFunction(final List<GenericFunction<X, Y>> functionList) {
		return new GenericFunction<List<X>, List<Y>>() {
			@Override
			public List<Y> evaluate(List<X> argument) {
				List<Y> result = new ArrayList<Y>();
				for (int i = 0; i < argument.size(); i++) {
					result.set(i, functionList.get(i).evaluate(argument.get(i)));
				}
				return result;
			}
		};
	}
	
	public static <X> GenericFunction<Collection<X>, double[]> doubleProductFunction(final Collection<GenericDoubleFunction<X>> functionList) {
		return new GenericFunction<Collection<X>, double[]>() {
			@Override
			public double[] evaluate(Collection<X> argument) {
				double[] result = new double[argument.size()];
				int index = 0;
				
				for (ParallelIterator<GenericDoubleFunction<X>, X> iterator = new ParallelIterator<GenericDoubleFunction<X>, X>(functionList, argument); iterator.hasNext(); ) {
					GenericPair<GenericDoubleFunction<X>, X> pair = iterator.next();
					result[index] = pair.getFirst().evaluate(pair.getSecond());
					index++;
				}
				
				return result;
			}
		};
	}
}
