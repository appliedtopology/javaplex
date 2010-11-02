package edu.stanford.math.plex4.homology.chain_basis;


/**
 * This interface defines the functionality of a primitive basis element type
 * for a chain complex. The two main implementing classes are designed to be
 * the Simplex and Cell classes. We refer to these as "primitive" basis types
 * since they are not built from other any other types. Whereas, we may also
 * construct a chain complex on a basis consisting of objects derived from other
 * basis types. An example of this is when we construct the tensor product of
 * two chain complexes - the basis type consists of tensors of elements.
 * 
 * We require that the implementing class provide the hashCode and equals functions.
 * 
 * @author Andrew Tausz
 *
 */
public interface PrimitiveBasisElement {
	
	/**
	 * This function returns the dimension of the basis element. Note that this
	 * is the actual geometric dimension of the simplex or cell. For example, for
	 * a simplex this would be one less than the number of indices in it.
	 * 
	 * @return the geometric dimension of this object
	 */
	public int getDimension();
	
	/**
	 * This function returns the boundary of the current basis element. As with
	 * the dimension, this actually obtains the geometric boundary of this object.
	 * Note that this function only returns the the objects in the boundary, but
	 * not their coefficients.
	 * 
	 * @return the boundary of the current object
	 */
	public PrimitiveBasisElement[] getBoundaryArray();
	
	/**
	 * This function returns the coefficients of the elements of the boundary array.
	 * It is implemented as a separate function, since this interface is designed 
	 * to be agnostic to its algebraic environment.
	 * 
	 * In the case of the Simplex class this will return the array consisting of
	 * alternating signs [1, -1, 1, ...]. However, in the case of the Cell class
	 * it will return the degrees of the composite attaching maps used to compute
	 * cellular homology.
	 * 
	 * @return the coefficients of the boundary elements
	 */
	public int[] getBoundaryCoefficients();
}
