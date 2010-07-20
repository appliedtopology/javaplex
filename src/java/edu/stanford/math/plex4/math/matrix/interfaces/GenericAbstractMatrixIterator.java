package edu.stanford.math.plex4.math.matrix.interfaces;

public interface GenericAbstractMatrixIterator<T> {
	public boolean hasNext();
	public void advance();
	public void remove();
	public int row();
	public int column();
	public T value();
}
