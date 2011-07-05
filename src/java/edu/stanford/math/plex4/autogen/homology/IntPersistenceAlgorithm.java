package edu.stanford.math.plex4.autogen.homology;

import java.util.Comparator;

import javax.annotation.Generated;

import edu.stanford.math.plex4.homology.barcodes.AnnotatedBarcodeCollection;
import edu.stanford.math.plex4.homology.barcodes.BarcodeCollection;
import edu.stanford.math.plex4.homology.interfaces.AbstractPersistenceBasisAlgorithm;
import edu.stanford.math.plex4.streams.interfaces.AbstractFilteredStream;
import edu.stanford.math.plex4.streams.utility.FilteredComparator;
import edu.stanford.math.primitivelib.autogen.algebraic.IntAbstractField;
import edu.stanford.math.primitivelib.autogen.formal_sum.IntAlgebraicFreeModule;
import edu.stanford.math.primitivelib.autogen.formal_sum.IntSparseFormalSum;
import gnu.trove.TObjectIntIterator;





/**
 * This class defines the functionality for a persistence algorithm with underlying
 * field type being int and underlying basis element type being U.
 * It acts as an intermediate layer between the interface AbstractPersistenceBasisAlgorithm
 * and the actual implementations of the persistent homology/cohomology algorithms.
 * 
 * <p>int the underlying type of the coefficient field</p>
 * <p>U the underlying basis type</p>
 * 
 * @author autogen
 *
 */
@Generated(value = { "edu.stanford.math.plex4.generation.GeneratorDriver" })
public abstract class IntPersistenceAlgorithm<U> extends AbstractPersistenceBasisAlgorithm<U, IntSparseFormalSum<U>> {
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
	public IntPersistenceAlgorithm(IntAbstractField field, Comparator<U> basisComparator, int minDimension, int maxDimension) {
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

	/**
	 * This function returns the free module used for the arithmetic computations.
	 * 
	 * @return the free module over chains in U
	 */
	public IntAlgebraicFreeModule<U> getChainModule() {
		return this.chainModule;
	}
	
		/**
	 * This function returns the field over which the homology is computed.
	 * 
	 * @return the field over type int
	 */
	public IntAbstractField getField() {
		return this.field;
	}
		
	public BarcodeCollection<Integer> computeIndexIntervals(AbstractFilteredStream<U> stream) {
		this.initializeFilteredComparator(stream);
		return this.computeIntervalsImpl(stream);
	}

	public AnnotatedBarcodeCollection<Integer, IntSparseFormalSum<U>> computeAnnotatedIndexIntervals(AbstractFilteredStream<U> stream) {
		this.initializeFilteredComparator(stream);
		return this.computeAnnotatedIntervalsImpl(stream);
	}
	
	/**
	 * This function provides the implementation of computeIntervals.
	 * 
	 * @param stream the filtered chain complex 
	 * @return the persistence intervals of the given complex
	 */
	protected abstract BarcodeCollection<Integer> computeIntervalsImpl(AbstractFilteredStream<U> stream);
	
	/**
	 * This function provides the implementation of computeAnnotatedIntervals.
	 * 
	 * @param stream the filtered chain complex
	 * @return the augmented persistence intervals
	 */
	protected abstract AnnotatedBarcodeCollection<Integer, IntSparseFormalSum<U>> computeAnnotatedIntervalsImpl(AbstractFilteredStream<U> stream);
	
	/**
	 * This function computes the operation low_A(j) as described in the paper. Note that if
	 * the chain is empty (for example the column contains only zeros), then this function
	 * returns null.
	 * 
	 * @param chain the chain to search
	 * @return  the lowest element of the chain
	 */
	protected U low(IntSparseFormalSum<U> chain) {
	
		U maxObject = null;
		
				
				
		for (TObjectIntIterator<U> iterator = chain.iterator(); iterator.hasNext(); ) {
			iterator.advance();
			if (maxObject == null || this.filteredComparator.compare(iterator.key(), maxObject) > 0) {
				maxObject = iterator.key();
			}
		}
		
		
		
		return maxObject;
	}
}
