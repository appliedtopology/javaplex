package edu.stanford.math.plex_plus.math.matrix.interfaces;

public interface GenericAbstractVectorIterator<T> {
	public boolean hasNext();
	public void advance();
	public void remove();
	public int index();
	public T value();
}
