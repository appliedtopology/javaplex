package edu.stanford.math.plex_plus.algebraic_structures.interfaces;

import edu.stanford.math.plex_plus.utility.ExceptionUtility;

/**
 * This interface defines a ring structure over the generic type T.
 * 
 * @author Andrew Tausz
 *
 * @param <T> the type over which the ring is defined
 */
public abstract class GenericRing<T> {
	public abstract T add(T a, T b);
	public abstract T subtract(T a, T b);
	public abstract T multiply(T a, T b);
	public abstract T negate(T a);
	
	public abstract T valueOf(int n);
	public abstract T getZero();
	public abstract T getOne();
	public T getNegativeOne() {
		return this.negate(this.getOne());
	}
	
	public abstract boolean isUnit(T a);
	public abstract boolean isZero(T a);
	public abstract boolean isOne(T a);
	
	public abstract int characteristic();
	
	public T add(int a, int b) {
		return this.add(this.valueOf(a), this.valueOf(b));
	}
	
	public T subtract(int a, int b) {
		return this.subtract(this.valueOf(a), this.valueOf(b));
	}
	
	public T multiply(int a, int b) {
		return this.multiply(this.valueOf(a), this.valueOf(b));
	}
	
	public T add(T a, int b) {
		return this.add(a, this.valueOf(b));
	}
	
	public T subtract(T a, int b) {
		return this.subtract(a, this.valueOf(b));
	}
	
	public T multiply(T a, int b) {
		return this.multiply(a, this.valueOf(b));
	}
	
	public T add(int a, T b) {
		return this.add(this.valueOf(a), b);
	}
	
	public T subtract(int a, T b) {
		return this.subtract(this.valueOf(a), b);
	}
	
	public T multiply(int a, T b) {
		return this.multiply(this.valueOf(a), b);
	}
	
	public T negate(int a) {
		return this.negate(this.valueOf(a));
	}
	
	public T power(T a, int n)	{
		ExceptionUtility.verifyNonNegative(n);
	    T result = this.getOne();
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
