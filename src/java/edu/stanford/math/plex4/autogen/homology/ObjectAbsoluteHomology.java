package edu.stanford.math.plex4.autogen.homology;

import java.util.Comparator;

import edu.stanford.math.plex4.homology.barcodes.IntAugmentedBarcodeCollection;
import edu.stanford.math.plex4.homology.barcodes.IntBarcodeCollection;
import edu.stanford.math.plex4.streams.interfaces.AbstractFilteredStream;
import edu.stanford.math.primitivelib.autogen.algebraic.ObjectAbstractField;
import edu.stanford.math.primitivelib.autogen.formal_sum.ObjectSparseFormalSum;
import edu.stanford.math.primitivelib.autogen.pair.ObjectObjectPair;
import gnu.trove.THashMap;





public class ObjectAbsoluteHomology<F, U> extends ObjectPersistentHomology<F, U> {
		/**
	 * This constructor initializes the object with a field and a comparator on the basis type.
	 * 
	 * @param field a field structure on the type F
	 * @param basisComparator a comparator on the basis type U
	 * @param minDimension the minimum dimension to compute 
	 * @param maxDimension the maximum dimension to compute
	 */
	public ObjectAbsoluteHomology(ObjectAbstractField<F> field, Comparator<U> basisComparator, int minDimension, int maxDimension) {
		super(field, basisComparator, minDimension, maxDimension);
	}
		
	@Override
	protected IntAugmentedBarcodeCollection<ObjectSparseFormalSum<F, U>> getAugmentedIntervals(
			ObjectObjectPair<THashMap<U, ObjectSparseFormalSum<F, U>>, 
			THashMap<U, ObjectSparseFormalSum<F, U>>> RV_pair, 
			AbstractFilteredStream<U> stream) {
			
		return this.getAugmentedIntervals(RV_pair, stream, true);
	}

	@Override
	protected IntBarcodeCollection getIntervals(ObjectObjectPair<THashMap<U, ObjectSparseFormalSum<F, U>>, 
			THashMap<U, ObjectSparseFormalSum<F, U>>> RV_pair,
			AbstractFilteredStream<U> stream) {
			
		return this.getIntervals(RV_pair, stream, true);
	}
}
