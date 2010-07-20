package edu.stanford.math.plex4.algebraic_structures.interfaces;

import edu.stanford.math.plex4.utility.ExceptionUtility;

/**
 * This interface defines a ring structure over the generic type T.
 * 
 * @author Andrew Tausz
 *
 * @param <R> the type over which the ring is defined
 */
public abstract class GenericRing<R> {
	public abstract R add(R a, R b);
	public abstract R subtract(R a, R b);
	public abstract R multiply(R a, R b);
	public abstract R negate(R a);
	
	public abstract R valueOf(int n);
	public abstract R getZero();
	public abstract R getOne();
	public R getNegativeOne() {
		return this.negate(this.getOne());
	}
	
	public abstract boolean isUnit(R a);
	public abstract boolean isZero(R a);
	public abstract boolean isOne(R a);
	
	public abstract int characteristic();
	
	public R add(int a, int b) {
		return this.add(this.valueOf(a), this.valueOf(b));
	}
	
	public R subtract(int a, int b) {
		return this.subtract(this.valueOf(a), this.valueOf(b));
	}
	
	public R multiply(int a, int b) {
		return this.multiply(this.valueOf(a), this.valueOf(b));
	}
	
	public R add(R a, int b) {
		return this.add(a, this.valueOf(b));
	}
	
	public R subtract(R a, int b) {
		return this.subtract(a, this.valueOf(b));
	}
	
	public R multiply(R a, int b) {
		return this.multiply(a, this.valueOf(b));
	}
	
	public R add(int a, R b) {
		return this.add(this.valueOf(a), b);
	}
	
	public R subtract(int a, R b) {
		return this.subtract(this.valueOf(a), b);
	}
	
	public R multiply(int a, R b) {
		return this.multiply(this.valueOf(a), b);
	}
	
	public R negate(int a) {
		return this.negate(this.valueOf(a));
	}
	
	public R power(R a, int n)	{
		ExceptionUtility.verifyNonNegative(n);
	    R result = this.getOne();
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
