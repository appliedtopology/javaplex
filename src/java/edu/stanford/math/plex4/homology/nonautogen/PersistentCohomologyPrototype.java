package edu.stanford.math.plex4.homology.nonautogen;

import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import edu.stanford.math.plex4.homology.barcodes.BarcodeCollection;
import edu.stanford.math.plex4.homology.interfaces.AbstractPersistenceAlgorithm;
import edu.stanford.math.plex4.streams.derived.DualStream;
import edu.stanford.math.plex4.streams.interfaces.AbstractFilteredStream;
import edu.stanford.math.plex4.streams.utility.FilteredComparator;
import edu.stanford.math.primitivelib.autogen.algebraic.IntAbstractField;
import edu.stanford.math.primitivelib.autogen.formal_sum.IntAlgebraicFreeModule;
import edu.stanford.math.primitivelib.autogen.formal_sum.IntSparseFormalSum;
import edu.stanford.math.primitivelib.autogen.formal_sum.IntVectorConverter;
import edu.stanford.math.primitivelib.autogen.matrix.IntSparseVector;
import edu.stanford.math.primitivelib.autogen.matrix.IntVectorEntry;
import gnu.trove.TIntHashSet;
import gnu.trove.TIntIntHashMap;
import gnu.trove.TIntIntIterator;
import gnu.trove.TIntObjectHashMap;
import gnu.trove.TIntObjectIterator;
import gnu.trove.TObjectIntHashMap;
import gnu.trove.TObjectIntIterator;

public class PersistentCohomologyPrototype<U> extends AbstractPersistenceAlgorithm<U> {
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

	public BarcodeCollection<Integer> computeIndexIntervals(AbstractFilteredStream<U> stream) {
		this.initializeFilteredComparator(stream);
		return this.pCohMatrix(stream);
	}

	private BarcodeCollection<Integer> pCohMatrix(AbstractFilteredStream<U> stream) {
		AbstractFilteredStream<U> dualStream = new DualStream<U>(stream);
		IntVectorConverter<U> vectorConverter = new IntVectorConverter<U>(stream);
		dualStream.finalizeStream();

		BarcodeCollection<Integer> collection = new BarcodeCollection<Integer>();
		collection.setLeftClosedDefault(true);
		collection.setRightClosedDefault(false);
		
		//List<IntSparseFormalSum<U>> Z = new ArrayList<IntSparseFormalSum<U>>();
		//List<U> birth = new ArrayList<U>();

		//Set<U> live_cocycle_indices = new HashSet<U>();
		//Map<U, IntSparseFormalSum<U>> cocycles = new HashMap<U, IntSparseFormalSum<U>>();
		//Map<U, IntSparseFormalSum<U>> cocycleCoboundaries = new HashMap<U, IntSparseFormalSum<U>>();

		TIntHashSet live_cocycle_indices = new TIntHashSet();
		TIntObjectHashMap<IntSparseVector> cocycles = new TIntObjectHashMap<IntSparseVector>();
		TIntObjectHashMap<IntSparseVector> cocycleCoboundaries = new TIntObjectHashMap<IntSparseVector>();

		TIntIntHashMap coefficients = new TIntIntHashMap();
		for (U sigma_k : stream) {
			int dim_sigma_k = stream.getDimension(sigma_k);
			
			/*
			 * Do not process simplices of higher dimension than maxDimension.
			 */
			if (dim_sigma_k < this.minDimension) {
				continue;
			}

			if (dim_sigma_k > this.maxDimension + 1) {
				break;
			}

			
			int j = -1;
			int c_j = 0;
			int k = vectorConverter.getIndex(sigma_k);
			
			coefficients.clear();

			for (TIntObjectIterator<IntSparseVector> iterator = cocycleCoboundaries.iterator(); iterator.hasNext(); ) {
				iterator.advance();
				IntSparseVector d_alpha_i = iterator.value();
				int i = iterator.key();
				int c_i = d_alpha_i.get(k);
				if (!field.isZero(c_i)) {
					coefficients.put(i, c_i);
					if (i > j) {
						j = i;
						c_j = c_i;
					}
				}
			}

			if (j < 0) {
				live_cocycle_indices.add(k);
				cocycles.put(k, vectorConverter.toSparseVector(chainModule.createNewSum(sigma_k)));
				cocycleCoboundaries.put(k, vectorConverter.toSparseVector(chainModule.createNewSum(dualStream.getBoundaryCoefficients(sigma_k), dualStream.getBoundary(sigma_k))));
			} else {
				IntSparseVector alpha_j = cocycles.get(j);
				IntSparseVector d_alpha_j = cocycleCoboundaries.get(j);
				live_cocycle_indices.remove(j);
				cocycles.remove(j);
				cocycleCoboundaries.remove(j);

				for (TIntIntIterator cIterator = coefficients.iterator(); cIterator.hasNext(); ) {
					cIterator.advance();
					int i = cIterator.key();
					if (i == j) {
						continue;
					}

					int c_i = cIterator.value();
					int q = field.negate(field.divide(c_i, c_j));

					accumulate(cocycles.get(i), alpha_j, q, this.field);
					accumulate(cocycleCoboundaries.get(i), d_alpha_j, q, this.field);
					
					if (cocycles.get(i).isEmpty()) {
						cocycles.remove(i);
						cocycleCoboundaries.remove(i);
					}
				}

				U sigma_j = vectorConverter.getBasisElement(j);
				int index_j = stream.getFiltrationIndex(sigma_j);
				int index_k = stream.getFiltrationIndex(sigma_k);
				int dimension = stream.getDimension(sigma_j);
				if (index_k > index_j && dimension < maxDimension) {
					collection.addInterval(dimension, index_j, index_k);
				}
			}
		}

		for (TIntObjectIterator<IntSparseVector> iterator = cocycleCoboundaries.iterator(); iterator.hasNext(); ) {
			iterator.advance();
			int i = iterator.key();
			U sigma_i = vectorConverter.getBasisElement(i);
			int dimension = stream.getDimension(sigma_i);
			if (dimension < maxDimension) {
				collection.addRightInfiniteInterval(dimension, stream.getFiltrationIndex(sigma_i));
			}
		}


		return collection;
	}

	@SuppressWarnings("unused")
	private BarcodeCollection<Integer> pCoh(AbstractFilteredStream<U> stream) {
		AbstractFilteredStream<U> dualStream = new DualStream<U>(stream);
		dualStream.finalizeStream();
		//Timing.stopAndDisplay("Constructed dual stream");
		//Timing.restart();
		BarcodeCollection<Integer> collection = new BarcodeCollection<Integer>();
		collection.setLeftClosedDefault(true);
		collection.setRightClosedDefault(false);
		
		//List<IntSparseFormalSum<U>> Z = new ArrayList<IntSparseFormalSum<U>>();
		//List<U> birth = new ArrayList<U>();

		Set<U> live_cocycle_indices = new HashSet<U>();
		Map<U, IntSparseFormalSum<U>> cocycles = new HashMap<U, IntSparseFormalSum<U>>();
		Map<U, IntSparseFormalSum<U>> cocycleCoboundaries = new HashMap<U, IntSparseFormalSum<U>>();

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

			TObjectIntHashMap<U> coefficients = new TObjectIntHashMap<U>();
			U j = null;
			int c_j = 0;

			int dim_sigma_k = stream.getDimension(sigma_k);

			for (U i: live_cocycle_indices) {
				IntSparseFormalSum<U> d_alpha_i = cocycleCoboundaries.get(i);
				int dim_i = stream.getDimension(i);

				if ((dim_i + 1 == dim_sigma_k) && d_alpha_i.containsObject(sigma_k)) {
					int c_i = d_alpha_i.getCoefficient(sigma_k);
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
				cocycleCoboundaries.put(sigma_k, chainModule.createNewSum(dualStream.getBoundaryCoefficients(sigma_k), dualStream.getBoundary(sigma_k)));
			} else {
				IntSparseFormalSum<U> alpha_j = cocycles.get(j);
				IntSparseFormalSum<U> d_alpha_j = cocycleCoboundaries.get(j);
				live_cocycle_indices.remove(j);
				cocycles.remove(j);
				cocycleCoboundaries.remove(j);

				for (TObjectIntIterator<U> cIterator = coefficients.iterator(); cIterator.hasNext(); ) {
					cIterator.advance();
					U i = cIterator.key();
					if (i.equals(j)) {
						continue;
					}
					int c_i = cIterator.value();
					int q = field.negate(field.divide(c_i, c_j));
					chainModule.accumulate(cocycles.get(i), alpha_j, q);
					chainModule.accumulate(cocycleCoboundaries.get(i), d_alpha_j, q);
					
					if (cocycles.get(i).isEmpty()) {
						cocycles.remove(i);
						cocycleCoboundaries.remove(i);
					}
				}

				int index_j = stream.getFiltrationIndex(j);
				int index_k = stream.getFiltrationIndex(sigma_k);
				int dimension = stream.getDimension(j);
				if (index_k > index_j && dimension < maxDimension) {
					collection.addInterval(stream.getDimension(j), index_j, index_k);
				}
			}
		}

		for (U i: live_cocycle_indices) {
			int dimension = stream.getDimension(i);
			if (dimension < maxDimension) {
				collection.addRightInfiniteInterval(stream.getDimension(i), stream.getFiltrationIndex(i));
			}
		}

		return collection;
	}

	private void accumulate(IntSparseVector a, IntSparseVector b, int c, IntAbstractField field) {
		for (IntVectorEntry pair: b) {
			int index = pair.getIndex();
			int value = pair.getValue();
			a.set(index, field.add(a.get(index), field.multiply(value, c)));
		}
	}
}
