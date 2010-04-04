package edu.stanford.math.plex_plus.math.matrix.interfaces;

public interface IntAbstractMatrixIterator {
	public boolean hasNext();
	public void advance();
	public void remove();
	public int row();
	public int column();
	public int value();
}
