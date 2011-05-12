package edu.stanford.math.plex4.autogen.homology;

import java.util.Comparator;

import javax.annotation.Generated;

import edu.stanford.math.plex4.homology.barcodes.IntAnnotatedBarcodeCollection;
import edu.stanford.math.plex4.homology.barcodes.IntBarcodeCollection;
import edu.stanford.math.plex4.streams.interfaces.AbstractFilteredStream;
import edu.stanford.math.primitivelib.autogen.formal_sum.BooleanSparseFormalSum;
import edu.stanford.math.primitivelib.autogen.pair.ObjectObjectPair;
import gnu.trove.THashMap;





@Generated(value = { "edu.stanford.math.plex4.generation.GeneratorDriver" })
public class BooleanAbsoluteHomology<U> extends BooleanPersistentHomology<U> {
		/**
	 * This constructor initializes the object with a comparator on the basis type.
	 * 
	 * @param basisComparator a comparator on the basis type U
	 * @param minDimension the minimum dimension to compute 
	 * @param maxDimension the maximum dimension to compute
	 */
	public BooleanAbsoluteHomology(Comparator<U> basisComparator, int minDimension, int maxDimension) {
		super(basisComparator, minDimension, maxDimension);
	}
		
	@Override
	protected IntAnnotatedBarcodeCollection<BooleanSparseFormalSum<U>> getAnnotatedIntervals(
			ObjectObjectPair<THashMap<U, BooleanSparseFormalSum<U>>, 
			THashMap<U, BooleanSparseFormalSum<U>>> RV_pair, 
			AbstractFilteredStream<U> stream) {
			
		return this.getAnnotatedIntervals(RV_pair, stream, true);
	}

	@Override
	protected IntBarcodeCollection getIntervals(ObjectObjectPair<THashMap<U, BooleanSparseFormalSum<U>>, 
			THashMap<U, BooleanSparseFormalSum<U>>> RV_pair,
			AbstractFilteredStream<U> stream) {
			
		return this.getIntervals(RV_pair, stream, true);
	}
}
