package edu.stanford.math.plex4.homology;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import edu.stanford.math.plex4.homology.barcodes.IntAugmentedBarcodeCollection;
import edu.stanford.math.plex4.homology.barcodes.IntBarcodeCollection;
import edu.stanford.math.plex4.homology.streams.interfaces.AbstractFilteredStream;
import edu.stanford.math.plex4.homology.streams.utility.FilteredComparator;
import edu.stanford.math.primitivelib.autogen.algebraic.IntAbstractField;
import edu.stanford.math.primitivelib.autogen.formal_sum.IntAlgebraicFreeModule;
import edu.stanford.math.primitivelib.autogen.formal_sum.IntSparseFormalSum;
import gnu.trove.TObjectIntIterator;

/**
 * This class implements the persistent homology and cohomology algorithms
 * described in the paper "Dualities in Persistent (Co)homology" by Vin
 * de Silva, Dmitriy Morozov, and Mikael Vejdemo-Johansson. In the above
 * paper, the authors describe algorithms for computing barcodes and 
 * generating (co)cycles for each of the four persistent objects:
 * - absolute homology
 * - absolute cohomology
 * - relative homology
 * - relative cohomology
 * 
 * In most cases, the user will probably be interested in computing barcodes
 * for persistent absolute homology. In this case, it turns out that the 
 * barcodes produced by absolute cohomology are the same. Due to the performance
 * benefits of computing persistent cohomology over homology (as described in the
 * above paper), we suggest that the user try the persistent cohomology
 * algorithm in cases where only barcodes are needed. 
 * 
 * @author Andrew Tausz
 *
 * @param <F> the type of the field over which we perform the computations 
 * @param <T> the type of the basis elements in the chain complex vector spaces
 */
public abstract class IntPersistenceAlgorithm<T> {
	/**
	 * This is the field over which we perform the arithmetic computations.
	 */
	protected final IntAbstractField field;
	
	/**
	 * This comparator defines the ordering on the basis elements.
	 */
	protected final Comparator<T> basisComparator;
	
	/**
	 * This comparator provides the dictionary ordering on filtration value - basis element
	 * pairs.
	 */
	protected Comparator<T> filteredComparator;
	
	/**
	 * This objects performs the chain computations.
	 */
	protected IntAlgebraicFreeModule<T> chainModule;

	/**
	 * This stores the minimum dimension for which to compute (co)homology.
	 */
	protected int minDimension = 0;
	
	/**
	 * This stores the maximum dimension for which to compute (co)homology.
	 */
	protected int maxDimension = 2;	
	
	protected double minGranularity = 0.001;
	
	/**
	 * This constructor initializes the object with its essential field values.
	 * 
	 * @param field the field over which to perform arithmetic
	 * @param basisComparator a Comparator for ordering the basis elements
	 * @param maxDimension the maximum dimension to compute to
	 */
	public IntPersistenceAlgorithm(IntAbstractField field, Comparator<T> basisComparator, int minDimension, int maxDimension) {
		this.field = field;
		this.basisComparator = basisComparator;
		this.minDimension = minDimension;
		this.maxDimension = maxDimension;
	}
	
	public IntBarcodeCollection computeIntervals(AbstractFilteredStream<T> stream) {
		this.initializeFilteredComparator(stream);
		return this.computeIntervalsImpl(stream);
	}
	public IntAugmentedBarcodeCollection<IntSparseFormalSum<T>> computeAugmentedIntervals(AbstractFilteredStream<T> stream) {
		this.initializeFilteredComparator(stream);
		return this.computeAugmentedIntervalsImpl(stream);
	}
	
	protected abstract IntBarcodeCollection computeIntervalsImpl(AbstractFilteredStream<T> stream);
	protected abstract IntAugmentedBarcodeCollection<IntSparseFormalSum<T>> computeAugmentedIntervalsImpl(AbstractFilteredStream<T> stream);
	
	protected void initializeFilteredComparator(AbstractFilteredStream<T> stream) {
		this.filteredComparator = new FilteredComparator<T>(stream, this.basisComparator);
		this.chainModule = new IntAlgebraicFreeModule<T>(this.field);
	}
	
	/**
	 * This function computes the operation low_A(j) as described in the paper. Note that if
	 * the chain is empty (for example the column contains only zeros), then this function
	 * returns null.
	 * 
	 * @param formalSum
	 * @return
	 */
	protected T low(IntSparseFormalSum<T> chain) {
		T maxObject = null;
		
		for (TObjectIntIterator<T> iterator = chain.iterator(); iterator.hasNext(); ) {
			iterator.advance();
			if (maxObject == null || this.filteredComparator.compare(iterator.key(), maxObject) > 0) {
				maxObject = iterator.key();
			}
		}

		return maxObject;
	}
	
	/**
	 * This function returns the columns of the boundary matrix of specified dimension.
	 * 
	 * @param stream the filtered chain complex
	 * @param dimension the dimension at which to get the boundary matrix
	 * @return the columns of the boundary matrix at the specified dimension
	 */
	public List<IntSparseFormalSum<T>> getBoundaryColumns(AbstractFilteredStream<T> stream, int dimension) {
		this.initializeFilteredComparator(stream);
		List<IntSparseFormalSum<T>> D = new ArrayList<IntSparseFormalSum<T>>();

		for (T i: stream) {
			if (stream.getDimension(i) != dimension) {
				continue;
			}

			D.add(chainModule.createNewSum(stream.getBoundaryCoefficients(i), stream.getBoundary(i)));
		}

		return D;
	}
}
