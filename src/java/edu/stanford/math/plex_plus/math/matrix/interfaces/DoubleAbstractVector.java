package edu.stanford.math.plex_plus.math.matrix.interfaces;

public interface DoubleAbstractVector {
	public double get(int index);
	public void set(int index, double value);
	public int getLength();
	public double innerProduct(DoubleAbstractVector other);
	
	public DoubleAbstractVectorIterator iterator();
	public String toString();
}
