package edu.stanford.math.plex4.autogen.homology;

import java.util.Comparator;

import edu.stanford.math.plex4.homology.barcodes.IntAugmentedBarcodeCollection;
import edu.stanford.math.plex4.homology.barcodes.IntBarcodeCollection;
import edu.stanford.math.plex4.homology.streams.interfaces.AbstractFilteredStream;
import edu.stanford.math.primitivelib.autogen.algebraic.IntAbstractField;
import edu.stanford.math.primitivelib.autogen.formal_sum.IntSparseFormalSum;
import edu.stanford.math.primitivelib.autogen.pair.ObjectObjectPair;
import gnu.trove.THashMap;





public class IntAbsoluteHomology<U> extends IntPersistentHomology<U> {
		/**
	 * This constructor initializes the object with a field and a comparator on the basis type.
	 * 
	 * @param field a field structure on the type int
	 * @param basisComparator a comparator on the basis type U
	 * @param minDimension the minimum dimension to compute 
	 * @param maxDimension the maximum dimension to compute
	 */
	public IntAbsoluteHomology(IntAbstractField field, Comparator<U> basisComparator, int minDimension, int maxDimension) {
		super(field, basisComparator, minDimension, maxDimension);
	}
		
	@Override
	protected IntAugmentedBarcodeCollection<IntSparseFormalSum<U>> getAugmentedIntervals(
			ObjectObjectPair<THashMap<U, IntSparseFormalSum<U>>, 
			THashMap<U, IntSparseFormalSum<U>>> RV_pair, 
			AbstractFilteredStream<U> stream) {
			
		return this.getAugmentedIntervals(RV_pair, stream, true);
	}

	@Override
	protected IntBarcodeCollection getIntervals(ObjectObjectPair<THashMap<U, IntSparseFormalSum<U>>, 
			THashMap<U, IntSparseFormalSum<U>>> RV_pair,
			AbstractFilteredStream<U> stream) {
			
		return this.getIntervals(RV_pair, stream, true);
	}
}
