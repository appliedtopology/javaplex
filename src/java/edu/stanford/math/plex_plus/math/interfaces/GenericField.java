package edu.stanford.math.plex_plus.math.interfaces;

/**
 * This interface defines a field structure over the generic type T.
 * 
 * @author Andrew Tausz
 *
 * @param <T> the type over which the field is defined
 */
public abstract class GenericField<T> extends GenericRing<T> {
	public abstract T divide(T a, T b);
	public abstract T invert(T a);
	
	public T divide(int a, int b) {
		return this.divide(this.valueOf(a), this.valueOf(b));
	}
	
	public T invert(int a) {
		return this.invert(this.valueOf(a));
	}
	
	@Override
	public T power(T a, int n)	{
		if (n < 0) {
			return this.power(this.invert(a), -n);
		}
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
