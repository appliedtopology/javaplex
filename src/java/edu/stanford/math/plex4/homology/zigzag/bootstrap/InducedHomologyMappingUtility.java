package edu.stanford.math.plex4.homology.zigzag.bootstrap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import edu.stanford.math.plex4.homology.barcodes.Interval;
import edu.stanford.math.plex4.homology.chain_basis.PrimitiveBasisElement;
import edu.stanford.math.plex4.homology.zigzag.AbstractHomologyTracker;
import edu.stanford.math.plex4.homology.zigzag.AbstractPersistenceTracker;
import edu.stanford.math.plex4.homology.zigzag.IntervalDescriptor;
import edu.stanford.math.plex4.homology.zigzag.IntervalTracker;
import edu.stanford.math.primitivelib.autogen.algebraic.IntAbstractModule;
import edu.stanford.math.primitivelib.autogen.formal_sum.IntAlgebraicFreeModule;
import edu.stanford.math.primitivelib.autogen.formal_sum.IntSparseFormalSum;
import edu.stanford.math.primitivelib.autogen.functional.ObjectObjectFunction;
import edu.stanford.math.primitivelib.autogen.pair.ObjectObjectPair;
import gnu.trove.TObjectIntIterator;


public class InducedHomologyMappingUtility {

	public static <K, I extends Comparable<I>, U extends PrimitiveBasisElement, V extends ObjectObjectPair<U, U>> IntervalTracker<K, I, IntSparseFormalSum<U>> 
	project(AbstractHomologyTracker<K, I, ?, IntSparseFormalSum<U>> X,
			AbstractHomologyTracker<K, I, ?, IntSparseFormalSum<V>> Z,
			AbstractHomologyTracker<K, I, ?, IntSparseFormalSum<U>> Y,
			AbstractPersistenceTracker<K, I, IntSparseFormalSum<U>> accumulator,
			IntAlgebraicFreeModule<U> chainModule,
			IntAlgebraicFreeModule<V> Z_chainModule,
			I X_index, I Y_index) {
		
		IntervalTracker<K, I, IntSparseFormalSum<U>> result = new IntervalTracker<K, I, IntSparseFormalSum<U>>();
		
		ObjectObjectFunction<IntSparseFormalSum<V>, IntSparseFormalSum<U>> firstProjection = InducedHomologyMappingUtility.getFirstGradedProjectionMap(chainModule, Z_chainModule);
		ObjectObjectFunction<IntSparseFormalSum<V>, IntSparseFormalSum<U>> secondProjection = InducedHomologyMappingUtility.getSecondGradedProjectionMap(chainModule, Z_chainModule);
		
		Map<K, K> ZX_map = computeInducedMap(Z.getState(), X, chainModule, firstProjection);
		Map<K, K> ZY_map = computeInducedMap(Z.getState(), Y, chainModule, secondProjection);
		Map<K, List<K>> XZ_map = reverse(ZX_map);
		Map<K, K> XY_map = new HashMap<K, K>();
		
		for (K X_key: XZ_map.keySet()) {
			for (K Z_key: XZ_map.get(X_key)) {
				if (ZY_map.containsKey(Z_key)) {
					XY_map.put(X_key, ZY_map.get(Z_key));
				}
			}
		}
		
		Set<K> X_processedIndices = new HashSet<K>();
		Set<K> Y_processedIndices = new HashSet<K>();

		for (K X_key: accumulator.getActiveGenerators().keySet()) {
			IntervalDescriptor<I, IntSparseFormalSum<U>> X_descriptor = accumulator.getActiveGenerators().get(X_key);
			
			if (XY_map.containsKey(X_key)) {
				K Y_key = XY_map.get(X_key);
				
				result.startInterval(Y_key, X_descriptor.getStart(), X_descriptor.getDimension(), X_descriptor.getGenerator());
				
				X_processedIndices.add(X_key);
				Y_processedIndices.add(Y_key);
			} else {
				result.startInterval(X_key, X_descriptor.getStart(), X_descriptor.getDimension(), X_descriptor.getGenerator());
				result.endInterval(X_key, X_index);
			}
		}
		
		for (K Y_key: Y.getState().getActiveGenerators().keySet()) {
			if (Y_processedIndices.contains(Y_key)) {
				continue;
			}
			
			IntervalDescriptor<I, IntSparseFormalSum<U>> Y_descriptor = Y.getState().getActiveGenerators().get(Y_key);
			
			result.startInterval(Y_key, Y_descriptor.getStart(), Y_descriptor.getDimension(), Y_descriptor.getGenerator());
		}
		
		for (Entry<Integer, List<ObjectObjectPair<Interval<I>, IntSparseFormalSum<U>>>> entry: accumulator.getInactiveGenerators()) {
			int dimension = entry.getKey();
			for (ObjectObjectPair<Interval<I>, IntSparseFormalSum<U>> pair: entry.getValue()) {
				result.getFiniteAnnotatedBarcodes().addInterval(dimension, pair.getFirst(), pair.getSecond());
			}
		}
		
		return result;
	}
	
	/*
	public static <K, I extends Comparable<I>, U extends PrimitiveBasisElement, V extends ObjectObjectPair<U, U>> IntervalTracker<K, I, IntSparseFormalSum<V>> 
	computeFirstProjection(AbstractPersistenceTracker<K, I, IntSparseFormalSum<V>> Z_state, 
			AbstractHomologyTracker<K, I, ?, IntSparseFormalSum<U>> X, 
					AbstractPersistenceTracker<K, I, IntSparseFormalSum<U>> accumulator,
					IntAlgebraicFreeModule<U> chainModule,
					I joinIndex) {

		ObjectObjectFunction<IntSparseFormalSum<V>, IntSparseFormalSum<U>> chainMap = InducedHomologyMappingUtility.getFirstGradedProjectionMap(chainModule);

		Map<K, K> joinMap = computeInducedMap(Z_state, X, chainModule, chainMap);

		IntervalTracker<K, I, IntSparseFormalSum<V>> result = join(accumulator, Z_state, joinMap, joinIndex, true);

		return result;
	}

	public static <K, I extends Comparable<I>, U extends PrimitiveBasisElement, V extends ObjectObjectPair<U, U>> 
	IntervalTracker<K, I, IntSparseFormalSum<U>> computeSecondProjection(AbstractPersistenceTracker<K, I, IntSparseFormalSum<V>> Z_state, 
			AbstractHomologyTracker<K, I, ?, IntSparseFormalSum<U>> Y, 
					AbstractPersistenceTracker<K, I, IntSparseFormalSum<V>> accumulator,
					IntAlgebraicFreeModule<U> chainModule, 
					I joinIndex) {

		ObjectObjectFunction<IntSparseFormalSum<V>, IntSparseFormalSum<U>> chainMap = InducedHomologyMappingUtility.getSecondGradedProjectionMap(chainModule);

		Map<K, K> joinMap = computeInducedMap(Z_state, Y, chainModule, chainMap);

		IntervalTracker<K, I, IntSparseFormalSum<U>> result = join(accumulator, Y.getState(), joinMap, joinIndex, false);

		return result;
	}
*/

	public static <K1, K2, I1 extends Comparable<I1>, I2 extends Comparable<I2>, G1, G2> Map<K1, K2> computeInducedMap(AbstractPersistenceTracker<K1, I1, G1> A_state, 
			AbstractHomologyTracker<K2, I2, ?, G2> B, 
			IntAbstractModule<G2> chainModule, 
			ObjectObjectFunction<G1, G2> chainMap) {

		AbstractPersistenceTracker<K2, I2, G2> B_state = B.getState();

		Map<K1, IntervalDescriptor<I1, G1>> A_generators = A_state.getActiveGenerators();
		Map<K2, IntervalDescriptor<I2, G2>> B_generators = B_state.getActiveGenerators();

		Map<K1, K2> joinMap = new HashMap<K1, K2>();

		for (K1 A_key: A_generators.keySet()) {
			int A_dimension = A_generators.get(A_key).getDimension();
			G1 A_chain = A_generators.get(A_key).getGenerator();

			G2 image = chainMap.evaluate(A_chain);

			for (K2 B_key: B_generators.keySet()) {
				int B_dimension = B_generators.get(B_key).getDimension();

				if (B_dimension != A_dimension) {
					continue;
				}

				G2 B_chain = B_generators.get(B_key).getGenerator();
				G2 difference = chainModule.subtract(B_chain, image);

				if (B.isBoundary(difference)) {
					joinMap.put(A_key, B_key);
				}
			}
		}

		return joinMap;
	}
	
	public static <K, I extends Comparable<I>, G1, G2> IntervalTracker<K, I, G2> join(AbstractPersistenceTracker<K, I, G1> A_state,
			AbstractPersistenceTracker<K, I, G2> B_state, 
			Map<K, K> joinMap, 
			I joinIndex,
			boolean reversed) {

		Map<K, IntervalDescriptor<I, G1>> A_generators = A_state.getActiveGenerators();
		Map<K, IntervalDescriptor<I, G2>> B_generators = B_state.getActiveGenerators();

		Set<K> processedAIndices = new HashSet<K>();
		Set<K> processedBIndices = new HashSet<K>();

		IntervalTracker<K, I, G2> result = new IntervalTracker<K, I, G2>();

		for (Entry<K, K> entry: joinMap.entrySet()) {
			K B_key, A_key;
			
			if (!reversed) {
				A_key = entry.getKey();
				B_key = entry.getValue();
			} else {
				B_key = entry.getKey();
				A_key = entry.getValue();
			}
			
			IntervalDescriptor<I, G1> A_descriptor = A_generators.get(A_key);
			IntervalDescriptor<I, G2> B_descriptor = B_generators.get(B_key);

			result.startInterval(B_key, A_descriptor.getStart(), B_descriptor.getDimension(), B_descriptor.getGenerator());
			processedBIndices.add(B_key);
			processedAIndices.add(A_key);
		}
		
		for (K A_key: A_generators.keySet()) {
			if (processedAIndices.contains(A_key)) {
				continue;
			}

			IntervalDescriptor<I, G1> A_descriptor = A_generators.get(A_key);

			result.startInterval(A_key, A_descriptor.getStart(), A_descriptor.getDimension(), null);
			result.endInterval(A_key, joinIndex);
		}
		
		
		for (K B_key: B_generators.keySet()) {
			if (processedBIndices.contains(B_key)) {
				continue;
			}

			IntervalDescriptor<I, G2> B_descriptor = B_generators.get(B_key);

			result.startInterval(B_key, B_descriptor.getStart(), B_descriptor.getDimension(), B_descriptor.getGenerator());
		}

		for (Entry<Integer, List<ObjectObjectPair<Interval<I>, G1>>> entry: A_state.getInactiveGenerators()) {
			int dimension = entry.getKey();
			for (ObjectObjectPair<Interval<I>, G1> pair: entry.getValue()) {
				result.getFiniteAnnotatedBarcodes().addInterval(dimension, pair.getFirst(), null);
			}
		}
		
		return result;
	}

	public static <X, Y> Map<Y, List<X>> reverse(Map<X, Y> map) {
		Map<Y, List<X>> result = new HashMap<Y, List<X>>();

		for (X key: map.keySet()) {
			Y value = map.get(key);
			
			if (!result.containsKey(value)) {
				result.put(value, new ArrayList<X>());
			}
			
			result.get(value).add(key);
		}

		return result;
	}

	public static <U extends PrimitiveBasisElement, V extends ObjectObjectPair<U, U>> ObjectObjectFunction<IntSparseFormalSum<V>, IntSparseFormalSum<U>> getFirstGradedProjectionMap(final IntAlgebraicFreeModule<U> chainModule, final IntAlgebraicFreeModule<V> dummy) {
		return new ObjectObjectFunction<IntSparseFormalSum<V>, IntSparseFormalSum<U>>() {

			public IntSparseFormalSum<U> evaluate(IntSparseFormalSum<V> chain) {

				if (chain.isEmpty()) {
					return chainModule.createNewSum();
				}

				int dimension = 0;

				{
					TObjectIntIterator<V> iterator = chain.iterator();

					if (iterator.hasNext()) {
						iterator.advance();
						dimension = iterator.key().getFirst().getDimension() + iterator.key().getSecond().getDimension();	
					}
				}

				return SimplexStreamUtility.filterByDimension(SimplexStreamUtility.projectFirst(chain, chainModule), dimension);
			}

		};
	}

	public static <U extends PrimitiveBasisElement, V extends ObjectObjectPair<U, U>> ObjectObjectFunction<IntSparseFormalSum<V>, IntSparseFormalSum<U>> getSecondGradedProjectionMap(final IntAlgebraicFreeModule<U> chainModule, final IntAlgebraicFreeModule<V> dummy) {
		return new ObjectObjectFunction<IntSparseFormalSum<V>, IntSparseFormalSum<U>>() {

			public IntSparseFormalSum<U> evaluate(IntSparseFormalSum<V> chain) {

				if (chain.isEmpty()) {
					return chainModule.createNewSum();
				}

				int dimension = 0;

				{
					TObjectIntIterator<V> iterator = chain.iterator();

					if (iterator.hasNext()) {
						iterator.advance();
						dimension = iterator.key().getFirst().getDimension() + iterator.key().getSecond().getDimension();	
					}
				}

				return SimplexStreamUtility.filterByDimension(SimplexStreamUtility.projectSecond(chain, chainModule), dimension);
			}

		};
	}
}
