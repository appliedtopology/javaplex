package edu.stanford.math.plex4.math.matrix.interfaces;

public interface IntAbstractMatrixIterator {
	public boolean hasNext();
	public void advance();
	public void remove();
	public int row();
	public int column();
	public int value();
}
