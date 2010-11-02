package edu.stanford.math.plex4.homology;

import java.util.Comparator;

import edu.stanford.math.plex4.homology.barcodes.IntAugmentedBarcodeCollection;
import edu.stanford.math.plex4.homology.barcodes.IntBarcodeCollection;
import edu.stanford.math.plex4.homology.streams.interfaces.AbstractFilteredStream;
import edu.stanford.math.primitivelib.autogen.algebraic.ObjectAbstractField;
import edu.stanford.math.primitivelib.autogen.formal_sum.ObjectSparseFormalSum;
import edu.stanford.math.primitivelib.autogen.pair.ObjectObjectPair;
import gnu.trove.THashMap;

public class GenericAbsoluteHomology<F, T> extends GenericPersistentHomology<F, T> {

	public GenericAbsoluteHomology(ObjectAbstractField<F> field, Comparator<T> comparator, int minDimension, int maxDimension) {
		super(field, comparator, minDimension, maxDimension);
	}
	
	public GenericAbsoluteHomology(ObjectAbstractField<F> field, Comparator<T> comparator, int maxDimension) {
		super(field, comparator, maxDimension);
	}

	@Override
	protected IntAugmentedBarcodeCollection<ObjectSparseFormalSum<F, T>> getAugmentedIntervals(
			ObjectObjectPair<THashMap<T, ObjectSparseFormalSum<F, T>>, THashMap<T, ObjectSparseFormalSum<F, T>>> RV_pair, AbstractFilteredStream<T> stream) {
		return this.getAugmentedIntervals(RV_pair, stream, true);
	}

	@Override
	protected IntBarcodeCollection getIntervals(ObjectObjectPair<THashMap<T, ObjectSparseFormalSum<F, T>>, THashMap<T, ObjectSparseFormalSum<F, T>>> RV_pair,
			AbstractFilteredStream<T> stream) {
		return this.getIntervals(RV_pair, stream, true);
	}
}
