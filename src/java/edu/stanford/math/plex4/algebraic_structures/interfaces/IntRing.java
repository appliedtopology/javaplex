package edu.stanford.math.plex4.algebraic_structures.interfaces;

import edu.stanford.math.plex4.utility.ExceptionUtility;

/**
 * This interface defines a ring structure on the 
 * the type int. Examples of this can include the standard
 * integer rings, Z/nZ or algebraic integers.
 * 
 * @author Andrew Tausz
 *
 */
public abstract class IntRing {
	public abstract int add(int a, int b);
	public abstract int subtract(int a, int b);
	public abstract int multiply(int a, int b);
	public abstract int negate(int a);
	
	public abstract int valueOf(int n);
	public abstract int getZero();
	public abstract int getOne();
	
	public int getNegativeOne() {
		return this.negate(this.getOne());
	}
	
	/**
	 * This function returns true if the given 
	 * value has a multiplicative inverse.
	 * 
	 * @param a the value to query
	 * @return true if the value is a unit and false otherwise
	 */
	public abstract boolean isUnit(int a);
	public abstract boolean isZero(int a);
	public abstract boolean isOne(int a);
	
	/**
	 * This function returns the characteristic of the ring.
	 * The characteristic is defined as the smallest positive n such
	 * that n * r = 0 for all r in the ring. If no such positive 
	 * integer exists, then it returns 0.
	 * 
	 * @return the characteristic of the ring
	 */
	public abstract int characteristic();
	
	/**
	 * This function computes the value of a^n within the ring. 
	 * It requires that n is non-negative.
	 * 
	 * @param a the base
	 * @param n the exponent
	 * @return the value of a^n in the ring
	 */
	public int power(int a, int n)	{
		ExceptionUtility.verifyNonNegative(n);
	    int result = this.getOne();
	    while (n > 0) {
	        if ((n & 1) == 1) {
	            result = this.multiply(result, a);
	        }
	        a = this.multiply(a, a);
	        n /= 2;
	    }
	    return result;
	}
	
	/**
	 * This function performs the valueOf function in place
	 * on each element of the array.
	 * 
	 * @param array the array to perform the valueOf operation on
	 */
	public void valueOfInPlace(int[] array) {
		ExceptionUtility.verifyNonNull(array);
		int length = array.length;
		for (int i = 0; i < length; i++) {
			array[i] = this.valueOf(array[i]);
		}		
	}
	
	/**
	 * This function performs the valueOf function in place on each
	 * element of the matrix.
	 * 
	 * @param array the 2-D array to perform the valueOf operation on
	 */
	public void valueOfInPlace(int[][] array) {
		ExceptionUtility.verifyNonNull(array);
		int m = array.length;
		for (int i = 0; i < m; i++) {
			this.valueOfInPlace(array[i]);			
		}
	}
}
