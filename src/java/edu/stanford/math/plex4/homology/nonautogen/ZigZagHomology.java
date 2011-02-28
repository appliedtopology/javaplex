package edu.stanford.math.plex4.homology.nonautogen;

import java.util.Comparator;
import java.util.List;

import edu.stanford.math.plex4.homology.barcodes.IntBarcodeCollection;
import edu.stanford.math.plex4.homology.interfaces.AbstractPersistenceAlgorithm;
import edu.stanford.math.plex4.streams.interfaces.AbstractFilteredStream;
import edu.stanford.math.plex4.streams.utility.FilteredComparator;
import edu.stanford.math.primitivelib.autogen.algebraic.IntAbstractField;
import edu.stanford.math.primitivelib.autogen.formal_sum.IntAlgebraicFreeModule;
import edu.stanford.math.primitivelib.autogen.pair.ObjectObjectPair;
import gnu.trove.THashMap;

public class ZigZagHomology<U> implements AbstractPersistenceAlgorithm<U> {
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

	public IntBarcodeCollection computeIntervals(AbstractFilteredStream<U> stream) {
		this.initializeFilteredComparator(stream);
		ZigZagPrototype<U> zz = new ZigZagPrototype<U>(this.field, this.basisComparator, stream);

		IntBarcodeCollection collection = new IntBarcodeCollection();

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

			zz.add(sigma, chainModule.createNewSum(stream.getBoundaryCoefficients(sigma), stream.getBoundary(sigma)));
		}

		List<ObjectObjectPair<Integer, U>> pairs = zz.getPairs();
		THashMap<Integer, U> birth = zz.getBirth();

		for (ObjectObjectPair<Integer, U> pair: pairs) {
			U sigma_j = birth.get(pair.getFirst());
			int dimension = stream.getDimension(sigma_j);
			int k = stream.getFiltrationIndex(sigma_j);
			int j = stream.getFiltrationIndex(pair.getSecond());
			if (k < j && dimension < this.maxDimension) {
				collection.addInterval(dimension, k, j);
			}

			birth.remove(pair.getFirst());
		}

		for (Integer index: birth.keySet()) {
			int dimension = stream.getDimension(birth.get(index));
			int k = stream.getFiltrationIndex(birth.get(index));
			if (dimension < this.maxDimension) {
				collection.addRightInfiniteInterval(dimension, k);
			}
		}

		return collection;
	}

}
