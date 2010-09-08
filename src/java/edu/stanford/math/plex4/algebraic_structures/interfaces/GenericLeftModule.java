/**
 * 
 */
package edu.stanford.math.plex4.algebraic_structures.interfaces;

/**
 * This interface defines a left R-module structure on
 * a generic data type.
 * 
 * @author Andrew Tausz
 *
 * @param <R> ring of scalars
 * @param <M> the underlying data type
 */
public interface GenericLeftModule<R, M> {
	public abstract M add(M a, M b);
	public abstract M subtract(M a, M b);
	public abstract M multiply(R r, M a);
	public abstract M negate(M a);
	public abstract M multiply(int r, M a);
	
	/**
	 * This function performs the operation a = a + b.
	 * 
	 * @param a the value to accumulate in
	 * @param b the module element to add
	 */
	public abstract void accumulate(M a, M b);
	
	/**
	 * This function performs the operation a = a + r * b.
	 * 
	 * @param a the value to accumulate in
	 * @param b the module element to add
	 * @param c the scalar multiplier
	 */
	public abstract void accumulate(M a, M b, R r);
}
