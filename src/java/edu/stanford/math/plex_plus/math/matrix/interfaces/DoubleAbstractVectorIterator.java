package edu.stanford.math.plex_plus.math.matrix.interfaces;

public interface DoubleAbstractVectorIterator {
	public boolean hasNext();
	public void advance();
	public void remove();
	public int index();
	public double value();
}
