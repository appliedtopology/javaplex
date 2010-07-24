package edu.stanford.math.plex4.homology;

import java.util.Comparator;

import edu.stanford.math.plex4.algebraic_structures.interfaces.GenericField;
import edu.stanford.math.plex4.datastructures.pairs.GenericPair;
import edu.stanford.math.plex4.free_module.AbstractGenericFormalSum;
import edu.stanford.math.plex4.homology.barcodes.AugmentedBarcodeCollection;
import edu.stanford.math.plex4.homology.barcodes.BarcodeCollection;
import edu.stanford.math.plex4.homology.streams.interfaces.AbstractFilteredStream;
import gnu.trove.map.hash.THashMap;

public class GenericRelativeHomology<F, T> extends GenericPersistentHomology<F, T> {

	public GenericRelativeHomology(GenericField<F> field, Comparator<T> comparator, int minDimension, int maxDimension) {
		super(field, comparator, minDimension, maxDimension);
	}
	
	public GenericRelativeHomology(GenericField<F> field, Comparator<T> comparator, int maxDimension) {
		super(field, comparator, maxDimension);
	}
	
	@Override
	protected AugmentedBarcodeCollection<AbstractGenericFormalSum<F, T>> getAugmentedIntervals(
			GenericPair<THashMap<T, AbstractGenericFormalSum<F, T>>, THashMap<T, AbstractGenericFormalSum<F, T>>> RV_pair, AbstractFilteredStream<T> stream) {
		return this.getAugmentedIntervals(RV_pair, stream, false);
	}

	@Override
	protected BarcodeCollection getIntervals(GenericPair<THashMap<T, AbstractGenericFormalSum<F, T>>, THashMap<T, AbstractGenericFormalSum<F, T>>> RV_pair,
			AbstractFilteredStream<T> stream) {
		return this.getIntervals(RV_pair, stream, false);
	}
}
