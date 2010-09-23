package edu.stanford.math.plex4.homology;

import java.util.Comparator;

import edu.stanford.math.plex4.algebraic_structures.interfaces.IntField;
import edu.stanford.math.plex4.datastructures.pairs.GenericPair;
import edu.stanford.math.plex4.free_module.IntFormalSum;
import edu.stanford.math.plex4.homology.barcodes.AugmentedBarcodeCollection;
import edu.stanford.math.plex4.homology.barcodes.BarcodeCollection;
import edu.stanford.math.plex4.homology.streams.interfaces.AbstractFilteredStream;
import gnu.trove.THashMap;

public class IntAbsoluteHomology<T> extends IntPersistentHomology<T> {

	public IntAbsoluteHomology(IntField field, Comparator<T> comparator, int minDimension, int maxDimension) {
		super(field, comparator, minDimension, maxDimension);
	}
	
	public IntAbsoluteHomology(IntField field, Comparator<T> comparator, int maxDimension) {
		super(field, comparator, maxDimension);
	}

	@Override
	protected AugmentedBarcodeCollection<IntFormalSum<T>> getAugmentedIntervals(
			GenericPair<THashMap<T, IntFormalSum<T>>, THashMap<T, IntFormalSum<T>>> RV_pair, AbstractFilteredStream<T> stream) {
		return this.getAugmentedIntervals(RV_pair, stream, true);
	}

	@Override
	protected BarcodeCollection getIntervals(GenericPair<THashMap<T, IntFormalSum<T>>, THashMap<T, IntFormalSum<T>>> RV_pair,
			AbstractFilteredStream<T> stream) {
		return this.getIntervals(RV_pair, stream, true);
	}
}
