package edu.stanford.math.plex4.homology;

import java.util.Comparator;

import edu.stanford.math.plex4.homology.barcodes.IntAugmentedBarcodeCollection;
import edu.stanford.math.plex4.homology.barcodes.IntBarcodeCollection;
import edu.stanford.math.plex4.homology.streams.interfaces.AbstractFilteredStream;
import edu.stanford.math.plex4.homology.streams.utility.FilteredComparator;
import edu.stanford.math.primitivelib.autogen.algebraic.ObjectAbstractField;
import edu.stanford.math.primitivelib.autogen.formal_sum.ObjectAlgebraicFreeModule;
import edu.stanford.math.primitivelib.autogen.formal_sum.ObjectSparseFormalSum;

public abstract class ObjectPersistenceAlgorithm<F, T> implements AbstractPersistenceBasisAlgorithm<T, ObjectSparseFormalSum<F, T>> {
	/**
	 * This is the field over which we perform the arithmetic computations.
	 */
	protected final ObjectAbstractField<F> field;
	
	/**
	 * This objects performs the chain computations.
	 */
	protected final ObjectAlgebraicFreeModule<F, T> chainModule;
	
	/**
	 * This comparator defines the ordering on the basis elements.
	 */
	protected final Comparator<T> basisComparator;
	
	/**
	 * This comparator provides the dictionary ordering on filtration value - basis element
	 * pairs.
	 */
	protected Comparator<T> filteredComparator = null;

	/**
	 * This constructor initializes the object with a field and a comparator on the basis type.
	 * 
	 * @param field a field structure on the type F
	 * @param basisComparator a comparator on the basis type T
	 */
	public ObjectPersistenceAlgorithm(ObjectAbstractField<F> field, Comparator<T> basisComparator) {
		this.field = field;
		this.chainModule = new ObjectAlgebraicFreeModule<F, T>(this.field);
		
		this.basisComparator = basisComparator;
	}

	/**
	 * This function simply updates the filtered comparator to the one induced by the given filtered stream.
	 * 
	 * @param stream the AbstractFilteredStream that provides the filtration index information
	 */
	protected void initializeFilteredComparator(AbstractFilteredStream<T> stream) {
		this.filteredComparator = new FilteredComparator<T>(stream, this.basisComparator);
	}
	
	public IntBarcodeCollection computeIntervals(AbstractFilteredStream<T> stream) {
		this.initializeFilteredComparator(stream);
		return this.computeIntervalsImpl(stream);
	}

	public IntAugmentedBarcodeCollection<ObjectSparseFormalSum<F, T>> computeAugmentedIntervals(AbstractFilteredStream<T> stream) {
		this.initializeFilteredComparator(stream);
		return this.computeAugmentedIntervalsImpl(stream);
	}
	
	/**
	 * This function provides the implementation of computeIntervals.
	 * 
	 * @param stream the filtered chain complex 
	 * @return the persistence intervals of the given complex
	 */
	protected abstract IntBarcodeCollection computeIntervalsImpl(AbstractFilteredStream<T> stream);
	
	/**
	 * This function provides the implementation of computeAugmentedIntervals.
	 * 
	 * @param stream the filtered chain complex
	 * @return the augmented persistence intervals
	 */
	protected abstract IntAugmentedBarcodeCollection<ObjectSparseFormalSum<F, T>> computeAugmentedIntervalsImpl(AbstractFilteredStream<T> stream);
}
