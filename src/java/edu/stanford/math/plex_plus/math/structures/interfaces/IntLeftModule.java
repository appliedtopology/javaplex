package edu.stanford.math.plex_plus.math.structures.interfaces;

/**
 * This interface defines a left R-module structure on
 * a generic data type. The base data type for the ring
 * is the type int.
 * 
 * @author Andrew Tausz
 *
 * @param <M> the underlying data type
 */
public interface IntLeftModule<M> {
	public abstract M add(M a, M b);
	public abstract M subtract(M a, M b);
	public abstract M multiply(int r, M a);
	public abstract M negate(M a);
}
