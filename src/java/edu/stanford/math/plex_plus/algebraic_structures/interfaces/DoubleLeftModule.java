package edu.stanford.math.plex_plus.algebraic_structures.interfaces;

/**
 * This interface defines a left R-module structure on
 * a generic data type. The base data type for the ring
 * is the type double.
 * 
 * @author Andrew Tausz
 *
 * @param <M> the underlying data type
 */
public interface DoubleLeftModule<M> {
	public abstract M add(final M a, final M b);
	public abstract M subtract(final M a, final M b);
	public abstract M multiply(final double r, final M a);
	public abstract M negate(final M a);
	
	public abstract M getAdditiveIdentity();
	
	public abstract void accumulate(M a, M b);
	public abstract void accumulate(M a, M b, double c);
}