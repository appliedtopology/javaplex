/**
 * 
 */
package edu.stanford.math.plex_plus.algebraic_structures.interfaces;

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
	
	public abstract void accumulate(M a, M b);
	public abstract void accumulate(M a, M b, R c);
}
