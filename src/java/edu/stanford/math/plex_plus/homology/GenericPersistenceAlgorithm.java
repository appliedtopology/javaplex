package edu.stanford.math.plex_plus.homology;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import edu.stanford.math.plex_plus.algebraic_structures.impl.GenericFreeModule;
import edu.stanford.math.plex_plus.algebraic_structures.interfaces.GenericField;
import edu.stanford.math.plex_plus.datastructures.GenericFormalSum;
import edu.stanford.math.plex_plus.datastructures.pairs.DoubleGenericPair;
import edu.stanford.math.plex_plus.datastructures.pairs.DoubleGenericPairComparator;
import edu.stanford.math.plex_plus.homology.barcodes.AugmentedBarcodeCollection;
import edu.stanford.math.plex_plus.homology.barcodes.BarcodeCollection;
import edu.stanford.math.plex_plus.homology.streams.interfaces.AbstractFilteredStream;

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
public abstract class GenericPersistenceAlgorithm<F, T> {
	/**
	 * This is the field over which we perform the arithmetic computations.
	 */
	protected final GenericField<F> field;
	
	/**
	 * This comparator defines the ordering on the basis elements.
	 */
	protected final Comparator<T> comparator;
	
	/**
	 * This comparator provides the dictionary ordering on filtration value - basis element
	 * pairs.
	 */
	protected final DoubleGenericPairComparator<T> filteredComparator;
	
	/**
	 * This objects performs the chain computations.
	 */
	protected final GenericFreeModule<F, T> chainModule;

	/**
	 * This stores the minimum dimension for which to compute (co)homology.
	 */
	protected int minDimension = 0;
	
	/**
	 * This stores the maximum dimension for which to compute (co)homology.
	 */
	protected int maxDimension = 2;	
	
	/**
	 * This constructor initializes the object with its essential field values.
	 * 
	 * @param field the field over which to perform arithmetic
	 * @param comparator a Comparator for ordering the basis elements
	 * @param maxDimension the maximum dimension to compute to
	 */
	public GenericPersistenceAlgorithm(GenericField<F> field, Comparator<T> comparator, int minDimension, int maxDimension) {
		this.field = field;
		this.comparator = comparator;
		this.filteredComparator = new DoubleGenericPairComparator<T>(this.comparator);
		this.chainModule = new GenericFreeModule<F, T>(field);
		this.minDimension = minDimension;
		this.maxDimension = maxDimension;
	}
	
	public abstract BarcodeCollection computeIntervals(AbstractFilteredStream<T> stream);
	
	public abstract AugmentedBarcodeCollection<GenericFormalSum<F, T>> computeAugmentedIntervals(AbstractFilteredStream<T> stream);
	
	
	/**
	 * This function computes the operation low_A(j) as described in the paper. Note that if
	 * the chain is empty (for example the column contains only zeros), then this function
	 * returns null.
	 * 
	 * @param formalSum
	 * @return
	 */
	protected T low(GenericFormalSum<F, T> chain) {
		T maxObject = null;

		for (Iterator<Entry<T, F>> iterator = chain.iterator(); iterator.hasNext(); ) {
			Entry<T, F> entry = iterator.next();
			if (maxObject == null) {
				maxObject = entry.getKey();
			}

			if (this.comparator.compare(entry.getKey(), maxObject) > 0) {
				maxObject = entry.getKey();
			}
		}

		return maxObject;
	}
	
	protected int compare(T a, T b, AbstractFilteredStream<T> stream) {
		return this.filteredComparator.compare(new DoubleGenericPair<T>(stream.getFiltrationValue(a), a), new DoubleGenericPair<T>(stream.getFiltrationValue(b), b));
	}
	
	/**
	 * This function returns the columns of the boundary matrix of specified dimension.
	 * 
	 * @param stream the filtered chain complex
	 * @param dimension the dimension at which to get the boundary matrix
	 * @return the columns of the boundary matrix at the specified dimension
	 */
	public List<GenericFormalSum<F, T>> getBoundaryColumns(AbstractFilteredStream<T> stream, int dimension) {
		List<GenericFormalSum<F, T>> D = new ArrayList<GenericFormalSum<F, T>>();

		for (T i: stream) {
			if (stream.getDimension(i) != dimension) {
				continue;
			}

			D.add(chainModule.createSum(stream.getBoundaryCoefficients(i), stream.getBoundary(i)));
		}

		return D;
	}
}
