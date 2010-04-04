package edu.stanford.math.plex_plus.math.structures.interfaces;

import edu.stanford.math.plex_plus.utility.ExceptionUtility;

/**
 * This interface defines a ring structure on the 
 * the type int. 
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
	
	public abstract boolean isUnit(int a);
	public abstract boolean isZero(int a);
	public abstract boolean isOne(int a);
	
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
}
