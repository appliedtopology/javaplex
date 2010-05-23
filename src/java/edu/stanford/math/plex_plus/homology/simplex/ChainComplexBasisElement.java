package edu.stanford.math.plex_plus.homology.simplex;


/**
 * This interface defines the basic functionality of a basis element of 
 * chain complex. 
 * 
 * 
 * @author Andrew Tausz
 *
 */
public interface ChainComplexBasisElement {
	
	/**
	 * This function returns the dimension of the simplex, which equals
	 * one less than the number of vertices needed to specify it.
	 * 
	 * @return the dimension of the simplex.
	 */
	public int getDimension();
	
	/**
	 * This function returns an array containing the boundary of the simplex
	 * without coefficients. The oriented boundary is simply the alternating
	 * some of the array containing the boundary elements. Since our algorithms
	 * are designed to be used for arbitrary coefficient domains, we chose
	 * not to include the coefficients. Also, it is actually redundant to store
	 * the coefficients, since we know they are equal to (-1)^k, where k is the
	 * index of the array.
	 * 
	 * For example, suppose that our simplex is [0, 5, 9], then this function
	 * would return the array of simplices [[5, 9], [0, 9], [0, 5]]. 
	 * 
	 * @return an array containing the boundary components of this simplex
	 */
	public ChainComplexBasisElement[] getBoundaryArray();
	
	
}
