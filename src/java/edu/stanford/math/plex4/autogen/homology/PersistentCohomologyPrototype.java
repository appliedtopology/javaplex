package edu.stanford.math.plex4.autogen.homology;

import java.util.Comparator;
import java.util.Map;
import java.util.Set;

import edu.stanford.math.plex4.homology.barcodes.IntBarcodeCollection;
import edu.stanford.math.plex4.homology.interfaces.AbstractPersistenceAlgorithm;
import edu.stanford.math.plex4.streams.interfaces.AbstractFilteredStream;
import edu.stanford.math.plex4.streams.utility.FilteredComparator;
import edu.stanford.math.primitivelib.autogen.algebraic.IntAbstractField;
import edu.stanford.math.primitivelib.autogen.formal_sum.IntAlgebraicFreeModule;
import edu.stanford.math.primitivelib.autogen.formal_sum.IntSparseFormalSum;
import gnu.trove.THashMap;
import gnu.trove.THashSet;
import gnu.trove.TObjectIntHashMap;
import gnu.trove.TObjectIntIterator;

public class PersistentCohomologyPrototype<U> implements AbstractPersistenceAlgorithm<U> {
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
	public PersistentCohomologyPrototype(IntAbstractField field, Comparator<U> basisComparator, int minDimension, int maxDimension) {
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

	public IntBarcodeCollection computeIntervals(AbstractFilteredStream<U> stream) {
		this.initializeFilteredComparator(stream);
		return this.pCoh(stream);
	}

	private IntBarcodeCollection pCoh(AbstractFilteredStream<U> stream) {
		IntBarcodeCollection collection = new IntBarcodeCollection();

		//List<IntSparseFormalSum<U>> Z = new ArrayList<IntSparseFormalSum<U>>();
		//List<U> birth = new ArrayList<U>();

		Set<U> live_cocycle_indices = new THashSet<U>();
		Map<U, IntSparseFormalSum<U>> cocycles = new THashMap<U, IntSparseFormalSum<U>>();
		Map<U, IntSparseFormalSum<U>> cocycleBoundaries = new THashMap<U, IntSparseFormalSum<U>>();

		for (U sigma_k : stream) {
			/*
			 * Do not process simplices of higher dimension than maxDimension.
			 */
			if (stream.getDimension(sigma_k) < this.minDimension) {
				continue;
			}

			if (stream.getDimension(sigma_k) > this.maxDimension + 1) {
				continue;
			}

			IntSparseFormalSum<U> boundary = chainModule.createNewSum(stream.getBoundaryCoefficients(sigma_k), stream.getBoundary(sigma_k));

			Set<U> candidateIndices = new THashSet<U>();

			for (U i: live_cocycle_indices) {
				IntSparseFormalSum<U> alpha_i = cocycles.get(i);

				inner: for (TObjectIntIterator<U> boundaryIterator = boundary.iterator(); boundaryIterator.hasNext(); ) {
					boundaryIterator.advance();
					if (alpha_i.containsObject(boundaryIterator.key())) {
						candidateIndices.add(i);
						break inner;
					}
				}
			}

			TObjectIntHashMap<U> coefficients = new TObjectIntHashMap<U>();
			U j = null;
			int c_j = 0;
			
			for (U i: candidateIndices) {
				IntSparseFormalSum<U> alpha_i = cocycles.get(i);
				int c_i = 0;
				for (TObjectIntIterator<U> boundaryIterator = boundary.iterator(); boundaryIterator.hasNext(); ) {
					boundaryIterator.advance();
					if (alpha_i.containsObject(boundaryIterator.key())) {
						c_i = this.field.add(c_i, field.multiply(boundaryIterator.value(), alpha_i.getCoefficient(boundaryIterator.key())));
					}	
				}
				if (!this.field.isZero(c_i)) {
					coefficients.put(i, c_i);
					if (j == null || (this.filteredComparator.compare(i, j) > 0)) {
						j = i;
						c_j = c_i;
					}
				}
			}
			
			if (j == null) {
				live_cocycle_indices.add(sigma_k);
				cocycles.put(sigma_k, chainModule.createNewSum(sigma_k));
				cocycleBoundaries.put(sigma_k, boundary);
			} else {
				IntSparseFormalSum<U> alpha_j = cocycles.get(j);
				live_cocycle_indices.remove(j);
				cocycles.remove(j);
				cocycleBoundaries.remove(j);

				for (U i: live_cocycle_indices) {
					int c_i = coefficients.get(i);
					int q = field.negate(field.divide(c_i, c_j));
					chainModule.accumulate(cocycles.get(i), alpha_j, q);
					cocycleBoundaries.remove(i);
					cocycleBoundaries.put(i, boundary(cocycles.get(i), stream));
				}

				int index_j = stream.getFiltrationIndex(j);
				int index_k = stream.getFiltrationIndex(sigma_k);
				if (index_k > index_j) {
					collection.addInterval(stream.getDimension(j), index_j, index_k);
				}
			}
		}

		for (U i: live_cocycle_indices) {
			collection.addRightInfiniteInterval(stream.getDimension(i), stream.getFiltrationIndex(i));
		}

		return collection;
	}

	/**
	 * Returns true iff a is contained in b
	 * @param a
	 * @param b
	 * @return
	 */
	private boolean contains(IntSparseFormalSum<U> a, IntSparseFormalSum<U> b) {

		for (TObjectIntIterator<U> iterator = a.iterator(); iterator.hasNext(); ) {
			iterator.advance();
			int b_coefficient = b.getCoefficient(iterator.key());
			if (iterator.value() != b_coefficient) {
				return false;
			}
		}
		return true;
	}

	private IntSparseFormalSum<U> boundary(IntSparseFormalSum<U> chain, AbstractFilteredStream<U> stream) {
		IntSparseFormalSum<U> result = new IntSparseFormalSum<U>();

		for (TObjectIntIterator<U> iterator = result.iterator(); iterator.hasNext(); ) {
			iterator.advance();
			IntSparseFormalSum<U> boundary = chainModule.createNewSum(stream.getBoundaryCoefficients(iterator.key()), stream.getBoundary(iterator.key()));
			chainModule.accumulate(result, boundary, iterator.value());
		}

		return result;
	}

}
