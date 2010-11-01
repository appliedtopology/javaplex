package edu.stanford.math.plex4.homology;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import edu.stanford.math.plex4.homology.barcodes.AugmentedBarcodeCollection;
import edu.stanford.math.plex4.homology.barcodes.BarcodeCollection;
import edu.stanford.math.plex4.homology.streams.interfaces.AbstractFilteredStream;
import edu.stanford.math.plex4.homology.streams.utility.FilteredComparator;
import edu.stanford.math.primitivelib.autogen.algebraic.ObjectAbstractField;
import edu.stanford.math.primitivelib.autogen.formal_sum.ObjectAlgebraicFreeModule;
import edu.stanford.math.primitivelib.autogen.formal_sum.ObjectSparseFormalSum;
import edu.stanford.math.primitivelib.autogen.pair.ObjectObjectPair;
import gnu.trove.THashMap;

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
	protected final ObjectAbstractField<F> field;
	
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
	protected ObjectAlgebraicFreeModule<F, T> chainModule;

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
	public GenericPersistenceAlgorithm(ObjectAbstractField<F> field, Comparator<T> basisComparator, int minDimension, int maxDimension) {
		this.field = field;
		this.basisComparator = basisComparator;
		this.minDimension = minDimension;
		this.maxDimension = maxDimension;
	}
	
	public BarcodeCollection computeIntervals(AbstractFilteredStream<T> stream) {
		this.initializeFilteredComparator(stream);
		return this.computeIntervalsImpl(stream);
	}
	public AugmentedBarcodeCollection<ObjectSparseFormalSum<F, T>> computeAugmentedIntervals(AbstractFilteredStream<T> stream) {
		this.initializeFilteredComparator(stream);
		return this.computeAugmentedIntervalsImpl(stream);
	}
	
	protected abstract BarcodeCollection computeIntervalsImpl(AbstractFilteredStream<T> stream);
	protected abstract AugmentedBarcodeCollection<ObjectSparseFormalSum<F, T>> computeAugmentedIntervalsImpl(AbstractFilteredStream<T> stream);
	
	protected void initializeFilteredComparator(AbstractFilteredStream<T> stream) {
		this.filteredComparator = new FilteredComparator<T>(stream, this.basisComparator);
		//this.chainModule = new OrderedGenericFreeModule<F, T>(this.field, this.filteredComparator);
		this.chainModule = new ObjectAlgebraicFreeModule<F, T>(this.field);
	}
	
	/**
	 * This function computes the operation low_A(j) as described in the paper. Note that if
	 * the chain is empty (for example the column contains only zeros), then this function
	 * returns null.
	 * 
	 * @param formalSum
	 * @return
	 */
	protected T low(ObjectSparseFormalSum<F, T> chain) {
	
		T maxObject = null;
		
		for (Iterator<Entry<T, F>> iterator = chain.iterator(); iterator.hasNext(); ) {
			Entry<T, F> entry = iterator.next();
			if (maxObject == null || this.filteredComparator.compare(entry.getKey(), maxObject) > 0) {
				maxObject = entry.getKey();
			}
		}

		return maxObject;
	}
	
	protected T high(ObjectSparseFormalSum<F, T> chain) {
		if (chain.isEmpty()) {
			return null;
		}
		
		T maxObject = null;
		
		for (Iterator<Entry<T, F>> iterator = chain.iterator(); iterator.hasNext(); ) {
			Entry<T, F> entry = iterator.next();
			if (maxObject == null || this.filteredComparator.compare(entry.getKey(), maxObject) < 0) {
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
	public List<ObjectSparseFormalSum<F, T>> getBoundaryColumns(AbstractFilteredStream<T> stream, int dimension) {
		this.initializeFilteredComparator(stream);
		List<ObjectSparseFormalSum<F, T>> D = new ArrayList<ObjectSparseFormalSum<F, T>>();

		for (T i: stream) {
			if (stream.getDimension(i) != dimension) {
				continue;
			}

			D.add(chainModule.createNewSum(stream.getBoundaryCoefficients(i), stream.getBoundary(i)));
		}

		return D;
	}
	
	protected boolean verifyDecomposition(ObjectObjectPair<THashMap<T, ObjectSparseFormalSum<F, T>>, THashMap<T, ObjectSparseFormalSum<F, T>>> RV_pair, AbstractFilteredStream<T> stream) {
		THashMap<T, ObjectSparseFormalSum<F, T>> R_perp = RV_pair.getFirst();
		THashMap<T, ObjectSparseFormalSum<F, T>> V_perp = RV_pair.getSecond();

		for (T i: stream) {
			ObjectSparseFormalSum<F, T> D_row = chainModule.createNewSum(stream.getBoundaryCoefficients(i), stream.getBoundary(i));
			for (T j: stream) {
				ObjectSparseFormalSum<F, T> V_col = V_perp.get(j);
				F product_entry = this.chainModule.innerProduct(D_row, V_col);
				F R_entry;
				if (R_perp.contains(j)) {
					R_entry = R_perp.get(j).getCoefficient(i);
					if (R_entry == null) {
						R_entry = this.field.getZero();
					}
				} else {
					R_entry = this.field.getZero();
				}
				if (!R_entry.equals(product_entry)) {
					return false;
				}
			}
		}

		return true;
	}
}
