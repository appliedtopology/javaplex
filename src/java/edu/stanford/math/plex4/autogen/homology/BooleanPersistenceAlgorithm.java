package edu.stanford.math.plex4.autogen.homology;

import java.util.Comparator;
import java.util.Iterator;

import edu.stanford.math.plex4.homology.barcodes.IntAnnotatedBarcodeCollection;
import edu.stanford.math.plex4.homology.barcodes.IntBarcodeCollection;
import edu.stanford.math.plex4.homology.interfaces.AbstractPersistenceBasisAlgorithm;
import edu.stanford.math.plex4.streams.interfaces.AbstractFilteredStream;
import edu.stanford.math.plex4.streams.utility.FilteredComparator;
import edu.stanford.math.primitivelib.autogen.formal_sum.BooleanPrimitiveFreeModule;
import edu.stanford.math.primitivelib.autogen.formal_sum.BooleanSparseFormalSum;





/**
 * This class defines the functionality for a persistence algorithm with underlying
 * field type being boolean and underlying basis element type being U.
 * It acts as an intermediate layer between the interface AbstractPersistenceBasisAlgorithm
 * and the actual implementations of the persistent homology/cohomology algorithms.
 * 
 * @author autogen
 *
 * @param <boolean> the underlying type of the field
 * @param <U> the type of the basis elements
 */
public abstract class BooleanPersistenceAlgorithm<U> implements AbstractPersistenceBasisAlgorithm<U, BooleanSparseFormalSum<U>> {
		
	/**
	 * This objects performs the chain computations.
	 */
	protected final BooleanPrimitiveFreeModule<U> chainModule;
	
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
	 * This constructor initializes the object with a comparator on the basis type.
	 * 
	 * @param basisComparator a comparator on the basis type U
	 * @param minDimension the minimum dimension to compute 
	 * @param maxDimension the maximum dimension to compute
	 */
	public BooleanPersistenceAlgorithm(Comparator<U> basisComparator, int minDimension, int maxDimension) {
		this.chainModule = new BooleanPrimitiveFreeModule<U>();
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

	/**
	 * This function returns the free module used for the arithmetic computations.
	 * 
	 * @return the free module over chains in U
	 */
	public BooleanPrimitiveFreeModule<U> getChainModule() {
		return this.chainModule;
	}
	
		
	public IntBarcodeCollection computeIntervals(AbstractFilteredStream<U> stream) {
		this.initializeFilteredComparator(stream);
		return this.computeIntervalsImpl(stream);
	}

	public IntAnnotatedBarcodeCollection<BooleanSparseFormalSum<U>> computeAnnotatedIntervals(AbstractFilteredStream<U> stream) {
		this.initializeFilteredComparator(stream);
		return this.computeAnnotatedIntervalsImpl(stream);
	}
	
	/**
	 * This function provides the implementation of computeIntervals.
	 * 
	 * @param stream the filtered chain complex 
	 * @return the persistence intervals of the given complex
	 */
	protected abstract IntBarcodeCollection computeIntervalsImpl(AbstractFilteredStream<U> stream);
	
	/**
	 * This function provides the implementation of computeAnnotatedIntervals.
	 * 
	 * @param stream the filtered chain complex
	 * @return the augmented persistence intervals
	 */
	protected abstract IntAnnotatedBarcodeCollection<BooleanSparseFormalSum<U>> computeAnnotatedIntervalsImpl(AbstractFilteredStream<U> stream);
	
	/**
	 * This function computes the operation low_A(j) as described in the paper. Note that if
	 * the chain is empty (for example the column contains only zeros), then this function
	 * returns null.
	 * 
	 * @param formalSum
	 * @return
	 */
	protected U low(BooleanSparseFormalSum<U> chain) {
	
		U maxObject = null;
		
				U current = null;
		for (Iterator<U> iterator = chain.iterator(); iterator.hasNext(); ) {
			current = iterator.next();
			if (maxObject == null || this.filteredComparator.compare(current, maxObject) > 0) {
				maxObject = current;
			}
		}
		
		
		return maxObject;
	}
}
