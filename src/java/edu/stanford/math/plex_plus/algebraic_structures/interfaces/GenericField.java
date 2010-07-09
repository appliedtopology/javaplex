package edu.stanford.math.plex_plus.algebraic_structures.interfaces;

/**
 * This interface defines a field structure over the generic type T.
 * 
 * @author Andrew Tausz
 *
 * @param <F> the type over which the field is defined
 */
public abstract class GenericField<F> extends GenericRing<F> {
	public abstract F divide(F a, F b);
	public abstract F invert(F a);
	
	public F divide(int a, int b) {
		return this.divide(this.valueOf(a), this.valueOf(b));
	}
	
	public F invert(int a) {
		return this.invert(this.valueOf(a));
	}
	
	@Override
	public F power(F a, int n)	{
		if (n < 0) {
			return this.power(this.invert(a), -n);
		}
	    F result = this.getOne();
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
