package edu.stanford.math.plex4.homology;

import java.util.Comparator;

import edu.stanford.math.plex4.homology.barcodes.IntAugmentedBarcodeCollection;
import edu.stanford.math.plex4.homology.barcodes.IntBarcodeCollection;
import edu.stanford.math.plex4.homology.streams.interfaces.AbstractFilteredStream;
import edu.stanford.math.primitivelib.autogen.algebraic.IntAbstractField;
import edu.stanford.math.primitivelib.autogen.formal_sum.IntSparseFormalSum;
import edu.stanford.math.primitivelib.autogen.pair.ObjectObjectPair;
import gnu.trove.THashMap;

public class IntAbsoluteHomology<T> extends IntPersistentHomology<T> {

	public IntAbsoluteHomology(IntAbstractField field, Comparator<T> comparator, int minDimension, int maxDimension) {
		super(field, comparator, minDimension, maxDimension);
	}
	
	public IntAbsoluteHomology(IntAbstractField field, Comparator<T> comparator, int maxDimension) {
		super(field, comparator, maxDimension);
	}

	@Override
	protected IntAugmentedBarcodeCollection<IntSparseFormalSum<T>> getAugmentedIntervals(
			ObjectObjectPair<THashMap<T, IntSparseFormalSum<T>>, THashMap<T, IntSparseFormalSum<T>>> RV_pair, AbstractFilteredStream<T> stream) {
		return this.getAugmentedIntervals(RV_pair, stream, true);
	}

	@Override
	protected IntBarcodeCollection getIntervals(ObjectObjectPair<THashMap<T, IntSparseFormalSum<T>>, THashMap<T, IntSparseFormalSum<T>>> RV_pair,
			AbstractFilteredStream<T> stream) {
		return this.getIntervals(RV_pair, stream, true);
	}
}
