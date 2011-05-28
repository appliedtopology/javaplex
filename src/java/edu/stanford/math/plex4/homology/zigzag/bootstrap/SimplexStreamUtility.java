package edu.stanford.math.plex4.homology.zigzag.bootstrap;

import edu.stanford.math.plex4.homology.chain_basis.PrimitiveBasisElement;
import edu.stanford.math.primitivelib.autogen.formal_sum.IntAlgebraicFreeModule;
import edu.stanford.math.primitivelib.autogen.formal_sum.IntSparseFormalSum;
import edu.stanford.math.primitivelib.autogen.pair.ObjectObjectPair;
import gnu.trove.TObjectIntIterator;

public class SimplexStreamUtility {


	public static <U> IntSparseFormalSum<U> projectFirst(IntSparseFormalSum<? extends ObjectObjectPair<U, U>> chain, IntAlgebraicFreeModule<U> chainModule) {
		IntSparseFormalSum<U> result = new IntSparseFormalSum<U>();

		for (TObjectIntIterator<? extends ObjectObjectPair<U, U>> iterator = chain.iterator(); iterator.hasNext(); ) {
			iterator.advance();
			chainModule.accumulate(result, iterator.key().getFirst(), iterator.value());
		}

		return result;
	}

	public static <U> IntSparseFormalSum<U> projectSecond(IntSparseFormalSum<? extends ObjectObjectPair<U, U>> chain, IntAlgebraicFreeModule<U> chainModule) {
		IntSparseFormalSum<U> result = new IntSparseFormalSum<U>();

		for (TObjectIntIterator<? extends ObjectObjectPair<U, U>> iterator = chain.iterator(); iterator.hasNext(); ) {
			iterator.advance();
			chainModule.accumulate(result, iterator.key().getSecond(), iterator.value());
		}

		return result;
	}
	
	public static <U extends PrimitiveBasisElement> IntSparseFormalSum<U> filterByDimension(IntSparseFormalSum<U> chain, int dimension) {
		IntSparseFormalSum<U> result = new IntSparseFormalSum<U>();
		
		for (TObjectIntIterator<U> iterator = chain.iterator(); iterator.hasNext(); ) {
			iterator.advance();
			
			if (iterator.key().getDimension() == dimension) {
				result.put(iterator.value(), iterator.key());
			}
		}
		
		return result;
	}
}
