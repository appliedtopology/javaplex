package edu.stanford.math.plex4.math.matrix.interfaces;

public interface IntAbstractVectorIterator {
	public boolean hasNext();
	public void advance();
	public void remove();
	public int index();
	public int value();
}
