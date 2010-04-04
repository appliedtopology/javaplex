package edu.stanford.math.plex_plus.math.matrix.interfaces;

public interface DoubleAbstractMatrix {
	public int getNumRows();
	public int getNumColumns();
	
	public double get(int row, int column);
	public void set(int row, int column, double value);
	
	public DoubleAbstractMatrixIterator iterator();
	
	public DoubleAbstractVector multiply(DoubleAbstractVector vector);
	public DoubleAbstractMatrix transpose();
	
	public String toString();
}
