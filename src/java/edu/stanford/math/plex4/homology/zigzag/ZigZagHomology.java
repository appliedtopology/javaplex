package edu.stanford.math.plex4.homology.zigzag;

import java.util.Comparator;

import edu.stanford.math.plex4.homology.barcodes.BarcodeCollection;
import edu.stanford.math.plex4.homology.chain_basis.PrimitiveBasisElement;
import edu.stanford.math.plex4.homology.interfaces.AbstractPersistenceAlgorithm;
import edu.stanford.math.plex4.streams.interfaces.AbstractFilteredStream;
import edu.stanford.math.plex4.streams.utility.FilteredComparator;
import edu.stanford.math.primitivelib.autogen.algebraic.IntAbstractField;
import edu.stanford.math.primitivelib.autogen.formal_sum.IntAlgebraicFreeModule;

public class ZigZagHomology<U extends PrimitiveBasisElement> implements AbstractPersistenceAlgorithm<U> {
	/**
	 * This is the field over which we perform the arithmetic computations.
	 */
	protected final IntAbstractField field;

	/**
	 * This objects performs the chain computations.
	 */
	protected final IntAlgebraicFreeModule<U> chainModule;

	/**
	 * This comparator defines the ordering on the basis elements.
	 */
	protected final Comparator<U> basisComparator;

	/**
	 * This comparator provides the dictionary ordering on filtration value - basis element
	 * pairs.
	 */
	protected Comparator<U> filteredComparator = null;

	/**
	 * This stores the minimum dimension for which to compute (co)homology.
	 */
	protected int minDimension = 0;

	/**
	 * This stores the maximum dimension for which to compute (co)homology.
	 */
	protected int maxDimension = 2;	

	/**
	 * This constructor initializes the object with a field and a comparator on the basis type.
	 * 
	 * @param field a field structure on the type int
	 * @param basisComparator a comparator on the basis type U
	 * @param minDimension the minimum dimension to compute 
	 * @param maxDimension the maximum dimension to compute
	 */
	public ZigZagHomology(IntAbstractField field, Comparator<U> basisComparator, int minDimension, int maxDimension) {
		this.field = field;
		this.chainModule = new IntAlgebraicFreeModule<U>(this.field);
		this.basisComparator = basisComparator;
		this.minDimension = minDimension;
		this.maxDimension = maxDimension;
	}

	/**
	 * This function simply updates the filtered comparator to the one induced by the given filtered stream.
	 * 
	 * @param stream the AbstractFilteredStream that provides the filtration index information
	 */
	protected void initializeFilteredComparator(AbstractFilteredStream<U> stream) {
		this.filteredComparator = new FilteredComparator<U>(stream, this.basisComparator);
	}

	public BarcodeCollection<Integer> computeIntervals(AbstractFilteredStream<U> stream) {
		this.initializeFilteredComparator(stream);
		HomologyBasisTracker<U> basisTracker = new HomologyBasisTracker<U>(this.field, this.basisComparator);

		basisTracker.getIntervalTracker().setUseRightClosedIntervals(false);
		
		for (U sigma : stream) {
			/*
			 * Do not process simplices of higher dimension than maxDimension.
			 */
			if (stream.getDimension(sigma) < this.minDimension) {
				continue;
			}

			if (stream.getDimension(sigma) > this.maxDimension + 1) {
				continue;
			}

			basisTracker.add(sigma, stream.getFiltrationIndex(sigma));
		}

		return basisTracker.getBarcodes();
	}
}
