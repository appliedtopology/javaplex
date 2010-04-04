package edu.stanford.math.plex_plus.math.matrix.interfaces;


public interface GenericAbstractVector<T> {
	public T get(int index);
	public void set(int index, T value);
	public int getLength();
	
	public GenericAbstractVectorIterator<T> iterator();
	public String toString();
}
