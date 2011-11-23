package edu.stanford.math.plex4.homology.zigzag.bootstrap;

import java.util.ArrayList;
import java.util.Collections;
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

	public static <I extends Comparable<I>, U extends PrimitiveBasisElement> IntervalTracker<Integer, I, IntSparseFormalSum<U>> 
	include(AbstractHomologyTracker<Integer, I, ?, IntSparseFormalSum<U>> X,
			AbstractHomologyTracker<Integer, I, ?, IntSparseFormalSum<U>> Z,
			AbstractHomologyTracker<Integer, I, ?, IntSparseFormalSum<U>> Y,
			final IntervalTracker<Integer, I, IntSparseFormalSum<U>> accumulator,
			IntAlgebraicFreeModule<U> chainModule,
			I X_index, I Y_index) {
		
		IntervalTracker<Integer, I, IntSparseFormalSum<U>> result = new IntervalTracker<Integer, I, IntSparseFormalSum<U>>();
		result.setUseLeftClosedIntervals(true);
		result.setUseRightClosedIntervals(true);
		ObjectObjectFunction<IntSparseFormalSum<U>, IntSparseFormalSum<U>> inclusionMap = InducedHomologyMappingUtility.getInclusionMap();

		Map<Integer, Integer> XZ_map = InducedHomologyMappingUtility.computeInducedMap(X.getState(), Z, chainModule, inclusionMap);
		Map<Integer, Integer> YZ_map = InducedHomologyMappingUtility.computeInducedMap(Y.getState(), Z, chainModule, inclusionMap);
		Map<Integer, List<Integer>> XY_map = new HashMap<Integer, List<Integer>>();
		
		Map<Integer, List<Integer>> ZY_map = reverse(YZ_map);
		
		for (Integer X_key: XZ_map.keySet()) {
			Integer Z_key = XZ_map.get(X_key);
			if (ZY_map.containsKey(Z_key)) {
				for (Integer Y_key: ZY_map.get(Z_key)) {
					if (!XY_map.containsKey(X_key)) {
						XY_map.put(X_key, new ArrayList<Integer>());
					}
					XY_map.get(X_key).add(Y_key);
				}
			}
		}
		
		Set<Integer> X_processedIndices = new HashSet<Integer>();
		Set<Integer> Y_processedIndices = new HashSet<Integer>();
		
		List<Integer> X_keys = new ArrayList<Integer>(XY_map.keySet());
		Collections.sort(X_keys);
		for (Integer X_key: X_keys) {
			List<Integer> Y_keys = XY_map.get(X_key);
			
			if (X_processedIndices.contains(X_key)) {
				continue;
			}
			
			if (Y_keys.size() > 0) {
				Integer Y_key = Y_keys.get(0);
				
				if (Y_processedIndices.contains(Y_key)) {
					continue;
				}
				
				X_processedIndices.add(X_key);
				Y_processedIndices.add(Y_key);
				
				IntervalDescriptor<I, IntSparseFormalSum<U>> X_descriptor = accumulator.getActiveGenerators().get(X_key);
				result.startInterval(Y_key, X_descriptor.getStart(), X_descriptor.getDimension(), X_descriptor.getGenerator());
			}
		}
		
		X_keys = new ArrayList<Integer>(accumulator.getActiveGenerators().keySet());
		Collections.sort(X_keys);
		for (Integer X_key: X_keys) {
			IntervalDescriptor<I, IntSparseFormalSum<U>> X_descriptor = accumulator.getActiveGenerators().get(X_key);
		
			if (X_processedIndices.contains(X_key)) {
				continue;
			}
			
			Integer tempKey = 0;
			
			while (result.containsActiveInterval(tempKey)) {
				tempKey++;
			}
			
			result.startInterval(tempKey, X_descriptor.getStart(), X_descriptor.getDimension(), X_descriptor.getGenerator());
			result.endInterval(tempKey, X_index);
		}
		
		List<Integer> Y_keys = new ArrayList<Integer>(Y.getState().getActiveGenerators().keySet());
		for (Integer Y_key: Y_keys) {
			if (Y_processedIndices.contains(Y_key)) {
				continue;
			}
			
			IntervalDescriptor<I, IntSparseFormalSum<U>> Y_descriptor = Y.getState().getActiveGenerators().get(Y_key);
			result.startInterval(Y_key, Y_index, Y_descriptor.getDimension(), Y_descriptor.getGenerator());
		}
		
		for (Entry<Integer, List<ObjectObjectPair<Interval<I>, IntSparseFormalSum<U>>>> entry: accumulator.getInactiveGenerators()) {
			int dimension = entry.getKey();
			for (ObjectObjectPair<Interval<I>, IntSparseFormalSum<U>> pair: entry.getValue()) {
				result.getFiniteAnnotatedBarcodes().addInterval(dimension, pair.getFirst(), pair.getSecond());
			}
		}
		
		return result;
	}
	
	
	public static <I extends Comparable<I>, U extends PrimitiveBasisElement, V extends ObjectObjectPair<U, U>> IntervalTracker<Integer, I, IntSparseFormalSum<U>> 
	project(AbstractHomologyTracker<Integer, I, ?, IntSparseFormalSum<U>> X,
			AbstractHomologyTracker<Integer, I, ?, IntSparseFormalSum<V>> Z,
			AbstractHomologyTracker<Integer, I, ?, IntSparseFormalSum<U>> Y,
			AbstractPersistenceTracker<Integer, I, IntSparseFormalSum<U>> accumulator,
			IntAlgebraicFreeModule<U> chainModule,
			IntAlgebraicFreeModule<V> Z_chainModule,
			I X_index, I Y_index) {
		
		IntervalTracker<Integer, I, IntSparseFormalSum<U>> result = new IntervalTracker<Integer, I, IntSparseFormalSum<U>>();
		
		ObjectObjectFunction<IntSparseFormalSum<V>, IntSparseFormalSum<U>> firstProjection = InducedHomologyMappingUtility.getFirstGradedProjectionMap(chainModule, Z_chainModule);
		ObjectObjectFunction<IntSparseFormalSum<V>, IntSparseFormalSum<U>> secondProjection = InducedHomologyMappingUtility.getSecondGradedProjectionMap(chainModule, Z_chainModule);
		
		Map<Integer, Integer> ZX_map = computeInducedMap(Z.getState(), X, chainModule, firstProjection);
		Map<Integer, Integer> ZY_map = computeInducedMap(Z.getState(), Y, chainModule, secondProjection);
		Map<Integer, List<Integer>> XZ_map = reverse(ZX_map);
		Map<Integer, Integer> XY_map = new HashMap<Integer, Integer>();
		
		for (Integer X_key: XZ_map.keySet()) {
			for (Integer Z_key: XZ_map.get(X_key)) {
				if (ZY_map.containsKey(Z_key)) {
					XY_map.put(X_key, ZY_map.get(Z_key));
				}
			}
		}
		
		Set<Integer> X_processedIndices = new HashSet<Integer>();
		Set<Integer> Y_processedIndices = new HashSet<Integer>();

		for (Integer X_key: accumulator.getActiveGenerators().keySet()) {
			IntervalDescriptor<I, IntSparseFormalSum<U>> X_descriptor = accumulator.getActiveGenerators().get(X_key);
			
			if (XY_map.containsKey(X_key)) {
				Integer Y_key = XY_map.get(X_key);
				
				result.startInterval(Y_key, X_descriptor.getStart(), X_descriptor.getDimension(), X_descriptor.getGenerator());
				
				X_processedIndices.add(X_key);
				Y_processedIndices.add(Y_key);
			} else {
				Integer tempKey = 0;
				
				while (result.containsActiveInterval(tempKey)) {
					tempKey++;
				}
				
				result.startInterval(X_key, X_descriptor.getStart(), X_descriptor.getDimension(), X_descriptor.getGenerator());
				result.endInterval(X_key, X_index);
			}
		}
		
		for (Integer Y_key: Y.getState().getActiveGenerators().keySet()) {
			if (Y_processedIndices.contains(Y_key)) {
				continue;
			}
			
			IntervalDescriptor<I, IntSparseFormalSum<U>> Y_descriptor = Y.getState().getActiveGenerators().get(Y_key);
			
			result.startInterval(Y_key, Y_index, Y_descriptor.getDimension(), Y_descriptor.getGenerator());
		}
		
		for (Entry<Integer, List<ObjectObjectPair<Interval<I>, IntSparseFormalSum<U>>>> entry: accumulator.getInactiveGenerators()) {
			int dimension = entry.getKey();
			for (ObjectObjectPair<Interval<I>, IntSparseFormalSum<U>> pair: entry.getValue()) {
				result.getFiniteAnnotatedBarcodes().addInterval(dimension, pair.getFirst(), pair.getSecond());
			}
		}
		
		return result;
	}

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

	public static <X extends Comparable<X>, Y> Map<Y, List<X>> reverse(Map<X, Y> map) {
		Map<Y, List<X>> result = new HashMap<Y, List<X>>();

		for (X key: map.keySet()) {
			Y value = map.get(key);
			
			if (!result.containsKey(value)) {
				result.put(value, new ArrayList<X>());
			}
			
			result.get(value).add(key);
		}

		for (Y key: result.keySet()) {
			Collections.sort(result.get(key));
		}
		
		return result;
	}

	public static <U extends PrimitiveBasisElement> ObjectObjectFunction<IntSparseFormalSum<U>, IntSparseFormalSum<U>> getInclusionMap() {
		return new ObjectObjectFunction<IntSparseFormalSum<U>, IntSparseFormalSum<U>>() {

			public IntSparseFormalSum<U> evaluate(IntSparseFormalSum<U> arg0) {
				return arg0;
			}
			
		};
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
