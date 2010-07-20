package edu.stanford.math.plex4.math.matrix.interfaces;

public interface BinaryAbstractVectorIterator {
	public boolean hasNext();
	public void advance();
	public void remove();
	public int index();
	public boolean value();
}
