package edu.stanford.math.plex_plus.homology.mapping;

import java.util.Comparator;
import java.util.List;

import edu.stanford.math.plex_plus.algebraic_structures.impl.GenericFreeModule;
import edu.stanford.math.plex_plus.algebraic_structures.interfaces.GenericOrderedField;
import edu.stanford.math.plex_plus.datastructures.GenericFormalSum;
import edu.stanford.math.plex_plus.datastructures.pairs.GenericPair;
import edu.stanford.math.plex_plus.homology.GenericPersistentHomology;
import edu.stanford.math.plex_plus.homology.barcodes.AugmentedBarcodeCollection;
import edu.stanford.math.plex_plus.homology.streams.derived.HomStream;
import edu.stanford.math.plex_plus.homology.streams.interfaces.AbstractFilteredStream;


public class HomComplexComputation<F extends Number, T, U> {
	private final GenericOrderedField<F> field;
	private final GenericFreeModule<F, GenericPair<T, U>> chainModule;

	public HomComplexComputation(GenericOrderedField<F> field) {
		this.field = field;
		chainModule = new GenericFreeModule<F, GenericPair<T, U>>(this.field);
	}
	
	public void computeMapping(AbstractFilteredStream<T> stream1, AbstractFilteredStream<U> stream2, Comparator<T> comparator1, Comparator<U> comparator2) {
		HomStream<T, U> homStream = new HomStream<T, U>(stream1, stream2, comparator1, comparator2);
		homStream.finalizeStream();

		GenericPersistentHomology<F, GenericPair<T, U>> homology = new GenericPersistentHomology<F, GenericPair<T, U>>(field, homStream.getDerivedComparator());
		AugmentedBarcodeCollection<GenericFormalSum<F, GenericPair<T, U>>> barcodes = homology.computeIntervals(homStream, 1);
		System.out.println(barcodes);

		List<GenericFormalSum<F, GenericPair<T, U>>> D_1 = homology.getBoundaryColumns(homStream, 1);
		System.out.println(D_1);

		GenericFormalSum<F, GenericPair<T, U>> generatingCycle = new GenericFormalSum<F, GenericPair<T, U>>();
		int numCycles = barcodes.getBarcode(0).getSize();
		for (int i = 0; i < numCycles; i++) {
			generatingCycle = chainModule.add(generatingCycle, barcodes.getBarcode(0).getGeneratingCycle(i));
		}
	}
}
