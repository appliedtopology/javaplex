package edu.stanford.math.plex_plus.math.matrix.interfaces;

public interface BinaryAbstractVectorIterator {
	public boolean hasNext();
	public void advance();
	public void remove();
	public int index();
	public boolean value();
}
