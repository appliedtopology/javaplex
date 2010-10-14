package edu.stanford.math.plex4.free_module;

import edu.stanford.math.plex4.utility.ExceptionUtility;
import gnu.trove.TIntObjectHashMap;
import gnu.trove.TObjectIntHashMap;

import java.util.Map;

/**
 * This class implements the isomorphism between a free module on a set of
 * finite generators, and the module R^n, where R is a commutative ring.
 * In this case, the set of free generators is given by an Iterable stream
 * of values of type M. This class contains the necessary functionality for
 * converting between these two formats: formal sums over a generating set
 * and arrays of elements in R. Thus basis elements correspond to standard
 * unit vectors.
 * 
 * @author Andrew Tausz
 *
 * @param <R> the type of the underlying ring
 * @param <M> the type of the set of free generators
 */
public class FreeModuleRepresentation<R extends Number, M> {
	/**
	 * This is the iterable collection of elements which constitute the free basis
	 * of the module.
	 */
	private final Iterable<M> stream;
	
	/**
	 * This maps a basis element to an index. The index is the appropriate
	 * index of the standard basis vector the generating element is mapped to.
	 */
	private final TObjectIntHashMap<M> indexMapping = new TObjectIntHashMap<M>();
	
	/**
	 * This maps an index (ie. the index of a standard basis vector) to a generating
	 * element of the free module.
	 */
	private final TIntObjectHashMap<M> basisMapping = new TIntObjectHashMap<M>();
	
	/**
	 * This stores the cardinality of the generating set of the module.
	 */
	private final int dimension;
	
	/**
	 * This constructor initializes the object with an Iterable stream of the 
	 * free generators of the module.
	 * 
	 * @param stream an iterable collection of free generators
	 */
	public FreeModuleRepresentation(Iterable<M> stream) {
		ExceptionUtility.verifyNonNull(stream);
		this.stream = stream;
		this.initializeMappings();
		this.dimension = this.indexMapping.size();
	}
	
	/**
	 * This function initializes the basis-index mappings.
	 */
	private void initializeMappings() {
		int index = 0;
		for (M basisElement: this.stream) {
			this.indexMapping.put(basisElement, index);
			this.basisMapping.put(index, basisElement);
			index++;
		}
	}
	
	/**
	 * This function returns the cardinality of the generating set of the free
	 * module.
	 * 
	 * @return the dimension of the free module
	 */
	public int getDimension() {
		return this.dimension;
	}

	/**
	 * This returns the index of a basis element. The index is defined to be the
	 * index of the standard basis vector that the element maps to.
	 * 
	 * @param basisElement the basis element to query
	 * @return the index of the basis element
	 */
	public int getIndex(M basisElement) {
		ExceptionUtility.verifyTrue(this.indexMapping.containsKey(basisElement));
		return this.indexMapping.get(basisElement);
	}
	
	/**
	 * This returns the basis element corresponding to the supplied index.
	 * 
	 * @param index the index to query
	 * @return the basis element with the supplied index
	 */
	public M getBasisElement(int index) {
		ExceptionUtility.verifyTrue(this.basisMapping.containsKey(index));
		return this.basisMapping.get(index);
	}
	
	/*
	public R[] toArray(GenericFormalSum<R, M> formalSum) {
		R[] array = ArrayUtility2.newGenericArray(this.getDimension());
		
		for (Map.Entry<M, R> entry: formalSum) {
			int index = this.getIndex(entry.getKey());
			array[index] = entry.getValue();
		}
		
		return array;
	}*/
	
	public double[] toDoubleArray(AbstractGenericFormalSum<R, M> formalSum) {
		double[] array = new double[this.dimension];
		
		for (Map.Entry<M, R> entry: formalSum) {
			int index = this.getIndex(entry.getKey());
			array[index] = entry.getValue().doubleValue();
		}
		
		return array;
	}
	
	public UnorderedGenericFormalSum<R, M> toFormalSum(R[] array) {
		UnorderedGenericFormalSum<R, M> sum = new UnorderedGenericFormalSum<R, M>();
		
		for (int i = 0; i < array.length; i++) {
			sum.put(array[i], this.getBasisElement(i));
		}
		
		return sum;
	}
}
