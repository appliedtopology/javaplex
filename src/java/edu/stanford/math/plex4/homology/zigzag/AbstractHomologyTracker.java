package edu.stanford.math.plex4.homology.zigzag;



public interface AbstractHomologyTracker<K, I extends Comparable<I>, U, G> {
	public void add(U sigma, K index);
	public void remove(U sigma, K index);
	
	public AbstractPersistenceTracker<K, I, G> getState();
	
	public boolean isBoundary(G generator);
}
