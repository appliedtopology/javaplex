/**
 * 
 */
package edu.stanford.math.plex_plus.algebraic_structures.interfaces;

/**
 * This abstract class defines a field structure 
 * over the integers. The main implementation in 
 * mind are the finite fields Z/pZ, although other 
 * ones are conceivable (maybe packed rationals?).
 * 
 * @author Andrew Tausz
 *
 */
public abstract class IntField extends IntRing {
	public abstract int divide(int a, int b);
	public abstract int invert(int a);
	
	@Override
	public int power(int a, int n)	{
		if (n < 0) {
			return this.power(this.invert(a), -n);
		}
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
