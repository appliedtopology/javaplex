package edu.stanford.math.plex_plus.math.matrix.interfaces;

public interface IntAbstractVector {
	public int get(int index);
	public void set(int index, int value);
	public int getLength();
	public int innerProduct(IntAbstractVector other);
	
	public IntAbstractVectorIterator iterator();
	public String toString();
}
