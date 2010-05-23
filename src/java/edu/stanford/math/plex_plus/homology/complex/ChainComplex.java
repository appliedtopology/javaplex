package edu.stanford.math.plex_plus.homology.complex;

public interface ChainComplex<T> {
	public T[] computeBoundary(T basisElement);
	public T[] computeCoboundary(T basisElement);
}
