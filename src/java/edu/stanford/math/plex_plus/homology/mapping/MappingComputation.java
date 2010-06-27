package edu.stanford.math.plex_plus.homology.mapping;

import java.util.Comparator;

import edu.stanford.math.plex_plus.algebraic_structures.interfaces.IntField;
import edu.stanford.math.plex_plus.datastructures.IntFormalSum;
import edu.stanford.math.plex_plus.homology.PersistentHomology;
import edu.stanford.math.plex_plus.homology.barcodes.AugmentedBarcodeCollection;
import edu.stanford.math.plex_plus.homology.simplex.ChainBasisElement;
import edu.stanford.math.plex_plus.homology.simplex.HomProductPair;
import edu.stanford.math.plex_plus.homology.simplex_streams.HomStream;
import edu.stanford.math.plex_plus.homology.simplex_streams.SimplexStream;
import gnu.trove.map.hash.THashMap;

public class MappingComputation<T extends ChainBasisElement, U extends ChainBasisElement> {
	private final IntField field;
	
	public MappingComputation(IntField field) {
		this.field = field;
	}
	
	public void computeMapping(SimplexStream<T> stream1, SimplexStream<U> stream2, Comparator<T> comparator1, Comparator<U> comparator2) {
		HomStream<T, U> homStream = new HomStream<T, U>(stream1, stream2, comparator1, comparator2);
		homStream.finalizeStream();
		
		PersistentHomology<HomProductPair<T, U>> homology = new PersistentHomology<HomProductPair<T, U>>(field, homStream.getBasisComparator());
		AugmentedBarcodeCollection<HomProductPair<T, U>> barcodes = homology.computeIntervals(homStream, 1);
		System.out.println(barcodes);
		
		THashMap<HomProductPair<T, U>, IntFormalSum<HomProductPair<T, U>>> D = homology.getBoundaryMapping(homStream, 1);
		System.out.println(D);
	}
}
