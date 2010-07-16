package edu.stanford.math.plex_plus.homology;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import edu.stanford.math.plex_plus.algebraic_structures.interfaces.GenericField;
import edu.stanford.math.plex_plus.free_module.AbstractGenericFormalSum;
import edu.stanford.math.plex_plus.free_module.AbstractGenericFreeModule;
import edu.stanford.math.plex_plus.free_module.OrderedGenericFormalSum;
import edu.stanford.math.plex_plus.free_module.UnorderedGenericFormalSum;
import edu.stanford.math.plex_plus.free_module.UnorderedGenericFreeModule;
import edu.stanford.math.plex_plus.homology.barcodes.AugmentedBarcodeCollection;
import edu.stanford.math.plex_plus.homology.barcodes.BarcodeCollection;
import edu.stanford.math.plex_plus.homology.streams.interfaces.AbstractFilteredStream;
import edu.stanford.math.plex_plus.homology.streams.utility.FilteredComparator;

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
	protected final Comparator<T> basisComparator;
	
	/**
	 * This comparator provides the dictionary ordering on filtration value - basis element
	 * pairs.
	 */
	protected Comparator<T> filteredComparator;
	
	/**
	 * This objects performs the chain computations.
	 */
	protected AbstractGenericFreeModule<F, T> chainModule;

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
	public GenericPersistenceAlgorithm(GenericField<F> field, Comparator<T> basisComparator, int minDimension, int maxDimension) {
		this.field = field;
		this.basisComparator = basisComparator;
		this.minDimension = minDimension;
		this.maxDimension = maxDimension;
	}
	
	public BarcodeCollection computeIntervals(AbstractFilteredStream<T> stream) {
		this.initializeFilteredComparator(stream);
		return this.computeIntervalsImpl(stream);
	}
	public AugmentedBarcodeCollection<AbstractGenericFormalSum<F, T>> computeAugmentedIntervals(AbstractFilteredStream<T> stream) {
		this.initializeFilteredComparator(stream);
		return this.computeAugmentedIntervalsImpl(stream);
	}
	
	protected abstract BarcodeCollection computeIntervalsImpl(AbstractFilteredStream<T> stream);
	protected abstract AugmentedBarcodeCollection<AbstractGenericFormalSum<F, T>> computeAugmentedIntervalsImpl(AbstractFilteredStream<T> stream);
	
	protected void initializeFilteredComparator(AbstractFilteredStream<T> stream) {
		this.filteredComparator = new FilteredComparator<T>(stream, this.basisComparator);
		//this.chainModule = new OrderedGenericFreeModule<F, T>(this.field, this.filteredComparator);
		this.chainModule = new UnorderedGenericFreeModule<F, T>(this.field);
	}
	
	/**
	 * This function computes the operation low_A(j) as described in the paper. Note that if
	 * the chain is empty (for example the column contains only zeros), then this function
	 * returns null.
	 * 
	 * @param formalSum
	 * @return
	 */
	protected T low(AbstractGenericFormalSum<F, T> chain) {
		if (chain.isEmpty()) {
			return null;
		}

		if (chain instanceof OrderedGenericFormalSum<?, ?>) {
			return this.orderedLow((OrderedGenericFormalSum<F, T>) chain);
		}else if (chain instanceof UnorderedGenericFormalSum<?, ?>) {
			return this.unorderedLow((UnorderedGenericFormalSum<F, T>) chain);
		}else {
			throw new UnsupportedOperationException();
		}
	}
	
	protected T orderedLow(OrderedGenericFormalSum<F, T> chain) {
		return chain.maximumObject();
	}
	
	protected T unorderedLow(UnorderedGenericFormalSum<F, T> chain) {
	
		T maxObject = null;
		
		for (Iterator<Entry<T, F>> iterator = chain.iterator(); iterator.hasNext(); ) {
			Entry<T, F> entry = iterator.next();
			if (maxObject == null || this.filteredComparator.compare(entry.getKey(), maxObject) > 0) {
				maxObject = entry.getKey();
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
	public List<AbstractGenericFormalSum<F, T>> getBoundaryColumns(AbstractFilteredStream<T> stream, int dimension) {
		this.initializeFilteredComparator(stream);
		List<AbstractGenericFormalSum<F, T>> D = new ArrayList<AbstractGenericFormalSum<F, T>>();

		for (T i: stream) {
			if (stream.getDimension(i) != dimension) {
				continue;
			}

			D.add(chainModule.createSum(stream.getBoundaryCoefficients(i), stream.getBoundary(i)));
		}

		return D;
	}
}
