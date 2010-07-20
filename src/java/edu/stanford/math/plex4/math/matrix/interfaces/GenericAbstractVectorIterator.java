package edu.stanford.math.plex4.math.matrix.interfaces;

public interface GenericAbstractVectorIterator<T> {
	public boolean hasNext();
	public void advance();
	public void remove();
	public int index();
	public T value();
}
