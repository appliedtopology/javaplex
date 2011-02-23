package edu.stanford.math.plex4.homology.nonautogen;

import java.util.Comparator;

import edu.stanford.math.plex4.homology.barcodes.IntBarcodeCollection;
import edu.stanford.math.plex4.homology.interfaces.AbstractPersistenceAlgorithm;
import edu.stanford.math.plex4.streams.interfaces.AbstractFilteredStream;
import edu.stanford.math.primitivelib.autogen.algebraic.IntAbstractField;
import edu.stanford.math.primitivelib.autogen.formal_sum.IntAlgebraicFreeModule;
import edu.stanford.math.primitivelib.autogen.formal_sum.IntSparseFormalSum;
import gnu.trove.THashMap;
import gnu.trove.TObjectIntIterator;

public class ZigZagPrototype<U> implements AbstractPersistenceAlgorithm<U> {
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
	public ZigZagPrototype(IntAbstractField field, Comparator<U> basisComparator, int minDimension, int maxDimension) {
		this.field = field;
		this.chainModule = new IntAlgebraicFreeModule<U>(this.field);
		this.basisComparator = basisComparator;
		this.minDimension = minDimension;
		this.maxDimension = maxDimension;
	}
	
	public IntBarcodeCollection computeIntervals(AbstractFilteredStream<U> stream) {
		//IntVectorConverter<U> vectorConverter = new IntVectorConverter<U>(stream);
		
		//TIntObjectHashMap<IntSparseVector> Z = new TIntObjectHashMap<IntSparseVector>();
		//TIntObjectHashMap<IntSparseVector> B = new TIntObjectHashMap<IntSparseVector>();
		
		THashMap<U, IntSparseFormalSum<U>> Z = new THashMap<U, IntSparseFormalSum<U>>();
		THashMap<U, IntSparseFormalSum<U>> B = new THashMap<U, IntSparseFormalSum<U>>();
		
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
			
			IntSparseFormalSum<U> boundary = chainModule.createNewSum(stream.getBoundaryCoefficients(sigma), stream.getBoundary(sigma));
			
			// compute representation of boundary of sigma in terms of cycles Z_i
			
		}
		
		
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * This function computes the operation low_A(j) as described in the paper. Note that if
	 * the chain is empty (for example the column contains only zeros), then this function
	 * returns null.
	 * 
	 * @param formalSum
	 * @return
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
